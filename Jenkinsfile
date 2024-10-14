pipeline {
    agent any

    stages {
        // Backend stages (unchanged)
        stage('Build Spring Boot') {
            steps {
                dir('Backend') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Find Jar Version') {
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
                script {
                    sh 'docker build -t soufi2001/devopsback -f Backend/Dockerfile .'
                }
            }
        }

        stage('Push Spring Docker Image to Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                    }
                    sh 'docker push soufi2001/devopsback'
                }
            }
        }

        // Frontend stages
        stage('Build Angular') {
            steps {
                dir('Frontend') {
                    script {
                        // Use a Node.js Docker image to run npm commands
                        sh '''
                        docker run --rm -v $PWD:/app -w /app node:16-alpine npm install
                        docker run --rm -v $PWD:/app -w /app node:16-alpine npm run build --prod
                        '''
                    }
                }
            }
        }

        stage('Build Angular Docker Image') {
            steps {
                script {
                    sh 'docker build -t soufi2001/devopsfront -f Frontend/Dockerfile .'
                }
            }
        }

        stage('Push Angular Docker Image to Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                    }
                    sh 'docker push soufi2001/devopsfront'
                }
            }
        }
    }

    post {
        success {
            echo 'Build and Docker push succeeded for both backend and frontend!'
        }
        failure {
            echo 'Build or Docker push failed.'
        }
    }
}
