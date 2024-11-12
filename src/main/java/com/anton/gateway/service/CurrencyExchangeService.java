package com.anton.gateway.service;

import com.anton.gateway.domain.CurrencyRecord;
import com.anton.gateway.domain.ExchangeRatesDTO;
import com.anton.gateway.exception.DuplicateRequestException;
import com.anton.gateway.repository.CurrencyRecordRepository;
import com.anton.gateway.repository.RequestRecordRepository;
import com.anton.gateway.service.internal.ApplicationStartTimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDate.parse;

@Service
public class CurrencyExchangeService {

    @Value("${fixer.io.api.key}")
    private String apiKey;

    private static final String FIXER_API_URL = "http://data.fixer.io/api/latest";
    private static final String EXCHANGE_RATES_KEY = "exchangeRates";
    private static final long ONE_HOUR_IN_MILLIS = 3_600_000;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CurrencyRecordRepository currencyExchangeRepository;
    private final RequestRecordRepository requestRecordRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ApplicationStartTimeService startTimeService;
    @Autowired
    public CurrencyExchangeService(RestTemplate restTemplate,
                                   ObjectMapper objectMapper,
                                   CurrencyRecordRepository currencyExchangeRepository, RequestRecordRepository requestRecordRepository,
                                   RedisTemplate<String, String> redisTemplate, ApplicationStartTimeService startTimeService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.currencyExchangeRepository = currencyExchangeRepository;
        this.requestRecordRepository = requestRecordRepository;
        this.redisTemplate = redisTemplate;
        this.startTimeService = startTimeService;
    }

    // Fetch exchange rates from Fixer.io raboti
    public ExchangeRatesDTO getExchangeRatesFromApi() {
        String url = FIXER_API_URL + "?access_key=" + apiKey;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return parseResponseToDTO(response.getBody());
    }

    // Parse API response to DTO raboti
    private ExchangeRatesDTO parseResponseToDTO(String response) {
        try {
            return objectMapper.readValue(response, ExchangeRatesDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Fixer API response", e);
        }
    }

    //raboti
    public void checkForDuplicateId(String requestId) {
        if (requestRecordRepository.findByRequestId(requestId).isPresent()) {
            throw new DuplicateRequestException(requestId);
        }
    }

    // Save exchange rates in Redis raboti
    public void saveExchangeRatesInRedis(ExchangeRatesDTO exchangeRatesDTO) {
        // Convert DTO to JSON and store in Redis
        try {
            String jsonString = objectMapper.writeValueAsString(exchangeRatesDTO);
            redisTemplate.opsForList().rightPush(EXCHANGE_RATES_KEY, jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Error saving to Redis", e);
        }
    }

    // Retrieve exchange rates from Redis raboti
    public ExchangeRatesDTO getExchangeRatesFromRedis() {
        List<String> lastPushedRecordList = redisTemplate.opsForList().range(EXCHANGE_RATES_KEY, -1, -1);
        if (lastPushedRecordList != null && !lastPushedRecordList.isEmpty()) {
            try {
                return objectMapper.readValue(lastPushedRecordList.get(0), ExchangeRatesDTO.class);
            } catch (Exception e) {
                throw new RuntimeException("Error parsing Redis data", e);
            }
        }
        return null; // No data in Redis
    }

    // Save exchange rates in the Database
    public void saveExchangeRatesInDatabase(ExchangeRatesDTO exchangeRatesDTO) {
        // Create the CurrencyRecord entity
        CurrencyRecord currencyRecord = new CurrencyRecord();
        currencyRecord.setSuccess(exchangeRatesDTO.isSuccess());
        currencyRecord.setTimestamp(Instant.ofEpochMilli(exchangeRatesDTO.getTimestamp()));
        currencyRecord.setBase(exchangeRatesDTO.getBase());
        LocalDate parsedDate = parse(exchangeRatesDTO.getDate());
        currencyRecord.setDate(parsedDate);
        currencyRecord.setRates(exchangeRatesDTO.getRates());
        // Save the exchange rates in the database
        currencyExchangeRepository.save(currencyRecord);
    }

    // Refresh exchange rates (fetch from API and store in Redis & DB) raboti
    public void refreshExchangeRateInRedis() {
            ExchangeRatesDTO exchangeRatesDTO = getExchangeRatesFromApi();
            saveExchangeRatesInRedis(exchangeRatesDTO);
            saveExchangeRatesInDatabase(exchangeRatesDTO);

    }

    public List<ExchangeRatesDTO> getExchangeRatesForLastPeriod(String currency, int hours) {
        // Get the current time
        long currentTime = Instant.now().toEpochMilli();

        // Get all the stored records in reverse order (latest first)
        List<String> records = redisTemplate.opsForList().range(EXCHANGE_RATES_KEY, -hours, -1);

        List<ExchangeRatesDTO> filteredRates = new ArrayList<>();

        // Parse and filter the records based on the timestamp and the provided currency
        for (String record : records) {
            try {
                ExchangeRatesDTO dto = objectMapper.readValue(record, ExchangeRatesDTO.class);
                long timestamp = dto.getTimestamp();

                // Check if the record is within the last "hours" hours
                if (Math.abs(currentTime - timestamp) > (ONE_HOUR_IN_MILLIS * hours)) {

                    // Filter the rates for the provided currency
                    Map<String, Double> filteredRatesMap = new HashMap<>();
                    if (dto.getRates().containsKey(currency)) {
                        filteredRatesMap.put(currency, dto.getRates().get(currency));
                        dto.setRates(filteredRatesMap); // Set only the provided currency's rate in the DTO
                        filteredRates.add(dto); // Add the filtered DTO to the result list
                    }
                }

                // Stop when we've collected enough records (matching the period)
                if (filteredRates.size() == hours) {
                    break;
                }
            } catch (JsonProcessingException e) {
                // Handle JSON parsing errors
                e.printStackTrace();
            }
        }

        return filteredRates;
    }

    //raboti
    public boolean validateCurrenciesExist(String currency) {
        ExchangeRatesDTO exchangeRatesFromRedis = getExchangeRatesFromRedis();
        return !exchangeRatesFromRedis.getRates().containsKey(currency);
    }

    public void refreshDbCurrencyRecords() {
        ExchangeRatesDTO updatedRedisRecords = getExchangeRatesFromRedis();
        if (updatedRedisRecords == null) {
            throw new RuntimeException("Redis returned no data");
        }
        saveExchangeRatesInDatabase(updatedRedisRecords);
    }
}
