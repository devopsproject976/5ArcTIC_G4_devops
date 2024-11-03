pipeline {
    agent any
    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'localhost:8090', description: 'Nexus URL')
        string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-snapshots', description: 'Nexus Repository Name')
        string(name: 'DOCKERHUB_REPO_BACKEND', defaultValue: 'hamoudaatti/backdevops', description: 'Docker Hub Repository for Backend')
        string(name: 'DOCKERHUB_REPO_FRONTEND', defaultValue: 'hamoudaatti/frontdevops', description: 'Docker Hub Repository for Frontend')
    }
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_CREDENTIAL_ID = "nexus"
        DOCKERHUB_CREDENTIAL_ID = "DOCKERHUB_CREDENTIALS"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Spring Boot') {
            steps {
                dir('Backend') {
                    echo 'Building Spring Boot application...'
                    script {
                        withCredentials([string(credentialsId: 'sonar', variable: 'sonar')]) {
                            sh "mvn clean package jacoco:report sonar:sonar -Dsonar.projectKey=5arctic3_g4_devops -Dsonar.login=${SONAR_TOKEN}"
                        }
                    }
                }
            }
        }

        stage('Find JAR Version') {
            steps {
                dir('Backend') {
                    script {
                        sh "ls -la target/"
                        env.JAR_FILE = sh(script: "ls target/*.jar | grep -v 'original' | head -n 1", returnStdout: true).trim()
                        if (!env.JAR_FILE) {
                            error "Le fichier JAR n'a pas été trouvé dans le répertoire target."
                        }
                    }
                    echo "Using JAR file: ${env.JAR_FILE}"
                }
            }
        }

        stage('Publish to Nexus and Build Docker Images') {
            parallel {
                stage('Publish to Nexus') {
                    steps {
                        dir('Backend') {
                            script {
                                def groupId = "tn.esprit"
                                def artifactId = "5ArcTIC3-G4-devops"
                                def version = "1.0-SNAPSHOT"
                                def packaging = "jar"
                                def artifactPath = "${env.JAR_FILE}"
                                def pomFile = "pom.xml"

                                if (fileExists(artifactPath)) {
                                    echo "*** File: ${artifactPath}, group: ${groupId}, packaging: ${packaging}, version: ${version}"

                                    nexusArtifactUploader(
                                        nexusVersion: NEXUS_VERSION,
                                        protocol: NEXUS_PROTOCOL,
                                        nexusUrl: params.NEXUS_URL,
                                        groupId: groupId,
                                        artifactId: artifactId,
                                        version: version,
                                        repository: params.NEXUS_REPOSITORY,
                                        credentialsId: NEXUS_CREDENTIAL_ID,
                                        artifacts: [
                                            [artifactId: artifactId, classifier: '', file: artifactPath, type: packaging],
                                            [artifactId: artifactId, classifier: '', file: pomFile, type: "pom"]
                                        ]
                                    )
                                } else {
                                    error "*** File could not be found or does not exist at ${artifactPath}."
                                }
                            }
                        }
                    }
                }

                stage('Build Spring Docker Image') {
                    steps {
                        echo 'Building Docker image for Spring Boot...'
                        dir('Backend') {
                            script {
                                sh "docker build -t ${params.DOCKERHUB_REPO_BACKEND} -f Dockerfile --build-arg JAR_FILE=${env.JAR_FILE} ."
                            }
                        }
                    }
                }

                stage('Build Angular Docker Image') {
                    steps {
                        echo 'Building Docker image for Angular...'
                        dir('Frontend') {
                            script {
                                sh "docker build -t ${params.DOCKERHUB_REPO_FRONTEND} ."
                            }
                        }
                    }
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                echo 'Pushing Docker images to Docker Hub...'
                script {
                    withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIAL_ID, usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        sh "echo ${DOCKERHUB_PASSWORD} | docker login -u ${DOCKERHUB_USERNAME} --password-stdin"
                        sh "docker push ${params.DOCKERHUB_REPO_BACKEND}"
                        sh "docker push ${params.DOCKERHUB_REPO_FRONTEND}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build and Docker push succeeded for backend and frontend!'
        }
        failure {
            echo 'Build or Docker push failed.'
        }
    }
}