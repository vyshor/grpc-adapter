mkdir -p /log/adapter 
ln -s /log/adapter log
java -jar adapter.jar --config broker.properties
