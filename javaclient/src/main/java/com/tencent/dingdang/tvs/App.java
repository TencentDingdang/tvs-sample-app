package com.tencent.dingdang.tvs;

import com.tencent.dingdang.tvs.auth.AuthSetup;
import com.tencent.dingdang.tvs.config.DeviceConfig;
import com.tencent.dingdang.tvs.config.DeviceConfigUtils;
import com.tencent.dingdang.tvs.http.TVSClientFactory;
import com.tencent.dingdang.tvs.ui.graphical.GraphicalUI;
import com.tencent.dingdang.tvs.ui.headless.HeadlessUI;
import com.tencent.dingdang.tvs.wakeword.WakeWordIPCFactory;

public class App {

    private TVSController controller;

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            new App(args[0]);
        } else {
            new App();
        }
    }

    public App() throws Exception {
        this(DeviceConfigUtils.readConfigFile());
    }

    public App(String configName) throws Exception {
        this(DeviceConfigUtils.readConfigFile(configName));
    }

    public App(DeviceConfig config) throws Exception {
        AuthSetup authSetup = new AuthSetup(config);
        controller =
                new TVSController(new TVSAudioPlayerFactory(), new AlertManagerFactory(),
                        getTVSClientFactory(config), DialogRequestIdAuthority.getInstance(),
                        new WakeWordIPCFactory(), config);
        if (config.getHeadlessModeEnabled()) {
            new HeadlessUI(controller, authSetup, config);
        } else {
            new GraphicalUI(controller, authSetup, config);
        }
    }

    protected TVSClientFactory getTVSClientFactory(DeviceConfig config) {
        return new TVSClientFactory(config);
    }

    protected TVSController getController() {
        return controller;
    }
}
