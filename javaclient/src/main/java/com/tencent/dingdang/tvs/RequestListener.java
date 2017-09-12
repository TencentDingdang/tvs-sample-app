package com.tencent.dingdang.tvs;

public abstract class RequestListener {
    public void onRequestFinished() {
    }

    public void onRequestError(Throwable e) {
    }

    public void onRequestSuccess() {
    }
}
