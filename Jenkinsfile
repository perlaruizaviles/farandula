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

    stage('Build') {
      steps {
        parallel(
          "Frontend": {
            dir('samples/client') {
              sh 'npm prune'
              sh 'npm install'
              sh 'npm test -- -u'
            }
          },
          "Java Backend": {
            dir('samples/java-server') {
              sh 'mvn clean package'
            }
          },
          "Ruby Backend": {
            echo 'TODO:Implement Ruby Backend'
          },
          "NodeJS Backend": {
            echo 'TODO:Implement NodeJS Backend'
          }
        )
      }
    }

    stage('Integration') {
      steps {
        echo 'TODO:Implement Integration Tests'
      }
    }

    stage('Deploy') {
      steps {
        echo 'TODO:Implement Deployment'
      }
    }
  }
  post {
    always {
      dir('samples/client') {
        sh 'npm prune'
        sh 'rm node_modules -rf'
      }
    }

    failure {
      sh 'echo \'Build failed\''
    }
  }
}
