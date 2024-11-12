package com.anton.gateway.service.internal;

import com.anton.gateway.domain.*;
import com.anton.gateway.exception.NoSuchCurrencyException;
import com.anton.gateway.repository.CurrencyRecordRepository;
import com.anton.gateway.service.CurrencyExchangeService;
import com.anton.gateway.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.anton.gateway.util.GatewayUtil.buildCurrencyResult;

@Service
public class GetCommandHandler implements CommandHandler {

    @Autowired
    private final CurrencyExchangeService currencyExchangeService;
    @Autowired
    private final RequestService requestService;
    @Autowired
    private final CurrencyRecordRepository currencyRecordRepository;

    public GetCommandHandler(CurrencyExchangeService currencyExchangeService, RequestService requestService, CurrencyRecordRepository currencyRecordRepository) {
        this.currencyExchangeService = currencyExchangeService;
        this.requestService = requestService;
        this.currencyRecordRepository = currencyRecordRepository;
    }

    @Override
    public List<CurrencyResult> handleCommand(CommandRequest command) {
        GetCommandRequest getCommand = command.getGetCommand();
        currencyExchangeService.checkForDuplicateId(command.getId());
        if (currencyExchangeService.validateCurrenciesExist(getCommand.getCurrency())) {
            throw new NoSuchCurrencyException(getCommand.getCurrency());
        }
        requestService.saveRequestDataXML(command, ServiceType.EXT_SERVICE_2);
        Optional<CurrencyRecord> currencyRecord = currencyRecordRepository.findLastRecord();
        Double rateForCurrency = currencyRecord
                .map(record -> record.getRates().get(getCommand.getCurrency()))
                .orElse(null);
        return List.of(buildCurrencyResult(currencyRecord.get(), rateForCurrency, getCommand.getCurrency()));
    }
}
