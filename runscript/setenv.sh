#!/usr/bin/env bash

export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Caminho base do projeto
export PROJECT_BASE=/home/ptrck/UFP/3_ano/SD/src/edu/ufp/inf/sd/rmi/projecto_SD/d_drive

# Caminho das classes compiladas
export CLASSES=/home/ptrck/UFP/3_ano/SD/out/production/SD

# Caminho das libs (RabbitMQ)
export LIB="/home/ptrck/UFP/3_ano/SD/lib/amqp-client-5.24.0.jar:/home/ptrck/UFP/3_ano/SD/lib/slf4j-api-1.7.30.jar:/home/ptrck/UFP/3_ano/SD/lib/slf4j-simple-1.7.30.jar:/home/ptrck/UFP/3_ano/SD/lib/json-20190722.jar"
export SERVER_RMI_HOST=localhost
export SERVER_RMI_PORT=1099
export CLIENT_RMI_HOST=localhost
export CLIENT_RMI_PORT=1099
export SERVICE_NAME=LoginService


# Classpath completo
export BASE_CP=/home/ptrck/UFP/3_ano/SD/out/production/SD
export CP=$LIB:$BASE_CP

# Policy
export JAVA_SECURITY_POLICY=$PROJECT_BASE/src/security.policy
