#ifndef SENSORY_WAKE_WORD_ENGINE_H
#define SENSORY_WAKE_WORD_ENGINE_H

#include "WakeWordEngine.h"
#include "snsr.h"

#include <memory>
#include <string>
#include <thread>
#include <atomic>

namespace TVSWakeWord {

class SensoryWakeWordEngine: public WakeWordEngine {
public:
  SensoryWakeWordEngine(WakeWordDetectedInterface* interface);
  ~SensoryWakeWordEngine();
  void pause();
  void resume();
  bool isRunning();
  void callWakeWordDetected();

private:

  // Utility functions controlling the sensory library
  void init();
  void start();
  void stop();
  void mainLoop();

  // audio is acquired and processed in this thread
  std::unique_ptr<std::thread> m_thread;
  std::atomic<bool> m_isRunning;

  // A handle on the sensory engine session.
  // This is typedef'd as a pointer to a struct within snsr.h.
  SnsrSession m_session;
};

}
#endif // SENSORY_WAKE_WORD_ENGINE_H
