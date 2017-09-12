package com.tencent.dingdang.tvs.message.request.context;

import com.tencent.dingdang.tvs.message.Header;
import com.tencent.dingdang.tvs.message.Payload;

public class ComponentState {
    private Header header;
    private Payload payload;

    public ComponentState(Header header, Payload payload) {
        this.header = header;
        this.payload = payload;
    }

    public Header getHeader() {
        return header;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
