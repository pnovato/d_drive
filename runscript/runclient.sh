#!/usr/bin/env bash
source ./setenv.sh

# Input dinâmico
echo -n "Usuário: "
read USERNAME

echo -n "Senha: "
read -s PASSWORD
echo ""

java -cp ${CP} edu.ufp.inf.sd.rmi.projecto_SD.d_drive.client.LoginClient ${CLIENT_RMI_HOST} ${CLIENT_RMI_PORT} ${SERVICE_NAME} ${USERNAME} ${PASSWORD}

