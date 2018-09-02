#!/usr/bin/env bash

mvn clean install

if [[ "$?" -ne 0 ]] ; then
    echo 'Maven build failed';
    exit 1
fi

mvn docker:build -pl cd-config,cd-discovery,cd-gateway,cd-query,cd-service,cd-otp-service,cd-auth-service

if [[ "$?" -ne 0 ]] ; then
    echo 'Maven docker build failed';
    exit 1
fi