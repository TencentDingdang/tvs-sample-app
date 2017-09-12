#ifndef TVS_VS_WAKE_WORD_IPC_INTERFACE_H_
#define TVS_VS_WAKE_WORD_IPC_INTERFACE_H_

#include <string>

namespace TVSWakeWord {

// This class provides the interface for interacting with the TVS Sample App
// over IPC.  In this project, this is required to communicate to the Sample App
// that the WakeWordAgent has detected the keyword ('TVS').  The Sample App
// responds in turn to the Agent when the user has completed their interaction
// with TVS.
class IPCInterface {

public:
  enum Command : uint32_t {
    DISCONNECT                = 1,  // OUTGOING : Ask the TVS client to disconnect from us
    WAKE_WORD_DETECTED        = 2,  // OUTGOING : sent to TVS client when a wake word is detected
    PAUSE_WAKE_WORD_ENGINE    = 3,  // INCOMING : request to pause the engine and yield the Mic
    RESUME_WAKE_WORD_ENGINE   = 4,  // INCOMING : request to resume the engine
    CONFIRM                   = 5,  // OUTGOING : sent to TVS client to confirm the engine has stopped
    UNKNOWN                   = 6   // n/a : for error & default cases
  };

  virtual ~IPCInterface() = default;

  // This will be called by the IPC handling code when an actual command
  // comes in over IPC from a wake-word agent - the implementation
  // of this function will handle it.
  virtual void onIPCCommandReceived(Command command) = 0;

  // Utility function
  static Command intToCommand(int intCommand);

private:

  // Utility function
  std::string commandToString(const Command command);
};

} // namespace TVSWakeWord

#endif // TVS_VS_WAKE_WORD_IPC_INTERFACE_H_
