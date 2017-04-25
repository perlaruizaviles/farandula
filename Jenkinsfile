pipeline {
  agent any
  tools {
    nodejs 'NodeJS 7.9.0'
  }
  environment {
    CI = 'true'
  }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
        echo 'Currently at branch: $BRANCH_NAME'
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
