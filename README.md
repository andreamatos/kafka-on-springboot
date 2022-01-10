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
![image](https://user-images.githubusercontent.com/42948627/148829947-8e011c8c-a09f-4db6-b24a-94a79fdbd647.png)

To stop type;

```
sudo bin/zkServer.sh stop
```

![image](https://user-images.githubusercontent.com/42948627/148830322-eb968c09-5739-4442-a29f-7fbcbed246d9.png)

## Install and run local Kafka
Access https://kafka.apache.org/downloads to download the last binary kafka version

With zookeeper started, in kafka directory type;

```
sudo bin/kafka-server-start.sh config/server.properties
```

![image](https://user-images.githubusercontent.com/42948627/148831406-99b0b115-87f2-4981-b00b-d6f5e2b042dc.png)

