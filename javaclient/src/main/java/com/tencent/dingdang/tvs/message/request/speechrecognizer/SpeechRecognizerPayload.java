package com.tencent.dingdang.tvs.message.request.speechrecognizer;

import com.tencent.dingdang.tvs.SpeechProfile;
import com.tencent.dingdang.tvs.message.Payload;

public final class SpeechRecognizerPayload extends Payload {
    private final String profile;
    private final String format;

    public SpeechRecognizerPayload(SpeechProfile profile, String format) {
        this.profile = profile.toString();
        this.format = format;
    }

    public String getProfile() {
        return profile;
    }

    public String getFormat() {
        return format;
    }
}
