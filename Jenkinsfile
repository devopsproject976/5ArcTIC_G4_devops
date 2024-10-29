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
        
        // **CI Phase** //
        
        stage('Setup Application Environment') {
            steps {
                echo 'Setting up application environment variables...'
            }
        }

        stage('Build and Code Analysis - Backend') {
            steps {
                dir('Backend') {
                    echo 'Building and running SonarQube analysis for backend...'
                    withSonarQubeEnv('sonar-jenkins') {
                        // Compile, run Jacoco, and Sonar in one command to optimize Maven build time
                        sh 'mvn clean install jacoco:report sonar:sonar -Dsonar.projectKey=5arctic3_g4_devops -DskipTests'
                    }
                }
            }
        }

        stage('Build and Lint - Angular') {
            steps {
                dir('Frontend') {
                    echo 'Building Angular application and running lint...'
                    // Install dependencies if not cached, build, and lint in one step
                    sh '''
                       npm ci  # Faster install with npm ci if package-lock.json is present
                       npm run lint  # Linting for code quality
                        npm run build --prod  # Production build
                    '''
                }
            }
        }

        stage('Export SonarQube Metrics') {
            steps {
                script {
                    exportSonarMetrics('codeReport.json', '5arctic3_g4_devops')
                }
            }
        }

        

        stage('Publish to Nexus') {
            steps {
                dir('Backend') {
                    publishToNexus()
                }
            }
        }

        // **CD Phase** //
        
        stage('Build Docker Image') {
            steps {
                echo 'Building Docker images for both backend and frontend...'
                 
                    backend: { buildDockerImage('Backend', 'soufi2001/devopsback:5arctic3-g4-devops') },
                    frontend: { buildDockerImage('Frontend', 'soufi2001/devopsfront:5arctic3-g4-devops') }
                
            }
        }

        stage('Push Docker Images') {
            steps {
                echo 'Pushing Docker images to DockerHub...'
                withDockerCredentials {
                        backendPush: { sh 'docker push soufi2001/devopsback:5arctic3-g4-devops' },
                        frontendPush: { sh 'docker push soufi2001/devopsfront:5arctic3-g4-devops' }
                    
                }
            }
        }
    }
    
    post {
        always {
            cleanUpEnvironment()
        }
        success {
            echo 'CI/CD pipeline completed successfully!'
        }
        failure {
            sendFailureEmail('Pipeline Failure Alert', "The pipeline encountered an error.")
        }
    }
}

// ** Helper Functions ** //

def exportSonarMetrics(filePath, projectKey) {
    withSonarQubeEnv('sonar-jenkins') {
        sh """
            curl -s -u ${SONAR_AUTH_TOKEN}: ${SONARQUBE_URL}/api/measures/component_tree?ps=100 \
            --get --data-urlencode "component=${projectKey}" \
            --data-urlencode "metricKeys=ncloc,bugs,vulnerabilities,code_smells,security_hotspots,coverage,duplicated_lines_density" \
            -o ${filePath}
        """
    }
}

def publishToNexus() {
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

def buildDockerImage(directory, imageName) {
    dir(directory) {
        sh "docker build -t ${imageName} ."
    }
}

def withDockerCredentials(closure) {
    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
        sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
        closure.call()
    }
}

def cleanUpEnvironment() {
    echo 'Cleaning up Docker Compose environments...'
    try {
        sh 'docker-compose down'
    } catch (Exception e) {
        echo "Failed to clean up Docker Compose: ${e.message}"
    }
}

def sendFailureEmail(subject, message) {
    emailext(
        subject: subject,
        body: message,
        to: "daadsoufi0157@gmail.com"
    )
}
