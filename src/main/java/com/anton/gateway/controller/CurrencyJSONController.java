package com.anton.gateway.controller;

import com.anton.gateway.domain.CurrencyResult;
import com.anton.gateway.domain.LatestCurrencySchema;
import com.anton.gateway.domain.RecentCurrencySchema;
import com.anton.gateway.exception.DuplicateRequestException;
import com.anton.gateway.exception.NoSuchCurrencyException;
import com.anton.gateway.service.CurrencySchemaRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.anton.gateway.domain.ServiceType.EXT_SERVICE_1;
import static com.anton.gateway.domain.ServiceType.EXT_SERVICE_2;

@RestController
@RequestMapping(path = "/json_api")
public class CurrencyJSONController {

    public static final String LATEST_CURRENCY_ENDPOINT = "/current";
    public static final String RECENT_CURRENCY_ENDPOINT = "/history";

    @Autowired
    private final CurrencySchemaRequestProcessor currencySchemaRequestProcessor;

    public CurrencyJSONController(CurrencySchemaRequestProcessor currencySchemaRequestProcessor) {
        this.currencySchemaRequestProcessor = currencySchemaRequestProcessor;
    }

    @PostMapping(path = LATEST_CURRENCY_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyResult> getLatestCurrencyRecord(@RequestBody LatestCurrencySchema latestCurrencyRequest) {
        CurrencyResult result = currencySchemaRequestProcessor.processLatest(latestCurrencyRequest, EXT_SERVICE_1);
        return new ResponseEntity<>(result, result.getResponseStatus());
    }

    @PostMapping(path = RECENT_CURRENCY_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CurrencyResult>> getRecentCurrencyRecords(@RequestBody RecentCurrencySchema recentCurrencyRequest) {
        List<CurrencyResult> currencyResults = currencySchemaRequestProcessor.processRecent(recentCurrencyRequest, EXT_SERVICE_1);
        return new ResponseEntity<>(currencyResults, HttpStatus.OK);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<String> handleDuplicateRequestId(DuplicateRequestException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(NoSuchCurrencyException.class)
    public ResponseEntity<String> handleNoSuchCurrency(NoSuchCurrencyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}