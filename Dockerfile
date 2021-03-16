FROM openjdk:11-jdk-slim-buster
LABEL maintainer=vyshor
LABEL name=powertac-grpc-adapter
###########################################

#adding all the needed dependencies, as alpine is a bloody lightweight
#RUN apk add --no-cache protobuf

#docker usually acts mostly on the root filepath, as it's common for only one process to run inside of a container.
WORKDIR /powertac

COPY adapter/target/adapter-1.5.1-SNAPSHOT.jar adapter.jar
#copy config and start the client
COPY broker.properties ./
COPY init.sh ./


CMD ./init.sh
