package com.tencent.dingdang.tvs.message.request;

import com.tencent.dingdang.tvs.TVSAPIConstants;
import com.tencent.dingdang.tvs.message.Header;
import com.tencent.dingdang.tvs.message.Payload;
import com.tencent.dingdang.tvs.message.request.context.AlertsStatePayload;
import com.tencent.dingdang.tvs.message.request.context.ComponentState;
import com.tencent.dingdang.tvs.message.request.context.NotificationsStatePayload;
import com.tencent.dingdang.tvs.message.request.context.PlaybackStatePayload;
import com.tencent.dingdang.tvs.message.request.context.SpeechStatePayload;
import com.tencent.dingdang.tvs.message.request.context.VolumeStatePayload;

public class ComponentStateFactory {

    public static ComponentState createPlaybackState(PlaybackStatePayload playerState) {
        return new ComponentState(new Header(TVSAPIConstants.AudioPlayer.NAMESPACE,
                TVSAPIConstants.AudioPlayer.Events.PlaybackState.NAME), playerState);
    }

    public static ComponentState createSpeechState(SpeechStatePayload speechState) {
        return new ComponentState(new Header(TVSAPIConstants.SpeechSynthesizer.NAMESPACE,
                TVSAPIConstants.SpeechSynthesizer.Events.SpeechState.NAME), speechState);
    }

    public static ComponentState createAlertState(AlertsStatePayload alertState) {
        return new ComponentState(new Header(TVSAPIConstants.Alerts.NAMESPACE,
                TVSAPIConstants.Alerts.Events.AlertsState.NAME), alertState);
    }

    public static ComponentState createVolumeState(VolumeStatePayload volumeState) {
        return new ComponentState(new Header(TVSAPIConstants.Speaker.NAMESPACE,
                TVSAPIConstants.Speaker.Events.VolumeState.NAME), volumeState);
    }

    public static ComponentState createNotificationsState(
            NotificationsStatePayload notificationsState) {
        return new ComponentState(
                new Header(TVSAPIConstants.Notifications.NAMESPACE,
                        TVSAPIConstants.Notifications.Events.IndicatorState.NAME),
                notificationsState);
    }

    public static ComponentState createComponentState(Payload payload) {
        ComponentState component;
        if (payload instanceof PlaybackStatePayload) {
            component = ComponentStateFactory.createPlaybackState((PlaybackStatePayload) payload);
        } else if (payload instanceof SpeechStatePayload) {
            component = ComponentStateFactory.createSpeechState((SpeechStatePayload) payload);
        } else if (payload instanceof AlertsStatePayload) {
            component = ComponentStateFactory.createAlertState((AlertsStatePayload) payload);
        } else if (payload instanceof VolumeStatePayload) {
            component = ComponentStateFactory.createVolumeState((VolumeStatePayload) payload);
        } else if (payload instanceof NotificationsStatePayload) {
            component = ComponentStateFactory
                    .createNotificationsState((NotificationsStatePayload) payload);
        } else {
            throw new IllegalArgumentException("Unknown payload type");
        }
        return component;
    }
}
