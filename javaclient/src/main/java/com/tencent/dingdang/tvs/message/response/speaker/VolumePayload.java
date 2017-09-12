package com.tencent.dingdang.tvs.message.response.speaker;

import com.tencent.dingdang.tvs.message.Payload;

public abstract class VolumePayload extends Payload {
    private long volume;

    public final void setVolume(long volume) {
        this.volume = volume;
    }

    public final long getVolume() {
        return volume;
    }
}
