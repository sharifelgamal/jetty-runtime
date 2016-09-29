node {
  // System Dependent Locations
  def mvntool = tool name: 'maven3', type: 'hudson.tasks.Maven$MavenInstallation'
  def jdktool = tool name: 'jdk8', type: 'hudson.model.JDK'

  // Environment
  List mvnEnv = ["PATH+MVN=${mvntool}/bin", "PATH+JDK=${jdktool}/bin", "JAVA_HOME=${jdktool}/", "MAVEN_HOME=${mvntool}"]
  mvnEnv.add("MAVEN_OPTS=-Xms256m -Xmx1024m -Djava.awt.headless=true")

  stage 'Checkout'

  checkout scm

  stage 'Build & Test'

  timeout(60) {
    withCredentials([[$class: 'FileBinding', credentialsId: 'gcloud-service-account', variable: 'GCLOUDAUTH']]) {
        sh "gcloud auth activate-service-account --key-file $GCLOUDAUTH"
        sh "gcloud config set project ${env.PROJECTID}"
    }
    withEnv(mvnEnv) {
      sh "mvn -B clean install -Dmaven.test.failure.ignore=true"
      // Report failures in the jenkins UI
      step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
    }
  }

}
