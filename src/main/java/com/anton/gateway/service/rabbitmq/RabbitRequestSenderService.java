package com.anton.gateway.service.rabbitmq;

import com.anton.gateway.domain.RequestRecord;
import com.anton.gateway.repository.RequestRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RabbitRequestSenderService {

    private final RequestRecordRepository requestRecordRepository;
    private final Producer producer;

    @Autowired
    public RabbitRequestSenderService(RequestRecordRepository requestRecordRepository, Producer producer) {
        this.requestRecordRepository = requestRecordRepository;
        this.producer = producer;
    }

    public void sendRequestToRabbit() {
        Optional<RequestRecord> latestRecord = requestRecordRepository.findLatestRecord();
        latestRecord.ifPresent(requestRecord -> producer.sendMessage(requestRecord.toString()));
    }
}
