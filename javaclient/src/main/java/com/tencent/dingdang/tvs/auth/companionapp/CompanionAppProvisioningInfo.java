package com.tencent.dingdang.tvs.auth.companionapp;

import org.apache.commons.lang3.StringUtils;

import com.tencent.dingdang.tvs.auth.AuthConstants;
import com.tencent.dingdang.tvs.auth.MissingParameterException;

/**
 * A container for the necessary provisioning information from the companion app/service.
 */
public class CompanionAppProvisioningInfo {
    private final String sessionId;
    private final String clientId;
    private final String redirectUri;
    private final String authCode;

    /**
     * Creates a {@link CompanionAppProvisioningInfo} object.
     *
     * @param sessionId The sessionId used to initiate this information.
     * @param clientId The clientId of the companion.
     * @param redirectUri The redirectUri used by the companion.
     * @param authCode The authCode from the companion.
     */
    public CompanionAppProvisioningInfo(String sessionId, String clientId, String redirectUri, String authCode) {
        super();

        if (StringUtils.isBlank(sessionId)) {
            throw new MissingParameterException(AuthConstants.SESSION_ID);
        }

        if (StringUtils.isBlank(clientId)) {
            throw new MissingParameterException(AuthConstants.CLIENT_ID);
        }

        if (StringUtils.isBlank(redirectUri)) {
            throw new MissingParameterException(AuthConstants.REDIRECT_URI);
        }

        if (StringUtils.isBlank(authCode)) {
            throw new MissingParameterException(AuthConstants.AUTH_CODE);
        }

        this.sessionId = sessionId;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.authCode = authCode;
    }

    /**
     * @return sessionId.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @return clientId.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @return redirectUri.
     */
    public String getRedirectUri() {
        return redirectUri;
    }

    /**
     * @return authCode.
     */
    public String getAuthCode() {
        return authCode;
    }

}
