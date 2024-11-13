package com.anton.gateway.domain;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "command")
public class CommandRequest {

    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private String id;

    @JacksonXmlProperty(localName = "get")
    public GetCommandRequest getCommand;

    @JacksonXmlProperty(localName = "history")
    private HistoryCommandRequest historyCommand;

    public CommandRequest() {
    }

    public CommandRequest(String id, GetCommandRequest getCommand, HistoryCommandRequest historyCommand) {
        this.id = id;
        this.getCommand = getCommand;
        this.historyCommand = historyCommand;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GetCommandRequest getGetCommand() {
        return getCommand;
    }

    public void setGetCommand(GetCommandRequest getCommand) {
        this.getCommand = getCommand;
    }

    public HistoryCommandRequest getHistoryCommand() {
        return historyCommand;
    }

    public void setHistoryCommand(HistoryCommandRequest historyCommand) {
        this.historyCommand = historyCommand;
    }
}
