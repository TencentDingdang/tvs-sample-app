package com.tencent.dingdang.tvs.message.request.context;

import com.tencent.dingdang.tvs.Alert;
import com.tencent.dingdang.tvs.message.Payload;

import java.util.List;

public final class AlertsStatePayload extends Payload {

    private final List<Alert> allAlerts;
    private final List<Alert> activeAlerts;

    public AlertsStatePayload(List<Alert> all, List<Alert> active) {
        this.allAlerts = all;
        this.activeAlerts = active;
    }

    public List<Alert> getAllAlerts() {
        return allAlerts;
    }

    public List<Alert> getActiveAlerts() {
        return activeAlerts;
    }
}
