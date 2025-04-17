#!/usr/bin/env bash
source ./setenv.sh

echo "Iniciando LoginServer ${SERVER_RMI_HOST}:${SERVER_RMI_PORT} com serviço '${SERVICE_NAME}'"

java -cp ${CP} edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server.LoginServer ${SERVER_RMI_HOST} ${SERVER_RMI_PORT} ${SERVICE_NAME}
