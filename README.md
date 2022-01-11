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

![image](https://user-images.githubusercontent.com/42948627/148831484-23ff22eb-820e-484f-a83a-d0640de58b3f.png)


Create a mysql container to use with springboot;

```
sudo docker pull mysql/mysql-server:latest
docker  network create --driver bridge mysql-network
docker run -p 6603:3306 --network mysql-network --detach --name=mysql-docker -e MYSQL_ROOT_PASSWORD=adm -e MY_DATABASE=starbucks -e MY_USER=root mysql
sudo docker exec -it mysql-docker bash
mysql -uroot -p
password: adm
create database starbucks;
show databases;
```

Lets verificate the status of brocker connection;

docker container run -d -p 8080:8181 -e KAFKA_BROKERS=kubernetes-worker.domain.name:9092 --add-host kafka-server:127.0.0.1 quay.io/cloudhut/kowl:master

![image](https://user-images.githubusercontent.com/42948627/148997673-acfebe7d-479a-47a6-b8c6-c3c4c3c94aa8.png)

![image](https://user-images.githubusercontent.com/42948627/148997703-2b5ff47e-4041-4586-bb38-7a404ffc94a3.png)
