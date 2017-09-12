package com.tencent.dingdang.tvs;

import com.tencent.dingdang.tvs.http.TVSClient.Resource;
import com.tencent.dingdang.tvs.http.MultipartParser;
import com.tencent.dingdang.tvs.http.RetryPolicy;

import org.eclipse.jetty.client.api.ContentProvider;

import java.util.Optional;

public class TVSRequest {
    private final Resource resource;
    private final ContentProvider contentProvider;
    private final RetryPolicy retryPolicy;
    private final MultipartParser multipartParser;
    private final RequestListener requestListener;

    public TVSRequest(Resource resource, ContentProvider contentProvider, RetryPolicy retryPolicy, MultipartParser multipartParser, RequestListener requestListener) {
        this.resource = resource;
        this.contentProvider = contentProvider;
        this.retryPolicy = retryPolicy;
        this.multipartParser = multipartParser;
        this.requestListener = requestListener;
    }

    public TVSRequest(Resource resource, ContentProvider contentProvider, RetryPolicy retryPolicy, MultipartParser multipartParser) {
        this(resource, contentProvider, retryPolicy, multipartParser, null);
    }

    public Resource getResource() {
        return resource;
    }

    public ContentProvider getContentProvider() {
        return contentProvider;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public MultipartParser getMultipartParser() {
        return multipartParser;
    }

    public Optional<RequestListener> getRequestListener() {
        return Optional.ofNullable(requestListener);
    }
}
