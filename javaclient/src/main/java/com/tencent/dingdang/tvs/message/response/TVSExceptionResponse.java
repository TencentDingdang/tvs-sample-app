package com.tencent.dingdang.tvs.message.response;

import com.tencent.dingdang.tvs.exception.TVSSystemException;
import com.tencent.dingdang.tvs.message.Header;
import com.tencent.dingdang.tvs.message.Message;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;

public class TVSExceptionResponse extends Message {

    public TVSExceptionResponse(Header header, JsonNode payload, String rawMessage)
            throws JsonParseException, JsonMappingException, IOException {
        super(header, payload, rawMessage);
    }

    /**
     * @throws TVSSystemException
     */
    public void throwException() throws TVSSystemException {
        com.tencent.dingdang.tvs.message.response.system.Exception payload =
                (com.tencent.dingdang.tvs.message.response.system.Exception) this.payload;
        throw new TVSSystemException(payload.getCode(), payload.getDescription());
    }
}
