package com.anton.gateway.domain;
import java.util.StringJoiner;
import static com.anton.gateway.util.GatewayUtil.getDate;

public class LatestCurrencySchema extends BaseCurrencySchema {

    @Override
    public String toString() {
        return new StringJoiner(",", LatestCurrencySchema.class.getSimpleName() + "[", "]").add("requestId='" + super.getRequestId() + "'")
                .add("timestamp=" + getDate(getTimestamp()) + "'")
                .add("client='" + super.getClient() + "'")
                .add("currency='" + super.getCurrency() + "'")
                .toString();
    }
}
