package com.anton.gateway.controller;

import com.anton.gateway.domain.*;
import com.anton.gateway.service.internal.CommandHandler;
import com.anton.gateway.service.internal.CommandHandlerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CurrencyXMLController {

    private static final String COMMAND_PATH = "/xml_api/command";
    private final CommandHandlerFactory handlerFactory;

    public CurrencyXMLController(CommandHandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }


    @PostMapping(path = COMMAND_PATH, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<List<CurrencyResult>> getLatestCurrencyRecordXML(@RequestBody CommandRequest commandRequest) {
            CommandHandler commandHandler = handlerFactory.getHandler(commandRequest);
            List<CurrencyResult> currencyResults = commandHandler.handleCommand(commandRequest);
            return new ResponseEntity<>(currencyResults, HttpStatus.OK);

    }

}
