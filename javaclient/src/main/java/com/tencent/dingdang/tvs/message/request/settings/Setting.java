package com.tencent.dingdang.tvs.message.request.settings;

import java.util.AbstractMap;

@SuppressWarnings("serial")
public class Setting extends AbstractMap.SimpleEntry<String, String>{
    public Setting(String key, String value) {
        super(key, value);
    }
}