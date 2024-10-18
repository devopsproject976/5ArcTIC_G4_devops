pipeline {
    agent any
//test
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "localhost:8081"
        NEXUS_REPOSITORY = "maven-releases" // Update to match your repository name
        NEXUS_CREDENTIAL_ID = "NEXUS_CREDENTIALS" // Jenkins credentials ID for Nexus
        SONARQUBE_URL = 'http://localhost:9000'
        SONARQUBE_CREDENTIALS = 'SONARQUBE_CREDENTIALS_ID'
    }

    stages {
        stage('Start MySQL Container') {
            steps {
                script {
                    sh 'docker run -d --name mysql-test -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=test_db -p 3306:3306 mysql:5.7'
                }
            }
        }
        stage('Build Spring Boot') {
            steps {
                dir('Backend') {
                    echo 'Building Spring Boot application...'
                    sh 'mvn clean package -DskipTests=true '
                }
            }
        }

        // SonarQube analysis
        stage('SonarQube Analysis') {
            steps {
                dir('Backend') {
                    echo 'Running SonarQube analysis...'
                    withSonarQubeEnv('sonar-jenkins') { // SonarQube env configuration
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }

        // Ensure SonarQube analysis has passed
        /*stage('Quality Gate') {
            steps {
                echo 'Checking SonarQube Quality Gate...'
                waitForQualityGate abortPipeline: true // Abort the pipeline if the quality gate fails
            }
        }*/

        stage('Find JAR Version') {
            steps {
                dir('Backend') {
                    script {
                        env.JAR_FILE = sh(script: "ls target/*.jar | grep -v 'original' | head -n 1", returnStdout: true).trim()
                    }
                    echo "Using JAR file: ${env.JAR_FILE}"
                }
            }
        }

         stage("publish to nexus") {
            steps {
                script {
                    // Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
                    pom = readMavenPom file: "pom.xml";
                    // Find built artifact under target folder
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    // Print some info from the artifact found
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    // Extract the path from the File found
                    artifactPath = filesByGlob[0].path;
                    // Assign to a boolean response verifying If the artifact name exists
                    artifactExists = fileExists artifactPath;

                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";

                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: ARTIFACT_VERSION,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                // Artifact generated such as .jar, .ear and .war files.
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging]
                            ]
                        );

                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
                }
            }
        }

    /*stage('Build and Push Docker Image') {
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
    }*/

    post {
        always {
            sh 'docker rm -f mysql-test || true'
        }
        success {
            echo 'Build and Nexus publish succeeded!'
        }
        failure {
            echo 'Build or Nexus publish failed.'
        }
    }
}
