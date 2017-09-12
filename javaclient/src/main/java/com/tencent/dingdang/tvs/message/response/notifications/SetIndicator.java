package com.tencent.dingdang.tvs.message.response.notifications;

import com.tencent.dingdang.tvs.message.Payload;

public final class SetIndicator extends Payload {

    // After triggering the "new notification" indicator, transition to
    // the queued notifications visual indicator
    private boolean persistVisualIndicator;

    // True if audio indicator should play
    private boolean playAudioIndicator;

    // The audio indicator to play when this directive is received
    private Asset asset;

    public static class Asset {

        private String assetId;

        private String url;

        public void setAssetId(String assetId) {
            this.assetId = assetId;
        }

        public String getAssetId() {
            return assetId;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    public void setPersistVisualIndicator(boolean persist) {
        this.persistVisualIndicator = persist;
    }

    public boolean shouldPersistVisualIndicator() {
        return persistVisualIndicator;
    }

    public void setPlayAudioIndicator(boolean playAudio) {
        this.playAudioIndicator = playAudio;
    }

    public boolean shouldPlayAudioIndicator() {
        return playAudioIndicator;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Asset getAsset() {
        return asset;
    }
}
