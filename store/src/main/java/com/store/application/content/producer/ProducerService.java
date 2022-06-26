package com.store.application.content.producer;

import java.util.List;

import org.springframework.stereotype.Service;

import com.store.application.exceptions.ItemNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerRepository repository;

    public List<Producer> getProducers(String filterText) {
        List<Producer> products;
        if (filterText == null || filterText == "") {
            products = repository.findAll();
        } else {
            products = repository.findByNameContaining(filterText);
        }
        return products;
    }

    public Producer getProducer(String name) throws Exception {
        Producer producer = repository.findByCode(name)
                .orElseThrow(() -> new ItemNotFoundException("Producer", "code", name));
        return producer;
    }

    public Producer get(String name){
        Producer producer = repository.findByCode(name)
                .orElse(null);
        return producer;
    }

    public void save(Producer p) {
        repository.save(p);
    }
}
