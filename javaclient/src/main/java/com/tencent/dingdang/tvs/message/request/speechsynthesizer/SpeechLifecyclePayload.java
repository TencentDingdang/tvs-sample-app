package com.tencent.dingdang.tvs.message.request.speechsynthesizer;

import com.tencent.dingdang.tvs.message.Payload;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public final class SpeechLifecyclePayload extends Payload {
    private final String token;

    public SpeechLifecyclePayload(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
