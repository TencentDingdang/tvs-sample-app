#ifndef TVS_VS_WAKE_WORD_WAKE_WORD_ENGINE_H_
#define TVS_VS_WAKE_WORD_WAKE_WORD_ENGINE_H_

#include "WakeWordDetectedInterface.h"

namespace TVSWakeWord {

// Interface for a WakeWordEngine.  This encapsulates the logic needed for
// detecting a wake-word (ie. 'TVS') from audio input.
class WakeWordEngine {

public:

  WakeWordEngine(WakeWordDetectedInterface* passedInterface);
  virtual ~WakeWordEngine() = default;

  // When paused, the microphone is released.
  virtual void pause() = 0;
  virtual void resume() = 0;

protected:

  // This will be called by subclasses of WakeWordEngine to notify that
  // a wake-word was detected.
  void wakeWordDetected();

private:

  // The object that needs to know when the wakeword is detected.
  WakeWordDetectedInterface* m_interface;
};

} /* namespace TVSWakeWord*/

#endif /* TVS_VS_WAKE_WORD_WAKE_WORD_ENGINE_H_ */
