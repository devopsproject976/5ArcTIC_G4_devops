pipeline {
    agent any
    tools {
        nodejs 'NodeJS' // Use the name you configured in Jenkins
    }
    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'localhost:8081', description: 'Nexus URL')
        string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-snapshots', description: 'Nexus Repository Name')
        string(name: 'MYSQL_VERSION', defaultValue: '5.7', description: 'MySQL Docker Image Version')
        string(name: 'SONARQUBE_URL', defaultValue: 'http://localhost:9000', description: 'SonarQube URL')
        string(name: 'FRONTEND_IMAGE_NAME', defaultValue: 'angular-frontend', description: 'Docker image name for Angular')

    }
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_CREDENTIAL_ID = "NEXUS_CREDENTIALS"
        SONARQUBE_CREDENTIALS = 'SONARQUBE_CREDENTIALS_ID'
        //RECIPIENTS = "daadsoufi0157@gmail.com"
        //MYSQL_CONTAINER_NAME = 'mysql-container'
       // SPRINGBOOT_CONTAINER_NAME = 'springboot-container'
        //DOCKER_COMPOSE_FILE = 'docker-compose.yml'
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

        stage('Build springboot backend') {
            steps {
                dir('Backend') {
                    echo 'Building Spring Boot application...'
                    sh 'mvn clean package -DskipTests=true'
                }
            }
        }

        stage('Test Angular Application') {
            steps {
                dir('Frontend') { // Assuming your Angular project is in a directory named Frontend
                    echo 'Installing Angular dependencies...'
                    sh 'npm install'
                    echo 'Running Angular tests...'
                    sh 'npm test -- --watch=false --bail' // Run tests without watch mode
                }
            }
        }

        stage('Build Angular App') {
            steps {
                dir('Frontend') {
                    echo 'Building Angular application...'
                    sh 'npm run build --prod' // Adjust according to your build script
                }
            }
        }

        stage('Code Analysis') {
            steps {
                dir('Backend') {
                    echo 'Running SonarQube analysis...'
                    withSonarQubeEnv('sonar-jenkins') {
                        sh 'mvn jacoco:report sonar:sonar -Dsonar.projectKey=5ArcTIC3-G4-devops -Dsonar.host.url=${SONARQUBE_URL} '
                    }
                }
            }
        }

         /*stage('Export SonarQube Metrics') {
    steps {
        echo 'Exporting SonarQube metrics...'
        script {
            def sonarProjectKey = '5ArcTIC3-G4-devops'
            def metricsFilePath = 'codeReport.json'
            
            // Use SonarQube environment to access credentials and URL securely
            withSonarQubeEnv('sonar-jenkins') {
                // Fetch SonarQube metrics using curl (ensure the token is correctly passed)
                sh """
                    curl -s -u ${SONAR_AUTH_TOKEN}: ${SONARQUBE_URL}/api/measures/component_tree?ps=100 \
                    --get --data-urlencode "component=${sonarProjectKey}" \
                    --data-urlencode "s=qualifier,name" \
                    --data-urlencode "strategy=children" \
                    --data-urlencode "metricKeys=ncloc,bugs,vulnerabilities,code_smells,security_hotspots,coverage,duplicated_lines_density" \
                    -o ${metricsFilePath}
                """

                // Validate if the file was created and has content
                if (fileExists(metricsFilePath) && readFile(metricsFilePath).trim()) {
                    echo "SonarQube metrics successfully exported to ${metricsFilePath}"
                } else {
                    error "Failed to export SonarQube metrics."
                }
            }
        }
    }
}





        stage('Send Email Notification') {
    steps {
        script {
            def metricsFilePath = 'codeReport.json'  // The file from the metrics stage
            
            // Check if the file exists
            if (fileExists(metricsFilePath)) {
                emailext(
                    subject: "SonarQube Metrics Report",
                    body: """
                        Hello,

                        Please find attached the SonarQube metrics report for project 5ArcTIC3-G4-devops.

                        Regards,
                        Jenkins
                    """,
                    to: "daadsoufi0157@gmail.com",
                    attachmentsPattern: metricsFilePath,  // Attach the metrics file
                    mimeType: 'application/json'  // Since the file is a JSON
                )
            } else {
                error "Metrics file (${metricsFilePath}) not found, cannot send email."
            }
        }
    }
}*/

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

        /*stage('Push Docker Image') {
            steps {
                echo 'Pushing Docker image to DockerHub...'
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                    }
                    sh 'docker push soufi2001/devopsback:5arctic3-g4-devops'
                }
            }*/
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
