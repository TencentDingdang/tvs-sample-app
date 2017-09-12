package com.tencent.dingdang.tvs.message.request.context;

import com.tencent.dingdang.tvs.message.Payload;

public class VolumeStatePayload extends Payload {
    private final long volume;
    private final boolean muted;

    public VolumeStatePayload(long volume, boolean muted) {
        this.volume = volume;
        this.muted = muted;
    }

    public long getVolume() {
        return volume;
    }

    public boolean getMuted() {
        return muted;
    }
}
