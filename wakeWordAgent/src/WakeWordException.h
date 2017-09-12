#ifndef TVS_VS_WAKE_WORD_WAKEWORD_EXCEPTION_H_
#define TVS_VS_WAKE_WORD_WAKEWORD_EXCEPTION_H_

#include <string>

namespace TVSWakeWord {

// Our project-specific exception class.
class WakeWordException: public std::exception {

public:

  explicit WakeWordException(const std::string& msg);
  virtual const char* what() const throw ();

private:
  std::string m_msg;
};

} /* namespace TVSWakeWord */

#endif /* TVS_VS_WAKE_WORD_WAKEWORD_EXCEPTION_H_ */
