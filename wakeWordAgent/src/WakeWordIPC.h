#ifndef TVS_VS_WAKE_WORD_WAKEWORDIPC_H_
#define TVS_VS_WAKE_WORD_WAKEWORDIPC_H_

#include "IPCInterface.h"

namespace TVSWakeWord {

// Base class for handling IPC communication.  Handles the sending and
// receiving of commands.
class WakeWordIPC {
public:
  WakeWordIPC(IPCInterface* interface);
  virtual ~WakeWordIPC() = default;

  // Allows a command to be sent
  virtual void sendCommand(const IPCInterface::Command command) = 0;

protected:

  // Sub-classes should call this function when a command is received
  void ipcCommandReceived(const IPCInterface::Command command);

private:

  // The object which needs to know when a command is received
  IPCInterface* m_interface;
};

}/* namespace TVSWakeWord*/

#endif /* TVS_VS_WAKE_WORD_WAKEWORDIPC_H_ */
