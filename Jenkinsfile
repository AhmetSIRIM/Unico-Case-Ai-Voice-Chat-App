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
                     quickFilterEnabled: true

        string name: 'notes',
               description: 'Write your release notes',
               defaultValue: ''
    }

    stages {
        stage('Clean') {
            steps {
                withCredentials([
                    string(credentialsId: 'GEMINI_API_KEY', variable: 'API_KEY'),
                    string(credentialsId: 'GEMINI_MODEL_NAME', variable: 'MODEL_NAME')
                ]) {
                    sh '''
                        echo "GEMINI_GENERATIVE_AI_API_KEY=$API_KEY" > secrets.properties
                        echo "GEMINI_GENERATIVE_AI_MODEL_NAME=$MODEL_NAME" >> secrets.properties
                        echo "GEMINI_GENERATIVE_AI_API_KEY=$API_KEY" > local.defaults.properties
                        echo "GEMINI_GENERATIVE_AI_MODEL_NAME=$MODEL_NAME" >> local.defaults.properties
                    '''
                    sh './gradlew clean'
                }
            }
        }

        stage('Build Release') {
            steps {
                sh './gradlew assembleRelease'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'app/build/outputs/apk/release/*.apk',
                                 fingerprint: true,
                                 allowEmptyArchive: false
            }
        }

        stage('Distribute') {
            steps {
                withCredentials([
                    file(credentialsId: 'unico_case_ai_voice_chat_app', variable: 'GOOGLE_APPLICATION_CREDENTIALS')
                ]) {
                    sh '''
                        touch releasenotes_release.txt
                        ./gradlew assembleRelease appDistributionUploadRelease \
                            --artifactType='APK' \
                            --groups='android' \
                            --releaseNotes="${params.notes} - Commit: ${COMMIT_MESSAGE} by ${AUTHOR_NAME}"
                    '''
                }
            }
        }
    }
}