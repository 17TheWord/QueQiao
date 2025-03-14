package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.response.Response;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static com.github.theword.queqiao.tool.utils.TestUtils.testParseJsonMessage;
import static com.github.theword.queqiao.tool.utils.Tool.logger;
import static org.slf4j.LoggerFactory.getLogger;

class ParseJsonToEventImplTest {
    ParseJsonToEventImpl parseJsonToEventImpl = new ParseJsonToEventImpl();

    @Test
    void parseMessage() throws FileNotFoundException {
        FileReader reader = new FileReader("../../test_messages.json");
        // IF <= fabric-1.16.5
        JsonElement testMessageJsonElement = new JsonParser().parse(reader);
        // ELSE
//        JsonElement testMessageJsonElement = JsonParser.parseReader(reader);
        // END IF
        logger = getLogger(getClass());

        if (!testMessageJsonElement.isJsonArray()) {
            logger.warn("jsonElement is not json array");
            return;
        }

        logger.info("Testing messages");

        for (JsonElement message : testMessageJsonElement.getAsJsonArray()) {
            logger.info("============================================");
            Response response = testParseJsonMessage(message.getAsString(), parseJsonToEventImpl);
            logger.info("response = " + response);
        }

    }

}