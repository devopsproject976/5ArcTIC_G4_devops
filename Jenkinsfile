pipeline {
    agent any

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "localhost:8081"
        NEXUS_REPOSITORY = "maven-releases" // Update to match your repository name
        NEXUS_CREDENTIAL_ID = "NEXUS_CREDENTIALS" // Jenkins credentials ID for Nexus
        SONARQUBE_URL = 'http://localhost:9000'
        SONARQUBE_CREDENTIALS = 'SONARQUBE_CREDENTIALS_ID'
    }

    stages {
        stage('Start MySQL Container') {
            steps {
                script {
                    sh 'docker run -d --name mysql-test -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=test_db -p 3306:3306 mysql:5.7'
                }
            }
        }
        stage('Build Spring Boot') {
            steps {
                dir('Backend') {
                    echo 'Building Spring Boot application...'
                    sh 'mvn clean package -DskipTests=true'
                }
            }
        }

        // SonarQube analysis
        stage('SonarQube Analysis') {
            steps {
                dir('Backend') {
                    echo 'Running SonarQube analysis...'
                    withSonarQubeEnv('sonar-jenkins') { // SonarQube env configuration
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }

        // Ensure SonarQube analysis has passed
        /*stage('Quality Gate') {
            steps {
                echo 'Checking SonarQube Quality Gate...'
                waitForQualityGate abortPipeline: true // Abort the pipeline if the quality gate fails
            }
        }*/

        stage('Find JAR Version') {
            steps {
                dir('Backend') {
                    script {
                        env.JAR_FILE = sh(script: "ls target/*.jar | grep -v 'original' | head -n 1", returnStdout: true).trim()
                    }
                    echo "Using JAR file: ${env.JAR_FILE}"
                }
            }
        }

        stage('Publish to Nexus') {
            steps {
                dir('Backend') {
                    script {
                        pom = readMavenPom file: "pom.xml"
                        filesByGlob = findFiles(glob: "target/*.${pom.packaging}")
                        artifactPath = filesByGlob[0].path

                        if (fileExists(artifactPath)) {
                            def customArtifactName = "5ArcTIC3-G4-SofienDaadoucha.jar"
                            echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}"
                            
                            // Rename the artifact to custom name
                            sh "mv ${artifactPath} target/${customArtifactName}"
                            artifactPath = "target/${customArtifactName}"
                            
                            nexusArtifactUploader(
                                nexusVersion: NEXUS_VERSION,
                                protocol: NEXUS_PROTOCOL,
                                nexusUrl: NEXUS_URL,
                                groupId: pom.groupId,
                                version: pom.version,
                                repository: NEXUS_REPOSITORY,
                                credentialsId: NEXUS_CREDENTIAL_ID,
                                artifacts: [
                                    [artifactId: pom.artifactId, classifier: '', file: artifactPath, type: pom.packaging],
                                    [artifactId: pom.artifactId, classifier: '', file: "pom.xml", type: "pom"]
                                ]
                            )
                        } else {
                            error "*** File: ${artifactPath} could not be found"
                        }
                    }
                }
            }
        }
    }

    /*stage('Build and Push Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t SofienDaadoucha-5ArcTIC3-G4-devops .'

                echo 'Pushing Docker image to DockerHub...'
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                    }
                    sh 'docker push SofienDaadoucha-5ArcTIC3-G4-devops'
                }
            }
        }
    }*/

    post {
        always {
            sh 'docker rm -f mysql-test || true'
        }
        success {
            echo 'Build and Nexus publish succeeded!'
        }
        failure {
            echo 'Build or Nexus publish failed.'
        }
    }
}
