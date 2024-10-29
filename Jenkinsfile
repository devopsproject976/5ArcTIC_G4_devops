pipeline {
    agent any

    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'localhost:8081', description: 'Nexus URL')
        string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-releases', description: 'Nexus Repository Name')
        string(name: 'SONARQUBE_URL', defaultValue: 'http://localhost:9000', description: 'SonarQube URL')
    }

    environment {

        DB_URL = 'jdbc:mysql://localhost:3306/devops'
        DB_USER = 'root'
        DB_PASSWORD = ''
        DOCKER_REGISTRY = 'aichanciri'
        DOCKER_IMAGE_BACKEND = 'devopsback'
        DOCKER_IMAGE_FRONTEND = 'devopsfront'
        SONAR_SCANNER_HOME = tool 'SonarQube Scanner'
        SONAR_PROJECT_KEY = 'devops-backend'
        SONAR_TOKEN = credentials('SonarQube_Token')
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'AichaNciri_5Arctic3_G4',
                    url: 'https://github.com/devopsproject976/devops.git',
                    credentialsId: 'dev'
            }
        }

        stage('Backend Pipeline') {
            parallel {
                stage('MySQL Setup') {
                    steps {
                        script {
                            sh 'docker rm -f mysql-test || true'
                            sh 'docker run -d --name mysql-test -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=test_db -p 3306:3306 mysql:5.7'
                        }
                    }
                }

                stage('Build Backend') {
                    steps {
                        dir('Backend') {
                            sh 'mvn clean package -X'
                        }
                    }
                }

                stage('Test Backend') {
                    steps {
                        dir('Backend') {
                            sh 'mvn test'
                        }
                    }
                }

                stage('JaCoCo Report') {
                    steps {
                        dir('Backend') {
                            sh 'mvn jacoco:report'
                        }
                    }
                }

                stage('SonarQube Analysis') {
                    steps {
                        script {
                            dir('Backend') {
                                withSonarQubeEnv('SonarQube Scanner') {
                                    sh "${SONAR_SCANNER_HOME}/bin/sonar-scanner " +
                                        "-Dsonar.projectKey=${SONAR_PROJECT_KEY} " +
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
                    }
                }

                stage('Docker Build Backend') {
                    steps {
                        dir('Backend') {
                            sh "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE_BACKEND}:latest ."
                        }
                    }
                }
            }
        }

        stage('Frontend Pipeline') {
            parallel {
                stage('Docker Build Frontend') {
                    steps {
                        dir('Frontend') {
                            sh "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE_FRONTEND}:latest ."
                        }
                    }
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                        sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_BACKEND}:latest"
                        sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_FRONTEND}:latest"
                    }
                }
            }
        }
        stage('Docker Compose Up') {
            steps {
                script {

                    sh 'docker-compose down || true' // Arrête les services en cours si nécessaire
                    sh 'docker-compose up -d' // Lancement en arrière-plan
                }
            }
        }
    }


    post {
        always {
            sh 'docker rm -f mysql-test || true'
             sh 'docker-compose down' // Nettoyage à la fin de la pipeline
              archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true  // Archive artifacts
        }
        success {
            junit 'Backend/target/surefire-reports/*.xml'
            echo 'Build and tests succeeded!'
        }
        failure {
            echo 'Build or test failed.'
        }
    }
}
