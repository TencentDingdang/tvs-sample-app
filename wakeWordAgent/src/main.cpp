#include <iostream>
#include <signal.h>
#include <string>
#include <unistd.h>
#include <stdlib.h>
#include <memory>
#include <string.h>

#include "Logger.h"
#include "WakeWordAgent.h"
#include "WakeWordUtils.h"

using namespace TVSWakeWord;
using namespace TVSWakeWord::Logger;

// Change this if you want more trace.
Logger::Level LOG_DEFAULT_LEVEL = Logger::INFO;

// Forward declarations
void shutdown();
void processKill(int signal);

void usage() {

  std::string msg = std::string(
          "Usage: ") +
          "WakeWordAgent -e <engine_type>\n" +
          " engine_type options:\n" +
          " 'kitt_ai'\n" +
          " 'sensory'\n" +
          " 'gpio'\n" +
          "WakeWordAgent [-h] [--help]\n" +
          " prints help (this message)\n";

  std::cout << msg << std::endl;
}

bool parseArgs(int argc, char* argv[],
               WakeWordEngineFactory::EngineType* engineType) {

  bool printHelp = (2 == argc) &&
          (std::string("-h") == argv[1] || std::string("--help") == argv[1]);

  bool validArgCount = printHelp || (3 == argc);

  if(!validArgCount) {
    usage();
    return false;
  }

  if(std::string("-e") != argv[1]) {
    usage();
    return false;
  }

  std::string engineParam = argv[2];

  if("kitt_ai" == engineParam) {
    *engineType = WakeWordEngineFactory::EngineType::KITT_AI_SNOWBOY_ENGINE;
    return true;
  } else if("sensory" == engineParam) {
    *engineType = WakeWordEngineFactory::EngineType::SENSORY_ENGINE;
    return true;
  } else if("gpio" == engineParam) {
    *engineType = WakeWordEngineFactory::EngineType::GPIO_ENGINE;
    return true;
  }

  usage();
  return false;
}

int main(int argc, char* argv[]) {

  setDefaultLogLevel(LOG_DEFAULT_LEVEL);

  WakeWordEngineFactory::EngineType selectedEngine =
          WakeWordEngineFactory::EngineType::KITT_AI_SNOWBOY_ENGINE;
  if(!parseArgs(argc, argv, &selectedEngine)) {
    return 0;
  }

  // Setup our signal handling code
  struct sigaction sigActionHandler;
  memset(&sigActionHandler, 0, sizeof(struct sigaction));
  sigActionHandler.sa_handler = processKill;
  sigemptyset(&sigActionHandler.sa_mask);
  sigActionHandler.sa_flags = 0;

  sigaction(SIGINT, &sigActionHandler, nullptr);

  log(Logger::INFO, "main: Starting Wake Word Agent");

  std::unique_ptr<WakeWordAgent> wakeWordAgent;

  try {
    log(Logger::DEBUG, "main: Creating Wake Word Agent instance");
    wakeWordAgent = make_unique<WakeWordAgent>(
                            selectedEngine,
                            WakeWordIPCFactory::IPCType::TCP_PROTOCOL);
  } catch (std::bad_alloc& e) {
    log(Logger::ERROR, "main: unable to allocate memory");
    shutdown();
  } catch (WakeWordException& e) {
    log(Logger::ERROR, std::string("main: Exception happened: ") + e.what());
    shutdown();
  }

  while (true) {

    // Ok - let agent threads run until we're terminated
    sleep(1);

  }
}

void shutdown() {
  log(Logger::INFO, "Shutting down Wake Word Agent");
  exit(0);
}

void processKill(int signal) {
  log(Logger::INFO, "Kill signal received");
  shutdown();
}
