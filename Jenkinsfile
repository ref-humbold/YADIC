pipeline {
  agent {
    label "local"
  }

  parameters {
    booleanParam(name: "archive", description: "Should artifacts be archived?", defaultValue: false)
    booleanParam(name: "javadoc", description: "Should generate Javadoc?", defaultValue: false)
  }

  environment {
    JDK_NAME = "Open JDK"
    ANT_NAME = "Ant"
    ANT_OUTPUT_DIR = "antBuild"
    GRADLE_OUTPUT_DIR = "build"
  }

  options {
    skipDefaultCheckout true
    timeout(time: 20, unit: "MINUTES")
    buildDiscarder(logRotator(numToKeepStr: "10", artifactNumToKeepStr: "5"))
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
        // withAnt(installation: "${env.ANT_NAME}", jdk: "${env.JDK_NAME}") {
        //   sh "ant main"
        // }
        withGradle {
          sh "gradle jar"
        }
      }
    }

    stage("Unit tests") {
      steps {
        echo "#INFO: Running unit tests"
        // withAnt(installation: "${env.ANT_NAME}", jdk: "${env.JDK_NAME}") {
        //   sh "ant test"
        // }
        withGradle {
          sh "gradle test"
        }
      }

      post {
        always {
          junit(
            // testResults: "${env.ANT_OUTPUT_DIR}/junit/result/TEST-*.xml",
            testResults: "${env.GRADLE_OUTPUT_DIR}/test-results/test/TEST-*.xml",
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
        // archiveArtifacts(artifacts: "${env.ANT_OUTPUT_DIR}/dist/*.jar", onlyIfSuccessful: true)
        archiveArtifacts(artifacts: "${env.GRADLE_OUTPUT_DIR}/libs/*.jar", onlyIfSuccessful: true)
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
        // withAnt(installation: "${env.ANT_NAME}", jdk: "${env.JDK_NAME}") {
        //   sh "ant docs"
        // }
        withGradle {
          sh "gradle javadoc"
        }
      }

      post {
        always {
          // javadoc(javadocDir: "${env.ANT_OUTPUT_DIR}/docs", keepAll: false)
          javadoc(javadocDir: "${env.GRADLE_OUTPUT_DIR}/docs/javadoc", keepAll: false)
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
