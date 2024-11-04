pipeline {
    agent any

    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'localhost:8081', description: 'Nexus URL')
        string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-releases', description: 'Nexus Repository Name')
        string(name: 'SONARQUBE_URL', defaultValue: 'http://localhost:9001', description: 'SonarQube URL')
        string(name: 'PROMETHEUS_URL', defaultValue: 'http://localhost:9090', description: 'Prometheus URL')
        string(name: 'GRAFANA_URL', defaultValue: 'http://localhost:3000', description: 'Grafana URL')
    }

    environment {
        // MySQL for Spring Boot
        DB_URL = 'jdbc:mysql://localhost:3307/devops' // Port 3307 as per your docker-compose
        DB_USER = 'root'
        DB_PASSWORD = '' // Update to a strong password in production

        // SonarQube PostgreSQL details
        SONAR_DB_URL = 'jdbc:postgresql://sonar_db:5432/sonar' // Assuming the container is networked correctly
        SONAR_DB_USER = 'sonar'
        SONAR_DB_PASSWORD = 'sonar'

        DOCKER_USERNAME = credentials('dockerhub-token')
        DOCKER_PASSWORD = credentials('dockerhub-token')
        IMAGE_NAME_BACKEND = 'nourbkh/devops-backend'
        IMAGE_TAG_BACKEND = 'nourbenkhairia-5arctic3-g4-devops'
        IMAGE_NAME_FRONTEND = 'nourbkh/devops-frontend'
        IMAGE_TAG_FRONTEND = 'nourbenkhairia-5arctic3-g4-devopsfrontend'
        SONAR_SCANNER_HOME = tool 'SonarQube Scanner'
        SONAR_PROJECT_KEY_BACKEND = 'devops-backend'
        SONAR_PROJECT_KEY_FRONTEND = 'devops-frontend'
        SONAR_TOKEN = credentials('sonarqube-token')
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
        MAVEN_REPO_ID = 'nexus'
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        CHROME_BIN = '/usr/bin/google-chrome'
        NOTIFICATION_EMAIL = 'nour.benkairia@gmail.com'
    }

    tools {
            nodejs 'Node 23' // Add the Node.js installation configured in Jenkins
        }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'nourbenkhairia_5Arctic3_G4',
                    url: 'https://github.com/devopsproject976/5ArcTIC_G4_devops.git',
                    credentialsId: 'devops-pipeline'
            }
        }



        stage('Check MySQL Connectivity') {
            steps {
                script {
                    sh '''
                    mysql -h mysql -P 3306 -u root -e ";" || echo "MySQL is not ready yet"
                    '''
                }
            }
        }



        stage('Install Chrome') {
            steps {
                sh 'wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb'
                sh 'echo "nourtest" | sudo -S apt-get install -y ./google-chrome-stable_current_amd64.deb'
            }
        }






        stage('Build Backend and Frontend') {
                    parallel {
                        stage('Build Backend') {
                            steps {
                                dir('Backend') {
                                    sh 'mvn clean package'
                                }
                            }
                        }
                        stage('Build Frontend') {
                            steps {
                                dir('Frontend') {
                                    //sh 'npm install' // Install dependencies
                                    //sh 'npm run build' // Build frontend
                                }
                            }
                        }
                    }
                }

                stage('Test Backend and Frontend') {
                    parallel {
                        stage('Test Backend') {
                            steps {
                                dir('Backend') {
                                    sh 'mvn test'
                                    sh 'mvn jacoco:report'
                                }
                            }
                        }
                        stage('Test Frontend') {
                            steps {
                                dir('Frontend') {
                                    //sh 'npm test'
                                }
                            }
                        }
                    }
                }

                stage('SonarQube Analysis Backend and Frontend') {
                    parallel {
                        stage('SonarQube Analysis Backend') {
                            steps {
                                script {
                                    dir('Backend') {
                                        withSonarQubeEnv('SonarQube') {
                                            sh "${SONAR_SCANNER_HOME}/bin/sonar-scanner " +
                                                "-Dsonar.projectKey=${SONAR_PROJECT_KEY_BACKEND} " +
                                                "-Dsonar.sources=src " +
                                                "-Dsonar.java.binaries=target/classes " +
                                                "-Dsonar.host.url=${params.SONARQUBE_URL} " +
                                                "-Dsonar.login=${env.SONAR_TOKEN} " +
                                                "-Dsonar.jacoco.reportPaths=target/jacoco.exec"
                                        }
                                    }
                                }
                            }
                        }
                        stage('SonarQube Analysis Frontend') {
                            steps {
                                script {
                                    dir('Frontend') {
                                        /*withSonarQubeEnv('SonarQube') {
                                            sh "${SONAR_SCANNER_HOME}/bin/sonar-scanner " +
                                                "-Dsonar.projectKey=${SONAR_PROJECT_KEY_FRONTEND} " +
                                                "-Dsonar.sources=src " +
                                                "-Dsonar.host.url=${params.SONARQUBE_URL} " +
                                                "-Dsonar.login=${env.SONAR_TOKEN}"
                                        }*/
                                    }
                                }
                            }
                        }
                    }
                }

        stage('Build Docker Images') {
            steps {
                // Build Docker image for backend
                dir('Backend') {
                    sh "docker build -t ${IMAGE_NAME_BACKEND}:${IMAGE_TAG_BACKEND} ."
                }

                // Build Docker image for frontend
                dir('Frontend') {
                    sh "docker build -t ${IMAGE_NAME_FRONTEND}:${IMAGE_TAG_FRONTEND} ."
                }
            }
        }


        stage('Verify JAR Creation') {
                    steps {
                        dir('Backend') {
                            sh 'ls -l target/'
                        }
                    }
                }

                 /* stage('Publish to Nexus') {
                    steps {
                        script {
                            echo "NEXUS_URL: ${NEXUS_URL}"
                             dir('Backend') {
                                                 script {
                                                     // Define artifact details based on the known pom.xml values
                                                             def groupId = "tn.esprit"
                                                             def artifactId = "5ArcTIC3-G4-devops"
                                                             def version = "1.0"
                                                             def packaging = "jar"  // Based on your project packaging
                                                             def artifactPath = "target/5ArcTIC3-G4-devops-1.0.jar"
                                                             def pomFile = "pom.xml"

                                                     // Check if the artifact exists
                                                     if (fileExists(artifactPath)) {
                                                         echo "*** File: ${artifactPath}, group: ${groupId}, packaging: ${packaging}, version ${version}"

                                                         // Upload artifact and POM to Nexus
                                                         nexusArtifactUploader(
                                                             nexusVersion: NEXUS_VERSION,
                                                             protocol: NEXUS_PROTOCOL,
                                                             nexusUrl: params.NEXUS_URL,
                                                             groupId: groupId,
                                                             artifactId: artifactId,
                                                             version: version,
                                                             repository: params.NEXUS_REPOSITORY,
                                                             credentialsId: 'nexus-credentials',
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

                            // Publish the frontend artifact to Nexus (assuming it's a JAR for this example)
                            dir('Frontend') {
                                //sh """
                                //mvn deploy:deploy-file \
                                    //-DgroupId=com.example \
                                    //-DartifactId=devops-frontend \
                                    //-Dversion=${IMAGE_TAG_FRONTEND} \
                                    //-Dpackaging=jar \
                                    //-Dfile=target/devops-frontend-${IMAGE_TAG_FRONTEND}.jar \
                                    //-DrepositoryId=${MAVEN_REPO_ID} \
                                    //-Durl=${NEXUS_PROTOCOL}://${NEXUS_URL}/repository/${NEXUS_REPOSITORY} \
                                    //-DskipTests
                                //"""
                            }
                        }
                    }
                }*/




        /*stage('Push Docker Images') {
            steps {
                script {
                    // Push Docker image for backend
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-token',
                                                      usernameVariable: 'DOCKER_USERNAME',
                                                      passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh """
                        echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
                        docker push ${IMAGE_NAME_BACKEND}:${IMAGE_TAG_BACKEND}
                        docker push ${IMAGE_NAME_FRONTEND}:${IMAGE_TAG_FRONTEND}
                        """
                    }
                }
            }
        }*/







        stage('Stop Services') {
            steps {
                script {
                    // Stop and remove services
                    sh 'docker-compose -f docker-compose.yml -f docker-compose-tools.yml down'
                }
            }
        }

    }

    post {
        always {
            script {
                // Cleanup or additional actions can be performed here
                sh 'docker-compose -f docker-compose.yml -f docker-compose-tools.yml down'
            }
        }
        success {
            echo 'Build and tests succeeded!'


        }
        failure {
            echo 'Build or test failed.'
        }


    }
}
