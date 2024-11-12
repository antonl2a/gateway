package com.anton.gateway.domain;


import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({GetCommandRequest.class, HistoryCommandRequest.class})
public class CommandRequest {

    @XmlAttribute
    private String id;

    @XmlElement(name = "get", required = true)
    public GetCommandRequest getCommand;

    @XmlElement(name = "history")
    private HistoryCommandRequest historyCommand;

    public CommandRequest() {
    }

    // Getters and Setters
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
