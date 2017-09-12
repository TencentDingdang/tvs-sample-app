package com.tencent.dingdang.tvs.message.response.alerts;

import com.tencent.dingdang.tvs.message.Payload;

public final class DeleteAlert extends Payload {

    // opaque identifier of the alert
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
