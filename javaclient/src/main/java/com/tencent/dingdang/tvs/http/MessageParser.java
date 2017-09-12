package com.tencent.dingdang.tvs.http;

import com.tencent.dingdang.tvs.config.ObjectMapperFactory;
import com.tencent.dingdang.tvs.exception.TVSJsonProcessingException;
import com.tencent.dingdang.tvs.message.Message;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageParser {
    private static final Logger log = LoggerFactory.getLogger(MessageParser.class);

    /**
     * Parses a single valid Message in the given byte array
     *
     * @return Message if the bytes composed a valid Message
     * @throws IOException
     *             Directive parsing failed
     */
    protected Message parseServerMessage(byte[] bytes) throws IOException {
        return parse(bytes, Message.class);
    }

    protected <T> T parse(byte[] bytes, Class<T> clazz) throws IOException {
        try {
            ObjectReader reader = ObjectMapperFactory.getObjectReader();
            Object logBody = reader.withType(Object.class).readValue(bytes);
            log.info("Response metadata: \n{}", ObjectMapperFactory
                    .getObjectWriter()
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(logBody));
            return reader.withType(clazz).readValue(bytes);
        } catch (JsonProcessingException e) {
            String unparseable = new String(bytes, "UTF-8");
            throw new TVSJsonProcessingException(
                    String.format("Failed to parse a %1$s", clazz.getSimpleName()), e, unparseable);
        }
    }
}
