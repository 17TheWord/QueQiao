package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.response.Response;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static com.github.theword.queqiao.tool.utils.TestUtils.testParseJsonMessage;


class ParseJsonToEventImplTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ParseJsonToEventImpl parseJsonToEventImpl = new ParseJsonToEventImpl();

    @Test
    void parseMessage() throws FileNotFoundException {
        FileReader reader = new FileReader("../../test_messages.json");
        JsonElement testMessageJsonElement = JsonParser.parseReader(reader);

        if (!testMessageJsonElement.isJsonArray()) {
            logger.warn("jsonElement is not json array");
            return;
        }

        logger.info("Testing messages");

        for (JsonElement message : testMessageJsonElement.getAsJsonArray()) {
            logger.info("============================================");
            Response response = testParseJsonMessage(message.getAsString(), parseJsonToEventImpl);
            logger.info("response = {}", response);
        }

    }

}