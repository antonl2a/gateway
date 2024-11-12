package com.anton.gateway.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class CurrencyResult {


    @JsonProperty("currencyInfo")
    CurrencyData currencyData;

    private String message;

    private HttpStatus responseStatus;


    public CurrencyResult() {
    }

    public CurrencyResult(CurrencyData currencyData, String message, HttpStatus responseStatus) {
        this.currencyData = currencyData;
        this.message = message;
        this.responseStatus = responseStatus;
    }

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(HttpStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public CurrencyData getCurrencyData() {
        return currencyData;
    }

    public void setCurrencyData(CurrencyData currencyData) {
        this.currencyData = currencyData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CurrencyResult{" +
                "currencyInfo=" + currencyData +
                ", message='" + message + '\'' +
                '}';
    }
}
