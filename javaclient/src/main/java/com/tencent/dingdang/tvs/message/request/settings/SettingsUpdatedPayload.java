package com.tencent.dingdang.tvs.message.request.settings;

import java.util.List;

import com.tencent.dingdang.tvs.message.Payload;

public class SettingsUpdatedPayload extends Payload {
    private final List<Setting> settings;

    public SettingsUpdatedPayload(List<Setting> settings) {
        this.settings = settings;
    }

    public List<Setting> getSettings() {
        return settings;
    }
}
