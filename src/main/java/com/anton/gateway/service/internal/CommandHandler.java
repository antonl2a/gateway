package com.anton.gateway.service.internal;

import com.anton.gateway.domain.CommandRequest;
import com.anton.gateway.domain.CurrencyResult;

import java.util.List;

public interface CommandHandler {
    List<CurrencyResult> handleCommand(CommandRequest command);

}
