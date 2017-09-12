package com.tencent.dingdang.tvs.ui.headless;

import static com.tencent.dingdang.tvs.ui.controllers.AccessTokenViewController.ACCESS_TOKEN_LABEL;

import com.tencent.dingdang.tvs.ui.AccessTokenUIHandler;

public class HeadlessAccessTokenView implements AccessTokenUIHandler {

    @Override
    public synchronized void onAccessTokenReceived(String accessToken) {
        System.out.println(ACCESS_TOKEN_LABEL + " " + accessToken);
    }

    @Override
    public void onAccessTokenRevoked() {
        System.out.println("Access token was revoked.");
    }
}
