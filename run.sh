#!/usr/bin/env bash
NAME=powertac-grpc-adapter
docker rm -f $NAME
docker run --net="host" --name $NAME vyshor/powertac-grpc-adapter:latest
