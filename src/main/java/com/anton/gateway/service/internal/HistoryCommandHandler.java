package com.anton.gateway.service.internal;

import com.anton.gateway.domain.*;
import com.anton.gateway.exception.NoSuchCurrencyException;
import com.anton.gateway.service.CurrencyExchangeService;
import com.anton.gateway.service.RequestService;
import com.anton.gateway.util.GatewayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.anton.gateway.domain.ServiceType.EXT_SERVICE_2;

@Service
public class HistoryCommandHandler implements CommandHandler{

    @Autowired
    private final CurrencyExchangeService currencyExchangeService;
    @Autowired
    private final RequestService requestService;

    public HistoryCommandHandler(CurrencyExchangeService currencyExchangeService, RequestService requestService) {
        this.currencyExchangeService = currencyExchangeService;
        this.requestService = requestService;
    }

    @Override
    public List<CurrencyResult> handleCommand(CommandRequest command) {
        HistoryCommandRequest historyCommand = command.getHistoryCommand();
        currencyExchangeService.checkForDuplicateId(command.getId());
        if (currencyExchangeService.validateCurrenciesExist(historyCommand.getCurrency())) {
            throw new NoSuchCurrencyException(historyCommand.getCurrency());
        }
        requestService.saveRequestDataXML(command, EXT_SERVICE_2);
        List<ExchangeRatesDTO> exchangeRatesForLastPeriod = currencyExchangeService.getExchangeRatesForLastPeriod(historyCommand.getCurrency(), historyCommand.getPeriod());
        return GatewayUtil.buildRecentCurrencyList(exchangeRatesForLastPeriod, historyCommand.getPeriod());
    }
}
