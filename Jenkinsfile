pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Dummy') {
      steps {
        dir('demos/client') {
          sh 'pwd'
        }
      }
    }
  }
  post {
    always {
      sh 'echo \'DONE\''
    }

    failure {
      sh 'echo \'Build failed\''
    }
  }
}
