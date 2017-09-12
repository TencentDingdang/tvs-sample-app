package com.tencent.dingdang.tvs.ui.headless;

import static com.tencent.dingdang.tvs.ui.controllers.DeviceNameViewController.DEVICE_LABEL;
import static com.tencent.dingdang.tvs.ui.controllers.DeviceNameViewController.DSN_LABEL;

import com.tencent.dingdang.tvs.ui.DeviceNameUIHandler;

public class HeadlessDeviceNameView implements DeviceNameUIHandler {

    public HeadlessDeviceNameView(String productId, String dsn) {
        System.out.println(DEVICE_LABEL + productId + ", " + DSN_LABEL + dsn);
    }

}
