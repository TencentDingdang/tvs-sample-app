package com.tencent.dingdang.tvs.message.response.system;

import com.tencent.dingdang.tvs.message.Payload;

public class SetEndpoint extends Payload {

    private String endpoint;

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

}
