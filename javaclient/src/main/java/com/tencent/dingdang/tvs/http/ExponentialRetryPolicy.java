package com.tencent.dingdang.tvs.http;

/**
 * Implements a {@link RetryPolicy} with an exponential backoff.
 */
public class ExponentialRetryPolicy extends AbstractRetryPolicy {
    private long mulitiplier;

    public ExponentialRetryPolicy(long mulitiplier, int maxAttempts) {
        super(maxAttempts);
        this.mulitiplier = mulitiplier;
    }

    @Override
    protected long getDelay(int attempts) {
        if (attempts == 0) {
            return 0;
        }

        attempts = Math.max(0, attempts - 1);
        double exp = Math.pow(2, attempts);
        return Math.round(exp * mulitiplier);
    }
}
