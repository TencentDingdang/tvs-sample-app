package com.tencent.dingdang.tvs.auth.companionapp;

import org.apache.commons.lang3.StringUtils;

import com.tencent.dingdang.tvs.auth.AuthConstants;
import com.tencent.dingdang.tvs.auth.OAuth2AccessToken;

/**
 * Container for information regarding accessTokens and refreshTokens.
 */
public class OAuth2TokensForPkce extends OAuth2AccessToken {

    private final String clientId;
    private final String refreshToken;

    /**
     * Creates an {@link OAuth2TokensForPkce} object.
     *
     * @param clientId The clientId of the companion app/service that initiated the workflow.
     * @param accessToken The accessToken returned from LWT.
     * @param refreshToken The refreshToken returned from LWT.
     * @param expiresIn Time in seconds that the accessToken expires in.
     */
    public OAuth2TokensForPkce(String clientId, String accessToken, String refreshToken, int expiresIn) {
        super(accessToken, expiresIn);

        if (StringUtils.isBlank(clientId)) {
            throw new IllegalArgumentException("Missing or empty " + AuthConstants.OAuth2.CLIENT_ID + " parameter");
        }

        if (StringUtils.isBlank(refreshToken)) {
            throw new IllegalArgumentException("Missing " + AuthConstants.OAuth2.REFRESH_TOKEN + " parameter");
        }

        this.clientId = clientId;
        this.refreshToken = refreshToken;
    }

    /**
     * @return clientId.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @return refreshToken.
     */
    public String getRefreshToken() {
        return refreshToken;
    }
}
