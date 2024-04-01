pipeline {
  agent {
    label "local"
  }

  parameters {
    booleanParam(
      name: "archive",
      description: "Should artifacts be archived?",
      defaultValue: false
    )
    booleanParam(
      name: "javadoc",
      description: "Should generate Javadoc?",
      defaultValue: false
    )
  }

  environment {
    JDK_NAME = "Open JDK"
    ANT_NAME = "Ant"
    ANT_OUTPUT_DIR = "antBuild"
  }

  options {
    skipDefaultCheckout true
    timeout(time: 20, unit: "MINUTES")
    buildDiscarder logRotator(numToKeepStr: "10", artifactNumToKeepStr: "5")
    timestamps()
  }

  stages {
    stage("Preparation") {
      steps {
        script {
          def scmEnv = checkout scm
          currentBuild.displayName = "${env.BUILD_NUMBER} ${scmEnv.GIT_COMMIT.take(8)}"
        }
      }
    }

    stage("Build") {
      steps {
        echo "#INFO: Building project"
        withAnt(installation: "${env.ANT_NAME}", jdk: "${env.JDK_NAME}") {
          sh "ant resolve jar"
        }
      }
    }

    stage("Unit tests") {
      steps {
        echo "#INFO: Running unit tests"
        withAnt(installation: "${env.ANT_NAME}", jdk: "${env.JDK_NAME}") {
          sh "ant test"
        }
      }

      post {
        always {
          junit(
            testResults: "${env.ANT_OUTPUT_DIR}/junit/result/TEST-*.xml",
            healthScaleFactor: 1.0,
            skipPublishingChecks: true
          )
        }
      }
    }

    stage("Archive artifacts") {
      when {
        beforeAgent true
        expression {
          params.archive
        }
      }

      steps {
        archiveArtifacts(artifacts: "${env.ANT_OUTPUT_DIR}/dist/*.jar", onlyIfSuccessful: true)
      }
    }

    stage("Javadoc") {
      when {
        beforeAgent true
        expression {
          params.javadoc
        }
      }

      steps {
        echo "#INFO: Publish Javadoc"
        withAnt(installation: "${env.ANT_NAME}", jdk: "${env.JDK_NAME}") {
          sh "ant docs"
        }
      }

      post {
        always {
          javadoc(javadocDir: "${env.ANT_OUTPUT_DIR}/docs", keepAll: false)
        }
      }
    }
  }

  post {
    always {
      chuckNorris()
    }

    cleanup {
      cleanWs()
    }
  }
}
