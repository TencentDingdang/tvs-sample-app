#ifndef TVS_VS_WAKE_WORD_WAKEWORDDETECTEDINTERFACE_H_
#define TVS_VS_WAKE_WORD_WAKEWORDDETECTEDINTERFACE_H_

namespace TVSWakeWord {

class WakeWordDetectedInterface {

public:
  virtual ~WakeWordDetectedInterface() = default;
  virtual void onWakeWordDetected() = 0;
};

} /* namespace TVSWakeWord*/

#endif /* TVS_VS_WAKE_WORD_WAKEWORDDETECTEDINTERFACE_H_ */
