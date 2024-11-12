package com.anton.gateway.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class GetCommandRequest {

    @JacksonXmlProperty(isAttribute = true, localName = "consumer")
    private String consumer;

    @JacksonXmlProperty(localName = "currency")
    private String currency;

    public GetCommandRequest() {
    }

    public GetCommandRequest(String consumer, String currency) {
        this.consumer = consumer;
        this.currency = currency;
    }

    // Getters and Setters
    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
