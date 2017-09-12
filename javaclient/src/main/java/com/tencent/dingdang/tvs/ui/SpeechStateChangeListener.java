package com.tencent.dingdang.tvs.ui;

public interface SpeechStateChangeListener {

    /**
     * Handles UI logic to display the processing state to the user
     */
    void onProcessing();

    /**
     * Handles UI logic to display the listening state to the user
     */
    void onListening();

    /**
     * Handles UI logic to display the processing finished state to the user
     */
    void onProcessingFinished();
}
