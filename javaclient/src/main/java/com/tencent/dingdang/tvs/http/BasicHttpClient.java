package com.tencent.dingdang.tvs.http;

import com.tencent.dingdang.tvs.http.TVSClient.RequestException;
import com.tencent.dingdang.tvs.http.jetty.InputStreamResponseListener;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class BasicHttpClient {
    private static final Logger log = LoggerFactory.getLogger(BasicHttpClient.class);

    private static final int REQUEST_TIMEOUT_IN_S = 15;

    private HttpClient httpClient;
    private SslContextFactory sslContextFactory;

    public BasicHttpClient(SslContextFactory sslContextFactory) {
        this.sslContextFactory = sslContextFactory;
        try {
            createNewHttpClient();
        } catch (Exception e) {
            // Since there is no HttpClient initially to stop, this
            // method cannot throw an exception.
            log.error("Exception creating the HttpClient", e);
        }
    }

    protected void stopHttpClient() throws Exception {
        if ((httpClient != null) && httpClient.isStarted()) {
            try {
                httpClient.stop();
            } catch (Exception e) {
                log.error("There was a problem stopping the HTTP client", e);
                throw e;
            }
        }
    }

    protected void createNewHttpClient() throws Exception {
        stopHttpClient();
        httpClient = new HttpClient(sslContextFactory);
        httpClient.start();
    }

    public InputStream downloadResourceAtUrl(String url) throws Exception {
        if (!httpClient.isStarted()) {
            createNewHttpClient();
        }
        Request r = httpClient.newRequest(url);
        if (!r.getScheme().toLowerCase().equals("https")) {
            throw new IllegalArgumentException("URL should be https: " + url);
        }
        InputStream is = doRequestInternal(r);
        return is;
    }

    protected InputStream doRequestInternal(Request request) {
        InputStreamResponseListener responseListener = new InputStreamResponseListener();
        Response response;
        InputStream inputStream = null;

        try {
            request.send(responseListener);
            response = responseListener.get(REQUEST_TIMEOUT_IN_S, TimeUnit.SECONDS);
            inputStream = responseListener.getInputStream();
        } catch (Exception e) {
            IOUtils.closeQuietly(inputStream);
            throw new RequestException(e);
        }

        int statusCode = response.getStatus();
        log.info("Response code: {}", statusCode);
        log.info("Response headers: {}", response.getHeaders());

        if (statusCode == HttpStatus.NO_CONTENT_204) {
            log.error("This response had no content.");
        }

        return inputStream;
    }
}
