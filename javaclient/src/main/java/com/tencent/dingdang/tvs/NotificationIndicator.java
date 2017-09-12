package com.tencent.dingdang.tvs;

public interface NotificationIndicator {

    void onNewNotification();

    void onQueuedNotifications();

    void onClearNotifications();
}
