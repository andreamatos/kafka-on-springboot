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

To verificate brocker connection run Kowl;

docker container run -d -p 8080:8181 -e KAFKA_BROKERS=kubernetes-worker.domain.name:9092 --add-host kafka-server:127.0.0.1 quay.io/cloudhut/kowl:master

![image](https://user-images.githubusercontent.com/42948627/148997673-acfebe7d-479a-47a6-b8c6-c3c4c3c94aa8.png)

![image](https://user-images.githubusercontent.com/42948627/148997703-2b5ff47e-4041-4586-bb38-7a404ffc94a3.png)

## API configuration

Create kafka configuration in application.yml;

```
  kafka:
    bootstrap-servers: kubernetes-worker.domain.name:9092
    numero-threads: 1
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    consumer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      group-id: strabucks-api
      auto-commit: true
      auto-commit-interval: 100
      session-timeout: 30000
      max-pool-interval: 50000
      max-pool-records: 5
```

Enable kafka;

```
@EnableKafka
@EnableRetry
@Configuration
public class KafkaConfiguration {
    @Value("${spring.kafka.bootstrap-servers}")
    private List<String> bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.numero-threads}")
    private Integer numeroThreads;

    @Value("${spring.kafka.consumer.auto-commit}")
    private Boolean autoCommit;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private Integer autoCommitInterval;

    @Value("${spring.kafka.consumer.session-timeout}")
    private Integer sessionTimeout;

    @Value("${spring.kafka.consumer.max-pool-interval}")
    private Integer maxPoolInterval;

    @Value("${spring.kafka.consumer.max-pool-records}")
    private Integer maxPoolRecords;

    @Bean
    public DefaultKafkaConsumerFactory<Object, Object> consumerFactory() {
        Map<String, Object> props = Stream
                .of(new Object[][] {
                        {ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",", bootstrapServers)},
                        {ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class},
                        {ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class},
                        {ConsumerConfig.GROUP_ID_CONFIG, groupId},
                        {ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout},
                        {ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPoolInterval},
                        {ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit},
                        {ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval},
                        {ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPoolRecords},

                }).collect(Collectors.toMap(data -> (String) data[0], data -> data[1]));

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaDefaultFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(this.numeroThreads);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaReportEmailRetryFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(this.numeroThreads);
        return factory;
    }

```

Run application;

![image](https://user-images.githubusercontent.com/42948627/149007126-60186c01-9277-48af-90e1-48ab815c0b44.png)

Console;

![image](https://user-images.githubusercontent.com/42948627/149007336-45667571-0563-4d55-80c4-c3d3ab62fd5d.png)

## Message Producer 

create a endpoint "producer";

![image](https://user-images.githubusercontent.com/42948627/149022438-58d88dce-aaba-4855-b58e-0f8b4c4d7507.png)

class to produce a message;

![image](https://user-images.githubusercontent.com/42948627/149022520-4eef9a76-4c5c-4b3c-a000-fca38abf2b99.png)


Access swagger;

http://localhost:8089/swagger-ui.html#/Bebidas

![image](https://user-images.githubusercontent.com/42948627/149022615-a85048f2-a393-4d19-9a6b-b24aad3a1c84.png)

![image](https://user-images.githubusercontent.com/42948627/149022981-0b5f58eb-1c9a-448a-9189-a50b1d5be684.png)

And the producer topic will be created

![image](https://user-images.githubusercontent.com/42948627/149023047-15961829-38e9-4f4c-b56f-fa9d731b6761.png)


## Message Consumer

Configure consumer classes;

```
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final ObjectMapper objectMapper;

    public <T> void consume(ConsumerRecord<String, String> message, Class<T> classe,
                            Consumer<T> consumer) {
        log.debug("Iniciando ação para o topico: {}", message.topic());

        try {
            final var obj = objectMapper.readValue(message.value(), classe);
            consumer.accept(obj);
        } catch (Exception e) {
            log.error(format("Erro ao executar ação para o tópico: %s", message.topic()), e);
            e.printStackTrace();
        }
    }

    public <T> void consume(ConsumerRecord<String, String> message, Class<T> classe,
                            BiConsumer<T, Headers> consumer) {
        log.debug("Iniciando ação para o topico: {}", message.topic());
        try {
            final var obj = objectMapper.readValue(message.value(), classe);
            consumer.accept(obj, message.headers());
        } catch (Exception e) {
            log.error(format("Erro ao executar ação para o tópico : %s", message.topic()), e);
            e.printStackTrace();
        }
    }
}
```

```
@Slf4j
@Component
@RequiredArgsConstructor
public class PagamentoNoturnoConsumer {
    private final KafkaConsumer kafkaConsumer;

    @KafkaListener(topics = "${spring.kafka.pagamento.topico-processo-noturno}",
                    containerFactory = "kafkaDefaultFactory",
                    autoStartup = "${spring.kafka.pagamento.enabled}")

    public void consume(ConsumerRecord<String, String> message) {
        kafkaConsumer.consume(message, Bebidas.class, (bebidas) -> {
            log.info(
                    "[bebidasId = {}, bebidasNome = {}] Mensagem Consumida com sucesso" ,
                    bebidas.getId(),
                    bebidas.getName());
        });
    }
}
```

Run api producer to consume the message;

**![image](https://user-images.githubusercontent.com/42948627/149155265-c258b065-ea61-4b81-b3ec-150519652641.png)

![image](https://user-images.githubusercontent.com/42948627/149155342-0a5e5ec7-8d47-4bb8-bcdc-00010087fabf.png)




