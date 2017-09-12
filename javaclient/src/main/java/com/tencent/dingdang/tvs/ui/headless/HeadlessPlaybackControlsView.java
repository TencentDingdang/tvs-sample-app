package com.tencent.dingdang.tvs.ui.headless;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.PlaybackAction;
import com.tencent.dingdang.tvs.ui.PlaybackControlsUIHandler;
import com.tencent.dingdang.tvs.ui.controllers.PlaybackViewController;

public class HeadlessPlaybackControlsView implements PlaybackControlsUIHandler {

    private final PlaybackViewController viewController;

    public HeadlessPlaybackControlsView(TVSController controller) {
        this.viewController = new PlaybackViewController(controller);
    }

    @Override
    public void buttonPressed(PlaybackAction action) {
        viewController.buttonPressed(action);
    }

    @Override
    public void onProcessing() {
        viewController.onProcessing();
    }

    @Override
    public void onListening() {
        viewController.onListening();
    }

    @Override
    public void onProcessingFinished() {
        viewController.onProcessingFinished();
    }

    @Override
    public void onAccessTokenReceived(String accessToken) {
    }

    @Override
    public void onAccessTokenRevoked() {
    }
}
