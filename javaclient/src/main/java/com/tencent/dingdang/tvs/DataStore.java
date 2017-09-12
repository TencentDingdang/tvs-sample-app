package com.tencent.dingdang.tvs;

public interface DataStore<T> {

    void loadFromDisk(ResultListener<T> listener);

    void writeToDisk(T payload, ResultListener<T> resultListener);

    interface ResultListener<T> {
        public void onSuccess(T payload);

        public void onFailure();
    }
}
