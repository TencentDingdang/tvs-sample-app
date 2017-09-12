package com.tencent.dingdang.tvs;

import java.io.InputStream;

public class SpeakItem {
    private final String token;
    private final InputStream audio;

    public SpeakItem(String token, InputStream audio) {
        this.token = token;
        this.audio = audio;
    }

    public String getToken() {
        return token;
    }

    public InputStream getAudio() {
        return audio;
    }
}
