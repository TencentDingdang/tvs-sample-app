package com.tencent.dingdang.tvs.ui.graphical;

import static com.tencent.dingdang.tvs.ui.controllers.AccessTokenViewController.ACCESS_TOKEN_LABEL;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.auth.AuthSetup;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.tencent.dingdang.tvs.ui.AccessTokenUIHandler;

public class GraphicalAccessTokenView extends JPanel implements AccessTokenUIHandler {

    private static final int TEXT_FIELD_COLUMNS = 50;

    private JTextField tokenTextField;

    GraphicalAccessTokenView(AuthSetup authSetup, TVSController controller) {
        super(new GridLayout(0, 1));
        this.add(new JLabel(ACCESS_TOKEN_LABEL));

        tokenTextField = new JTextField(TEXT_FIELD_COLUMNS);
        tokenTextField.addActionListener(e -> {
            controller.onUserActivity();
            authSetup.onAccessTokenReceived(tokenTextField.getText());
        });
        this.add(tokenTextField);
    }

    @Override
    public synchronized void onAccessTokenReceived(String accessToken) {
        SwingUtilities.invokeLater(() -> tokenTextField.setText(accessToken));
    }

    @Override
    public synchronized void onAccessTokenRevoked() {
        SwingUtilities.invokeLater(() -> tokenTextField.setText(""));
    }
}
