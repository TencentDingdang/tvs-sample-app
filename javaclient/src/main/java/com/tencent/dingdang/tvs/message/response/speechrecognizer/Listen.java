package com.tencent.dingdang.tvs.message.response.speechrecognizer;

import com.tencent.dingdang.tvs.message.Payload;

public class Listen extends Payload {
    // duration of wait for the customer to open the microphone before issuing a ListenTimeout event
    private String timeoutIntervalInMillis;

    public String getTimeoutIntervalInMillis() {
        return timeoutIntervalInMillis;
    }

    public void setTimeoutIntervalInMillis(String timeoutIntervalInMillis) {
        this.timeoutIntervalInMillis = timeoutIntervalInMillis;
    }
}
