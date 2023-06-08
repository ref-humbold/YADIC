pipeline {
  agent {
    label "local"
  }

  parameters {
    booleanParam(name: "archive", description: "Should artifacts be archived?", defaultValue: false)
  }

  options {
    skipDefaultCheckout(true)
    timeout(time: 20, unit: "MINUTES")
    buildDiscarder(logRotator(numToKeepStr: "10", artifactNumToKeepStr: "5"))
    timestamps()
  }

  stages {
    stage("Preparation") {
      steps {
        script {
          def scmEnv = checkout(scm)
          currentBuild.displayName = "${env.BUILD_NUMBER} ${scmEnv.GIT_COMMIT.take(8)}"
        }
      }
    }

    stage("Build") {
      steps {
        echo "#INFO: Building project"
        withAnt(installation: "Ant", jdk: "Open JDK") {
          sh "ant main"
        }
      }
    }

    stage("Unit tests") {
      steps {
        echo "#INFO: Running unit tests"
        withAnt(installation: "Ant", jdk: "Open JDK") {
          sh "ant test"
        }
      }

      post {
        always {
          junit(
            testResults: "antBuild/junit/result/TEST-*.xml",
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
        archiveArtifacts(artifacts: "antBuild/dist/*.jar", onlyIfSuccessful: true)
      }
    }

    stage("Javadoc") {
      steps {
        echo "#INFO: Publish Javadoc"
        withAnt(installation: "Ant", jdk: "Open JDK") {
          sh "ant docs"
        }
      }

      post {
        always {
          javadoc(javadocDir: "antBuild/docs", keepAll: false)
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
