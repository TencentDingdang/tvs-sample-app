package com.tencent.dingdang.tvs.wakeword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class WakeWordIPCSocket extends WakeWordIPC implements Runnable {

    private ServerSocket serverSocket = null;
    private Thread ipcThread = null;
    private final Set<WakeWordIPCConnectedClient> connectedClients = new HashSet<>();
    private static final Logger log = LoggerFactory.getLogger(WakeWordIPCSocket.class);

    public WakeWordIPCSocket(WakeWordDetectedHandler handler, int portNumber) throws IOException {
        super(handler);
        serverSocket = new ServerSocket(portNumber, 0, InetAddress.getByName(null));
    }

    public void init() {
        if (ipcThread == null) {
            ipcThread = new Thread(this);
            ipcThread.start();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                WakeWordIPCConnectedClient newConnectedClient =
                        new WakeWordIPCConnectedClient(clientSocket, this);
                newConnectedClient.init();
                registerClient(newConnectedClient);
            } catch (Exception e) {
                log.warn("Could not accept/connect IPC client", e);
            }
        }
    }

    public synchronized void registerClient(WakeWordIPCConnectedClient newClient) {
        connectedClients.add(newClient);
        log.info("New IPC client was accepted, current of current clients is "
                + connectedClients.size());
    }

    public synchronized void unregisterClient(WakeWordIPCConnectedClient oldClient) {
        connectedClients.remove(oldClient);
        log.info(
                "IPC client was removed, current of current clients is " + connectedClients.size());
    }

    @Override
    public synchronized void sendCommand(IPCCommand command) throws IOException {
        log.debug("Sending command " + command + " to all connected clients");
        for (WakeWordIPCConnectedClient client : connectedClients) {
            client.send(command);
        }
    }

    public void processWakeWordDetected() {
        log.info("Wake Word Detected ......");
        (new Thread() {
            @Override
            public void run() {
                wakeWordDetected();
            }
        }).start();
    }
}
