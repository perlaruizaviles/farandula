pipeline {
  agent any
  tools {
    nodejs 'NodeJS 7.9.0'
  }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Frontend') {
      steps {
        dir('demos/client') {
          sh 'npm prune'
          sh 'npm install'
          sh 'npm test'
        }
      }
    }
  }
  post {
    always {
      dir('demos/client') {
        sh 'npm prune'
        sh 'rm node_modules -rf'
      }
    }

    failure {
      sh 'echo \'Build failed\''
    }
  }
}
