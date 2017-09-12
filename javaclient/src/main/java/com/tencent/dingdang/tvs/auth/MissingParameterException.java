package com.tencent.dingdang.tvs.auth;

@SuppressWarnings("javadoc")
public class MissingParameterException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private final String missingParameter;

    public MissingParameterException(String missingParameter) {
        super();
        this.missingParameter = missingParameter;
    }

    @Override
    public String getMessage() {
        return "The following parameter was missing or an empty string: " + this.missingParameter;
    }

    public String getMissingParameter() {
        return this.missingParameter;
    }
}
