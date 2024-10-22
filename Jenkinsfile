pipeline {
    agent any
    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'localhost:8081', description: 'Nexus URL')
        string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-snapshots', description: 'Nexus Repository Name')
        string(name: 'MYSQL_VERSION', defaultValue: '5.7', description: 'MySQL Docker Image Version')
        string(name: 'SONARQUBE_URL', defaultValue: 'http://localhost:9000', description: 'SonarQube URL')
    }
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_CREDENTIAL_ID = "NEXUS_CREDENTIALS"
        SONARQUBE_CREDENTIALS = 'SONARQUBE_CREDENTIALS_ID'
        RECIPIENTS = "daadsoufi0157@gmail.com"
    }

    stages {
        stage('Setup Environment') {
            steps {
                echo 'Starting MySQL container...'
                script {
                    try {
                        sh "docker run -d --name mysql-test -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=test_db -p 3306:3306 mysql:${params.MYSQL_VERSION}"
                    } catch (Exception e) {
                        error "Failed to start MySQL container: ${e.message}"
                    }
                }
            }
        }

        stage('Build') {
            steps {
                dir('Backend') {
                    echo 'Building Spring Boot application...'
                    sh 'mvn clean package -DskipTests=true'
                }
            }
        }

        stage('Code Analysis') {
            steps {
                dir('Backend') {
                    echo 'Running SonarQube analysis...'
                    withSonarQubeEnv('sonar-jenkins') {
                        sh 'mvn sonar:sonar -Dsonar.projectKey=5ArcTIC3-G4-devops -Dsonar.host.url=${SONARQUBE_URL} '
                    }
                }
            }
        }

        stage('Export SonarQube Issues') {
            steps {
                echo 'Exporting SonarQube issues...'
                script {
                    def sonarProjectKey = '5ArcTIC3-G4-devops'
                    def issuesFilePath = 'sonarqube_issues.json'
                    def csvFilePath = 'sonarqube_issues.csv'

                    // Export issues as JSON and convert to CSV
                    sh """
                        curl -u ${SONARQUBE_CREDENTIALS}: ${params.SONARQUBE_URL}/api/issues/search?componentKeys=${sonarProjectKey} > ${issuesFilePath}
                        cat ${issuesFilePath} | jq -r '.issues[] | [.key, .severity, .message, .rule, .component] | @csv' > ${csvFilePath}
                    """
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

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t soufi2001/devopsback:5arctic3-g4-devops -f Backend/Dockerfile .'
            }
        }

        stage('Push Docker Image') {
            steps {
                echo 'Pushing Docker image to DockerHub...'
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                    }
                    sh 'docker push soufi2001/devopsback:5arctic3-g4-devops'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'sonarqube_issues.json, sonarqube_issues.csv', allowEmptyArchive: true

            // Stop MySQL container
            script {
                try {
                    sh 'docker rm -f mysql-test || true'
                } catch (Exception e) {
                    echo "Failed to stop MySQL container: ${e.message}"
                }
            }
        }

        success {
            echo 'Build and deployment succeeded!'

            script {
                def subject = "Jenkins Build Success - ${env.JOB_NAME} #${env.BUILD_NUMBER}"
                def body = """
                    Hi team,

                    The Jenkins build for ${env.JOB_NAME} (Build #${env.BUILD_NUMBER}) was successful.
                    Please find attached the SonarQube issues report for review.

                    Thanks,
                    Jenkins Pipeline
                """
                emailext(
                    to: "${RECIPIENTS}",
                    subject: subject,
                    body: body,
                    attachmentsPattern: 'sonarqube_issues.json, sonarqube_issues.csv'
                )
            }
        }

        failure {
            echo 'Build or deployment failed!'

            script {
                def subject = "Jenkins Build Failed - ${env.JOB_NAME} #${env.BUILD_NUMBER}"
                def body = """
                    Hi team,

                    The Jenkins build for ${env.JOB_NAME} (Build #${env.BUILD_NUMBER}) has failed.
                    Please review the logs for more information.

                    Thanks,
                    Jenkins Pipeline
                """
                emailext(
                    to: "${RECIPIENTS}",
                    subject: subject,
                    body: body
                )
            }
        }
    }
}
