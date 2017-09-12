#include "WakeWordEngine.h"
#include "Logger.h"

using namespace TVSWakeWord::Logger;

namespace TVSWakeWord {

WakeWordEngine::WakeWordEngine(WakeWordDetectedInterface* interface) :
        m_interface{interface} {
}

void WakeWordEngine::wakeWordDetected() {

  if(!m_interface) {
    log(Logger::WARNING, "Wakeword Interface has not been set");
    return;
  }

  m_interface->onWakeWordDetected();
}

} // namespace TVSWakeWord
