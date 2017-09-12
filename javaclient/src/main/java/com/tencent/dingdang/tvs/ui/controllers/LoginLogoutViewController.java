package com.tencent.dingdang.tvs.ui.controllers;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.auth.AuthSetup;
import com.tencent.dingdang.tvs.auth.companionservice.RegCodeDisplayHandler;
import com.tencent.dingdang.tvs.ui.LoginLogoutUIHandler;

public class LoginLogoutViewController implements LoginLogoutUIHandler {
    public static final String LOGIN_LABEL = "Login";
    public static final String LOGOUT_LABEL = "Logout";

    private final TVSController controller;
    private final AuthSetup authSetup;
    private final RegCodeDisplayHandler regCodeDisplayHandler;

    public LoginLogoutViewController(
            final TVSController controller,
            final AuthSetup authSetup,
            final RegCodeDisplayHandler regCodeDisplayHandler) {
        this.controller = controller;
        this.authSetup = authSetup;
        this.regCodeDisplayHandler = regCodeDisplayHandler;
    }

    @Override
    public void startLogin() {
        controller.onUserActivity();
        authSetup.startProvisioningThread(regCodeDisplayHandler);
    }

    @Override
    public void startLogout() {
        controller.onUserActivity();
        authSetup.startLogoutThread(regCodeDisplayHandler);
    }

    @Override
    public void onAccessTokenReceived(String accessToken) {
    }

    @Override
    public void onAccessTokenRevoked() {
    }
}