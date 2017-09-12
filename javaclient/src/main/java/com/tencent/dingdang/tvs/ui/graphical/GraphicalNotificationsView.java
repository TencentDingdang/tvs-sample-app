package com.tencent.dingdang.tvs.ui.graphical;

import static com.tencent.dingdang.tvs.ui.controllers.NotificationsViewController.NEW_NOTIFICATION;
import static com.tencent.dingdang.tvs.ui.controllers.NotificationsViewController.NO_NOTIFICATIONS;
import static com.tencent.dingdang.tvs.ui.controllers.NotificationsViewController.QUEUED_NOTIFICATIONS;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.tencent.dingdang.tvs.ui.NotificationsUIHandler;

public class GraphicalNotificationsView extends JPanel implements NotificationsUIHandler {

    private static final String NOTIFICATION_NAME = "notification";

    private JLabel notificationStatus;

    GraphicalNotificationsView() {
        super();
        notificationStatus = new JLabel(NO_NOTIFICATIONS);
        notificationStatus.setName(NOTIFICATION_NAME);
        this.add(notificationStatus);
        this.setSize(150, 50);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    public void onNewNotification() {
        SwingUtilities.invokeLater(() -> {
            setBackground(Color.YELLOW);
            notificationStatus.setText(NEW_NOTIFICATION);
        });
    }

    @Override
    public void onQueuedNotifications() {
        SwingUtilities.invokeLater(() -> {
            setBackground(Color.YELLOW);
            notificationStatus.setText(QUEUED_NOTIFICATIONS);
        });
    }

    @Override
    public void onClearNotifications() {
        SwingUtilities.invokeLater(() -> {
            setBackground(null);
            notificationStatus.setText(NO_NOTIFICATIONS);
        });
    }
}
