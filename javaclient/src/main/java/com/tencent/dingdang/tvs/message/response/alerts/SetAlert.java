package com.tencent.dingdang.tvs.message.response.alerts;

import com.tencent.dingdang.tvs.DateUtils;
import com.tencent.dingdang.tvs.message.Payload;

import org.codehaus.jackson.annotate.JsonProperty;

import java.time.ZonedDateTime;

public final class SetAlert extends Payload {

    public enum AlertType {
        ALARM,
        TIMER;
    }

    // Opaque identifier of the alert
    private String token;

    private AlertType type;

    // Time when the alarm or timer is scheduled
    private ZonedDateTime scheduledTime;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setType(String type) {
        this.type = AlertType.valueOf(type.toUpperCase());
    }

    public AlertType getType() {
        return type;
    }

    @JsonProperty("scheduledTime")
    public void setScheduledTime(String dateTime) {
        scheduledTime = ZonedDateTime.parse(dateTime, DateUtils.TVS_ISO_OFFSET_DATE_TIME);
    }

    public void setScheduledTime(ZonedDateTime dateTime) {
        scheduledTime = dateTime;
    }

    public ZonedDateTime getScheduledTime() {
        return scheduledTime;
    }
}
