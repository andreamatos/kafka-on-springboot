package com.project.starbucksapi.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper jsonMapper;

    private ListenableFuture<SendResult<String, String>> notificar(
            String mensagem,
            String topico,
            Map<String, ?> propriedadesAdicionaisCabecalho
    ) {
        final var message = MessageBuilder.withPayload(mensagem)
                .setHeader(KafkaHeaders.TOPIC, topico)
                .copyHeaders(propriedadesAdicionaisCabecalho)
                .build();

        return kafkaTemplate.send(message);
    }

    public <T> void notificarAssincrono(
            T mensagem,
            String topico,
            Map<String, ?> propriedadesAdicionaisCabecalho
    ) throws JsonProcessingException {
        final var message = jsonMapper.writeValueAsString(mensagem);

        notificar(message, topico, propriedadesAdicionaisCabecalho)
            .addCallback(new ListenableFutureCallback<>() {
                public void onSuccess(SendResult<String, String> result) {
                    log.info("Sent message=[" + message + "] with offset=[" +
                            result.getRecordMetadata().offset() + "]");
                }

            public void onFailure(Throwable ex) {
                ex.getLocalizedMessage();
                log.error("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

    public <T> void notificarSincrono(
            T mensagem,
            String topico,
            Map<String, ?> propriedadesAdicionaisCabecalho
    ) throws JsonProcessingException, ExecutionException, InterruptedException {
        final var message = jsonMapper.writeValueAsString(mensagem);
        notificar(message, topico, propriedadesAdicionaisCabecalho).get();
    }

    public <T> void notificarSincrono(
            T mensagem,
            String topico
    ) throws JsonProcessingException, ExecutionException, InterruptedException {
        notificarSincrono(mensagem, topico, null);
    }
}