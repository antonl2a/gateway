package com.anton.gateway.domain;

import jakarta.xml.bind.annotation.*;

@XmlType(propOrder = { "consumer", "currency" })
@XmlAccessorType(XmlAccessType.FIELD)
public class GetCommandRequest {
    @XmlAttribute
    private String consumer;

    @XmlElement
    private String currency;

    public GetCommandRequest() {
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
