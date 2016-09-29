node {
  // System Dependent Locations
  def mvntool = tool name: 'maven3', type: 'hudson.tasks.Maven$MavenInstallation'
  def jdktool = tool name: 'jdk8', type: 'hudson.model.JDK'

  // Environment
  List mvnEnv = ["PATH+MVN=${mvntool}/bin", "PATH+JDK=${jdktool}/bin", "JAVA_HOME=${jdktool}/", "MAVEN_HOME=${mvntool}"]
  mvnEnv.add("MAVEN_OPTS=-Xms256m -Xmx1024m -Djava.awt.headless=true")

  stage ("Checkout") {
    checkout scm
  }

  stage ("Build & Test") {
    withCredentials([[$class: 'FileBinding', credentialsId: 'gcloud-service-account', variable: 'GCLOUDAUTH']]) {
      sh "gcloud auth activate-service-account --key-file $GCLOUDAUTH"
      sh "gcloud config set project ${env.PROJECTID}"
      // Show the projects that this account has access to
      sh "gcloud projects list"
      sh "gcloud projects describe ${env.PROJECTID}"
      // Perform build & test
      withEnv(mvnEnv) {
        timeout(120) {
          sh "mvn -B clean install -Dmaven.test.failure.ignore=true"
          // Report junit failures in the jenkins UI
          step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
          // Report integration testing failures in the jenkins UI
          step([$class: 'JUnitResultArchiver', testResults: '**/target/failsafe-reports/TEST-*.xml'])
        }
      }
    }
  }

}
