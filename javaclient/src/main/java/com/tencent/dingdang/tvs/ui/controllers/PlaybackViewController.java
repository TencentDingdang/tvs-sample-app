package com.tencent.dingdang.tvs.ui.controllers;

import static com.tencent.dingdang.tvs.PlaybackAction.PAUSE;
import static com.tencent.dingdang.tvs.PlaybackAction.PLAY;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.PlaybackAction;
import com.tencent.dingdang.tvs.ui.PlaybackControlsUIHandler;

public class PlaybackViewController implements PlaybackControlsUIHandler {

    private final TVSController controller;
    private boolean playbackControlsEnabled;

    public PlaybackViewController(TVSController controller) {
        this.controller = controller;
        this.playbackControlsEnabled = true;
    }

    @Override
    public void buttonPressed(PlaybackAction action) {
        if (playbackControlsEnabled) {
            controller.onUserActivity();
            switch (action) {
                case PAUSE:
                case PLAY:
                    controller.handlePlaybackAction(controller.isPlaying() ? PAUSE : PLAY);
                    break;
                default:
                    controller.handlePlaybackAction(action);
            }
        }
    }

    @Override
    public void onProcessing() {
        // No-op
    }

    @Override
    public void onListening() {
        this.playbackControlsEnabled = false;
    }

    @Override
    public void onProcessingFinished() {
        this.playbackControlsEnabled = true;
    }

    @Override
    public void onAccessTokenReceived(String accessToken) {
    }

    @Override
    public void onAccessTokenRevoked() {
    }
}
