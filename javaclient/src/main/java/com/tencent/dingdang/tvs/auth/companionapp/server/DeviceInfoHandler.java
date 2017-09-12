package com.tencent.dingdang.tvs.auth.companionapp.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.tencent.dingdang.tvs.auth.companionapp.CompanionAppAuthManager;
import com.tencent.dingdang.tvs.auth.companionapp.DeviceProvisioningInfo;

/**
 * A Jetty Handler for sending {@link DeviceProvisioningInfo} to companion applications.
 */
public class DeviceInfoHandler extends AbstractHandler {
    private final CompanionAppAuthManager authManager;

    /**
     * Creates a {@link DeviceInfoHandler} object.
     * @param authManager
     */
    public DeviceInfoHandler(CompanionAppAuthManager authManager) {
        this.authManager = authManager;
    }

    /**
     * Handle sending the necessary device information to the companion application to start provisioning.
     *
     * {@inheritDoc}
     */
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Only handle this as a GET request.
        if (!request.getMethod().equals("GET")) {
            baseRequest.setHandled(false);
            return;
        }

        // Setup the response. We'll always return JSON.
        baseRequest.setHandled(true);
        response.setContentType("application/json");

        DeviceProvisioningInfo deviceProvisioningInfo = authManager.getDeviceProvisioningInfo();

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(deviceProvisioningInfo.toJson().toString());
        return;
    }
}
