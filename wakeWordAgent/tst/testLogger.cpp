#include "testLogger.h"
#include "../src/Logger.h"

#include <string>

using namespace TVSWakeWord;

using namespace Logger;

bool testLogger() {

  std::string testString = "test trace";

  // set to each possible value
  setDefaultLogLevel(Logger::DEBUG);
  setDefaultLogLevel(Logger::INFO);
  setDefaultLogLevel(Logger::WARNING);
  setDefaultLogLevel(Logger::ERROR);

  // ensure logging to all levels works
  log(Logger::DEBUG, testString);
  log(Logger::INFO, testString);
  log(Logger::WARNING, testString);
  log(Logger::ERROR, testString);

  return true;
}

