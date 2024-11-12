package com.anton.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CurrencyExchangeScheduler {
    private final CurrencyExchangeService currencyExchangeService;

    @Autowired
    public CurrencyExchangeScheduler(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    // Update exchange rates every hour raboti
    @Scheduled(fixedRate = 3600000)  // 1 hour in milliseconds
    public void refreshExchangeRateInRedis() {
        currencyExchangeService.refreshExchangeRateInRedis();
    }
}
