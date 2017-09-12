package com.tencent.dingdang.tvs.ui.graphical;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.PlaybackAction;
import com.tencent.dingdang.tvs.auth.AccessTokenListener;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.tencent.dingdang.tvs.ui.PlaybackControlsUIHandler;
import com.tencent.dingdang.tvs.ui.UserSpeechVisualizerUIHandler;
import com.tencent.dingdang.tvs.ui.controllers.PlaybackViewController;

public class GraphicalPlaybackControlsView extends JPanel implements PlaybackControlsUIHandler {

    private static final String PREVIOUS_LABEL = "\u21E4";
    private static final String NEXT_LABEL = "\u21E5";
    private static final String PAUSE_LABEL = "\u275A\u275A";
    private static final String PLAY_LABEL = "\u25B6";

    private final UserSpeechVisualizerUIHandler visualizer;
    private final PlaybackViewController viewController;

    private JButton playPauseButton;
    private JButton prevButton;
    private JButton nextButton;

    GraphicalPlaybackControlsView(UserSpeechVisualizerUIHandler visualizerView,
            TVSController controller) {
        super();
        viewController = new PlaybackViewController(controller);
        this.visualizer = visualizerView;
        this.setLayout(new GridLayout(1, 5));

        prevButton = createMusicButton(PREVIOUS_LABEL, PlaybackAction.PREVIOUS);
        createPlayPauseButton(controller);
        nextButton = createMusicButton(NEXT_LABEL, PlaybackAction.NEXT);
    }

    private JButton createMusicButton(String label, final PlaybackAction action) {
        JButton button = new JButton(label);
        button.setEnabled(false);
        button.addActionListener(e -> buttonPressed(action));
        this.add(button);
        return button;
    }

    private void createPlayPauseButton(final TVSController controller) {
        playPauseButton = new JButton(PLAY_LABEL);
        playPauseButton.setEnabled(false);
        playPauseButton.addActionListener(e -> {
            controller.onUserActivity();
            if (controller.isPlaying()) {
                buttonPressed(PlaybackAction.PAUSE);
                playPauseButton.setText(PAUSE_LABEL);
            } else {
                buttonPressed(PlaybackAction.PLAY);
                playPauseButton.setText(PLAY_LABEL);
            }
        });
        this.add(playPauseButton);
    }

    private void setButtonsEnabled(boolean buttonsEnabled) {
        playPauseButton.setEnabled(buttonsEnabled);
        prevButton.setEnabled(buttonsEnabled);
        nextButton.setEnabled(buttonsEnabled);
    }

    private void setPlaybackControlEnabled(boolean enable) {
        SwingUtilities.invokeLater(() -> setComponentsOfContainerEnabled(this, enable));
    }

    /**
     * Recursively Enable/Disable components in a container
     *
     * @param container
     *            Object of type Container (like JPanel).
     * @param enable
     *            Set true to enable all components in the container. Set to false to disable all.
     */
    private void setComponentsOfContainerEnabled(Container container, boolean enable) {
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                setComponentsOfContainerEnabled((Container) component, enable);
            }
            SwingUtilities.invokeLater(() -> component.setEnabled(enable));
        }
    }

    @Override
    public void onProcessing() {
        viewController.onProcessing();
    }

    @Override
    public void onListening() {
        viewController.onListening();
        setPlaybackControlEnabled(false);
    }

    @Override
    public void onProcessingFinished() {
        viewController.onProcessingFinished();
        setPlaybackControlEnabled(true);
    }

    @Override
    public synchronized void onAccessTokenReceived(String accessToken) {
        SwingUtilities.invokeLater(() -> setButtonsEnabled(true));
    }

    @Override
    public synchronized void onAccessTokenRevoked() {
        SwingUtilities.invokeLater(() -> setButtonsEnabled(false));
    }

    public void buttonPressed(PlaybackAction action) {
        SwingWorker<Void, Void> tvsCall = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() throws Exception {
                visualizer.onProcessing();
                viewController.buttonPressed(action);
                return null;
            }

            @Override
            public void done() {
                visualizer.onProcessingFinished();
            }
        };
        tvsCall.execute();
    }
}
