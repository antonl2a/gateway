package com.anton.gateway.service;

import com.anton.gateway.domain.CurrencyRecord;
import com.anton.gateway.domain.ExchangeRatesDTO;
import com.anton.gateway.exception.DuplicateRequestException;
import com.anton.gateway.repository.CurrencyRecordRepository;
import com.anton.gateway.repository.RequestRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    @Autowired
    public CurrencyExchangeService(RestTemplate restTemplate,
                                   ObjectMapper objectMapper,
                                   CurrencyRecordRepository currencyExchangeRepository, RequestRecordRepository requestRecordRepository,
                                   RedisTemplate<String, String> redisTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.currencyExchangeRepository = currencyExchangeRepository;
        this.requestRecordRepository = requestRecordRepository;
        this.redisTemplate = redisTemplate;
    }

    public ExchangeRatesDTO getExchangeRatesFromApi() {
        String url = FIXER_API_URL + "?access_key=" + apiKey;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return parseResponseToDTO(response.getBody());
    }

    private ExchangeRatesDTO parseResponseToDTO(String response) {
        try {
            return objectMapper.readValue(response, ExchangeRatesDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Fixer API response", e);
        }
    }

    public ExchangeRatesDTO getExchangeRatesFromRedis() {
        List<String> lastPushedRecordList = redisTemplate.opsForList().range(EXCHANGE_RATES_KEY, -1, -1);
        if (lastPushedRecordList != null && !lastPushedRecordList.isEmpty()) {
            try {
                return objectMapper.readValue(lastPushedRecordList.get(0), ExchangeRatesDTO.class);
            } catch (Exception e) {
                throw new RuntimeException("Error parsing Redis data", e);
            }
        }
        return null;
    }

    public void saveExchangeRatesInRedis(ExchangeRatesDTO exchangeRatesDTO) {
        try {
            String jsonString = objectMapper.writeValueAsString(exchangeRatesDTO);
            redisTemplate.opsForList().rightPush(EXCHANGE_RATES_KEY, jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Error saving to Redis", e);
        }
    }

    public void checkForDuplicateId(String requestId) {
        if (requestRecordRepository.findByRequestId(requestId).isPresent()) {
            throw new DuplicateRequestException(requestId);
        }
    }

    public void saveExchangeRatesInDatabase(ExchangeRatesDTO exchangeRatesDTO) {
        CurrencyRecord currencyRecord = new CurrencyRecord();
        currencyRecord.setSuccess(exchangeRatesDTO.isSuccess());
        currencyRecord.setTimestamp(Instant.ofEpochMilli(exchangeRatesDTO.getTimestamp()));
        currencyRecord.setBase(exchangeRatesDTO.getBase());
        LocalDate parsedDate = parse(exchangeRatesDTO.getDate());
        currencyRecord.setDate(parsedDate);
        currencyRecord.setRates(exchangeRatesDTO.getRates());
        currencyExchangeRepository.save(currencyRecord);
    }

    public void refreshExchangeRateInRedis() {
            ExchangeRatesDTO exchangeRatesDTO = getExchangeRatesFromApi();
            saveExchangeRatesInRedis(exchangeRatesDTO);
            saveExchangeRatesInDatabase(exchangeRatesDTO);

    }

    public List<ExchangeRatesDTO> getExchangeRatesForLastPeriod(String currency, int hours) {
        long currentTime = Instant.now().toEpochMilli();

        List<String> records = redisTemplate.opsForList().range(EXCHANGE_RATES_KEY, -hours, -1);

        List<ExchangeRatesDTO> filteredRates = new ArrayList<>();

        for (String record : records) {
            try {
                ExchangeRatesDTO dto = objectMapper.readValue(record, ExchangeRatesDTO.class);
                long timestamp = dto.getTimestamp();

                if (Math.abs(currentTime - timestamp) > (ONE_HOUR_IN_MILLIS * hours)) {

                    Map<String, Double> filteredRatesMap = new HashMap<>();
                    if (dto.getRates().containsKey(currency)) {
                        filteredRatesMap.put(currency, dto.getRates().get(currency));
                        dto.setRates(filteredRatesMap);
                        filteredRates.add(dto);
                    }
                }

                if (filteredRates.size() == hours) {
                    break;
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed processing Json", e);
            }
        }

        return filteredRates;
    }

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
