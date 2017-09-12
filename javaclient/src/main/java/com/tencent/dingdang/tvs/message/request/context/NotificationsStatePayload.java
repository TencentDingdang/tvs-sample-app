package com.tencent.dingdang.tvs.message.request.context;

import com.tencent.dingdang.tvs.message.Payload;

public class NotificationsStatePayload extends Payload {
    private boolean isEnabled;
    private boolean isVisualIndicatorPersisted;

    public NotificationsStatePayload() {
    }

    public NotificationsStatePayload(boolean enabled, boolean persisted) {
        this.isEnabled = enabled;
        this.isVisualIndicatorPersisted = persisted;
    }

    public void setIsEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsVisualIndicatorPersisted(boolean persisted) {
        this.isVisualIndicatorPersisted = persisted;
    }

    public boolean getIsVisualIndicatorPersisted() {
        return isVisualIndicatorPersisted;
    }
}
