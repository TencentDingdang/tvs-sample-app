package com.tencent.dingdang.tvs;

import com.tencent.dingdang.tvs.config.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.type.TypeReference;

import java.util.List;

/**
 * A file-backed data store for TVS Alerts
 */
public class AlertsFileDataStore extends FileDataStore<List<Alert>> {
    private static final String ALARM_FILE = "alarms.json";
    private static AlertsFileDataStore sInstance = new AlertsFileDataStore();

    private AlertsFileDataStore() {
        super(ALARM_FILE);
    }

    public synchronized static AlertsFileDataStore getInstance() {
        return sInstance;
    }

    @Override
    public ObjectReader getObjectReader() {
        return ObjectMapperFactory.getObjectReader().withType(new TypeReference<List<Alert>>() {
        });
    }
}
