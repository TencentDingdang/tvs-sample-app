#include "testIPCFactory.h"
#include "../src/WakeWordIPCFactory.h"
#include "../src/WakeWordAgent.h"
#include "../src/Logger.h"

#include <unistd.h>
#include <string>

using namespace TVSWakeWord;

using namespace Logger;

// dummy class for passing in for ipc object creation
class DummyWakeWordAgent: public WakeWordDetectedInterface, public IPCInterface {

  void onWakeWordDetected() { }

  void onIPCCommandReceived(Command command) { }
};

void createTempIPCFactory() {

  auto dummyAgent = std::make_shared<DummyWakeWordAgent>();

  auto ipc = WakeWordIPCFactory::createIPCHandler(dummyAgent.get(),
                                                  WakeWordIPCFactory::IPCType::TCP_PROTOCOL);
}

bool testIPCFactory() {

  createTempIPCFactory();

  // if we got here, no issues creating & destroying the IPC object
  return true;
}

