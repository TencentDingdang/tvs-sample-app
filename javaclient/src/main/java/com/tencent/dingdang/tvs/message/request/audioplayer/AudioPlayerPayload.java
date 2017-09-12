package com.tencent.dingdang.tvs.message.request.audioplayer;

import com.tencent.dingdang.tvs.message.Payload;

public class AudioPlayerPayload extends Payload {

    private final String token;
    private final long offsetInMilliseconds;

    public AudioPlayerPayload(String token, long offsetInMilliseconds) {
        this.token = token;
        this.offsetInMilliseconds = offsetInMilliseconds;
    }

    public String getToken() {
        return token;
    }

    public long getOffsetInMilliseconds() {
        return offsetInMilliseconds;
    }
}
