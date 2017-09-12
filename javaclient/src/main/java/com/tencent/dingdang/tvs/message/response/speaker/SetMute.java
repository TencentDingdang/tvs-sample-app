package com.tencent.dingdang.tvs.message.response.speaker;

import com.tencent.dingdang.tvs.message.Payload;

public class SetMute extends Payload {
    private boolean mute;

    public final void setMute(boolean mute) {
        this.mute = mute;
    }

    public final boolean getMute() {
        return mute;
    }
}
