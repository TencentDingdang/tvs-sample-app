package com.tencent.dingdang.tvs.message.response;

public class ProgressReport {
    private long progressReportDelayInMilliseconds;
    private long progressReportIntervalInMilliseconds;

    public long getProgressReportDelayInMilliseconds() {
        return progressReportDelayInMilliseconds;
    }

    public long getProgressReportIntervalInMilliseconds() {
        return progressReportIntervalInMilliseconds;
    }

    public void setProgressReportDelayInMilliseconds(long progressReportDelayInMilliseconds) {
        this.progressReportDelayInMilliseconds = progressReportDelayInMilliseconds;
    }

    public void setProgressReportIntervalInMilliseconds(long progressReportIntervalInMilliseconds) {
        this.progressReportIntervalInMilliseconds = progressReportIntervalInMilliseconds;
    }

    public boolean isRequired() {
        return progressReportDelayInMilliseconds > 0 || progressReportIntervalInMilliseconds > 0;
    }
}
