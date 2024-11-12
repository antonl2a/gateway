package com.anton.gateway.config;

import com.anton.gateway.repository.CurrencyRecordRepository;
import com.anton.gateway.repository.RequestRecordRepository;
import com.anton.gateway.service.CurrencyExchangeService;
import com.anton.gateway.service.CurrencySchemaRequestProcessor;
import com.anton.gateway.service.RequestService;
import com.anton.gateway.service.internal.ApplicationStartTimeService;
import com.anton.gateway.service.internal.MapToJsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SpringBeanConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CurrencySchemaRequestProcessor currencySchemaRequestProcessor(CurrencyExchangeService currencyExchangeService, CurrencyRecordRepository currencyRecordRepository, RequestRecordRepository requestRecordRepository,
                                                                         RequestService requestService) {
        return new CurrencySchemaRequestProcessor(currencyExchangeService, currencyRecordRepository, requestRecordRepository, requestService);
    }

    @Bean
    public CurrencyExchangeService currencyExchangeService(RestTemplate restTemplate, ObjectMapper objectMapper, CurrencyRecordRepository currencyRecordRepository,
                                                           RequestRecordRepository requestRecordRepository, RedisTemplate<String, String> redisTemplate) {
        return new CurrencyExchangeService(restTemplate, objectMapper, currencyRecordRepository, requestRecordRepository, redisTemplate);
    }

    @Bean
    public ApplicationStartTimeService applicationStartTimeService() {
        return new ApplicationStartTimeService();
    }

    @Bean
    public RequestService requestService(RequestRecordRepository requestRecordRepository) {
        return new RequestService(requestRecordRepository);
    }

    @Bean
    public MapToJsonConverter mapToJsonConverter(ObjectMapper objectMapper) {
        return new MapToJsonConverter(objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
