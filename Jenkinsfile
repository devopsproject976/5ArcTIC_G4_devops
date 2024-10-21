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
                    try {
                        sh "docker run -d --name mysql-test -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=test_db -p 3306:3306 mysql:${params.MYSQL_VERSION}"
                    } catch (Exception e) {
                        error "Failed to start MySQL container: ${e.message}"
                    }
                }
            }
        }

        
        stage('Build and Analyze') {
            parallel {
                stage('Build Spring Boot') {
                    steps {
                        dir('Backend') {
                            echo 'Building Spring Boot application...'
                            sh 'mvn clean package -DskipTests=true'
                        }
                    }
                }

                stage('SonarQube Analysis') {
                    steps {
                        dir('Backend') {
                            echo 'Running SonarQube analysis...'
                            withSonarQubeEnv('sonar-jenkins') { // SonarQube env configuration
                                sh 'mvn clean package jacoco:report sonar:sonar -Dsonar.projectKey=5arctic3_g4_devops -Dsonar.login=admin -Dsonar.password=admin'
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
                                    error "No JAR file found in target directory."
                                }
                            }
                            echo "Using JAR file: ${env.JAR_FILE}"
                        }
                    }
                }
            }
        }

        stage('Publish to Nexus and Build Docker Image') {
            parallel {
                stage('Publish to Nexus') {
                    steps {
                        dir('Backend') {
                            script {
                                pom = readMavenPom file: "pom.xml"
                                filesByGlob = findFiles(glob: "target/*.${pom.packaging}")
                                artifactPath = filesByGlob[0]?.path

                                if (artifactPath && fileExists(artifactPath)) {
                                    echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}"
                                    nexusArtifactUploader(
                                        nexusVersion: NEXUS_VERSION,
                                        protocol: NEXUS_PROTOCOL,
                                        nexusUrl: params.NEXUS_URL,
                                        groupId: pom.groupId,
                                        version: pom.version,
                                        repository: params.NEXUS_REPOSITORY,
                                        credentialsId: NEXUS_CREDENTIAL_ID,
                                        artifacts: [
                                            [artifactId: pom.artifactId, classifier: '', file: artifactPath, type: pom.packaging],
                                            [artifactId: pom.artifactId, classifier: '', file: "pom.xml", type: "pom"]
                                        ]
                                    )
                                } else {
                                    error "*** File could not be found or does not exist."
                                }
                            }
                        }
                    }
                }

                stage('Build and Push Docker Image') {
                    steps {
                        echo 'Building Docker image...'
                        sh 'docker build -t SofienDaadoucha-5ArcTIC3-G4-devops .'

                        echo 'Pushing Docker image to DockerHub...'
                        script {
                            withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                                sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                            }
                            sh 'docker push SofienDaadoucha-5ArcTIC3-G4-devops'
                        }
                    }
                }
            }
        
    

    post {
        always {
            script {
                try {
                    sh 'docker rm -f mysql-test || true'
                } catch (Exception e) {
                    echo "Failed to remove MySQL container: ${e.message}"
                }
            }
        }
        success {
            echo 'Build and Nexus publish succeeded!'
        }
        failure {
            echo 'Build or Nexus publish failed.'
        }
    }
}
