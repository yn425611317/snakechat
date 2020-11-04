pipeline {
  agent any
  stages {
    stage('test') {
      steps {
        sh 'echo 123'
        build(job: 'aa', quietPeriod: 1)
      }
    }

  }
}