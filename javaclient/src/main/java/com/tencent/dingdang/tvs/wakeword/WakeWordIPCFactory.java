package com.tencent.dingdang.tvs.wakeword;

import java.io.IOException;

public class WakeWordIPCFactory {

    public WakeWordIPC createWakeWordIPC(WakeWordDetectedHandler handler, int portNumber)
            throws IOException {
        return new WakeWordIPCSocket(handler, portNumber);
    }
}
