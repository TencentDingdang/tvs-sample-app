package com.tencent.dingdang.tvs.message.request.audioplayer;

import com.tencent.dingdang.tvs.message.Payload;

public class PlaybackNearlyFinishedPayload extends Payload {
    private final String navigationToken;

    public PlaybackNearlyFinishedPayload(String navigationToken) {
        this.navigationToken = navigationToken;
    }

    public String getNavigationToken() {
        return navigationToken;
    }

}
