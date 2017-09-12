package com.tencent.dingdang.tvs.ui.headless;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.RecordingRMSListener;
import com.tencent.dingdang.tvs.RequestListener;
import com.tencent.dingdang.tvs.ui.ListenUIHandler;
import com.tencent.dingdang.tvs.ui.SpeechStateChangeListener;
import com.tencent.dingdang.tvs.ui.controllers.ListenViewController;

public class HeadlessListenView implements ListenUIHandler {
    private ListenViewController listenViewController;

    public HeadlessListenView(RecordingRMSListener rmsListener, TVSController controller) {
        listenViewController =
                new ListenViewController(rmsListener, controller, new SpeechRequestListener());
    }

    @Override
    public void listenButtonPressed() {
        listenViewController.listenButtonPressed();
    }

    @Override
    public void addSpeechStateChangeListener(SpeechStateChangeListener listener) {
        listenViewController.addSpeechStateChangeListener(listener);
    }

    @Override
    public void onStopCaptureDirective() {
        listenViewController.onStopCaptureDirective();
    }

    @Override
    public void onProcessing() {
        listenViewController.onProcessing();
    }

    @Override
    public void onListening() {
        listenViewController.onListening();
    }

    @Override
    public void onProcessingFinished() {
        listenViewController.onProcessingFinished();
    }

    @Override
    public void onExpectSpeechDirective() {
        listenViewController.onExpectSpeechDirective();
    }

    @Override
    public synchronized void onWakeWordDetected() {
        listenViewController.onWakeWordDetected();
    }

    private static class SpeechRequestListener extends RequestListener {

        @Override
        public void onRequestFinished() {
        }

        @Override
        public void onRequestError(Throwable e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public void onAccessTokenReceived(String accessToken) {
        listenViewController.onAccessTokenReceived(accessToken);
    }

    @Override
    public void onAccessTokenRevoked() {
        listenViewController.onAccessTokenRevoked();
    }
}
