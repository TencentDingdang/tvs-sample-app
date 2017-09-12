#include "WakeWordIPC.h"
#include "Logger.h"

using namespace TVSWakeWord::Logger;

namespace TVSWakeWord {

WakeWordIPC::WakeWordIPC(IPCInterface* interface) :
        m_interface{interface} {
}

void WakeWordIPC::ipcCommandReceived(IPCInterface::Command command) {

  if(!m_interface) {
    return;
  }

  log(Logger::DEBUG, "WakeWordIPC: received command");
  m_interface->onIPCCommandReceived(command);
}

} // namespace TVSWakeWord
