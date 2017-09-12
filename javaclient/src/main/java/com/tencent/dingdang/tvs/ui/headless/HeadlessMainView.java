package com.tencent.dingdang.tvs.ui.headless;

import com.tencent.dingdang.tvs.ui.MainUIHandler;
import com.tencent.dingdang.tvs.ui.controllers.MainViewController;

public class HeadlessMainView implements MainUIHandler {

    MainViewController mainViewController;

    public HeadlessMainView() {
        mainViewController = new MainViewController();
        System.out.println(getAppTitle());
    }

    @Override
    public String getAppTitle() {
        return mainViewController.getAppTitle();
    }
}
