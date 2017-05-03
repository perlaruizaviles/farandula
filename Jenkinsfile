pipeline {
  agent any
  tools {
    nodejs 'NodeJS 7.9.0'
    maven 'Maven 3.3.9'
  }
  environment {
    CI = 'true'
  }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
        echo "Currently at branch: ${env.BRANCH_NAME}"
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

    stage('Java Backend') {
      steps {
        dir('demos/java-server') {
          sh 'mvn clean package'
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
