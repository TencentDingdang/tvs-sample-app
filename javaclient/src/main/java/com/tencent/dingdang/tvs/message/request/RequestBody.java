package com.tencent.dingdang.tvs.message.request;

public class RequestBody {

    private final Event event;

    public RequestBody(Event event) {
        this.event = event;
    }

    public final Event getEvent() {
        return event;
    }
}
