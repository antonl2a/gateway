package com.anton.gateway.service.internal;

import com.anton.gateway.domain.CommandRequest;
import com.anton.gateway.domain.GetCommandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandHandlerFactory {

    private final GetCommandHandler getCommandHandler;
    private final HistoryCommandHandler historyCommandHandler;

    @Autowired
    public CommandHandlerFactory(GetCommandHandler getCommandHandler, HistoryCommandHandler historyCommandHandler) {
        this.getCommandHandler = getCommandHandler;
        this.historyCommandHandler = historyCommandHandler;
    }

    public CommandHandler getHandler(CommandRequest command) {
        if (command.getGetCommand() != null) {
            return getCommandHandler;
        } else if (command.getHistoryCommand() != null) {
            return historyCommandHandler;
        }
        throw new IllegalArgumentException("Unknown command type");
    }
}
