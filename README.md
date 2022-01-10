## kafka-on-springboot
![image](https://user-images.githubusercontent.com/42948627/148816729-9348793a-1b49-4d09-bb11-f1e1ad26cd20.png)

## Objective
Run a local springboot api to communicate with kafka using producers and consumers.

## Install and run local zookeeper
Kafka needs to keep some basic information and use zookeeper for it, whoever create /data/zookeeper.

To run zookeeper is nescessary to create a configureation on /kafka_local/apache-zookeeper-3.7.0-bin/conf/zoo.cfg

```
tickTime=2000
dataDir=/data/zookeeper
clientPort=2181
maxClientCnxns=60
```

To start zookeeper type;

```
sudo bin/zkServer.sh start
```
![image](https://user-images.githubusercontent.com/42948627/148829764-9a2aa3d8-ed02-4f4c-9357-078886562f2a.png)

To stop type;

```
sudo bin/zkServer.sh stop
```

![image](https://user-images.githubusercontent.com/42948627/148829870-dad506c4-c02f-4742-aea3-d4ca9334e54d.png)


## Install and run local Kafka
Access https://kafka.apache.org/downloads to download the last binary kafka version
