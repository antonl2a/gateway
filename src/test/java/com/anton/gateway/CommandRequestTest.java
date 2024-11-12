package com.anton.gateway;

import com.anton.gateway.domain.CommandRequest;
import com.anton.gateway.domain.GetCommandRequest;
import com.anton.gateway.domain.HistoryCommandRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommandRequestTest {

//    @Test
    void testUnmarshalCommandRequest() throws JAXBException {
        String xml = """
                <command id="12344">
                    <get consumer="13617162">
                        <currency>EUR</currency>
                    </get>
                </command>
                """;

        // Set up JAXB context and unmarshaller
        JAXBContext context = JAXBContext.newInstance(CommandRequest.class, GetCommandRequest.class, HistoryCommandRequest.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // Unmarshal the XML into a CommandRequest object
        CommandRequest commandRequest = (CommandRequest) unmarshaller.unmarshal(new StringReader(xml));

        // Verify the commandRequest object is not null
        assertNotNull(commandRequest, "CommandRequest should not be null");

        // Verify the id attribute
        assertEquals("12344", commandRequest.getId(), "ID should be '12344'");

        // Verify the getCommand object is not null
        GetCommandRequest getCommand = commandRequest.getGetCommand();
        assertNotNull(getCommand, "GetCommandRequest should not be null");

        // Verify the consumer attribute and currency element
        assertEquals("13617162", getCommand.getConsumer(), "Consumer should be '13617162'");
        assertEquals("EUR", getCommand.getCurrency(), "Currency should be 'EUR'");
    }
}
