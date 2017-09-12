#include "WakeWordEngineFactory.h"
#include "Logger.h"
#include "WakeWordUtils.h"
#include "KittAiSnowboyWakeWordEngine.h"
#include "SensoryWakeWordEngine.h"
#include "GPIOWakeWordEngine.h"

using namespace TVSWakeWord::Logger;

namespace TVSWakeWord {

namespace WakeWordEngineFactory {

std::unique_ptr<WakeWordEngine> createEngine(
        WakeWordDetectedInterface* interface,
        EngineType engineType) {

  switch(engineType) {
    case EngineType::KITT_AI_SNOWBOY_ENGINE:
      log(Logger::DEBUG, "WakeWordEngineFactory: creating Kitt-Ai Engine");
      return make_unique<KittAiSnowboyWakeWordEngine>(interface);
    case EngineType::SENSORY_ENGINE:
      log(Logger::DEBUG, "WakeWordEngineFactory: creating Sensory Engine");
      return make_unique<SensoryWakeWordEngine>(interface);
    case EngineType::GPIO_ENGINE:
      log(Logger::DEBUG, "WakeWordEngineFactory: creating GPIO Engine");
      return make_unique<GPIOWakeWordEngine>(interface);
    default:
      log(Logger::ERROR, "WakeWordEngineFactory: unhandled switch case");
      return nullptr;
  }
}

std::string engineTypeToString(const EngineType type) {

  switch (type) {
    case EngineType::KITT_AI_SNOWBOY_ENGINE:
      return "KITT_AI_SNOWBOY_ENGINE";
    case EngineType::SENSORY_ENGINE:
      return "SENSORY_ENGINE";
    case EngineType::GPIO_ENGINE:
      return "GPIO_ENGINE";
    default:
      return "UNKNOWN";
  }
}

} // namespace WakeWordEngineFactory

} // namespace TVSWakeWord
