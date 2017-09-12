#ifndef TVS_VS_WAKE_WORD_UTILS_H_
#define TVS_VS_WAKE_WORD_UTILS_H_

#include <memory>

// required for pre-c++14 compilers
template<typename T, typename ...Args>
std::unique_ptr<T> make_unique(Args &&...args) {
  return std::unique_ptr<T>(new T(std::forward<Args>(args)...));
}

#endif
