package com.tencent.dingdang.tvs.auth;

/**
 * Interface for listening when the accessToken is received.
 */
public interface AccessTokenListener {
    /**
     * @param accessToken
     */
    void onAccessTokenReceived(String accessToken);

    void onAccessTokenRevoked();
}
