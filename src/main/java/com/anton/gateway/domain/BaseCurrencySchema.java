package com.anton.gateway.domain;

import java.time.Instant;

public class BaseCurrencySchema {

    private String requestId;

    private Long timestamp;

    private String client;

    private String currency;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Instant getTimestamp() {
        return Instant.ofEpochMilli(timestamp);
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
