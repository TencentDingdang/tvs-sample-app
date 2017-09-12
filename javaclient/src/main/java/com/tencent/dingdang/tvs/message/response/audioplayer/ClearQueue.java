package com.tencent.dingdang.tvs.message.response.audioplayer;

import com.tencent.dingdang.tvs.message.Payload;

public final class ClearQueue extends Payload {

    public enum ClearBehavior {
        CLEAR_ENQUEUED,
        CLEAR_ALL;
    }

    private ClearBehavior clearBehavior;

    public ClearBehavior getClearBehavior() {
        return clearBehavior;
    }

    public void setClearBehavior(String clearBehavior) {
        this.clearBehavior = ClearBehavior.valueOf(clearBehavior);
    }
}
