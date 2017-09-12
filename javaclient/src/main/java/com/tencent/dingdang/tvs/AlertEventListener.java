package com.tencent.dingdang.tvs;

public interface AlertEventListener {

    void onAlertStarted(String alertToken);

    void onAlertStopped(String alertToken);

    void onAlertSet(String alertToken, boolean success);

    void onAlertDelete(String alertToken, boolean success);

}
