package com.tencent.dingdang.tvs;

import com.tencent.dingdang.tvs.message.response.alerts.SetAlert.AlertType;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * Represents an alert (timer/alarm)
 */
public class Alert {
    private final String token;
    private final AlertType type;
    private final ZonedDateTime scheduledTime;

    public Alert(String token, AlertType type, ZonedDateTime scheduledTime) {
        this.token = token;
        this.type = type;
        this.scheduledTime = scheduledTime;
    }

    @JsonCreator
    public Alert(@JsonProperty("token") String token, @JsonProperty("type") AlertType type,
            @JsonProperty("scheduledTime") String scheduledTime) {
        this.token = token;
        this.type = type;
        this.scheduledTime = ZonedDateTime.parse(scheduledTime, DateUtils.TVS_ISO_OFFSET_DATE_TIME);
    }

    public String getToken() {
        return this.token;
    }

    public AlertType getType() {
        return this.type;
    }

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    public ZonedDateTime getScheduledTime() {
        return scheduledTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((token == null) ? 0 : token.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Alert other = (Alert) obj;
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
            return false;
        }
        return true;
    }

    public static class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {
        @Override
        public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator,
                SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString(zonedDateTime.format(DateUtils.TVS_ISO_OFFSET_DATE_TIME));
        }
    }
}
