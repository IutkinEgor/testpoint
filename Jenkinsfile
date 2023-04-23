pipeline {
    agent any

    tools {
       // Install the Gradle version configured and add it to the path.
        gradle "gradle_7_6_1"
    }

    stages {
        stage('Build Gradle') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/IutkinEgor/testpoint.git']])
                sh "gradle buildAndPutInDockerRoot"
            }
        }
        stage('Build docker image') {
            steps {
                    sh "cd docker"
                    sh "docker build -t iutkined/testpoint ."
            }
        }
    }
}
