package com.project.starbucksapi.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.starbucksapi.constantes.KafkaConstants;
import com.project.starbucksapi.model.Bebidas;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagamentoNoturnoProducer {

    private final KafkaProducer kafkaProducer;

    @Value("${spring.kafka.pagamento.topico-processo-noturno}")
    private String topico;

    public void produce(@NonNull final Bebidas bebidas) {
        try {
            log.debug(
                    "bebidasId = {}, bebidasNome = {}, topico = {}",
                        bebidas.getId(),
                        bebidas.getName(),
                        topico);

            kafkaProducer.notificarAssincrono(
                    bebidas,
                    topico,
                    Map.of(KafkaConstants.DELIVERY_ID, bebidas.getId(),
                           KafkaConstants.INSTALLMENT_NUMBER, bebidas.getName())
            );

            log.debug(
                    "[bebidasId = {}, bebidasNome = {}, topico = {}] Mensagem enviada com sucesso" ,
                    bebidas.getId(),
                    bebidas.getName(),
                    topico);

        } catch (JsonProcessingException e) {
            log.error(
                    "[bebidasId = {}, bebidasNome = {}, topico = {}] Erro ao enviar mensagem" ,
                    bebidas.getId(),
                    bebidas.getName(), topico, e);

            e.printStackTrace();
        }
    }

}
