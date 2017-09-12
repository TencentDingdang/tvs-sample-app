package com.tencent.dingdang.tvs.ui.graphical;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.auth.AuthSetup;
import com.tencent.dingdang.tvs.config.DeviceConfig;
import com.tencent.dingdang.tvs.config.DeviceConfig.ProvisioningMethod;
import com.tencent.dingdang.tvs.ui.LoginLogoutUIHandler;
import com.tencent.dingdang.tvs.ui.controllers.LoginLogoutViewController;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GraphicalLoginLogoutView extends JPanel implements LoginLogoutUIHandler {

    private JButton loginButton;
    private JButton logoutButton;

    private final TVSController controller;
    private final AuthSetup authSetup;
    private final GraphicalUI graphicalUI;

    private LoginLogoutViewController loginLogoutController;

    public GraphicalLoginLogoutView(DeviceConfig config, TVSController controller,
            AuthSetup authSetup, GraphicalUI graphicalUI) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));

        this.controller = controller;
        this.authSetup = authSetup;
        this.graphicalUI = graphicalUI;

        if (config.getProvisioningMethod() == ProvisioningMethod.COMPANION_SERVICE) {
            loginButton = new JButton(LoginLogoutViewController.LOGIN_LABEL);
            loginButton.setEnabled(true);
            loginButton.addActionListener(e -> startLogin());
            this.add(loginButton);

            logoutButton = new JButton(LoginLogoutViewController.LOGOUT_LABEL);
            logoutButton.setEnabled(false);
            logoutButton.addActionListener(e -> startLogout());
            this.add(logoutButton);
        }
    }

    private void createLoginLogoutControllerIfMissing() {
        if (loginLogoutController == null) {
            this.loginLogoutController = new LoginLogoutViewController(controller, authSetup,
                    graphicalUI.getRegCodeDisplayHandler());
        }
    }

    @Override
    public void startLogin() {
        createLoginLogoutControllerIfMissing();
        loginLogoutController.startLogin();
    }

    @Override
    public void startLogout() {
        createLoginLogoutControllerIfMissing();
        loginLogoutController.startLogout();
    }

    private void afterLogin() {
        if (loginButton != null) {
            loginButton.setEnabled(false);
        }
        if (logoutButton != null) {
            logoutButton.setEnabled(true);
        }
    }

    private void afterLogout() {
        if (loginButton != null) {
            loginButton.setEnabled(true);
        }
        if (logoutButton != null) {
            logoutButton.setEnabled(false);
        }
    }

    @Override
    public synchronized void onAccessTokenReceived(String accessToken) {
        SwingUtilities.invokeLater(() -> afterLogin());
    }

    @Override
    public synchronized void onAccessTokenRevoked() {
        SwingUtilities.invokeLater(() -> afterLogout());
    }
}