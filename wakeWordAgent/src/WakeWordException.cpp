#include "WakeWordException.h"

namespace TVSWakeWord {

WakeWordException::WakeWordException(const std::string& msg)
        : m_msg{msg} {

}

const char* WakeWordException::what() const throw() {
  return m_msg.c_str();
}

} // namespace TVSWakeWord
