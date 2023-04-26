pipeline {
    agent any
    tools {
        gradle "gradle_7_6_1"
    }
    environment {
        DOCKER_HUB = credentials('DOCKER_HUB')
        NEW_CONTAINER_ID = ""
        OLD_CONTAINER_ID = ""
        NEW_IMAGE_TAG = ""
        OLD_IMAGE_TAG = ""
        STATUS = ""
        STATUS_RUNNING = false
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
                    script {
                        def newImageId = sh(returnStdout: true, script: 'docker build -q -t $DOCKER_HUB_USR/$JOB_NAME:$GIT_TAG .')?.trim()
                        echo "New image id: $newImageId"
                    }
                }
            }
        }
        stage('Stop old container'){
            steps {
                script {
                    OLD_CONTAINER_ID = sh(returnStdout: true, script: "docker ps | grep $DOCKER_HUB_USR/$JOB_NAME | awk '{ print \$1 }'")?.trim()
                    if(OLD_CONTAINER_ID){
                        OLD_IMAGE_TAG = sh(returnStdout: true, script: "docker inspect --format='{{.Config.Image}}' $OLD_CONTAINER_ID")?.trim()
                        sh "docker stop $OLD_CONTAINER_ID"
                    }
                    else {
                        echo "Old container not detected"
                    }
                }
            }
        }
        stage('Run new container') {
            steps {
                script {
                    sh "docker run -d --name ${JOB_NAME}_${BUILD_NUMBER}  $DOCKER_HUB_USR/$JOB_NAME:$GIT_TAG"
                    NEW_CONTAINER_ID = sh(returnStdout: true, script: "docker ps -q --filter=NAME=${JOB_NAME}_${BUILD_NUMBER}")?.trim()
                    NEW_IMAGE_TAG = sh(returnStdout: true, script: "docker inspect --format='{{.Config.Image}}' $NEW_CONTAINER_ID")?.trim()
                    echo "New container id: $NEW_CONTAINER_ID"
                    echo "New image tag: $NEW_IMAGE_TAG"
                }
            }
        }
        stage('Await 30 seconds'){
            steps {
                sleep time: 30, unit: 'SECONDS'
            }
        }
        stage('Check status') {
            steps {
                script {
                    STATUS = sh(returnStdout: true, script: "docker inspect --format='{{.State.Status}}' $NEW_CONTAINER_ID")?.trim()
                    STATUS_RUNNING = (STATUS == 'running')
                    echo "Status: $STATUS"
                    if(STATUS_RUNNING) {
                        stage('Remove old container') {
                            steps {
                                script {
                                    if(OLD_CONTAINER_ID){
                                        sh "docker rm $OLD_CONTAINER_ID"
                                    } else {
                                        echo "Old container not detected"
                                    }
                                }
                            }
                        }
                        stage('Remove old tag') {
                            steps {
                                script {
                                    if(OLD_IMAGE_TAG){
                                        sh "docker rmi $OLD_IMAGE_TAG"
                                    } else {
                                        echo "Old tag not detected"
                                    }
                                }
                            }
                        }
                    }
                    if(!STATUS_RUNNING) {
                        stage('Restart old container') {
                           steps {
                               script {
                                 if(OLD_CONTAINER_ID){
                                       sh "docker run -d $OLD_CONTAINER_ID"
                                   } else {
                                       echo "Old container not detected"
                                   }
                               }
                           }
                       }
                       stage('Failed container log') {
                           steps {
                               sh "docker logs $NEW_CONTAINER_ID"
                           }
                       }
                       stage('Remove failed container') {
                           steps {
                               sh "docker rm $NEW_CONTAINER_ID"
                           }
                       }
                       stage('Remove failed tag') {
                            steps {
                                sh "docker rmi $NEW_IMAGE_TAG"
                            }
                       }
                    }
                }
            }
        }
//         stage('On Success') {
//             when {
//                 expression { STATUS == 'running'}
//             }
//             stages {
//
//             }
//         }
//         stage('On Failure') {
//             when {
//                 expression { STATUS != 'running'}
//             }
//             stages {
//
//             }
//         }
    }
}


