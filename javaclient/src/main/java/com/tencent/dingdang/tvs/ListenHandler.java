package com.tencent.dingdang.tvs;

import com.tencent.dingdang.tvs.wakeword.WakeWordDetectedHandler;

public interface ListenHandler extends ExpectSpeechListener, ExpectStopCaptureListener,
        WakeWordDetectedHandler{ }
