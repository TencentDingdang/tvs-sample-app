package com.tencent.dingdang.tvs.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public abstract class AbstractRetryPolicy implements RetryPolicy {
    private int maxAttempts;

    private static final Logger log = LoggerFactory.getLogger(AbstractRetryPolicy.class);

    public AbstractRetryPolicy(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tryCall(Callable<Void> callable, Class<? extends Throwable> exception)
            throws Exception {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                callable.call();
                break;
            } catch (Exception e) {
                attempts++;
                if ((exception != null) && (exception.isAssignableFrom(e.getClass()))
                        && !(attempts >= maxAttempts)) {
                    log.warn("Error occured while making call. This call will retry.", e);
                    Thread.sleep(getDelay(attempts));
                } else {
                    throw e;
                }
            }
        }
    }

    /**
     * Get the expected delay in milliseconds.
     *
     * @param attempts
     * @return
     */
    protected abstract long getDelay(int attempts);
}
