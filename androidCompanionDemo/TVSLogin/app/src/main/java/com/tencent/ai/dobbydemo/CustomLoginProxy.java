package com.tencent.ai.dobbydemo;

import android.content.Context;

import com.tencent.ai.tvs.AbsProxy;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class CustomLoginProxy extends AbsProxy {

    private IWXAPI helper;
    private String appid;

    public void registerApp(Context context) {
        helper = WXAPIFactory.createWXAPI(context, appid, false);
        helper.registerApp(appid);
        setWXAttr(helper, this);
    }

    public void unRegisterApp() {
        helper.unregisterApp();
        clearWXAttr();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
}
