package com.tencent.dingdang.tvs.ui;

import com.tencent.dingdang.tvs.PlaybackAction;
import com.tencent.dingdang.tvs.auth.AccessTokenListener;

/**
 * Handles playback events (Play/Pause/Next/Previous).
 */
public interface PlaybackControlsUIHandler extends SpeechStateChangeListener, AccessTokenListener {

    /**
     * Handler for button events. When a playback button is pressed, call this method with the
     * appropriate action (Play, Paused, Next, Previous).
     *
     * @param action
     *         Action that occurred.
     */
    void buttonPressed(PlaybackAction action);
}
