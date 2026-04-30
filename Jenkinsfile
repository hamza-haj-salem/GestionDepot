pipeline {
    agent any

    // 🔧 On force l'utilisation de Java 11
    // Pourquoi ?
    // - Le projet est en Java 11 (voir pom.xml)
    // - Jenkins utilisait une autre version (ex: Java 25) → erreurs de compilation
    // - Cette config garantit que Maven compile avec la bonne version
    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-11'
        PATH = "${env.JAVA_HOME}\\bin;${env.PATH}" // ajoute Java 11 au PATH pour qu'il soit utilisé en priorité
    }

    stages {

        stage('Debug') {
            steps {
                // 🧪 Vérification de l'environnement
                // Permet de confirmer que Jenkins utilise bien Java 11
                bat 'java -version'
                bat 'mvn -version'
            }
        }

        stage('Build') {
            steps {
                // 🏗️ Compile le projet
                // équivalent local : mvn clean compile
                bat 'mvn clean compile'
            }
        }
    }
}