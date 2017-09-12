package com.tencent.dingdang.tvs.message.response.system;

import com.tencent.dingdang.tvs.message.Payload;

/**
 * Exception response from the server
 */
public class Exception extends Payload {
    private String code;
    private String description;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }
}
