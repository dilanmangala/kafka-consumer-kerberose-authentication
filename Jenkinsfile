pipeline {
    agent any
    stages {
        stage('git repo & clean') {
            steps {
               bat "rmdir  /s /q kafkaConsumer"
                bat "git clone https://github.com/dilanmangala/kafka-consumer.git"
                bat "mvn clean -f kafkaConsumer"
            }
        }
        stage('install') {
            steps {
                bat "mvn install -f kafkaConsumer"
            }
        }
        stage('test') {
            steps {
                bat "mvn test -f kafkaConsumer"
            }
        }
        stage('package') {
            steps {
                bat "mvn package -f kafkaConsumer"
            }
        }
    }
}
