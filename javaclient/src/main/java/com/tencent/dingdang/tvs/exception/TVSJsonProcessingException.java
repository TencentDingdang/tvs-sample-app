package com.tencent.dingdang.tvs.exception;

import org.codehaus.jackson.JsonProcessingException;

public class TVSJsonProcessingException extends JsonProcessingException {

    private final String unparseable;

    public TVSJsonProcessingException(String message, JsonProcessingException e, String unparseable) {
        super(message, e);
        this.unparseable = unparseable;
    }

    public String getUnparseable() {
        return unparseable;
    }
}
