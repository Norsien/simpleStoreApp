package com.store.application.content.producer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProducerDTO {
    public String name;
    public String code;
    public String email;

    public Producer toProducer() {
        Producer producer = new Producer();
        producer.name = name;
        producer.code = code;
        producer.email = email;
        return producer;
    }
}
