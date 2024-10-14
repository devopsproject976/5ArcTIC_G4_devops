pipeline {
    agent any

    stages {

        stage('Start MySQL Container') {
    steps {
        echo 'Starting MySQL container...'
        script {
            // Pull and run the MySQL container without a root password
            sh 'docker run -d --name mysql-test -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=test_db -p 3306:3306 mysql:5.7'
            // Wait for MySQL to be fully up and running
            sh 'sleep 30'
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
                sh 'docker build -t soufi2001/devopsback -f Backend/Dockerfile .'
            }
        }

        stage('Push Spring Docker Image to Docker Hub') {
            steps {
                echo 'Pushing Spring Boot Docker image to Docker Hub...'
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                    }
                    sh 'docker push soufi2001/devopsback'
                }
            }
        }

        
    post {
        success {
            echo 'Build and Docker push succeeded for  backend !'
        }
        failure {
            echo 'Build or Docker push failed.'
        }
    }
}
