#include <iostream>

#include "testLogger.h"
#include "testKittAi.h"
#include "testSensory.h"
#include "testIPCFactory.h"
#include "testGPIO.h"
#include "unistd.h"

using namespace std;

// runs the test-suite
int main() {

  // logging
  if(!testLogger()) {
    std::cout << "ERROR testing log functionality" << std::endl;
    return 0;
  }

  std::cout << " **** Logging tests passed ok ****" << std::endl;

  // kitt-ai
  if(!testKittAi()) {
    std::cout << "ERROR testing kitt-ai functionality" << std::endl;
    return 0;
  }

  std::cout << " **** Kitt-ai tests passed ok ****" << std::endl;

  // sensory
  if(!testSensory()) {
    std::cout << "ERROR testing sensory functionality" << std::endl;
    return 0;
  }

  std::cout << " **** Sensory tests passed ok ****" << std::endl;

  // GPIO
  if(!testGPIO()) {
    std::cout << "ERROR testing GPIO functionality" << std::endl;
    return 0;
  }

  std::cout << " **** GPIO tests passed ok ****" << std::endl;

  // ipc factory
  if(!testIPCFactory()) {
    std::cout << "ERROR testing ipc factory functionality" << std::endl;
    return 0;
  }

  std::cout << " **** ipc factory tests passed ok ****" << std::endl;

  std::cout << " *****************************" << std::endl;
  std::cout << " **** ALL TESTS PASSED OK ****" << std::endl;
  std::cout << " *****************************" << std::endl;

  return 0;
}

