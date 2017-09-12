package com.tencent.dingdang.tvs.ui.headless;

import static com.tencent.dingdang.tvs.ui.controllers.NotificationsViewController.NEW_NOTIFICATION;
import static com.tencent.dingdang.tvs.ui.controllers.NotificationsViewController.NO_NOTIFICATIONS;
import static com.tencent.dingdang.tvs.ui.controllers.NotificationsViewController.QUEUED_NOTIFICATIONS;

import com.tencent.dingdang.tvs.ui.NotificationsUIHandler;

public class HeadlessNotificationsView implements NotificationsUIHandler {

    @Override
    public void onNewNotification() {
        System.out.println(NEW_NOTIFICATION);
    }

    @Override
    public void onQueuedNotifications() {
        System.out.println(QUEUED_NOTIFICATIONS);
    }

    @Override
    public void onClearNotifications() {
        System.out.println(NO_NOTIFICATIONS);
    }
}
