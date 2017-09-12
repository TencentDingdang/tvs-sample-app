package com.tencent.dingdang.tvs.message.response;

import com.tencent.dingdang.tvs.message.DialogRequestIdHeader;
import com.tencent.dingdang.tvs.message.Header;
import com.tencent.dingdang.tvs.message.Message;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;

public class Directive extends Message {

    @JsonIgnore
    private final String dialogRequestId;

    public Directive(Header header, JsonNode payload, String rawMessage)
            throws JsonParseException, JsonMappingException, IOException {
        super(header, payload, rawMessage);
        dialogRequestId = extractDialogRequestId();
    }

    public String getDialogRequestId() {
        return dialogRequestId;
    }

    private String extractDialogRequestId() {
        if (header instanceof DialogRequestIdHeader) {
            DialogRequestIdHeader dialogRequestIdHeader = (DialogRequestIdHeader) header;
            return dialogRequestIdHeader.getDialogRequestId();
        } else {
            return null;
        }
    }
}
