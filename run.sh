#!/usr/bin/env bash
NAME=powertac-grpc-adapter
docker rm $NAME
docker run --net host --name $NAME pascalwhoop/powertac-grpc-adapter:latest
