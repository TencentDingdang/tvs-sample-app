#ifndef TVS_VS_WAKE_WORD_LOGGER_H_
#define TVS_VS_WAKE_WORD_LOGGER_H_

#include <string>

namespace TVSWakeWord {

// Our mini-logger code.  Currently this supports levels of logging,
// and prints log trace to std::out.  To minimize code intrusion, the exposed
// interface is a namespace with a simple log() function.
// The .cpp file contains a class with the implementation.
// Both the namespace functions, and underlying class, may be extended to
// allow more complex things.
namespace Logger {

  // Not an enum class, to allow trivial < operator
  enum Level {
    DEBUG = 1,
    INFO,
    WARNING,
    ERROR
  };

  // Log at the specific level.
  void log(const Level level, const std::string& msg);

  // Set the level.  Future log messages below that level will be suppressed.
  void setDefaultLogLevel(const Level level);

} // namespace logger

} // namespace TVSWakeWord

#endif // TVS_VS_WAKE_WORD_LOGGER_H_
