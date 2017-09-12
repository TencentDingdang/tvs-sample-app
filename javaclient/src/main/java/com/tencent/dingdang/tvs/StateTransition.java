package com.tencent.dingdang.tvs;

import java.util.Collections;
import java.util.Set;

public abstract class StateTransition<E> {

    protected Set<E> validStartStates;

    public StateTransition(Set<E> validStartStates) {
        this.validStartStates = Collections.unmodifiableSet(validStartStates);
    }

    public final void transition(State<E> currentState) {
        if (validStartStates.contains(currentState.get())) {
            onTransition(currentState);
        } else {
            onInvalidStartState(currentState);
        }
    }

    protected abstract void onTransition(State<E> state);

    protected abstract void onInvalidStartState(State<E> currentState);
}
