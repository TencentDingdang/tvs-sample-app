package com.tencent.dingdang.tvs.message.response.audioplayer;

public final class AudioItem {
    private String audioItemId;
    private Stream stream;

    public String getAudioItemId() {
        return audioItemId;
    }

    public Stream getStream() {
        return stream;
    }

    public void setAudioItemId(String audioItemId) {
        this.audioItemId = audioItemId;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }
}
