#include "testGPIO.h"
#include "../src/GPIOWakeWordEngine.h"
#include "../src/WakeWordAgent.h"
#include "../src/Logger.h"

#include <unistd.h>
#include <string>

using namespace TVSWakeWord;

using namespace Logger;

// dummy class for passing in for GPIO creation
class DummyWakeWordAgent: public WakeWordDetectedInterface, public IPCInterface {

  void onWakeWordDetected() { }

  void onIPCCommandReceived(Command command) { }
};

void createTempGPIO() {

  auto wwAgent = std::make_shared<DummyWakeWordAgent>();

  auto GPIO = std::make_shared<GPIOWakeWordEngine>(wwAgent.get());
}

bool testGPIO() {

  // this function will create a GPIO object, and wake-word agent
  // and call various member functions.  After the end of the function,
  // the objects will be destroyed.
  // this function call will verify that the object's threads are being
  // correctly ended, and that the destructors are running without issue.
  createTempGPIO();

  // if we got here, it went well.
  return true;
}

