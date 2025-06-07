pipeline{
    agent any

    environment{
        DOCKER_IMAGE = 'saravjot91/rate-limit'
    }

    stages{
        stage("Building"){
            script{
                sh "docker build -t ${DOCKER_IMAGE}"
            }
        }
    }
}
