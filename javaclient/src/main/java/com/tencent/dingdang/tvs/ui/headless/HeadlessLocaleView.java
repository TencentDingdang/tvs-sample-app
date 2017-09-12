package com.tencent.dingdang.tvs.ui.headless;

import java.util.Locale;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.config.DeviceConfig;
import com.tencent.dingdang.tvs.ui.LocaleUIHandler;
import com.tencent.dingdang.tvs.ui.controllers.LocaleViewController;

public class HeadlessLocaleView implements LocaleUIHandler {

    private LocaleViewController localeViewController;

    public HeadlessLocaleView(DeviceConfig config, TVSController controller) {
        localeViewController = new LocaleViewController(config, controller);
    }

    @Override
    public void handleLocaleChange(Locale locale) {
        localeViewController.handleLocaleChange(locale);
    }
}
