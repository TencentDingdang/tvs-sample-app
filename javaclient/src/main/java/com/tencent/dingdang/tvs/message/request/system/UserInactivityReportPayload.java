package com.tencent.dingdang.tvs.message.request.system;

import com.tencent.dingdang.tvs.message.Payload;

public class UserInactivityReportPayload extends Payload {

    private long inactiveTimeInSeconds;

    public UserInactivityReportPayload(long inactiveTimeInSeconds) {
        this.inactiveTimeInSeconds = inactiveTimeInSeconds;
    }

    public long getInactiveTimeInSeconds() {
        return inactiveTimeInSeconds;
    }
}
