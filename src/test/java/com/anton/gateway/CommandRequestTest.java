package com.anton.gateway;

import com.anton.gateway.domain.CommandRequest;
import com.anton.gateway.domain.GetCommandRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommandRequestTest {

    @Test
    void testUnmarshalCommandRequest() throws JAXBException {
        String xml = """
                <command id="12344">
                    <get consumer="13617162">
                        <currency>EUR</currency>
                    </get>
                </command>
                """;

        JAXBContext context = JAXBContext.newInstance(CommandRequest.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        CommandRequest commandRequest = (CommandRequest) unmarshaller.unmarshal(new StringReader(xml));

        assertNotNull(commandRequest, "CommandRequest should not be null");

        assertEquals("12344", commandRequest.getId(), "ID should be '12344'");

        GetCommandRequest getCommand = commandRequest.getGetCommand();
        assertNotNull(getCommand, "GetCommandRequest should not be null");

        assertEquals("13617162", getCommand.getConsumer(), "Consumer should be '13617162'");
        assertEquals("EUR", getCommand.getCurrency(), "Currency should be 'EUR'");
    }
}
