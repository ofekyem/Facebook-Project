cmake_minimum_required(VERSION 3.14)
project("ex1") 

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

include(FetchContent)
FetchContent_Declare(
googletest
URL
https://github.com/google/googletest/archive/03597a01ee50ed
33e9dfd640b249b4be3799d395.zip
)

enable_testing()
set(SOURCE_FILES
src/Main.cpp # this is where the code we want to test
)

set(TEST_FILES
tests/test_main.cpp # this is the main for tests
tests/all_tests.cpp # this file contain the tests
)

add_executable(CalcTests ${TEST_FILES})

# Link Google Test to the test executable
target_link_libraries(CalcTests PRIVATE gtest gtest_main)

# Include the source directory for tests
target_include_directories(CalcTests PRIVATE src)

include(GoogleTest)

add_test(NAME CalcTests COMMAND CalcTests)