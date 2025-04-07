#!/usr/bin/env bash

export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Caminho base do projeto
export PROJECT_BASE=/home/ptrck/UFP/3_ano/SD

# Caminho das classes compiladas
export CLASSES=$PROJECT_BASE/out/production/SD

# Caminho das libs (RabbitMQ)
export LIB=$PROJECT_BASE/lib

# Classpath completo
export CP=/home/ptrck/UFP/3_ano/SD/out/production/SD

# Policy
export JAVA_SECURITY_POLICY=$PROJECT_BASE/src/security.policy
