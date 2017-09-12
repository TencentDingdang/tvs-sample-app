package com.tencent.dingdang.tvs.message.request.settings;

@SuppressWarnings("serial")
public class LocaleSetting extends Setting {
    private static final String SETTING_KEY = "locale";

    public LocaleSetting(String locale) {
        super(SETTING_KEY, locale);
    }
}