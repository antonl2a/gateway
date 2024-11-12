package com.anton.gateway.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyData {
    @JsonProperty("currencyBase")
    private String currencyBase = "EUR";

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("currencyRate")
    private Double rate;

    public CurrencyData(String currencyBase, String currency, Double rate) {
        this.currencyBase = currencyBase;
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrencyBase() {
        return currencyBase;
    }

    public void setCurrencyBase(String currencyBase) {
        this.currencyBase = currencyBase;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "CurrencyInfo {" +
                "currencyBase='" + currencyBase + '\'' +
                ", currency='" + currency + '\'' +
                ", rate=" + rate +
                '}';
    }
}
