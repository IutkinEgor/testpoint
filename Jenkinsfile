pipeline {
    agent any
    tools {
        // Install the Gradle version configured and add it to the path.
        gradle "gradle_7_6_1"
    }
    environment {
        DOCKER_HUB = credentials('DOCKER_HUB')
    }
    stages {
        stage('Info'){
            steps{
                echo "Build number: $BUILD_NUMBER"
                echo "Job name: $JOB_NAME"
                echo "Git tag: $GIT_TAG"
            }
        }
        stage('Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/IutkinEgor/testpoint.git']])
            }
        }
        stage('Build') {
            steps {
                echo "Build Gradle Project"
                sh "gradle buildAndPutInDockerRoot"
            }
        }
        stage('Build docker image') {
            steps {
                dir('docker'){
                    sh "docker build -t $DOCKER_HUB_USR/testpoint:$GIT_TAG ."
                }
            }
        }
        stage('Push to docker hub') {
            steps {
                sh 'docker login -u $DOCKER_HUB_USR -p $DOCKER_HUB_PSW'
                sh "docker push $DOCKER_HUB_USR/testpoint:$GIT_TAG"
                sh "docker logout"
            }
        }
        stage('Run container') {
            steps {
                script {
                    def oldContainerId = sh(returnStdout: true, script: "docker ps --filter ancestor=testpoint --format='{{.ID}}'")
                    if(oldContainerId?.trim()) {
                        echo "Old container id: $oldContainerId"
                        sh("docker stop $oldContainerId")
                    }
                    def newContainerId = sh("docker run -d $DOCKER_HUB_USR/testpoint:$GIT_TAG")
                    def inspectResult = sh(returnStdout: true, script: "docker inspect --format='{{.State.Status}}' $newContainerId")
                    sleep time: 30, unit: 'SECONDS'
                    if (inspectResult != 'running') {
                        error "Container failed to start"
                        if(oldContainerId?.trim()) {
                            sh "docker run -d $oldContainerId"
                        }
                        sh "docker logs $newContainerId"
                        sh "docker rm -i $newContainerId"
                    }
                    else {
                        if(oldContainerId?.trim()) {
                            sh "docker rm -i $oldContainerId"
                        }
                    }
                }
            }
        }
    }
}
