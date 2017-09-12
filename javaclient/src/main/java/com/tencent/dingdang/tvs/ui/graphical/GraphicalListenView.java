package com.tencent.dingdang.tvs.ui.graphical;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.RecordingRMSListener;
import com.tencent.dingdang.tvs.RequestListener;
import com.tencent.dingdang.tvs.auth.AccessTokenListener;
import com.tencent.dingdang.tvs.ui.ListenUIHandler;
import com.tencent.dingdang.tvs.ui.SpeechStateChangeListener;
import com.tencent.dingdang.tvs.ui.controllers.ListenViewController;

public class GraphicalListenView extends JPanel implements ListenUIHandler {

    private static final String NOT_LISTENING_LABEL = "Tap to speak to TVS";
    private static final String LISTENING_LABEL = "Listening";
    private static final String PROCESSING_LABEL = " ";
    private static final String ERROR_DIALOG_TITLE = "Error";

    private ImageIcon notListeningIcon;
    private ImageIcon listeningIcon;
    private JLabel actionButtonLabel;
    private JButton actionButton;
    private ListenViewController listenViewController;

    GraphicalListenView(RecordingRMSListener rmsListener, TVSController controller) {
        super();
        ClassLoader resLoader = Thread.currentThread().getContextClassLoader();
        notListeningIcon = new ImageIcon(resLoader.getResource("res/tvs-mic-icon.png"));
        listeningIcon = new ImageIcon(resLoader.getResource("res/tvs-blue-mic-icon.png"));
        listenViewController =
                new ListenViewController(rmsListener, controller, new SpeechRequestListener(this));

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        actionButtonLabel = new JLabel(NOT_LISTENING_LABEL);
        actionButtonLabel.setAlignmentX(CENTER_ALIGNMENT);
        this.add(actionButtonLabel);

        actionButton = new JButton(notListeningIcon);
        actionButton.setAlignmentX(CENTER_ALIGNMENT);

        actionButton.setEnabled(false);
        actionButton.addActionListener(e -> listenButtonPressed());

        this.add(actionButton);
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
        SwingUtilities.invokeLater(() -> {
            actionButton.setEnabled(false);
            actionButtonLabel.setText(PROCESSING_LABEL);
        });
    }

    @Override
    public void onListening() {
        listenViewController.onListening();
        SwingUtilities.invokeLater(() -> {
            actionButtonLabel.setText(LISTENING_LABEL);
            actionButton.setIcon(listeningIcon);
        });
    }

    @Override
    public void onProcessingFinished() {
        listenViewController.onProcessingFinished();
        SwingUtilities.invokeLater(() -> {
            actionButton.setEnabled(true);
            actionButtonLabel.setText(NOT_LISTENING_LABEL);
            actionButton.setIcon(notListeningIcon);
        });
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

        private GraphicalListenView parentComponent;

        SpeechRequestListener(GraphicalListenView listenView) {
            parentComponent = listenView;
        }

        @Override
        public void onRequestFinished() {
        }

        @Override
        public void onRequestError(Throwable e) {
            JOptionPane.showMessageDialog(parentComponent, e.getMessage(), ERROR_DIALOG_TITLE,
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public synchronized void onAccessTokenReceived(String accessToken) {
        SwingUtilities.invokeLater(() -> actionButton.setEnabled(true));
    }

    @Override
    public synchronized void onAccessTokenRevoked() {
        SwingUtilities.invokeLater(() -> actionButton.setEnabled(false));
    }
}
