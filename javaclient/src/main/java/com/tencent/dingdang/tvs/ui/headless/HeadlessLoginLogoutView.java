package com.tencent.dingdang.tvs.ui.headless;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.auth.AuthSetup;
import com.tencent.dingdang.tvs.auth.companionservice.RegCodeDisplayHandler;
import com.tencent.dingdang.tvs.config.DeviceConfig;
import com.tencent.dingdang.tvs.config.DeviceConfig.ProvisioningMethod;
import com.tencent.dingdang.tvs.ui.LoginLogoutUIHandler;
import com.tencent.dingdang.tvs.ui.controllers.LoginLogoutViewController;

public class HeadlessLoginLogoutView implements LoginLogoutUIHandler {

    private final LoginLogoutViewController loginLogoutController;
    private final DeviceConfig config;

    public HeadlessLoginLogoutView(DeviceConfig config, TVSController controller,
            AuthSetup authSetup, RegCodeDisplayHandler regCodeDisplayHandler) {
        this.config = config;
        this.loginLogoutController = new LoginLogoutViewController(controller, authSetup,
                regCodeDisplayHandler);
    }

    @Override
    public synchronized void onAccessTokenReceived(String accessToken) {
        System.out.println("Logged in.");
    }

    @Override
    public synchronized void onAccessTokenRevoked() {
        System.out.println("Logged out.");
    }

    @Override
    public void startLogin() {
        if (config.getProvisioningMethod() == ProvisioningMethod.COMPANION_SERVICE) {
            loginLogoutController.startLogin();
        } else {
            System.out.println("Can only login using the companion service.");
        }
    }

    @Override
    public void startLogout() {
        if (config.getProvisioningMethod() == ProvisioningMethod.COMPANION_SERVICE) {
            loginLogoutController.startLogout();
        } else {
            System.out.println("Can only logout of the companion service.");
        }
    }
}