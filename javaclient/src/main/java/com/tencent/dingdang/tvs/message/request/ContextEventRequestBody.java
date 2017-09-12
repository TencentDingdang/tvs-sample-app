package com.tencent.dingdang.tvs.message.request;

import com.tencent.dingdang.tvs.message.request.context.ComponentState;

import java.util.List;

public class ContextEventRequestBody extends RequestBody {

    private final List<ComponentState> context;

    public ContextEventRequestBody(List<ComponentState> context, Event event) {
        super(event);
        this.context = context;
    }

    public final List<ComponentState> getContext() {
        return context;
    }

}
