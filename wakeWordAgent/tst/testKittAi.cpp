#include "testKittAi.h"
#include "../src/KittAiSnowboyWakeWordEngine.h"
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

void createTempKittAi() {

  auto wwAgent = std::make_shared<DummyWakeWordAgent>();

  auto kitt = std::make_shared<KittAiSnowboyWakeWordEngine>(wwAgent.get());
}

bool testKittAi() {

  // this function will create a kitt.ai object, and wake-word agent
  // and call various member functions.  After the end of the function,
  // the objects will be destroyed.
  // this function call will verify that the object's threads are being
  // correctly ended, and that the destructors are running without issue.
  createTempKittAi();

  // if we got here, it went well.
  return true;
}

