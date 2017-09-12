#ifndef TVS_VS_WAKE_WORD_WAKE_WORD_ENGINE_FACTORY_H_
#define TVS_VS_WAKE_WORD_WAKE_WORD_ENGINE_FACTORY_H_

#include "WakeWordEngine.h"

#include <memory>

namespace TVSWakeWord {

// A namespace, not a class.  We don't need more at this time, and it's
// simpler to implement, use, and extend.
namespace WakeWordEngineFactory {

// Currently supported wake-word audio engines
enum class EngineType {
  KITT_AI_SNOWBOY_ENGINE = 1,
  SENSORY_ENGINE = 2,
  GPIO_ENGINE = 3
};

// Creation function
std::unique_ptr<WakeWordEngine> createEngine(
        WakeWordDetectedInterface* interface, EngineType engineType);

// Utility function
std::string engineTypeToString(const EngineType type);

} // namespace WakeWordEngineFactory

} // namespace TVSWakeWord

#endif // TVS_VS_WAKE_WORD_WAKE_WORD_ENGINE_FACTORY_H_
