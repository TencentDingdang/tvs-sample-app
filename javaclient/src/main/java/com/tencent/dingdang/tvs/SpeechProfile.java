package com.tencent.dingdang.tvs;

public enum SpeechProfile {

    // For a hold-to-talk/push-to-talk device
    CLOSE_TALK("CLOSE_TALK"),
    
    // For a tap-to-talk device that relies on TVS's end-of-speech detection.
    NEAR_FIELD("NEAR_FIELD");

    private final String profileName;

    SpeechProfile(String profileName) {
        this.profileName = profileName;
    }

    @Override
    public String toString() {
        return this.profileName;
    }
}
