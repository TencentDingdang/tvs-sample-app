package com.tencent.dingdang.tvs.message;

import java.util.UUID;

public class MessageIdHeader extends Header {
    private String messageId;

    public MessageIdHeader() {
        // For Jackson
    }

    public MessageIdHeader(String namespace, String name) {
        super(namespace, name);
        this.messageId = UUID.randomUUID().toString();
    }

    public final void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public final String getMessageId() {
        return messageId;
    }

    @Override
    public String toString() {
        return String.format("%1$s id:%2$s", super.toString(), messageId);
    }
}
