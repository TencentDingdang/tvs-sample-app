#ifndef TVS_VS_WAKE_WORD_WAKE_WORD_AGENT_H_
#define TVS_VS_WAKE_WORD_WAKE_WORD_AGENT_H_

#include "IPCInterface.h"
#include "WakeWordException.h"
#include "WakeWordEngineFactory.h"
#include "WakeWordIPCFactory.h"

#include <memory>
#include <thread>
#include <atomic>
#include <condition_variable>
#include <mutex>

#include <time.h>

namespace TVSWakeWord {

// The key class for this project.  Contains a wake-word detector, and
// an IPC interaction module.  This class implements callbacks that both
// modules will call at the appropriate times.
// Take note of the states this class supports, which are required to
// manage interaction with the Java Sample App.
// For our target of the Raspberry Pi, the microphone input cannot be owned
// by both this process and the Sample App simultaneously.  The IPC
// flow, combined with the states of this class, provide a working solution
// to this.
class WakeWordAgent: public WakeWordDetectedInterface, public IPCInterface {

public:
  WakeWordAgent(WakeWordEngineFactory::EngineType engineType,
          WakeWordIPCFactory::IPCType ipcType);
  ~WakeWordAgent();

  // An implementation for handling wake word detection
  void onWakeWordDetected();

  // An implementation for handling an IPC command
  void onIPCCommandReceived(IPCInterface::Command command);

private:
  enum class State {
    UNINITIALIZED = 1,            // agent is not initialized.
    IDLE,                         // initialized, but idle.
    WAKE_WORD_DETECTED,           // The agent has detected the wake word.
    SENT_WAKE_WORD_DETECTED,      // wake word has been detected, and an IPC
                                  // message has been sent to the TVS client.
    WAKE_WORD_PAUSE_REQUESTED,    // we have received a request to stop
                                  // and release the mic.
    WAKE_WORD_PAUSED,             // paused - the mic is released.
    WAKE_WORD_RESUME_REQUESTED    // we have received a request to resume.
  };

  // Utility function for setting state
  void setState(State state);

  // Utility function
  std::string stateToString(State state);

  // The code for our internal thread
  void mainLoop();

  // Our main thread and state variables
  std::unique_ptr<std::thread> m_thread;
  std::atomic<bool> m_isRunning;
  State m_currentState;

  // The objects that handle wakeword detection and IPC
  std::unique_ptr<WakeWordEngine> m_wakeWordEngine;
  std::unique_ptr<WakeWordIPC> m_IPCHandler;

  // Synchronization variables
  std::mutex m_mtx;
  std::condition_variable m_cvStateChange;
};

} // namespace TVSWakeWord

#endif // TVS_VS_WAKE_WORD_WAKE_WORD_AGENT_H_
