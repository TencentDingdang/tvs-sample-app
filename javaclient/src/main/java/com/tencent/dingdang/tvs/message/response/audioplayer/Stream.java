package com.tencent.dingdang.tvs.message.response.audioplayer;

import com.tencent.dingdang.tvs.message.response.ProgressReport;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.InputStream;

public final class Stream {
    private String url;
    private String token;
    private String expiryTime;
    private long offsetInMilliseconds;
    private ProgressReport progressReport;
    private boolean urlIsAContentId;
    private String expectedPreviousToken;

    @JsonIgnore
    private InputStream attachedContent;

    public String getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public long getOffsetInMilliseconds() {
        return offsetInMilliseconds;
    }

    public boolean getProgressReportRequired() {
        return progressReport != null && progressReport.isRequired();
    }

    public ProgressReport getProgressReport() {
        return progressReport;
    }

    public String getExpectedPreviousToken() {
        return expectedPreviousToken;
    }

    public void setUrl(String url) {
        urlIsAContentId = url.startsWith("cid");
        if (urlIsAContentId) {
            this.url = url.substring(4);
        } else {
            this.url = url;
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    public void setOffsetInMilliseconds(long offsetInMilliseconds) {
        this.offsetInMilliseconds = offsetInMilliseconds;
    }

    public void setProgressReport(ProgressReport progressReport) {
        this.progressReport = progressReport;
    }

    public void setExpectedPreviousToken(String expectedPreviousToken) {
        this.expectedPreviousToken = expectedPreviousToken;
    }

    public boolean requiresAttachedContent() {
        return urlIsAContentId && !hasAttachedContent();
    }

    public boolean hasAttachedContent() {
        return attachedContent != null;
    }

    public void setAttachedContent(InputStream attachedContent) {
        this.attachedContent = attachedContent;
    }

    @JsonIgnore
    public InputStream getAttachedContent() {
        return attachedContent;
    }
}
