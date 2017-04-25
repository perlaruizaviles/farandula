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
        sh 'echo \'Hello Jenkins\''
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
