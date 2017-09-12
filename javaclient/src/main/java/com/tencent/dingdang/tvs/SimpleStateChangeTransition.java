package com.tencent.dingdang.tvs;

import org.slf4j.Logger;

import java.util.Set;

public class SimpleStateChangeTransition<E> extends StateTransition<E> {

    private final E endState;

    private final Logger errorLogger;

    public SimpleStateChangeTransition(Set<E> validStartStates, E endState, Logger errorLogger) {
        super(validStartStates);
        this.endState = endState;
        this.errorLogger = errorLogger;
    }

    @Override
    protected final void onTransition(State<E> state) {
        state.set(endState);
    }

    @Override
    protected final void onInvalidStartState(State<E> currentState) {
        errorLogger.debug("Invalid {} from {}.", this.getClass().getSimpleName(),
                currentState.get());
    }
}
