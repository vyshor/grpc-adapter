#!/usr/bin/env bash
IMAGE=pascalwhoop/powertac-grpc-adapter
if [ $# -eq 0 ]
  then
      tag='latest'
  else
    tag=$1
fi

mvn package
docker build --tag ${IMAGE}:${tag} ./
