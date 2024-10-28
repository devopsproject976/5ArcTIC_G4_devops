pipeline {
    agent any

    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'localhost:8081', description: 'Nexus URL')
        string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-releases', description: 'Nexus Repository Name')
        string(name: 'SONARQUBE_URL', defaultValue: 'http://localhost:9001', description: 'SonarQube URL')
    }

    environment {
        // MySQL for Spring Boot
        DB_URL = 'jdbc:mysql://localhost:3307/devops' // Port 3307 as per your docker-compose
        DB_USER = 'root'
        DB_PASSWORD = '' // Update to a strong password in production

        // SonarQube PostgreSQL details
        SONAR_DB_URL = 'jdbc:postgresql://sonar_db:5432/sonar' // Assuming the container is networked correctly
        SONAR_DB_USER = 'sonar'
        SONAR_DB_PASSWORD = 'sonar'

        DOCKER_USERNAME = credentials('dockerhub-token')
        DOCKER_PASSWORD = credentials('dockerhub-token')
        IMAGE_NAME_BACKEND = 'nourbkh/devops-backend'
        IMAGE_TAG_BACKEND = 'nourbenkhairia-5arctic3-g4-devops'
        IMAGE_NAME_FRONTEND = 'nourbkh/devops-frontend'
        IMAGE_TAG_FRONTEND = 'nourbenkhairia-5arctic3-g4-devopsfrontend'
        SONAR_SCANNER_HOME = tool 'SonarQube Scanner'
        SONAR_PROJECT_KEY_BACKEND = 'devops-backend'
        SONAR_PROJECT_KEY_FRONTEND = 'devops-frontend'
        SONAR_TOKEN = credentials('sonarqube-token')
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
        MAVEN_REPO_ID = 'nexus'
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'nourbenkhairia_5Arctic3_G4',
                    url: 'https://github.com/devopsproject976/5ArcTIC_G4_devops.git',
                    credentialsId: 'devops-pipeline'
            }
        }

        stage('Start Services') {
            steps {
                script {
                    // Start all services defined in the Docker Compose files
                    sh 'docker-compose -f docker-compose.yml -f docker-compose-tools.yml up -d'
                }
            }
        }

        stage('Build and Test') {
                    steps {
                        // Print environment variables
                        script {
                            echo "Current Java version: ${sh(script: 'java -version', returnStdout: true).trim()}"
                            echo "Current Maven version: ${sh(script: 'mvn -v', returnStdout: true).trim()}"
                        }

                        // Build and test backend
                        dir('Backend') {
                            sh 'mvn clean package'
                            sh 'mvn test'
                            sh 'mvn jacoco:report'
                        }

                        // SonarQube analysis for backend
                        script {
                            dir('Backend') {
                                withSonarQubeEnv('SonarQube') {
                                    sh "${SONAR_SCANNER_HOME}/bin/sonar-scanner " +
                                        "-Dsonar.projectKey=${SONAR_PROJECT_KEY_BACKEND} " +
                                        "-Dsonar.sources=src " +
                                        "-Dsonar.java.binaries=target/classes " +
                                        "-Dsonar.host.url=${params.SONARQUBE_URL} " +
                                        "-Dsonar.login=${env.SONAR_TOKEN} " +
                                        "-Dsonar.jacoco.reportPaths=target/jacoco.exec"
                                }
                            }
                        }

                        // Build frontend
                        dir('Frontend') {
                            sh 'npm install' // Assuming you need to install dependencies
                            sh 'npm run build' // Replace with your build command

                            // SonarQube analysis for frontend
                            withSonarQubeEnv('SonarQube') {
                                sh "${SONAR_SCANNER_HOME}/bin/sonar-scanner " +
                                    "-Dsonar.projectKey=${SONAR_PROJECT_KEY_FRONTEND} " +
                                    "-Dsonar.sources=src " +
                                    "-Dsonar.host.url=${params.SONARQUBE_URL} " +
                                    "-Dsonar.login=${env.SONAR_TOKEN}"
                            }
                        }
                    }
                }

        stage('Build Docker Images') {
            steps {
                // Build Docker image for backend
                dir('Backend') {
                    sh "docker build -t ${IMAGE_NAME_BACKEND}:${IMAGE_TAG_BACKEND} ."
                }

                // Build Docker image for frontend
                dir('Frontend') {
                    sh "docker build -t ${IMAGE_NAME_FRONTEND}:${IMAGE_TAG_FRONTEND} ."
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    // Push Docker image for backend
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-token',
                                                      usernameVariable: 'DOCKER_USERNAME',
                                                      passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh """
                        echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
                        docker push ${IMAGE_NAME_BACKEND}:${IMAGE_TAG_BACKEND}
                        docker push ${IMAGE_NAME_FRONTEND}:${IMAGE_TAG_FRONTEND}
                        """
                    }
                }
            }
        }

        stage('Publish to Nexus') {
            steps {
                script {
                    // Publish the backend artifact to Nexus
                    dir('Backend') {
                        sh """
                        mvn deploy:deploy-file \
                            -DgroupId=com.example \
                            -DartifactId=devops-backend \
                            -Dversion=${IMAGE_TAG_BACKEND} \
                            -Dpackaging=jar \
                            -Dfile=target/devops-backend-${IMAGE_TAG_BACKEND}.jar \
                            -DrepositoryId=${MAVEN_REPO_ID} \
                            -Durl=${NEXUS_PROTOCOL}://${NEXUS_URL}/repository/${NEXUS_REPOSITORY} \
                            -DskipTests
                        """
                    }

                    // Publish the frontend artifact to Nexus (assuming it's a JAR for this example)
                    dir('Frontend') {
                        sh """
                        mvn deploy:deploy-file \
                            -DgroupId=com.example \
                            -DartifactId=devops-frontend \
                            -Dversion=${IMAGE_TAG_FRONTEND} \
                            -Dpackaging=jar \
                            -Dfile=target/devops-frontend-${IMAGE_TAG_FRONTEND}.jar \
                            -DrepositoryId=${MAVEN_REPO_ID} \
                            -Durl=${NEXUS_PROTOCOL}://${NEXUS_URL}/repository/${NEXUS_REPOSITORY} \
                            -DskipTests
                        """
                    }
                }
            }
        }

        stage('Stop Services') {
            steps {
                script {
                    // Stop and remove services
                    sh 'docker-compose -f docker-compose.yml -f docker-compose-tools.yml down'
                }
            }
        }
    }

    post {
        always {
            script {
                // Cleanup or additional actions can be performed here
                sh 'docker-compose -f docker-compose.yml -f docker-compose-tools.yml down'
            }
        }
        success {
            echo 'Build and tests succeeded!'
        }
        failure {
            echo 'Build or test failed.'
        }
    }
}
