package com.tencent.dingdang.tvs;

import java.util.List;

public class AlertManagerFactory {

    public AlertManager getAlertManager(AlertEventListener listener, AlertHandler handler,
            DataStore<List<Alert>> dataStore) {
        return new AlertManager(listener, handler, dataStore);
    }
}
