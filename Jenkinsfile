pipeline {
    agent any
    tools {
        // Install the Gradle version configured and add it to the path.
        gradle "gradle_7_6_1"
        gradle "openjdk_17"
    }
    environment {
        DOCKER_HUB = credentials('DOCKER_HUB')
    }
    stages {
        stage('Info'){
            steps{
                echo "Build number: ${BUILD_NUMBER}"
                echo "Job name: ${JOB_NAME}"
                echo "Git tag: ${GIT_TAG}"
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
                    sh "docker build -t $DOCKER_HUB_USR/testpoint:${GIT_TAG} ."
                }
            }
        }
        stage('Push to docker hub') {
            steps {
                sh 'docker login -u $DOCKER_HUB_USR -p $DOCKER_HUB_PSW'
                sh "docker push -t $DOCKER_HUB_USR/testpoint:${GIT_TAG} ."
                sh "docker logout"
            }
        }
    }
}
