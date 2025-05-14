package com.github.theword.queqiao.utils;

import com.github.theword.queqiao.tool.response.Response;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import net.md_5.bungee.api.ChatColor;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static com.github.theword.queqiao.tool.utils.TestUtils.testParseJsonMessage;
import static com.github.theword.queqiao.tool.utils.Tool.logger;
import static org.slf4j.LoggerFactory.getLogger;


class ParseJsonToEventImplTest {

    @Test
    void parseChatColor() {
        // IF spigot-1.12.2
//        ChatColor yellow = ChatColor.valueOf("BLACK");
        // ELSE
//        ChatColor yellow = ChatColor.of("BLACK");
        // END IF
        System.out.println("yellow = " + yellow);
    }

    ParseJsonToEventImpl parseJsonToEventImpl = new ParseJsonToEventImpl();

    @Test
    void parseMessage() throws FileNotFoundException {
        FileReader reader = new FileReader("../../test_messages.json");
        // IF spigot-1.12.2
//        JsonElement testMessageJsonElement = new JsonParser().parse(reader);
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