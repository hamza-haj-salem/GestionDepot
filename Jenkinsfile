pipeline {
    agent any

    tools {
        jdk 'jdk11'
    }

    stages {
        stage('Build') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
                bat 'mvn clean compile'
            }
        }
    }
}