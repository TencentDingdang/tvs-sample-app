package com.tencent.dingdang.tvs.message.request;

import com.tencent.dingdang.tvs.message.Header;
import com.tencent.dingdang.tvs.message.Message;
import com.tencent.dingdang.tvs.message.Payload;

import org.apache.commons.lang3.StringUtils;

/**
 * A message from the client to the server
 */
public class Event extends Message {

    public Event(Header header, Payload payload) {
        super(header, payload, StringUtils.EMPTY);
    }
}
