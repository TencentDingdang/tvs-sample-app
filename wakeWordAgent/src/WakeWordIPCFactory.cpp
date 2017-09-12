#include "WakeWordIPCFactory.h"
#include "Logger.h"
#include "WakeWordIPCSocket.h"
#include "WakeWordUtils.h"

using namespace TVSWakeWord::Logger;

namespace TVSWakeWord {

namespace WakeWordIPCFactory {

std::unique_ptr<WakeWordIPC> createIPCHandler(IPCInterface* interface,
                                              IPCType ipcType) {

  switch(ipcType) {
    case IPCType::TCP_PROTOCOL:
      log(Logger::DEBUG, "createIPCHandler: Creating TCP handler");
      return make_unique<WakeWordIPCSocket>(interface);
    default:
      log(Logger::ERROR, "createIPCHandler: Unhandled switch case");
      return nullptr;
  }
}

std::string IPCTypeToString(const IPCType type) {

  switch(type) {
    case IPCType::TCP_PROTOCOL:
      return "TCP_PROTOCOL";
    default:
      log(Logger::ERROR, "IPCTypeToString: Unhandled switch case");
      return "UNKNOWN";
  }
}

} // WakeWordIPCFactory

} // namespace TVSWakeWord
