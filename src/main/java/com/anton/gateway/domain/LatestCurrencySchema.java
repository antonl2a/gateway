package com.anton.gateway.domain;


import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

public class LatestCurrencySchema extends BaseCurrencySchema {

    @Override
    public String toString() {
        return new StringJoiner(",", LatestCurrencySchema.class.getSimpleName() + "[", "]").add("requestId='" + super.getRequestId() + "'")
                .add("timestamp=" + Instant.ofEpochMilli(getTimestamp()).atZone(ZoneId.of("UTC")).toLocalDate() + "'")
                .add("client='" + super.getClient() + "'")
                .add("currency='" + super.getCurrency() + "'")
                .toString();
    }
}
