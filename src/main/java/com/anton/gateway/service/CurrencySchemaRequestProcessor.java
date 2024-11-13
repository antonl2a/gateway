package com.anton.gateway.service;

import com.anton.gateway.domain.*;
import com.anton.gateway.exception.NoSuchCurrencyException;
import com.anton.gateway.repository.CurrencyRecordRepository;
import com.anton.gateway.repository.RequestRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.anton.gateway.util.GatewayUtil.buildCurrencyResult;
import static com.anton.gateway.util.GatewayUtil.buildRecentCurrencyList;

@Service
public class CurrencySchemaRequestProcessor {

    @Autowired
    private final CurrencyExchangeService currencyExchangeService;
    @Autowired
    private final CurrencyRecordRepository currencyRecordRepository;
    @Autowired
    private final RequestService requestService;

    public CurrencySchemaRequestProcessor(CurrencyExchangeService currencyExchangeService, CurrencyRecordRepository currencyRecordRepository, RequestService requestService) {
        this.currencyExchangeService = currencyExchangeService;
        this.currencyRecordRepository = currencyRecordRepository;
        this.requestService = requestService;
    }

    public CurrencyResult processLatest(LatestCurrencySchema latestCurrencyRequest, ServiceType serviceType) {
        currencyExchangeService.checkForDuplicateId(latestCurrencyRequest.getRequestId());

        if (currencyExchangeService.validateCurrenciesExist(latestCurrencyRequest.getCurrency())) {
            throw new NoSuchCurrencyException(latestCurrencyRequest.getCurrency());
        }
        requestService.saveRequestDataJson(latestCurrencyRequest, serviceType);
        Optional<CurrencyRecord> currencyRecord = currencyRecordRepository.findLastRecord();
        Double rateForCurrency = getRateForCurrency(latestCurrencyRequest, currencyRecord);
        return buildCurrencyResult(currencyRecord.get(), rateForCurrency, latestCurrencyRequest.getCurrency());
    }

    public List<CurrencyResult> processRecent(RecentCurrencySchema recentCurrencyRequest, ServiceType serviceType) {
        currencyExchangeService.checkForDuplicateId(recentCurrencyRequest.getRequestId());
        if (currencyExchangeService.validateCurrenciesExist(recentCurrencyRequest.getCurrency())) {
            throw new NoSuchCurrencyException(recentCurrencyRequest.getCurrency());
        }
        requestService.saveRequestDataJson(recentCurrencyRequest, serviceType);
        List<ExchangeRatesDTO> exchangeRatesForLastPeriod = currencyExchangeService.getExchangeRatesForLastPeriod(recentCurrencyRequest.getCurrency(), recentCurrencyRequest.getPeriod());

        return buildRecentCurrencyList(exchangeRatesForLastPeriod, recentCurrencyRequest.getPeriod());
    }

    private static Double getRateForCurrency(LatestCurrencySchema latestCurrencyRequest, Optional<CurrencyRecord> currencyRecord) {
        return currencyRecord
                .map(record -> record.getRates().get(latestCurrencyRequest.getCurrency()))
                .orElse(null);
    }




}
