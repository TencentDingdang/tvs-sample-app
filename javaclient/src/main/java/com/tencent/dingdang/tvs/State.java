package com.tencent.dingdang.tvs;

import java.util.concurrent.atomic.AtomicReference;

public class State<V> extends AtomicReference<V> {
    public State(V startState) {
        set(startState);
    }
}
