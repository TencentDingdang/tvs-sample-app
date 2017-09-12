#ifndef TVS_VS_WAKE_WORD_WAKE_WORD_IPC_SOCKET_H_
#define TVS_VS_WAKE_WORD_WAKE_WORD_IPC_SOCKET_H_

#include "WakeWordIPC.h"

#include <stdint.h>
#include <thread>
#include <atomic>
#include <arpa/inet.h>

namespace TVSWakeWord {

// an implementation of the IPC class, which uses sockets.
class WakeWordIPCSocket: public WakeWordIPC {

public:

  WakeWordIPCSocket(IPCInterface* interface);
  virtual ~WakeWordIPCSocket();
  void sendCommand(const IPCInterface::Command command);

private:

  // Utility functions
  void clearSocketMembers();
  void init();
  bool initializeSocket();
  bool makeConnection();
  bool receiveCommand();

  // The main thread loop
  void mainLoop();

  // Thread management variables
  std::atomic<bool> m_isRunning;
  std::unique_ptr<std::thread> m_thread;

  // Socket variables
  int m_socketHandle;
  bool m_socketConnected;
  struct sockaddr_in m_socketAddr;
};

} // namespace TVSWakeWord

#endif // TVS_VS_WAKE_WORD_WAKE_WORD_IPC_SOCKET_H_
