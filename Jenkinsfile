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
        stage('CI: Build and Test') {
            steps {
                script {
   
                    // Construire l'application Spring Boot
                    dir('Backend') {
                        echo 'Building Spring Boot application...'
                        withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONARQUBE_TOKEN')]) {
                            sh "mvn clean package jacoco:report sonar:sonar -Dsonar.projectKey=5arctic3_g4_devops -Dsonar.login=${SONARQUBE_TOKEN}"
                        }
                    }

                    // Trouver la version du JAR
                    dir('Backend') {
                        env.JAR_FILE = sh(script: "ls target/*.jar | grep -v 'original' | head -n 1", returnStdout: true).trim()
                        echo "Using JAR file: ${env.JAR_FILE}"
                    }
                }
            }
        }

        stage('CD: Publish and Deploy') {
            parallel {
                stage('Publish to Nexus') {
                    steps {
                        dir('Backend') {
                            script {
                                def groupId = "tn.esprit"
                                def artifactId = "5ArcTIC3-G4-devops"
                                def version = "1.0"
                                def packaging = "jar"
                                def artifactPath = "target/5ArcTIC3-G4-devops-1.0.jar"
                                def pomFile = "pom.xml"

                                if (fileExists(artifactPath)) {
                                    echo "*** File: ${artifactPath}, group: ${groupId}, packaging: ${packaging}, version ${version}"

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
                        sh 'docker build -t medaminetrabelsi/devopsback -f Backend/Dockerfile .'
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed, whether successful or failed.'
            // Aucune action pour arrêter ou supprimer les conteneurs MySQL
        }
        success {
            echo 'Build, Nexus publish, and Docker image creation succeeded!'
        }
        failure {
            echo 'Build or publish failed.'
            // Ajoutez des notifications à votre équipe ici
        }
    }
}
