pipeline {
    agent any
    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'localhost:8081', description: 'Nexus URL')
        string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-releases', description: 'Nexus Repository Name')
    }
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_CREDENTIAL_ID = "nexus-credentials" // Jenkins credentials ID for Nexus
    }
    stages {
        stage('Start MySQL Container') {
            steps {
                script {
                    sh 'docker rm -f mysql-test || true'
                    sh 'docker run -d --name mysql-test -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=test_db -p 3306:3306 mysql:5.7'
                }
            }
        }

        // Backend stages
        stage('Build Spring Boot') {
            steps {
                dir('Backend') {
                    echo 'Building Spring Boot application...'
                    sh 'mvn clean package jacoco:report sonar:sonar -Dsonar.projectKey=5arctic3_g4_devops -Dsonar.login=admin -Dsonar.password=hamma1234'
                }
            }
        }

        stage('Publish to Nexus and Build Docker Image') {
            parallel {
                stage('Publish to Nexus') {
                    steps {
                        dir('Backend') {
                            script {
                                // Define artifact details based on the known pom.xml values
                                def groupId = "tn.esprit"
                                def artifactId = "5ArcTIC3-G4-devops"
                                def version = "1.0"
                                def packaging = "jar"  // Based on your project packaging
                                def artifactPath = "target/5ArcTIC3-G4-devops-1.0.jar"
                                def pomFile = "pom.xml"

                                // Check if the artifact exists
                                if (fileExists(artifactPath)) {
                                    echo "*** File: ${artifactPath}, group: ${groupId}, packaging: ${packaging}, version ${version}"

                                    // Upload artifact and POM to Nexus
                                    nexusArtifactUploader(
                                        nexusVersion: NEXUS_VERSION,
                                        protocol: NEXUS_PROTOCOL,
                                        nexusUrl: params.NEXUS_URL,
                                        groupId: groupId,
                                        artifactId: artifactId,
                                        version: version,
                                        repository: params.NEXUS_REPOSITORY,
                                        credentialsId: 'nexus-credentials',
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
                } // Closing the Publish to Nexus stage

                stage('Build Spring Docker Image') {
                    steps {
                        echo 'Building Docker image for Spring Boot...'
                        sh 'docker build -t medaminetrabelsi/devopsback -f Backend/Dockerfile .'
                    }
                }

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

                /* 
                stage('Push Spring Docker Image to Docker Hub') {
                    steps {
                        echo 'Pushing Spring Boot Docker image to Docker Hub...'
                        script {
                            withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                                sh 'echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USER} --password-stdin'
                            }
                            sh 'docker push medaminetrabelsi/devopsback'
                        }
                    }
                }
                */
            } // Closing the parallel block **test****
        }

    }

    post {
        always {
            sh 'docker rm -f mysql-test || true'
        }
        success {
            echo 'Build and Docker push succeeded for backend!'
        }
        failure {
            echo 'Build or Docker push failed.'
        }
    }
}
