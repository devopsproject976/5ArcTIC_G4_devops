pipeline {
    agent any
    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'localhost:8081', description: 'Nexus URL')
        string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-snapshots', description: 'Nexus Repository Name') // Changement ici pour utiliser les snapshots
    }
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_CREDENTIAL_ID = "nexus-credentials" // Jenkins credentials ID for Nexus
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm // Utilise la configuration SCM par défaut....
            }
        }

        // Backend stages
        stage('Build Spring Boot') {
            steps {
                dir('Backend') {
                    echo 'Building Spring Boot application...'
                    script {
                        withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
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
                        env.JAR_FILE = sh(script: "ls target/*.jar | grep -v 'original' | head -n 1", returnStdout: true).trim()
                        if (!env.JAR_FILE) {
                            error "Le fichier JAR n'a pas été trouvé dans le répertoire target."
                        }
                    }
                    echo "Using JAR file: ${env.JAR_FILE}"
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
                                def version = "1.0" // Modifié ici pour inclure le suffixe -SNAPSHOT
                                def packaging = "jar"  // Based on your project packaging
                                def artifactPath = "target/${artifactId}-${version}.jar"
                                def pomFile = "pom.xml"

                                // Check if the artifact exists
                                if (fileExists(artifactPath)) {
                                    echo "*** File: ${artifactPath}, group: ${groupId}, packaging: ${packaging}, version: ${version}"

                                    // Upload artifact and POM to Nexus
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
                } // Closing the Publish to Nexus stage

                stage('Build Spring Docker Image') {
                    steps {
                        echo 'Building Docker image for Spring Boot...'
                        script {
                            // Vérification que JAR_FILE est défini avant la construction de l'image Docker
                            if (!env.JAR_FILE) {
                                error "La variable d'environnement JAR_FILE n'est pas définie."
                            }
                            sh "docker build --build-arg JAR_FILE=${env.JAR_FILE} -t medaminetrabelsi/devopsback -f Backend/Dockerfile ."
                        }
                    }
                }
            } // Closing the parallel block
        }
    }

    post {
        success {
            echo 'Build and Docker push succeeded for backend!'
        }
        failure {
            echo 'Build or Docker push failed.'
        }
    }
}
