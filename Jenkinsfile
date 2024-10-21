pipeline {
    agent any

    stages {

        stage('Start MySQL Container') {
            steps {
                script {
                    // Remove existing container if it exists
                    sh 'docker rm -f mysql-test || true'
                    
                    // Check if the MySQL port is in use
                    script {
                        def portInUse = sh(script: 'lsof -i :3307', returnStatus: true)
                        if (portInUse == 0) {
                            error("Port 3306 is already in use. Please stop the service using this port.")
                        }
                    }

                    // Start a new MySQL container with root password
                    sh 'docker run -d --name mysql-test -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=hamouda -p 3307:3307 mysql:5.7'
                }
            }
        }
           stage('SonarQube Analysis') {
                            steps {
                                dir('Backend') {
                                    echo 'Running SonarQube analysis...'
                                    withSonarQubeEnv('sonar-jenkins') { // SonarQube env configuration
                                        sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=5ArcTIC3-G4-devops -Dsonar.host.url=http://192.168.157.133:9000 -Dsonar.login=${SONAR_AUTH_TOKEN}'
                                    }
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
                sh 'docker build -t hamoudaatti/backdevops -f Backend/Dockerfile .'
            }
        }

        stage('Push Spring Docker Image to Docker Hub') {
            steps {
                echo 'Pushing Spring Boot Docker image to Docker Hub...'
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                    }
                    sh 'docker push hamoudaatti/backdevops'
                }
            }
        }
    }

    post {
        always {
            // Clean up the MySQL container after the build
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
