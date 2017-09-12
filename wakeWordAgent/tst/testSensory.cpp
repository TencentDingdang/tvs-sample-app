#include "testSensory.h"
#include "../src/SensoryWakeWordEngine.h"
#include "../src/WakeWordAgent.h"
#include "../src/Logger.h"

#include <unistd.h>
#include <string>

using namespace TVSWakeWord;

using namespace Logger;

// dummy class for passing in for kittAi creation
class DummyWakeWordAgent: public WakeWordDetectedInterface, public IPCInterface {

  void onWakeWordDetected() { }

  void onIPCCommandReceived(Command command) { }
};

void createTempSensory() {

  auto wwAgent = std::make_shared<DummyWakeWordAgent>();

  auto sensory = std::make_shared<SensoryWakeWordEngine>(wwAgent.get());
}

bool testSensory() {

  // This function will create a sensory object, wake-word agent,
  // and call various member functions.  After the end of the function,
  // the objects will be destroyed.
  // This function call will verify that the object's threads are being
  // correctly ended, and that the destructors are running without issue.
  createTempSensory();

  // if we got here, it went well.
  return true;
}

