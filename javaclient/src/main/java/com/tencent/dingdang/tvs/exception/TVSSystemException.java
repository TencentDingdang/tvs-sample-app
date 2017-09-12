package com.tencent.dingdang.tvs.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This exception is only for exceptions returned from the Server as a System.Exception message
 */
@SuppressWarnings("serial")
public class TVSSystemException extends TVSException {
    private static final Logger log = LoggerFactory.getLogger(TVSSystemException.class);
    // Parsed exception code from raw string
    private TVSSystemExceptionCode exceptionCode;
    // Raw string for the exception code
    private final String rawCode;

    public TVSSystemException(String code, String description) {
        super(description);
        rawCode = code;
        try {
            this.exceptionCode = TVSSystemExceptionCode.valueOf(code);
        } catch (IllegalArgumentException e) {
            log.warn(String
                    .format("Received TVSSystemException with unrecognized code %s.", code));
        }
    }

    @Override
    public String toString() {
        return "" + rawCode + ": " + getMessage();
    }

    public String getDescription() {
        return getMessage();
    }

    public TVSSystemExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
