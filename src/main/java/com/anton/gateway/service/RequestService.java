package com.anton.gateway.service;

import com.anton.gateway.domain.BaseCurrencySchema;
import com.anton.gateway.domain.CommandRequest;
import com.anton.gateway.domain.RequestRecord;
import com.anton.gateway.domain.ServiceType;
import com.anton.gateway.repository.RequestRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RequestService {

    @Autowired
    private final RequestRecordRepository requestRecordRepository;

    public RequestService(RequestRecordRepository requestRecordRepository) {
        this.requestRecordRepository = requestRecordRepository;
    }

    //raboti
    public void saveRequestDataJson(BaseCurrencySchema request, ServiceType serviceType) {
        requestRecordRepository.save(parseRequestToRecordJson(request, serviceType));
    }

    public void saveRequestDataXML(CommandRequest request, ServiceType serviceType) {
        requestRecordRepository.save(parseRequestToRecordXML(request, serviceType));
    }

    //raboti
    private RequestRecord parseRequestToRecordJson(BaseCurrencySchema request, ServiceType serviceType) {
        RequestRecord requestRecord = new RequestRecord();
        requestRecord.setRequestId(request.getRequestId());
        requestRecord.setClientId(request.getClient());
        requestRecord.setServiceType(serviceType);
        requestRecord.setReceivedAt(request.getTimestamp());
        requestRecord.setCurrency(request.getCurrency());
        requestRecord.setHours(requestRecord.getHours() != null ? requestRecord.getHours() : 1);
        return requestRecord;
    }

    private RequestRecord parseRequestToRecordXML(CommandRequest request, ServiceType serviceType) {
        RequestRecord requestRecord = new RequestRecord();
        requestRecord.setRequestId(request.getId());
        requestRecord.setClientId(request.getGetCommand().getCurrency() != null ? request.getGetCommand().getConsumer() : request.getHistoryCommand().getConsumer());
        requestRecord.setServiceType(serviceType);
        requestRecord.setReceivedAt(Instant.now().getEpochSecond());
        requestRecord.setCurrency(request.getGetCommand().getCurrency() != null ? request.getGetCommand().getCurrency() : request.getHistoryCommand().getCurrency());
        return requestRecord;
    }
}
