package com.tencent.dingdang.tvs.ui.headless;

import com.tencent.dingdang.tvs.ui.UserSpeechVisualizerUIHandler;

public class HeadlessUserSpeechVisualizerView implements UserSpeechVisualizerUIHandler {

    @Override
    public void onProcessing() {
        System.out.println("Processing");
    }

    @Override
    public void onListening() {
        System.out.println("Listening");
    }

    @Override
    public void onProcessingFinished() {
        System.out.println("Done");
    }

    @Override
    public void rmsChanged(int rms) {
    }
}
