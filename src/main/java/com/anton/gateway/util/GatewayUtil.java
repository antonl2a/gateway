package com.anton.gateway.util;

import com.anton.gateway.domain.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class GatewayUtil {
    public static CurrencyResult buildCurrencyResult(CurrencyRecord currencyRecord, Double rate, String currency) {
        CurrencyResult currencyResult = new CurrencyResult();
        CurrencyData currencyData = new CurrencyData("EUR", currency, rate);
        currencyResult.setCurrencyData(currencyData);
        currencyResult.setResponseStatus(HttpStatus.OK);
        currencyResult.setMessage("Latest currency history for the last hour");
        return currencyResult;
    }

    public static List<CurrencyResult> buildRecentCurrencyList(List<ExchangeRatesDTO> exchangeRatesDTOList, int hours) {
        List<CurrencyResult> currencyResults = new ArrayList<>();
        for (ExchangeRatesDTO dto : exchangeRatesDTOList) {
            String currency = dto.getRates().keySet().iterator().next();
            CurrencyData currencyData = new CurrencyData("EUR", currency, dto.getRates().get(currency));
            CurrencyResult currencyResult = new CurrencyResult(currencyData, "Currency history for the last " + String.valueOf(hours) + " hours", HttpStatus.OK);
            currencyResults.add(currencyResult);
        }
        return currencyResults;
    }
}
