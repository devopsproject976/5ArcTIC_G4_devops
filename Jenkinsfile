pipeline {
    agent any
    environment {
        DOCKER_HUB_CREDENTIALS = 'DOCKERHUB_CREDENTIALS' // Remplacez par votre ID de credentials Docker Hub
        SONARQUBE_CREDENTIALS = 'sonarqube-token' // ID de credentials SonarQube
        NEXUS_CREDENTIALS = 'nexus-credentials' // ID de credentials Nexus
        NEXUS_URL = 'http://localhost:8081/repository/maven-releases/' // Remplacez par l'URL de votre Nexus
    }
    stages {
        stage('Clone Repository') {
            steps {
                checkout scm
            }
        }
        stage('Build with Maven') {
            steps {
                script {
                    // Ajoutez des options de build ici si nécessaire
                    sh 'mvn clean package jacoco:report'
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: SONARQUBE_CREDENTIALS, variable: 'SONAR_TOKEN')]) {
                    script {
                        sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
                    }
                }
            }
        }
        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: NEXUS_CREDENTIALS, usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    script {
                        // Push l'artefact vers Nexus
                        sh """
                            mvn deploy:deploy-file \
                            -DgroupId=com.example \
                            -DartifactId=your-artifact-id \
                            -Dversion=1.0.0 \
                            -Dpackaging=jar \
                            -Dfile=target/your-artifact.jar \
                            -DrepositoryId=nexus-repo \
                            -Durl=${NEXUS_URL}
                        """
                    }
                }
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    // Authentification sur Docker Hub
                    withCredentials([usernamePassword(credentialsId: DOCKER_HUB_CREDENTIALS, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                        sh 'docker build -t medaminetrabelsi/devopsback ./Backend'
                        sh 'docker build -t medaminetrabelsi/devopsfront ./Frontend'
                    }
                }
            }
        }
        stage('Docker Push') {
            steps {
                script {
                    sh 'docker push medaminetrabelsi/devopsback'
                    sh 'docker push medaminetrabelsi/devopsfront'
                }
            }
        }
    }
    post {
        always {
            // Actions à effectuer après l'exécution du pipeline, comme envoyer des notifications
            cleanWs() // Nettoyer l'espace de travail
        }
        failure {
            // Actions spécifiques en cas d'échec
            echo 'Pipeline a échoué !'
        }
    }
}
