pipeline {
    agent any
        environment {
            AUTHOR_NAME = sh(returnStdout: true, script: "git log -1 --pretty=format:'%an'").trim()
            COMMIT_MESSAGE = sh(returnStdout: true, script: "git log -1 --pretty=%B | tr -d '\\n'").trim()
        }

    parameters {
        gitParameter name: 'BRANCH',
            type: 'PT_BRANCH',
            defaultValue: 'master',
            description: 'Choose your branch',
            selectedValue: 'DEFAULT',
            sortMode: 'ASCENDING_SMART',
            quickFilterEnabled: 'true'
    }
    {
      string description: 'write your notes', name: 'notes'
    }

    stages {
        stage('Clean') {
            steps {
                sh "./gradlew clean"
            }
        }

        stage('Build Release') {
            steps {
                sh "./gradlew assembleRelease"
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'app/build/outputs/apk/release/*.apk', fingerprint: true, allowEmptyArchive: false
            }
        }

        stage('Distribute') {
            steps {
                withCredentials([file(credentialsId: 'unico_case_ai_voice_chat_app', variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
                    sh """
                        touch releasenotes_release.txt
                        ./gradlew assembleRelease appDistributionUploadRelease --artifactType='APK' --groups='android' --releaseNotes='${params.notes} - Commit: ${COMMIT_MESSAGE} by ${AUTHOR_NAME}'
                    """
                }
            }
        }
    }
}