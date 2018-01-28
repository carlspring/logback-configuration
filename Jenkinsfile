@Library('jenkins-shared-libraries')

def REPO_NAME  = 'carlspring/logback-configuration'
def SERVER_ID  = 'carlspring-oss-snapshots'
def SERVER_URL = 'https://dev.carlspring.org/nexus/content/repositories/carlspring-oss-snapshots/'

pipeline {
    agent {
        docker {
            args '-v /mnt/ramdisk/3:/home/jenkins --privileged=true'
            image 'hub.carlspring.org/jenkins/opensuse-slave:latest'
        }
    }
    options {
        timeout(time: 2, unit: 'HOURS')
        disableConcurrentBuilds()
        skipDefaultCheckout()
    }
    stages {
        stage('Preparing')
        {
            steps {
                script {
                    cleanWs deleteDirs: true
                    checkout scm
                }
            }
        }
        stage('Building...')
        {
            steps {
                withMaven(maven: 'maven-3.3.9', mavenSettingsConfig: 'a5452263-40e5-4d71-a5aa-4fc94a0e6833')
                {
                    sh "mvn -U clean install"
                }
            }
        }
        stage('Code Analysis') {
            steps {
                withMaven(maven: 'maven-3.3.9', mavenSettingsConfig: 'a5452263-40e5-4d71-a5aa-4fc94a0e6833')
                {
                    script {
                        if(BRANCH_NAME.startsWith("PR-"))
                        {
                            withSonarQubeEnv('sonar') {
                                def PR_NUMBER = env.CHANGE_ID
                                echo "Triggering sonar analysis in comment-only mode for PR: ${PR_NUMBER}."
                                sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar " +
                                   "-Dsonar.github.repository=${REPO_NAME} " +
                                   "-Dsonar.github.pullRequest=${PR_NUMBER} " +
                                   "-Psonar-github"
                            }
                        }
                        else
                        {
                            echo "This step is skipped for branches other than PR-*"
                        }
                    }
                }
            }
        }
        stage('Deploy') {
            when {
                expression { BRANCH_NAME == 'master' && (currentBuild.result == null || currentBuild.result == 'SUCCESS') }
            }
            steps {
                script {
                    withMaven(maven: 'maven-3.3.9', mavenSettingsConfig: 'a5452263-40e5-4d71-a5aa-4fc94a0e6833')
                    {
                        sh "mvn deploy -DaltDeploymentRepository=${SERVER_ID}::default::${SERVER_URL}"
                    }
                }
            }
        }
    }
    post {
        changed {
            script {
                if(BRANCH_NAME == 'master') {
                    def skype = new org.carlspring.jenkins.notification.skype.Skype()
                    skype.sendNotification("admins;devs");
                }
            }
        }
        always {
            // Email notification
            script {
                def email = new org.carlspring.jenkins.notification.email.Email()
                if(BRANCH_NAME == 'master') {
                    email.sendNotification()
                } else {
                    email.sendNotification(null, false, null, [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']])
                }
            }
        }
    }
}
