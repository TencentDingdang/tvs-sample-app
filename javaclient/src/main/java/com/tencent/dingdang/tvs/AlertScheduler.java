package com.tencent.dingdang.tvs;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A timer used to trigger TVS alerts on schedule
 */
public class AlertScheduler extends Timer {
    private final Alert alert;
    private final AlertHandler handler;
    private boolean active = false;

    public AlertScheduler(final Alert alert, final AlertHandler handler) {
        super();
        schedule(new TimerTask() {
            @Override
            public void run() {
                setActive(true);
                handler.startAlert(alert.getToken());
            }
        }, Date.from(alert.getScheduledTime().toInstant()));
        this.alert = alert;
        this.handler = handler;
    }

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void cancel() {
        super.cancel();
        if (isActive()) {
            handler.stopAlert(alert.getToken());
            setActive(false);
        }
    }

    public Alert getAlert() {
        return alert;
    }
}
