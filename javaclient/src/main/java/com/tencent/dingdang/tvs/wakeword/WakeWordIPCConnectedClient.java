package com.tencent.dingdang.tvs.wakeword;

import com.tencent.dingdang.tvs.wakeword.WakeWordIPC.IPCCommand;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// this class represents a single wake word agent that is connected
// over some form of IPC.  This agent is expected to notify us
// when the wake-word is detected from the audio input.
public class WakeWordIPCConnectedClient implements Runnable {

    private WakeWordIPCSocket wakeWordIPCSocket = null;
    private Thread connectionThread = null;
    private Socket clientSocket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private volatile boolean running = true;

    private static final Logger log = LoggerFactory.getLogger(WakeWordIPCSocket.class);

    public WakeWordIPCConnectedClient(Socket clientSocket, WakeWordIPCSocket wakeWordIPCSocket)
            throws IOException {
        this.wakeWordIPCSocket = wakeWordIPCSocket;
        this.clientSocket = clientSocket;
        input = new DataInputStream(clientSocket.getInputStream());
        output = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void init() {
        if (connectionThread == null) {
            connectionThread = new Thread(this);
            connectionThread.start();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                int receivedCommand = input.readInt();
                if (IPCCommand.IPC_DISCONNECT.getValue() == receivedCommand) {
                    terminate();
                } else if (IPCCommand.IPC_WAKE_WORD_DETECTED.getValue() == receivedCommand) {
                    log.info("Received wake word detected");
                    wakeWordIPCSocket.processWakeWordDetected();
                }
            } catch (Exception e) {
                log.warn("Could not read/process the command received:", e);
                terminate();
            }
        }
    }

    void terminate() {
        log.info("Terminating/Disconneting a Wake Word Agent, Bye!");
        running = false;
        wakeWordIPCSocket.unregisterClient(this);
        try {
            if (input != null) {
                input.close();
                if (clientSocket != null)
                    clientSocket.close();
            }
        } catch (IOException e) {
            log.warn("Could not close streams and socket:", e);
        }
    }

    public void send(IPCCommand command) throws IOException {
        output.writeInt(command.getValue());
    }
}
