#include "Logger.h"

#include <iostream>

namespace TVSWakeWord {

namespace Logger {

// File-local utility function
std::string logLevelToString(Logger::Level level) {

  switch (level) {
    case Logger::DEBUG:
      return "DEBUG";
    case Logger::INFO:
      return "INFO";
    case Logger::WARNING:
      return "WARNING";
    case Logger::ERROR:
      return "ERROR";
    default:
      return "UNKNOWN";
  }
}

/////////////////////////////////////////////////////////
// Implementation class - only visible in this file
class LoggerImpl {

public:

  static void log(const Level level, const std::string& msg) {

    if(level < m_defaultLevel) {
      return;
    }

    std::cout << logLevelToString(level) << ":" << msg << std::endl;
  }

  void setDefaultLogLevel(const Level level) {
    m_defaultLevel = level;
  }

private:

  static Level m_defaultLevel;
};

Level LoggerImpl::m_defaultLevel = Level::WARNING;

/////////////////////////////////////////////////////////
// File-local utility function
LoggerImpl getLogger() {
  static LoggerImpl logger;
  return logger;
}

/////////////////////////////////////////////////////////
// Implementation of namespace-visible functions to outside callers
//
void log(const Level level, const std::string& msg) {
  getLogger().log(level, msg);
}

void setDefaultLogLevel(const Level level) {
  getLogger().setDefaultLogLevel(level);
}

} // namespace Logger

} // namespace TVSWakeWord
