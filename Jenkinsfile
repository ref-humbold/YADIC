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
    GRADLE_OUTPUT_DIR = "build"
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
        withGradle {
          sh "gradle jar"
        }
      }
    }

    stage("Unit tests") {
      steps {
        echo "#INFO: Running unit tests"
        withGradle {
          sh "gradle test"
        }
      }

      post {
        always {
          junit(
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
        withGradle {
          sh "gradle javadoc"
        }
      }

      post {
        always {
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
