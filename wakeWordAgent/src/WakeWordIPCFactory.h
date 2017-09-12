#ifndef TVS_VS_WAKE_WORD_WAKE_WORD_IPC_FACTORY_H_
#define TVS_VS_WAKE_WORD_WAKE_WORD_IPC_FACTORY_H_

#include "WakeWordIPC.h"

#include <memory>
#include <string>
#include <stdint.h>

namespace TVSWakeWord {

// A namespace, not a class.  We don't need more at this time, and it's
// simpler to implement, use, and extend.
namespace WakeWordIPCFactory {

// Currently supported IPC types
enum class IPCType {
  TCP_PROTOCOL = 1
};

// Creation
std::unique_ptr<WakeWordIPC> createIPCHandler(IPCInterface* interface,
                                              IPCType engineType);

// Utility function
std::string IPCTypeToString(const IPCType type);

} // namespace WakeWordIPCFactory

} // namespace TVSWakeWord

#endif // TVS_VS_WAKE_WORD_WAKE_WORD_IPC_FACTORY_H_
