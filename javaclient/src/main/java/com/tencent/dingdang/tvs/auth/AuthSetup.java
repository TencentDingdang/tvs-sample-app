package com.tencent.dingdang.tvs.auth;

import com.tencent.dingdang.tvs.auth.companionapp.CodeChallengeWorkflow;
import com.tencent.dingdang.tvs.auth.companionapp.CompanionAppAuthManager;
import com.tencent.dingdang.tvs.auth.companionapp.OAuth2ClientForPkce;
import com.tencent.dingdang.tvs.auth.companionapp.server.CompanionAppProvisioningServer;
import com.tencent.dingdang.tvs.auth.companionservice.CompanionServiceAuthManager;
import com.tencent.dingdang.tvs.auth.companionservice.CompanionServiceClient;
import com.tencent.dingdang.tvs.auth.companionservice.RegCodeDisplayHandler;
import com.tencent.dingdang.tvs.config.DeviceConfig;
import com.tencent.dingdang.tvs.config.DeviceConfig.ProvisioningMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Initializes and owns the two ways to provision this device: via a companion service where this
 * device acts as a client, and via a companion application where this device acts as a server.
 */
public class AuthSetup implements AccessTokenListener {

    private static final Logger log = LoggerFactory.getLogger(AuthSetup.class);

    private final DeviceConfig deviceConfig;
    private final Set<AccessTokenListener> accessTokenListeners = new HashSet<>();

    /**
     * Creates an {@link AuthSetup} object.
     *
     * @param deviceConfig
     *            Information about this device.
     */
    public AuthSetup(final DeviceConfig deviceConfig) {
        this.deviceConfig = deviceConfig;
    }

    public void addAccessTokenListener(AccessTokenListener accessTokenListener) {
        accessTokenListeners.add(accessTokenListener);
    }

    /**
     * Initializes threads for the {@link CompanionAppProvisioningServer} and the
     * {@link CompanionServiceClient}, depending on which is selected by the user.
     */
    public void startProvisioningThread(RegCodeDisplayHandler regCodeDisplayHandler) {
        if (deviceConfig.getProvisioningMethod() == ProvisioningMethod.COMPANION_APP) {
            OAuth2ClientForPkce oAuthClient =
                    new OAuth2ClientForPkce(deviceConfig.getCompanionAppInfo().getLwtUrl());
            CompanionAppAuthManager authManager = new CompanionAppAuthManager(deviceConfig,
                    oAuthClient, CodeChallengeWorkflow.getInstance(), this);

            final CompanionAppProvisioningServer registrationServer =
                    new CompanionAppProvisioningServer(authManager, deviceConfig);

            Runnable provisioningTask = () -> {
                try {
                    registrationServer.startServer();
                } catch (Exception e) {
                    log.error("Failed to start companion app provisioning server", e);
                }
            };
            new Thread(provisioningTask).start();
        } else if (deviceConfig.getProvisioningMethod() == ProvisioningMethod.COMPANION_SERVICE) {
            CompanionServiceClient remoteProvisioningClient =
                    new CompanionServiceClient(deviceConfig);
            final CompanionServiceAuthManager authManager = new CompanionServiceAuthManager(
                    deviceConfig, remoteProvisioningClient, regCodeDisplayHandler, this);

            Runnable provisioningTask = () -> {
                try {
                    authManager.startRemoteProvisioning();
                } catch (Exception e) {
                    if (e.getMessage() != null && e.getMessage().startsWith("InvalidSessionId")) {
                        log.error("Could not authenticate. Did you sign into Tencent before "
                                + "proceeding?");
                    }
                    log.error("Failed to start companion service client", e);
                }
            };
            new Thread(provisioningTask).start();
        }
    }

    public void startLogoutThread(RegCodeDisplayHandler regCodeDisplayHandler) {
        if (deviceConfig.getProvisioningMethod() == ProvisioningMethod.COMPANION_SERVICE) {
            CompanionServiceClient remoteProvisioningClient =
                    new CompanionServiceClient(deviceConfig);
            final CompanionServiceAuthManager authManager = new CompanionServiceAuthManager(
                    deviceConfig, remoteProvisioningClient, regCodeDisplayHandler, this);

            Runnable logoutTask = () -> {
                try {
                    authManager.revokeToken();
                } catch (Exception e) {
                    if (e.getMessage().startsWith("InvalidSessionId")) {
                        log.error(
                                "Could not logout. Were you logged in?");
                    }
                    log.error("Failed to start companion service client", e);
                }
            };
            new Thread(logoutTask).start();
        }
    }

    @Override
    public void onAccessTokenReceived(String accessToken) {
        accessTokenListeners
                .stream()
                .forEach(listener -> listener.onAccessTokenReceived(accessToken));
    }

    @Override
    public void onAccessTokenRevoked() {
        accessTokenListeners
                .stream()
                .forEach(listener -> listener.onAccessTokenRevoked());
    }
}
