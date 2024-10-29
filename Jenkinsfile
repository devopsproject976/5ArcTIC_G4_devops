pipeline {
    agent any
    tools {
        nodejs 'NodeJS' // Use the name you configured in Jenkins
    }
    
    parameters {
        string(name: 'NEXUS_URL', defaultValue: 'localhost:8081', description: 'Nexus URL')
        string(name: 'NEXUS_REPOSITORY', defaultValue: 'maven-snapshots', description: 'Nexus Repository Name')
        string(name: 'MYSQL_VERSION', defaultValue: '5.7', description: 'MySQL Docker Image Version')
        string(name: 'SONARQUBE_URL', defaultValue: 'http://localhost:9001', description: 'SonarQube URL')

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

        
        stage('Setup Tool Environment (Nexus, SonarQube)') {
            steps {
                script {
                    // Start docker-compose in detached mode
                    sh 'docker-compose up -d'

                    // Wait for services to be ready
                    sh '''
                    echo "Waiting for all services to start..."
                    until [ "`docker inspect -f {{.State.Running}} mysqldb`" == "true" ] &&
                          [ "`docker inspect -f {{.State.Running}} sonarqube`" == "true" ] &&
                          [ "`docker inspect -f {{.State.Running}} sonar_db`" == "true" ]; do
                      echo "Waiting for services to be up..."
                      sleep 10
                    done
                    echo "All services are up and running."
                    '''
                }
                
            }
        }

        /*stage('Setup Application Environment (MySQL, Spring Boot, Angular)') {
            steps {
                echo 'Starting application environment (MySQL, Spring Boot, Angular) with Docker Compose...'
                sh 'docker compose -f docker-compose.yml up '
            }    
        */

        

        /*stage('Build Angular Application') {
            steps {
                dir('Frontend') {
                    echo 'Building Angular application...'
                    sh 'npm install'
                    sh 'npm run build --prod' // Adjust based on your Angular build script
                }
            }
        }*/

        

        stage(' Code Analysis') {
            steps {
                dir('Backend') {
                    echo ' Running SonarQube analysis...'
                    withSonarQubeEnv('sonar-jenkins') {
                        sh 'mvn  jacoco:report sonar:sonar -Dsonar.projectKey=5arctic3_g4_devops -DskipTests '
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

        /*stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t soufi2001/devopsback:5arctic3-g4-devops -f Backend/Dockerfile .'
                sh 'docker build --no-cache -t soufi2001/devopsfront:5arctic3-g4-devops -f Frontend/Dockerfile .'
            }
        }*/

        /*stage('Push Docker Image') {
            steps {
                echo 'Pushing Docker image to DockerHub...'
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}'
                    }
                    sh 'docker push soufi2001/devopsback:5arctic3-g4-devops'
                    sh 'docker push soufi2001/devopsfront:5arctic3-g4-devops'
                    
                }
            }
        }*/

        

    }
    

   post {
        always {
            script {
                try {
                    echo 'Cleaning up Docker Compose environments...'
                   // sh 'docker-compose -f docker-compose-tools.yml down -v'
                    //sh 'docker-compose down '
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

