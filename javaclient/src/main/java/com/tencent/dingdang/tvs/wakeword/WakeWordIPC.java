package com.tencent.dingdang.tvs.wakeword;

import java.io.IOException;

public abstract class WakeWordIPC {

    public enum IPCCommand {

        IPC_DISCONNECT(1), IPC_WAKE_WORD_DETECTED(2), IPC_PAUSE_WAKE_WORD_ENGINE(3),
        IPC_RESUME_WAKE_WORD_ENGINE(4), IPC_CONFIRM(5);

        private final int value;

        IPCCommand(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private WakeWordDetectedHandler handler = null;

    public WakeWordIPC(WakeWordDetectedHandler handler) {
        this.handler = handler;
    }

    protected void wakeWordDetected() {
        if (handler != null) {
            handler.onWakeWordDetected();
        }
    }

    public abstract void sendCommand(IPCCommand command) throws IOException;

    public abstract void init();
}
