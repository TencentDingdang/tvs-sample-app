package com.tencent.dingdang.tvs.http;

import java.util.concurrent.Callable;

/**
 * A policy for describing how an action should be retried.
 */
public interface RetryPolicy {
    /**
     * Attempt to execute the {@link Callable}, and retry using the logic of the concrete
     * implementation of this interface if we receive an Exception of type exception.
     *
     * @param callable
     *            The {@link Callable} to call on each attempt.
     * @param exception
     *            The type of {@link Exception} to cause a retry.
     * @throws Exception
     */
    void tryCall(Callable<Void> callable, Class<? extends Throwable> exception) throws Exception;
}
