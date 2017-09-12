package com.tencent.dingdang.tvs.ui;

import com.tencent.dingdang.tvs.ExpectSpeechListener;
import com.tencent.dingdang.tvs.ExpectStopCaptureListener;
import com.tencent.dingdang.tvs.ListenHandler;
import com.tencent.dingdang.tvs.auth.AccessTokenListener;
import com.tencent.dingdang.tvs.wakeword.WakeWordDetectedHandler;

/**
 * Controls behavior related to the user indicating a desire to speak to TVS.
 */
public interface ListenUIHandler
        extends SpeechStateChangeListener, ExpectStopCaptureListener, ExpectSpeechListener,
        WakeWordDetectedHandler, ListenHandler, AccessTokenListener {

    /**
     * Triggered when the user indicated a desire to listen. Note: This method may also be triggered
     * while the user is already listening, in which case it should trigger an end to listening.
     */
    void listenButtonPressed();

    /**
     * Registers listeners that care about various speech state changes.
     */
    void addSpeechStateChangeListener(SpeechStateChangeListener listener);
}
