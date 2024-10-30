pipeline {
    agent any
    tools {
        nodejs 'NodeJS'
    }
    
    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'localhost:8081', description: 'Nexus URL')
        string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-snapshots', description: 'Nexus Repository Name')
        string(name: 'SONARQUBE_URL', defaultValue: 'http://localhost:9001', description: 'SonarQube URL')
    }
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_CREDENTIAL_ID = "NEXUS_CREDENTIALS"
        SONARQUBE_CREDENTIALS = 'SONARQUBE_CREDENTIALS_ID'
    }

    
     stages {
        stage('Build and Code Analysis - Backend') {
            steps {
                dir('Backend') {
                    echo 'Building and running SonarQube analysis for backend...'
                    withSonarQubeEnv('sonar-jenkins') {
                        sh 'mvn clean package jacoco:report sonar:sonar -Dsonar.projectKey=5arctic3_g4_devops -DskipTests'
                    }
                }
            }
        }

        stage('Build and Lint - Angular') {
            steps {
                dir('Frontend') {
                    echo 'Building Angular application and running lint...'
                    sh '''
                       npm ci
                       npm run build --prod
                    '''
                }
            }
        }

        stage('Export SonarQube Metrics') {
            steps {
                script {
                    withSonarQubeEnv('sonar-jenkins') {
                        sh """
                            curl -s -u ${SONAR_AUTH_TOKEN}: ${SONARQUBE_URL}/api/measures/component_tree?ps=100 \
                            --get --data-urlencode "component=5arctic3_g4_devops" \
                            --data-urlencode "metricKeys=ncloc,bugs,vulnerabilities,code_smells,security_hotspots,coverage,duplicated_lines_density" \
                            -o codeReport.json
                        """
                    }
                }
            }
        }

        stage('Publish to Nexus') {
            steps {
                dir('Backend') {
                    script {
                        def pom = readMavenPom file: "pom.xml"
                        def artifactPath = findFiles(glob: "target/*.${pom.packaging}")[0]?.path
                        if (artifactPath) {
                            nexusArtifactUploader(
                                nexusVersion: NEXUS_VERSION,
                                protocol: NEXUS_PROTOCOL,
                                nexusUrl: params.NEXUS_URL,
                                groupId: pom.groupId,
                                version: pom.version,
                                repository: params.NEXUS_REPOSITORY,
                                credentialsId: NEXUS_CREDENTIAL_ID,
                                artifacts: [
                                    [artifactId: pom.artifactId, file: artifactPath, type: pom.packaging],
                                    [artifactId: pom.artifactId, file: "pom.xml", type: "pom"]
                                ]
                            )
                        } else {
                            error "Artifact not found or does not exist."
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker images for both backend and frontend...'
                dir('Backend') {
                    sh 'docker build -t soufi2001/devopsback:5arctic3-g4-devops .'
                }
                dir('Frontend') {
                    sh 'docker build -t soufi2001/devopsfront:5arctic3-g4-devops .'
                }
            }
        }

        /*stage('Push Docker Images') {
            steps {
                echo 'Pushing Docker images to DockerHub...'
                withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                    sh 'docker push soufi2001/devopsback:5arctic3-g4-devops'
                    sh 'docker push soufi2001/devopsfront:5arctic3-g4-devops'
                }
            }
        }*/
    }

    post {
        always {
            echo 'Cleaning up Docker Compose environments...'
            script {
                try {
                    sh 'docker-compose down'
                } catch (Exception e) {
                    echo "Failed to clean up Docker Compose: ${e.message}"
                }
            }
        }
        success {
            echo 'CI/CD pipeline completed successfully!'
        }
        failure {
            emailext(
                subject: 'Pipeline Failure Alert',
                body: "The pipeline encountered an error.",
                to: "daadsoufi0157@gmail.com"
            )
        }
    }
}
