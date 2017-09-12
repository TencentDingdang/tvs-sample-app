package com.tencent.dingdang.tvs.message.request.context;

import com.tencent.dingdang.tvs.message.Payload;

public final class SpeechStatePayload extends Payload {
    private final String token;
    private final long offsetInMilliseconds;
    private final String playerActivity;

    public SpeechStatePayload(String token, long offsetInMilliseconds, String playerActivity) {
        this.token = token;
        this.offsetInMilliseconds = offsetInMilliseconds;
        this.playerActivity = playerActivity;
    }

    public String getToken() {
        return this.token;
    }

    public long getOffsetInMilliseconds() {
        return this.offsetInMilliseconds;
    }

    public String getPlayerActivity() {
        return this.playerActivity;
    }

}
