package com.project.starbucksapi.service;

import java.util.List;

import com.project.starbucksapi.producer.PagamentoNoturnoProducer;
import org.springframework.stereotype.Service;

import com.project.starbucksapi.model.Bebidas;
import com.project.starbucksapi.repository.BebidasRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BebidasService {

	private final BebidasRepository bebidasRepository;
    private final PagamentoNoturnoProducer pagamentoNoturnoProducer;

    public List<Bebidas> findAll() {
        return bebidasRepository.findAll();
    }

    public void save(Bebidas bebida) {
        bebidasRepository.save(bebida);
    }

    public void produce(Bebidas bebida) {
        pagamentoNoturnoProducer.produce(bebida);
    }
}
