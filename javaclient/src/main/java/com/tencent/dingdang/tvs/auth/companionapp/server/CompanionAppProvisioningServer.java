package com.tencent.dingdang.tvs.auth.companionapp.server;

import com.tencent.dingdang.tvs.auth.companionapp.CompanionAppAuthManager;
import com.tencent.dingdang.tvs.config.DeviceConfig;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * A Jetty server for handling local device provisioning.
 */
public class CompanionAppProvisioningServer {
    private final CompanionAppAuthManager authManager;
    private final DeviceConfig deviceConfig;

    /**
     * Creates a {@link CompanionAppProvisioningServer} object.
     *
     * @param authManager
     * @param deviceConfig
     */
    public CompanionAppProvisioningServer(CompanionAppAuthManager authManager,
            DeviceConfig deviceConfig) {
        this.authManager = authManager;
        this.deviceConfig = deviceConfig;
    }

    /**
     * Start the Jetty server and setup port information, resources, etc.
     *
     * @throws Exception
     */
    public void startServer() throws Exception {
        int localPort = deviceConfig.getCompanionAppInfo().getLocalPort();
        Server jettyServer = new Server();

        ContextHandler beginContext = new ContextHandler("/provision/deviceInfo");
        beginContext.setAllowNullPathInfo(true);
        beginContext.setHandler(new DeviceInfoHandler(authManager));

        ContextHandler finishContext = new ContextHandler("/provision/companionInfo");
        finishContext.setAllowNullPathInfo(true);
        finishContext.setHandler(new CompanionInfoHandler(authManager));

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] {
                beginContext,
                finishContext,
        });
        jettyServer.setHandler(contexts);

        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(localPort);

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(deviceConfig.getCompanionAppInfo().getSslKeyStore());
        sslContextFactory.setKeyStorePassword(deviceConfig
                .getCompanionAppInfo()
                .getSslKeyStorePassphrase());
        sslContextFactory.setKeyStoreType("PKCS12");

        // SSL HTTP Configuration
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        // SSL Connector
        ServerConnector sslConnector =
                new ServerConnector(jettyServer, new SslConnectionFactory(sslContextFactory,
                        HttpVersion.HTTP_1_1.asString()), new HttpConnectionFactory(https_config));
        sslConnector.setPort(localPort);
        jettyServer.setConnectors(new Connector[] { sslConnector
        });

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }
}
