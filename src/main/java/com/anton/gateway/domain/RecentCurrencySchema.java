package com.anton.gateway.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;
import java.time.ZoneId;
import java.util.StringJoiner;


@XmlRootElement(name = "command")
public class RecentCurrencySchema extends BaseCurrencySchema {

    @XmlElement(name = "period")
    @JsonProperty("period")
    private Integer period;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return new StringJoiner(",", LatestCurrencySchema.class.getSimpleName() + "[", "]").add("requestId='" + super.getRequestId() + "'")
                .add("timestamp=" + Instant.ofEpochMilli(getTimestamp()).atZone(ZoneId.of("UTC")).toLocalDate() + "'")
                .add("client='" + super.getClient() + "'")
                .add("currency='" + super.getCurrency() + "'")
                .add("period=" + getPeriod().toString() + "'")
                .toString();
    }
}
