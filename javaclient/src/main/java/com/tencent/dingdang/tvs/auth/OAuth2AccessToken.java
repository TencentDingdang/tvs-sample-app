package com.tencent.dingdang.tvs.auth;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * Holds relevent accessToken information from LWT.
 */
public class OAuth2AccessToken {

    private final String accessToken;
    private final long expiresTime;

    /**
     * Creates an {@link OAuth2AccessToken} object.
     *
     * @param accessToken The accessToken returned from LWT.
     * @param expiresIn Time in seconds that the accessToken expires in.
     */
    public OAuth2AccessToken(String accessToken, int expiresIn) {
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("Missing " + AuthConstants.OAuth2.ACCESS_TOKEN + " parameter");
        }

        if (expiresIn < 0) {
            throw new IllegalArgumentException("Invalid " + AuthConstants.OAuth2.EXPIRES_IN
                    + " value. Must be a positive number.");
        }

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.SECOND, expiresIn);

        this.accessToken = accessToken;
        this.expiresTime = calendar.getTime().getTime();
    }

    /**
     * @return accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * The time in milliseconds that the accessToken expires.
     * @return time in milliseconds that the accessToken expires.
     */
    public long getExpiresTime() {
        return expiresTime;
    }
}
