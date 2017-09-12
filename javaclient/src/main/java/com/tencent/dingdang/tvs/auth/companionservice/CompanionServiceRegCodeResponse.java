package com.tencent.dingdang.tvs.auth.companionservice;

import org.apache.commons.lang3.StringUtils;

import com.tencent.dingdang.tvs.auth.AuthConstants;

/**
 * A container for the necessary provisioning information from the companion service to start the provisioning process.
 */
public class CompanionServiceRegCodeResponse {
    private final String sessionId;
    private final String regCode;

    /**
     * Creates a {@link CompanionServiceRegCodeResponse} object.
     *
     * @param sessionId The sessionId from the companion service.
     * @param regCode The registration code to be shown to the user to register on the companion service.
     */
    public CompanionServiceRegCodeResponse(String sessionId, String regCode) {
        if (StringUtils.isBlank(sessionId)) {
            throw new IllegalArgumentException("Missing " + AuthConstants.SESSION_ID + " parameter");
        }

        if (StringUtils.isBlank(regCode)) {
            throw new IllegalArgumentException("Missing " + AuthConstants.REG_CODE + " parameter");
        }

        this.sessionId = sessionId;
        this.regCode = regCode;
    }

    /**
     * @return sessionId.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @return regCode.
     */
    public String getRegCode() {
        return regCode;
    }
}
