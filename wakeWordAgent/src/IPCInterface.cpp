#include "IPCInterface.h"

namespace TVSWakeWord {

std::string IPCInterface::commandToString(
        const Command command) {

  switch(command) {
    case DISCONNECT:
      return "DISCONNECT";
    case WAKE_WORD_DETECTED:
      return "WAKE_WORD_DETECTED";
    case PAUSE_WAKE_WORD_ENGINE:
      return "PAUSE_WAKE_WORD_ENGINE";
    case RESUME_WAKE_WORD_ENGINE:
      return "RESUME_WAKE_WORD_ENGINE";
    case CONFIRM:
      return "CONFIRM";
    default:
      return "UNKNOWN";
  }
}

IPCInterface::Command IPCInterface::intToCommand(int intCommand) {
  switch(intCommand) {
    case DISCONNECT:
      return DISCONNECT;
    case WAKE_WORD_DETECTED:
      return WAKE_WORD_DETECTED;
    case PAUSE_WAKE_WORD_ENGINE:
      return PAUSE_WAKE_WORD_ENGINE;
    case RESUME_WAKE_WORD_ENGINE:
      return RESUME_WAKE_WORD_ENGINE;
    case CONFIRM:
      return CONFIRM;
    default:
      return UNKNOWN;
  }
}

} // namespace TVSWakeWord
