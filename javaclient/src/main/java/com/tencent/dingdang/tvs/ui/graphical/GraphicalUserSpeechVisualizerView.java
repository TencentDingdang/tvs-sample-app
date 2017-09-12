package com.tencent.dingdang.tvs.ui.graphical;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.tencent.dingdang.tvs.ui.UserSpeechVisualizerUIHandler;

public class GraphicalUserSpeechVisualizerView extends JProgressBar
        implements UserSpeechVisualizerUIHandler {

    GraphicalUserSpeechVisualizerView() {
        super(0, 100);
    }

    @Override
    public void onProcessing() {
        SwingUtilities.invokeLater(() -> setIndeterminate(true));
    }

    @Override
    public void onListening() {
        // No-op
    }

    @Override
    public void onProcessingFinished() {
        SwingUtilities.invokeLater(() -> setIndeterminate(false));
    }

    @Override
    public void rmsChanged(int rms) { // AudioRMSListener callback
        // update the visualizer
        SwingUtilities.invokeLater(() -> setValue(rms));
    }
}
