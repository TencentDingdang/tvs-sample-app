package com.tencent.dingdang.tvs.http;

import com.tencent.dingdang.tvs.DirectiveEnqueuer;
import com.tencent.dingdang.tvs.ResultListener;
import com.tencent.dingdang.tvs.config.DeviceConfig;

import org.eclipse.jetty.util.ssl.SslContextFactory;

public class TVSClientFactory {
    private DeviceConfig config;

    public TVSClientFactory(DeviceConfig config) {
        this.config = config;
    }

    public TVSClient getTVSClient(DirectiveEnqueuer directiveEnqueuer,
                                  ParsingFailedHandler parsingFailedHandler, ResultListener resultListener)
            throws Exception {
        return new TVSClient(config.getTvsHost(), directiveEnqueuer, new SslContextFactory(),
                parsingFailedHandler, resultListener);
    }
}
