pipeline {
    agent any

    stages {

        stage('Start MySQL Container') {
            steps {
                script {
                    // Remove existing container if it exists
                    sh 'docker rm -f mysql-test || true'
                    
                    // Start a new MySQL container
                    sh 'docker run -d --name mysql-test -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=test_db -p 3306:3306 mysql:5.7'
                }
            }
        }
        
       // Backend stages
        stage('Build Spring Boot') {
            steps {
                dir('Backend') {
                    echo 'Building Spring Boot application...'
                    sh 'mvn clean package'
                }
            }
        }

        stage('Find JAR Version') {
            steps {
                dir('Backend') {
                    script {
                        // Find the generated JAR file, excluding the original JAR
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

        stage('Push Spring Docker Image to Docker Hub') {
            steps {
                echo 'Pushing Spring Boot Docker image to Docker Hub...'
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                       // sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                        sh 'echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USER} --password-stdin'
                    }
                    sh 'docker push medaminetrabelsi/devopsback'
                }
            }
        }
    }
        
    post {
        always {
            // Clean up the MySQL container after the build test
            sh 'docker rm -f mysql-test || true'
        }
        success {
            echo 'Build and Docker push succeeded for  backend !'
        }
        failure {
            echo 'Build or Docker push failed.'
        }
    }
}
