package com.anton.gateway.controller;

import com.anton.gateway.domain.*;
import com.anton.gateway.service.CurrencySchemaRequestProcessor;
import com.anton.gateway.service.internal.CommandHandler;
import com.anton.gateway.service.internal.CommandHandlerFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static com.anton.gateway.domain.ServiceType.EXT_SERVICE_1;
import static com.anton.gateway.domain.ServiceType.EXT_SERVICE_2;

@RestController
public class CurrencyXMLController {

    private static final String COMMAND_PATH = "/xml_api/command";
    private final CommandHandlerFactory handlerFactory;

    public CurrencyXMLController(CommandHandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }


    @PostMapping(path = COMMAND_PATH, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<List<CurrencyResult>> getLatestCurrencyRecordXML(@RequestBody byte[] requestBody) {
        try {
            // Convert byte array to InputStream
            InputStream inputStream = new ByteArrayInputStream(requestBody);

            // Unmarshal XML from the InputStream
            CommandRequest commandRequest = customUnmarshal(inputStream);

            // Determine appropriate handler and process the command
            CommandHandler commandHandler = handlerFactory.getHandler(commandRequest);
            List<CurrencyResult> currencyResults = commandHandler.handleCommand(commandRequest);

            return new ResponseEntity<>(currencyResults, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging purposes
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.emptyList());
        }
    }

    private CommandRequest customUnmarshal(InputStream xmlStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(CommandRequest.class, GetCommandRequest.class, HistoryCommandRequest.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        // Unmarshal XML to CommandRequest object
        CommandRequest commandRequest = (CommandRequest) unmarshaller.unmarshal(xmlStream);

        // Confirm which type of command is present
        if (commandRequest.getGetCommand() != null) {
            System.out.println("Received GetCommandRequest");
        } else if (commandRequest.getHistoryCommand() != null) {
            System.out.println("Received HistoryCommandRequest");
        } else {
            throw new IllegalArgumentException("Unknown command type in XML");
        }
        return commandRequest;
    }

}
