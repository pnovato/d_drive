#!/usr/bin/env bash
source ./setenv.sh

cd $CLASSES
java -cp $CP edu.ufp.inf.sd.rmi.project_SD.d_drive.client.LoginClient
