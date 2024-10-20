pipeline {
    agent any

    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'localhost:8081', description: 'Nexus URL')
        string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-releases', description: 'Nexus Repository Name')
        string(name: 'MYSQL_VERSION', defaultValue: '5.7', description: 'MySQL Docker Image Version')
        string(name: 'SONARQUBE_URL', defaultValue: 'http://localhost:9000', description: 'SonarQube URL')
    }

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_CREDENTIAL_ID = "NEXUS_CREDENTIALS" // Jenkins credentials ID for Nexus
        SONARQUBE_CREDENTIALS = 'SONARQUBE_CREDENTIALS_ID'
    }

    stages {
        stage('Start MySQL Container') {
            steps {
                script {
                    sh "docker rm -f mysql-test || true"
                    sh "docker run -d --name mysql-test -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=test_db -p 3306:3306 mysql:${MYSQL_VERSION}"
                }
            }
        }

        // Backend stages
        stage('Build Spring Boot') {
            steps {
                dir('Backend') {
                    echo 'Building Spring Boot application...'
                    sh "mvn clean package jacoco:report sonar:sonar -Dsonar.projectKey=5arctic3_g4_devops -Dsonar.login=admin -Dsonar.password=hamma1234 -Dsonar.host.url=${SONARQUBE_URL}"
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                dir('Backend') {
                    echo 'Deploying to Nexus...'
                    script {
                        withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIAL_ID}", usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                            sh "mvn deploy -DskipTests -DaltDeploymentRepository=nexus-releases::default::${NEXUS_PROTOCOL}://${NEXUS_USER}:${NEXUS_PASSWORD}@${NEXUS_URL}/repository/${NEXUS_REPOSITORY}/"
                        }
                    }
                }
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

        stage('Build Spring Docker Image') {
            steps {
                echo 'Building Docker image for Spring Boot...'
                sh 'docker build -t medaminetrabelsi/devopsback -f Backend/Dockerfile .'
            }
        }

        // Commented out stage for pushing to Docker Hub
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
