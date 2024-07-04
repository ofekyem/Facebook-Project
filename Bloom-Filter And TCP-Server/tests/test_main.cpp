#include <gtest/gtest.h>
/*
here we run all tests.
*/
int main(int argc, char** argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}