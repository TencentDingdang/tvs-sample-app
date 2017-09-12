package com.tencent.dingdang.tvs.auth.companionapp;

@SuppressWarnings({
        "serial",
        "javadoc"
})
public class LWTException extends RuntimeException {
    private final int responseCode;

    public LWTException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
