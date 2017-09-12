package com.tencent.dingdang.tvs.ui.controllers;

import java.util.Locale;

import com.tencent.dingdang.tvs.TVSController;
import com.tencent.dingdang.tvs.config.DeviceConfig;
import com.tencent.dingdang.tvs.config.DeviceConfigUtils;
import com.tencent.dingdang.tvs.ui.LocaleUIHandler;

public class LocaleViewController implements LocaleUIHandler {

    private TVSController controller;
    private DeviceConfig deviceConfig;

    public LocaleViewController(DeviceConfig config, TVSController controller) {
        this.deviceConfig = config;
        this.controller = controller;
    }

    @Override
    public void handleLocaleChange(Locale locale) {
        deviceConfig.setLocale(locale);
        DeviceConfigUtils.updateConfigFile(deviceConfig);
        controller.setLocale(locale);
    }
}
