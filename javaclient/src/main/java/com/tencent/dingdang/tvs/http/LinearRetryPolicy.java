package com.tencent.dingdang.tvs.http;

/**
 * Implements a {@link RetryPolicy} with a linear backoff.
 */
public class LinearRetryPolicy extends AbstractRetryPolicy {
    private long initialDelay;

    public LinearRetryPolicy(long initialDelay, int maxAttempts) {
        super(maxAttempts);
        this.initialDelay = initialDelay;
    }

    @Override
    protected long getDelay(int attempts) {
        return attempts * initialDelay;
    }
}
