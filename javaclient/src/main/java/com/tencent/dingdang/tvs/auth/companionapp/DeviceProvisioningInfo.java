package com.tencent.dingdang.tvs.auth.companionapp;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.tencent.dingdang.tvs.auth.AuthConstants;

/**
 * A container for the necessary provisioning information about this device.
 */
public class DeviceProvisioningInfo {
    private final String productId;
    private final String dsn;
    private final String sessionId;
    private final String codeChallenge;
    private final String codeChallengeMethod;

    /**
     * Creates a {@link DeviceProvisioningInfo} object.
     *
     * @param productId The productId of this device.
     * @param dsn The dsn of this device.
     * @param sessionId The sessionId associated with this information.
     * @param codeChallenge The codeChallenge for this request.
     * @param codeChallengeMethod  The codeChallengeMethod for this request.
     */
    public DeviceProvisioningInfo(String productId, String dsn, String sessionId, String codeChallenge, String codeChallengeMethod) {
        this.productId = productId;
        this.dsn = dsn;
        this.sessionId = sessionId;
        this.codeChallenge = codeChallenge;
        this.codeChallengeMethod = codeChallengeMethod;
    }

    /**
     * @return productId.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @return dsn.
     */
    public String getDsn() {
        return dsn;
    }

    /**
     * @return sessionId.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @return codeChallenge.
     */
    public String getCodeChallenge() {
        return codeChallenge;
    }

    /**
     * @return codeChallengeMethod.
     */
    public String getCodeChallengeMethod() {
        return codeChallengeMethod;
    }

    /**
     * Serialize this object to JSON.
     *
     * @return A JSON representation of this object.
     */
    public JsonObject toJson() {
        return toJson(false);
    }

    /**
     * Serialize this object to JSON.
     *
     * @param removeSessionId Whether or not to remove sessionId in the serialization.
     * @return A JSON representation of this object.
     */
    public JsonObject toJson(boolean removeSessionId) {
        JsonObjectBuilder builder =
                Json.createObjectBuilder()
                        .add(AuthConstants.PRODUCT_ID, productId)
                        .add(AuthConstants.DSN, dsn)
                        .add(AuthConstants.CODE_CHALLENGE, codeChallenge)
                        .add(AuthConstants.CODE_CHALLENGE_METHOD, codeChallengeMethod);

        if (!removeSessionId) {
            builder.add(AuthConstants.SESSION_ID, sessionId);
        }

        JsonObject object = builder.build();
        return object;
    }
}
