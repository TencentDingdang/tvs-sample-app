package com.tencent.dingdang.tvs.ui.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tencent.dingdang.tvs.ui.MainUIHandler;

public class MainViewController implements MainUIHandler {
    private static final Logger log = LoggerFactory.getLogger(MainViewController.class);

    private static final String APP_TITLE = "Tencent Voice Service";
    private static final String VERSION_PROPERTIES_FILE = "/res/version.properties";
    private static final String VERSION_KEY = "version";

    @Override
    public String getAppTitle() {
        String version = getAppVersion();
        String title = APP_TITLE;
        if (version != null) {
            title += " - v" + version;
        }
        return title;
    }

    private String getAppVersion() {
        final Properties properties = new Properties();
        try (final InputStream stream = getClass().getResourceAsStream(VERSION_PROPERTIES_FILE)) {
            properties.load(stream);
            if (properties.containsKey(VERSION_KEY)) {
                return properties.getProperty(VERSION_KEY);
            }
        } catch (IOException e) {
            log.warn("version.properties file not found on classpath");
        }
        return null;
    }
}
