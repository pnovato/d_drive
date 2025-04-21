#!/usr/bin/env bash
source ./setenv.sh

cd $CLASSES
rmiregistry 1099 &
