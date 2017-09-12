package com.tencent.dingdang.tvs.message.request;

import com.tencent.dingdang.tvs.TVSAPIConstants;
import com.tencent.dingdang.tvs.SpeechProfile;
import com.tencent.dingdang.tvs.exception.DirectiveHandlingException.ExceptionType;
import com.tencent.dingdang.tvs.message.DialogRequestIdHeader;
import com.tencent.dingdang.tvs.message.Header;
import com.tencent.dingdang.tvs.message.MessageIdHeader;
import com.tencent.dingdang.tvs.message.Payload;
import com.tencent.dingdang.tvs.message.request.alerts.AlertPayload;
import com.tencent.dingdang.tvs.message.request.audioplayer.AudioPlayerPayload;
import com.tencent.dingdang.tvs.message.request.audioplayer.PlaybackFailedPayload;
import com.tencent.dingdang.tvs.message.request.audioplayer.PlaybackFailedPayload.ErrorType;
import com.tencent.dingdang.tvs.message.request.audioplayer.PlaybackStutterFinishedPayload;
import com.tencent.dingdang.tvs.message.request.context.AlertsStatePayload;
import com.tencent.dingdang.tvs.message.request.context.ComponentState;
import com.tencent.dingdang.tvs.message.request.context.NotificationsStatePayload;
import com.tencent.dingdang.tvs.message.request.context.PlaybackStatePayload;
import com.tencent.dingdang.tvs.message.request.context.SpeechStatePayload;
import com.tencent.dingdang.tvs.message.request.context.VolumeStatePayload;
import com.tencent.dingdang.tvs.message.request.settings.Setting;
import com.tencent.dingdang.tvs.message.request.settings.SettingsUpdatedPayload;
import com.tencent.dingdang.tvs.message.request.speechrecognizer.SpeechRecognizerPayload;
import com.tencent.dingdang.tvs.message.request.speechsynthesizer.SpeechLifecyclePayload;
import com.tencent.dingdang.tvs.message.request.system.ExceptionEncounteredPayload;
import com.tencent.dingdang.tvs.message.request.system.UserInactivityReportPayload;

import java.util.LinkedList;
import java.util.List;

public class RequestFactory {

    public interface Request {
        RequestBody withPlaybackStatePayload(PlaybackStatePayload state);
    }

    public static RequestBody createSpeechRecognizerRecognizeRequest(String dialogRequestId,
            SpeechProfile profile, String format, PlaybackStatePayload playerState,
            SpeechStatePayload speechState, AlertsStatePayload alertState,
            VolumeStatePayload volumeState) {
        SpeechRecognizerPayload payload = new SpeechRecognizerPayload(profile, format);
        Header header = new DialogRequestIdHeader(TVSAPIConstants.SpeechRecognizer.NAMESPACE,
                TVSAPIConstants.SpeechRecognizer.Events.Recognize.NAME, dialogRequestId);
        Event event = new Event(header, payload);
        return createRequestWithStates(event, playerState, speechState, alertState, volumeState);
    }

    public static RequestBody createAudioPlayerPlaybackStartedEvent(String streamToken,
            long offsetInMilliseconds) {
        return createAudioPlayerEvent(TVSAPIConstants.AudioPlayer.Events.PlaybackStarted.NAME,
                streamToken, offsetInMilliseconds);
    }

    public static RequestBody createAudioPlayerPlaybackNearlyFinishedEvent(String streamToken,
            long offsetInMilliseconds) {
        return createAudioPlayerEvent(
                TVSAPIConstants.AudioPlayer.Events.PlaybackNearlyFinished.NAME, streamToken,
                offsetInMilliseconds);
    }

    public static RequestBody createAudioPlayerPlaybackStutterStartedEvent(String streamToken,
            long offsetInMilliseconds) {
        return createAudioPlayerEvent(
                TVSAPIConstants.AudioPlayer.Events.PlaybackStutterStarted.NAME, streamToken,
                offsetInMilliseconds);
    }

    public static RequestBody createAudioPlayerPlaybackStutterFinishedEvent(String streamToken,
            long offsetInMilliseconds, long stutterDurationInMilliseconds) {
        Header header = new MessageIdHeader(TVSAPIConstants.AudioPlayer.NAMESPACE,
                TVSAPIConstants.AudioPlayer.Events.PlaybackStutterFinished.NAME);
        Event event = new Event(header, new PlaybackStutterFinishedPayload(streamToken,
                offsetInMilliseconds, stutterDurationInMilliseconds));
        return new RequestBody(event);
    }

    public static RequestBody createAudioPlayerPlaybackFinishedEvent(String streamToken,
            long offsetInMilliseconds) {
        return createAudioPlayerEvent(TVSAPIConstants.AudioPlayer.Events.PlaybackFinished.NAME,
                streamToken, offsetInMilliseconds);
    }

    public static RequestBody createAudioPlayerPlaybackStoppedEvent(String streamToken,
            long offsetInMilliseconds) {
        return createAudioPlayerEvent(TVSAPIConstants.AudioPlayer.Events.PlaybackStopped.NAME,
                streamToken, offsetInMilliseconds);
    }

    public static RequestBody createAudioPlayerPlaybackPausedEvent(String streamToken,
            long offsetInMilliseconds) {
        return createAudioPlayerEvent(TVSAPIConstants.AudioPlayer.Events.PlaybackPaused.NAME,
                streamToken, offsetInMilliseconds);
    }

    public static RequestBody createAudioPlayerPlaybackResumedEvent(String streamToken,
            long offsetInMilliseconds) {
        return createAudioPlayerEvent(TVSAPIConstants.AudioPlayer.Events.PlaybackResumed.NAME,
                streamToken, offsetInMilliseconds);
    }

    public static RequestBody createAudioPlayerPlaybackQueueClearedEvent() {
        Header header = new MessageIdHeader(TVSAPIConstants.AudioPlayer.NAMESPACE,
                TVSAPIConstants.AudioPlayer.Events.PlaybackQueueCleared.NAME);
        Event event = new Event(header, new Payload());
        return new RequestBody(event);
    }

    public static RequestBody createAudioPlayerPlaybackFailedEvent(String streamToken,
            PlaybackStatePayload playbackStatePayload, ErrorType errorType) {
        Header header = new MessageIdHeader(TVSAPIConstants.AudioPlayer.NAMESPACE,
                TVSAPIConstants.AudioPlayer.Events.PlaybackFailed.NAME);
        Event event = new Event(header,
                new PlaybackFailedPayload(streamToken, playbackStatePayload, errorType));
        return new RequestBody(event);
    }

    public static RequestBody createAudioPlayerProgressReportDelayElapsedEvent(String streamToken,
            long offsetInMilliseconds) {
        return createAudioPlayerEvent(
                TVSAPIConstants.AudioPlayer.Events.ProgressReportDelayElapsed.NAME, streamToken,
                offsetInMilliseconds);
    }

    public static RequestBody createAudioPlayerProgressReportIntervalElapsedEvent(
            String streamToken, long offsetInMilliseconds) {
        return createAudioPlayerEvent(
                TVSAPIConstants.AudioPlayer.Events.ProgressReportIntervalElapsed.NAME, streamToken,
                offsetInMilliseconds);
    }

    private static RequestBody createAudioPlayerEvent(String name, String streamToken,
            long offsetInMilliseconds) {
        Header header = new MessageIdHeader(TVSAPIConstants.AudioPlayer.NAMESPACE, name);
        Payload payload = new AudioPlayerPayload(streamToken, offsetInMilliseconds);
        Event event = new Event(header, payload);
        return new RequestBody(event);
    }

    public static RequestBody createPlaybackControllerNextEvent(PlaybackStatePayload playbackState,
            SpeechStatePayload speechState, AlertsStatePayload alertState,
            VolumeStatePayload volumeState) {
        return createPlaybackControllerEvent(
                TVSAPIConstants.PlaybackController.Events.NextCommandIssued.NAME, playbackState,
                speechState, alertState, volumeState);
    }

    public static RequestBody createPlaybackControllerPreviousEvent(
            PlaybackStatePayload playbackState, SpeechStatePayload speechState,
            AlertsStatePayload alertState, VolumeStatePayload volumeState) {
        return createPlaybackControllerEvent(
                TVSAPIConstants.PlaybackController.Events.PreviousCommandIssued.NAME, playbackState,
                speechState, alertState, volumeState);
    }

    public static RequestBody createPlaybackControllerPlayEvent(PlaybackStatePayload playbackState,
            SpeechStatePayload speechState, AlertsStatePayload alertState,
            VolumeStatePayload volumeState) {
        return createPlaybackControllerEvent(
                TVSAPIConstants.PlaybackController.Events.PlayCommandIssued.NAME, playbackState,
                speechState, alertState, volumeState);
    }

    public static RequestBody createPlaybackControllerPauseEvent(PlaybackStatePayload playbackState,
            SpeechStatePayload speechState, AlertsStatePayload alertState,
            VolumeStatePayload volumeState) {
        return createPlaybackControllerEvent(
                TVSAPIConstants.PlaybackController.Events.PauseCommandIssued.NAME, playbackState,
                speechState, alertState, volumeState);
    }

    private static RequestBody createPlaybackControllerEvent(String name,
            PlaybackStatePayload playbackState, SpeechStatePayload speechState,
            AlertsStatePayload alertState, VolumeStatePayload volumeState) {
        Header header = new MessageIdHeader(TVSAPIConstants.PlaybackController.NAMESPACE, name);
        Event event = new Event(header, new Payload());
        return createRequestWithStates(event, playbackState, speechState, alertState, volumeState);
    }

    public static RequestBody createSpeechSynthesizerSpeechStartedEvent(String speakToken) {
        return createSpeechSynthesizerEvent(
                TVSAPIConstants.SpeechSynthesizer.Events.SpeechStarted.NAME, speakToken);
    }

    public static RequestBody createSpeechSynthesizerSpeechFinishedEvent(String speakToken) {
        return createSpeechSynthesizerEvent(
                TVSAPIConstants.SpeechSynthesizer.Events.SpeechFinished.NAME, speakToken);
    }

    private static RequestBody createSpeechSynthesizerEvent(String name, String speakToken) {
        Header header = new MessageIdHeader(TVSAPIConstants.SpeechSynthesizer.NAMESPACE, name);
        Event event = new Event(header, new SpeechLifecyclePayload(speakToken));
        return new RequestBody(event);
    }

    public static RequestBody createAlertsSetAlertEvent(String alertToken, boolean success) {
        if (success) {
            return createAlertsEvent(TVSAPIConstants.Alerts.Events.SetAlertSucceeded.NAME,
                    alertToken);
        } else {
            return createAlertsEvent(TVSAPIConstants.Alerts.Events.SetAlertFailed.NAME, alertToken);
        }
    }

    public static RequestBody createAlertsDeleteAlertEvent(String alertToken, boolean success) {
        if (success) {
            return createAlertsEvent(TVSAPIConstants.Alerts.Events.DeleteAlertSucceeded.NAME,
                    alertToken);
        } else {
            return createAlertsEvent(TVSAPIConstants.Alerts.Events.DeleteAlertFailed.NAME,
                    alertToken);
        }
    }

    public static RequestBody createAlertsAlertStartedEvent(String alertToken) {
        return createAlertsEvent(TVSAPIConstants.Alerts.Events.AlertStarted.NAME, alertToken);
    }

    public static RequestBody createAlertsAlertStoppedEvent(String alertToken) {
        return createAlertsEvent(TVSAPIConstants.Alerts.Events.AlertStopped.NAME, alertToken);
    }

    public static RequestBody createAlertsAlertEnteredForegroundEvent(String alertToken) {
        return createAlertsEvent(TVSAPIConstants.Alerts.Events.AlertEnteredForeground.NAME,
                alertToken);
    }

    public static RequestBody createAlertsAlertEnteredBackgroundEvent(String alertToken) {
        return createAlertsEvent(TVSAPIConstants.Alerts.Events.AlertEnteredBackground.NAME,
                alertToken);
    }

    private static RequestBody createAlertsEvent(String name, String alertToken) {
        Header header = new MessageIdHeader(TVSAPIConstants.Alerts.NAMESPACE, name);
        Payload payload = new AlertPayload(alertToken);
        Event event = new Event(header, payload);
        return new RequestBody(event);
    }

    public static RequestBody createSpeakerVolumeChangedEvent(long volume, boolean muted) {
        return createSpeakerEvent(TVSAPIConstants.Speaker.Events.VolumeChanged.NAME, volume, muted);
    }

    public static RequestBody createSpeakerMuteChangedEvent(long volume, boolean muted) {
        return createSpeakerEvent(TVSAPIConstants.Speaker.Events.MuteChanged.NAME, volume, muted);
    }

    public static RequestBody createSpeakerEvent(String name, long volume, boolean muted) {
        Header header = new MessageIdHeader(TVSAPIConstants.Speaker.NAMESPACE, name);

        Event event = new Event(header, new VolumeStatePayload(volume, muted));
        return new RequestBody(event);
    }

    public static RequestBody createSystemSynchronizeStateEvent(PlaybackStatePayload playerState,
            SpeechStatePayload speechState, AlertsStatePayload alertState,
            VolumeStatePayload volumeState, NotificationsStatePayload notificationsState) {
        Header header = new MessageIdHeader(TVSAPIConstants.System.NAMESPACE,
                TVSAPIConstants.System.Events.SynchronizeState.NAME);
        Event event = new Event(header, new Payload());
        return createRequestWithStates(event, playerState, speechState, alertState, volumeState,
                notificationsState);
    }

    public static RequestBody createSystemExceptionEncounteredEvent(String directiveJson,
            ExceptionType type, String message, PlaybackStatePayload playbackState,
            SpeechStatePayload speechState, AlertsStatePayload alertState,
            VolumeStatePayload volumeState) {
        Header header = new MessageIdHeader(TVSAPIConstants.System.NAMESPACE,
                TVSAPIConstants.System.Events.ExceptionEncountered.NAME);

        Event event =
                new Event(header, new ExceptionEncounteredPayload(directiveJson, type, message));

        return createRequestWithStates(event, playbackState, speechState, alertState, volumeState);
    }

    public static RequestBody createSystemUserInactivityReportEvent(long inactiveTimeInSeconds) {
        Header header = new MessageIdHeader(TVSAPIConstants.System.NAMESPACE,
                TVSAPIConstants.System.Events.UserInactivityReport.NAME);
        Event event = new Event(header, new UserInactivityReportPayload(inactiveTimeInSeconds));
        return new RequestBody(event);
    }

    public static RequestBody createSettingsUpdatedEvent(List<Setting> settings) {
        Header header = new MessageIdHeader(TVSAPIConstants.Settings.NAMESPACE,
                TVSAPIConstants.Settings.Events.SettingsUpdated.NAME);
        Event event = new Event(header, new SettingsUpdatedPayload(settings));
        return new RequestBody(event);
    }

    private static RequestBody createRequestWithStates(Event event, Payload... payloads) {
        List<ComponentState> context = new LinkedList<ComponentState>();
        for (Payload p : payloads) {
            context.add(ComponentStateFactory.createComponentState(p));
        }
        return new ContextEventRequestBody(context, event);
    }
}
