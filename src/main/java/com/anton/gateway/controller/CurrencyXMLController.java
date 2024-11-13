package com.anton.gateway.controller;

import com.anton.gateway.domain.*;
import com.anton.gateway.exception.DuplicateRequestException;
import com.anton.gateway.exception.NoSuchCurrencyException;
import com.anton.gateway.service.internal.CommandHandler;
import com.anton.gateway.service.internal.CommandHandlerFactory;

import com.anton.gateway.service.rabbitmq.RabbitRequestSenderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CurrencyXMLController {

    private static final String COMMAND_PATH = "/xml_api/command";
    private final CommandHandlerFactory handlerFactory;
    private final RabbitRequestSenderService rabbitRequestSenderService;


    public CurrencyXMLController(CommandHandlerFactory handlerFactory, RabbitRequestSenderService rabbitRequestSenderService) {
        this.handlerFactory = handlerFactory;
        this.rabbitRequestSenderService = rabbitRequestSenderService;
    }


    @PostMapping(path = COMMAND_PATH, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<List<CurrencyResult>> getLatestCurrencyRecordXML(@RequestBody CommandRequest commandRequest) {
            CommandHandler commandHandler = handlerFactory.getHandler(commandRequest);
            List<CurrencyResult> currencyResults = commandHandler.handleCommand(commandRequest);
            rabbitRequestSenderService.sendRequestToRabbit();
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
