package com.tencent.dingdang.tvs.message.response.audioplayer;

import com.tencent.dingdang.tvs.message.Payload;
import com.tencent.dingdang.tvs.message.response.AttachedContentPayload;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.InputStream;

public final class Play extends Payload implements AttachedContentPayload {

    public enum PlayBehavior {
        REPLACE_ALL,
        ENQUEUE,
        REPLACE_ENQUEUED;
    }

    private PlayBehavior playBehavior;
    private AudioItem audioItem;

    public PlayBehavior getPlayBehavior() {
        return playBehavior;
    }

    public AudioItem getAudioItem() {
        return audioItem;
    }

    public void setPlayBehavior(String playBehavior) {
        this.playBehavior = PlayBehavior.valueOf(playBehavior);
    }

    public void setAudioItem(AudioItem audioItem) {
        this.audioItem = audioItem;
    }

    @Override
    public boolean requiresAttachedContent() {
        return audioItem.getStream().requiresAttachedContent();
    }

    @Override
    public boolean hasAttachedContent() {
        return audioItem.getStream().hasAttachedContent();
    }

    @Override
    public String getAttachedContentId() {
        if (requiresAttachedContent()) {
            return audioItem.getStream().getUrl();
        } else {
            return null;
        }
    }

    @JsonIgnore
    @Override
    public InputStream getAttachedContent() {
        return audioItem.getStream().getAttachedContent();
    }

    @Override
    public void setAttachedContent(String cid, InputStream content) {
        if (getAttachedContentId().equals(cid)) {
            audioItem.getStream().setAttachedContent(content);
        } else {
            throw new IllegalArgumentException(
                    "Tried to add the wrong audio content to a Play directive. This cid: "
                            + getAttachedContentId() + " other cid: " + cid);
        }
    }
}
