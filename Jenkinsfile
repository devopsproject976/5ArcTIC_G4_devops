pipeline {
    agent any
     parameters {
            string(name: 'NEXUS_URL', defaultValue: 'localhost:8090', description: 'Nexus URL')
            string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-snapshots', description: 'Nexus Repository Name')
        }
        environment {
            NEXUS_VERSION = "nexus3"
            NEXUS_PROTOCOL = "http"
            NEXUS_CREDENTIAL_ID = "nexus"
        }

    stages {

     /*   stage('Start MySQL Container') {
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
                                    withSonarQubeEnv('hamouda') { // SonarQube env configuration
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
        }*/
           stage('Setup Application Environment (MySQL, Spring Boot, Angular)') {
                    steps {
                        echo 'Starting application environment (MySQL, Spring Boot, Angular) with Docker Compose...'
                        sh 'docker-compose -f docker-compose-app.yml up -d'
                    }
                }

           stage('Build Springboot and Code Analysis') {
                 steps {
                     dir('Backend') {
                         echo 'Building Spring Boot application and Running SonarQube analysis...'
                         withSonarQubeEnv('sonar-jenkins') {
                             sh 'mvn clean package jacoco:report sonar:sonar -Dsonar.projectKey=5arctic3_g4_devops '
                         }
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
        stage('Publish to Nexus') {
                    steps {
                        dir('Backend') {
                            script {
                                pom = readMavenPom file: "pom.xml"
                                artifactPath = findFiles(glob: "target/*.${pom.packaging}")[0]?.path

                                if (artifactPath && fileExists(artifactPath)) {
                                    echo "*** Publishing artifact: ${artifactPath}, version: ${pom.version}"
                                    nexusArtifactUploader(
                                        nexusVersion: NEXUS_VERSION,
                                        protocol: NEXUS_PROTOCOL,
                                        nexusUrl: params.NEXUS_URL,
                                        groupId: pom.groupId,
                                        version: pom.version,
                                        repository: params.NEXUS_REPOSITORY,
                                        credentialsId: NEXUS_CREDENTIAL_ID,
                                        artifacts: [
                                            [artifactId: pom.artifactId, file: artifactPath, type: pom.packaging],
                                            [artifactId: pom.artifactId, file: "pom.xml", type: "pom"]
                                        ]
                                    )
                                } else {
                                    error "*** Artifact not found or does not exist."
                                }
                            }
                        }
                    }
                }

       /* stage('Build Spring Docker Image') {
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
*/
   post {
         always {
             script {
                 try {
                     echo 'Cleaning up Docker Compose environments...'
                    // sh 'docker-compose -f docker-compose-tools.yml down -v'
                     sh 'docker-compose -f docker-compose-app.yml down -v'
                 } catch (Exception e) {
                     echo "Failed to stop Docker Compose containers: ${e.message}"
                 }
             }
         }
         success {
             echo 'Pipeline completed successfully!'
         }
         failure {
             echo 'Pipeline encountered an error.'
         }
     }
 }
