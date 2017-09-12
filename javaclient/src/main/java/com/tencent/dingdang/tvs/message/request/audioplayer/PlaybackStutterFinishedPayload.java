package com.tencent.dingdang.tvs.message.request.audioplayer;

public class PlaybackStutterFinishedPayload extends AudioPlayerPayload {

    private final long stutterDurationInMilliseconds;

    public PlaybackStutterFinishedPayload(String token, long offsetInMilliseconds,
            long stutterDurationInMilliseconds) {
        super(token, offsetInMilliseconds);
        this.stutterDurationInMilliseconds = stutterDurationInMilliseconds;
    }

    public long getStutterDurationInMilliseconds() {
        return stutterDurationInMilliseconds;
    }
}
