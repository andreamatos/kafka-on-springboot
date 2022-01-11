package com.project.starbucksapi;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.retry.annotation.EnableRetry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
}