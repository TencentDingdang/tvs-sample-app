package com.tencent.dingdang.tvs.message.request.context;

import com.tencent.dingdang.tvs.message.Payload;

public final class PlaybackStatePayload extends Payload {
    private final String token;
    private final long offsetInMilliseconds;
    private final String playerActivity;

    public PlaybackStatePayload(String token, long offsetInMilliseconds, String playerActivity) {
        this.token = token;
        this.offsetInMilliseconds = offsetInMilliseconds;
        this.playerActivity = playerActivity;
    }

    public String getToken() {
        return token;
    }

    public long getOffsetInMilliseconds() {
        return offsetInMilliseconds;
    }

    public String getPlayerActivity() {
        return playerActivity;
    }

}
