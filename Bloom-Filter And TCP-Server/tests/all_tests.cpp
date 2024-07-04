#include <gtest/gtest.h>
#include "../src/ConsoleMenu.cpp"
#include "../src/Option1.cpp"
#include "../src/Option2.cpp"
#include "../src/Options.cpp"
#include "../src/OptionMenu.cpp"
#include "../src/Options.h"
#include <string>
#include <vector>

/*
this file include all the tests that we used while we worked on our project.
*/
using namespace std; 

TEST(ChechFirstInput1,pass){
    ConsoleMenu a;
    EXPECT_EQ(a.firstInputCheck("8 1"),1);
}
TEST(ChechFirstInput2,pass){
    ConsoleMenu a;
    EXPECT_EQ(a.firstInputCheck("8 1 1 1 1 1 1"),1);
}
TEST(ChechFirstInput3,pass){
    ConsoleMenu a;
    EXPECT_EQ(a.firstInputCheck("8 1 2"),3);
}
TEST(ChechFirstInput4,pass){
    ConsoleMenu a;
    EXPECT_EQ(a.firstInputCheck("8 2"),2);
}
TEST(ChechFirstInput5,pass){
    ConsoleMenu a;
    EXPECT_EQ(a.firstInputCheck("8 2 2 2 2 2 2"),2);
}
TEST(ChechFirstInput6,pass){
    ConsoleMenu a;
    EXPECT_EQ(a.firstInputCheck("8 2 1 2 1 2 2"),3);
}
TEST(ChechFirstInput7,failed){
    ConsoleMenu a;
    EXPECT_EQ(a.firstInputCheck("8 2 1 2 100 2 2"),0);
}
TEST(ChechFirstInput8,failed){
    ConsoleMenu a;
    EXPECT_EQ(a.firstInputCheck("      8 1 2"),0);
}
TEST(ChechFirstInput9,failed){
    ConsoleMenu a;
    EXPECT_EQ(a.firstInputCheck("8      1 2"),0);
}
TEST(CheckString1,pass){
    OptionMenu a;
    EXPECT_EQ(a.checkStringFun("1 www"),1);
} 
TEST(CheckString2,failed){
    OptionMenu a;
    EXPECT_EQ(a.checkStringFun("3 www"),0);
}
TEST(CheckString3,failed){
    OptionMenu a;
    EXPECT_EQ(a.checkStringFun("2   www"),0);
}
TEST(kelet,pass){
    ConsoleMenu a;
    EXPECT_EQ(a.getArraySize(),0);
}
TEST(kelet2,pass){
    ConsoleMenu a;
    EXPECT_EQ(a.getFirstInputSize(),0);
}

//false in input size of 2
TEST(outPutFor2a, pass){
    int bloomFilter[8];
    for(int i=0;i<8;i++){
        bloomFilter[i]=0;
    }
    string url = "www.example.com0";
    vector <string> myVector;
    int firstInputSize = 2;
    int hashTimes = 2;
    int hash1 = 0;
    int hash2 = 0;
    int arraySize = 8;
    Option2 p2(firstInputSize, hashTimes, hash1, hash2, arraySize);
    EXPECT_EQ(p2.checking1(bloomFilter, url, myVector),0);
}

//true false in case of 2 inputs
TEST(outPutFor2b, pass){
    int bloomFilter[8];
    for(int i=0;i<8;i++){
        bloomFilter[i]=0;
    }
    string url = "www.example.com";
    vector <string> myVector;
    int firstInputSize = 2;
    int hashTimes = 2;
    bloomFilter[1]=1;
    int hash1 = 0;
    int hash2 = 0;
    int arraySize = 8;
    Option2 p2(firstInputSize, hashTimes, hash1, hash2, arraySize);
    EXPECT_EQ(p2.checking1(bloomFilter, url, myVector),3);
}

//true true in case of 2 inputs
TEST(outPutFor2c, pass){
    int bloomFilter[8];
    for(int i=0;i<8;i++){
        bloomFilter[i]=0;
    }
    string url = "www.example.com";
    vector <string> myVector;
    myVector.push_back(url);
    int firstInputSize = 2;
    int hashTimes = 2;
    bloomFilter[1]=1;
    int hash1 = 0;
    int hash2 = 0;
    int arraySize = 8;
    Option2 p2(firstInputSize, hashTimes, hash1, hash2, arraySize);
    EXPECT_EQ(p2.checking1(bloomFilter, url, myVector),2);
}

//false in case of 3 inputs
TEST(outPutFor3a, pass){
    int bloomFilter[8];
    for(int i=0;i<8;i++){
        bloomFilter[i]=0;
    }
    string url = "www.example.com0";
    vector <string> myVector;
    int firstInputSize = 3;
    int hashTimes = 0;
    int hash1 = 1;
    int hash2 = 2;
    int arraySize = 8;
    Option2 p2(firstInputSize, hashTimes, hash1, hash2, arraySize);
    EXPECT_EQ(p2.checking2(bloomFilter, url, myVector),0);
}

//true false in case of 3 inputs
TEST(outPutFor3b, pass){
    int bloomFilter[8];
    for(int i=0;i<8;i++){
        bloomFilter[i]=0;
    }
    string url = "www.example.com";
    vector <string> myVector;
    int firstInputSize = 3;
    int hashTimes = 0;
    bloomFilter[0]=1;
    bloomFilter[1]=1;
    int hash1 = 1;
    int hash2 = 2;
    int arraySize = 8;
    Option2 p2(firstInputSize, hashTimes, hash1, hash2, arraySize);
    EXPECT_EQ(p2.checking2(bloomFilter, url, myVector),3);
}

//true true in case of 3 inputs
TEST(outPutFor3c, pass){
    int bloomFilter[8];
    for(int i=0;i<8;i++){
        bloomFilter[i]=0;
    }
    string url = "www.example.com";
    vector <string> myVector;
    int firstInputSize = 3;
    int hashTimes = 0;
    int hash1 = 1;
    int hash2 = 2;
    bloomFilter[0]=1;
    bloomFilter[1]=1;
    myVector.push_back(url);
    int arraySize = 8;
    Option2 p2(firstInputSize, hashTimes, hash1, hash2, arraySize);
    EXPECT_EQ(p2.checking2(bloomFilter, url, myVector),2);
}

//doHash tests.
TEST(doHash1Test1, pass){
    Options options;
    EXPECT_EQ(options.doHash(1,"www.example.com0"),8231510004620773819);
}
TEST(doHash2Test1, pass){
    Options options;
    EXPECT_EQ(options.doHash(2,"www.example.com0"),6657855685155196946);
}
TEST(doHash1Test2, pass){
    Options options;
    EXPECT_EQ(options.doHash(1,"www.example.com11"),8170639658965728443);
}
TEST(doHash2Test2, pass){
    Options options;
    EXPECT_EQ(options.doHash(2,"www.example.com11"),8961118054305463587);
}
TEST(doHash1Test3, pass){
    Options options;
    EXPECT_EQ(options.doHash(1,"www.example.com4"),2204681767864682784);
}
TEST(doHash2Test3, pass){
    Options options;
    EXPECT_EQ(options.doHash(2,"www.example.com4"),1700393512410395922);
}

//option1 tests.
TEST(option1Test1, pass){
    vector <string> myVector;
    int bloomFilter[8];
    for(int i=0;i<8;i++){
        bloomFilter[i]=0;
    }
    string url = "www.example.com0";
    int firstInputSize = 3;
    int hashTimes = 0;
    int hash1 = 1;
    int hash2 = 2;
    int arraySize = 8;
    Option1 option1(firstInputSize, hashTimes, hash1, hash2, arraySize);
    EXPECT_EQ(option1.pushToArray(bloomFilter,url,myVector),1);
}
//option1 tests.
TEST(option1Test2, pass){
    vector <string> myVector;
    int bloomFilter[8];
    for(int i=0;i<8;i++){
        bloomFilter[i]=0;
    }
    string url = "www.example.com11";
    int firstInputSize = 3;
    int hashTimes = 0;
    int hash1 = 1;
    int hash2 = 2;
    int arraySize = 8;
    Option1 option1(firstInputSize, hashTimes, hash1, hash2, arraySize);
    EXPECT_EQ(option1.pushToArray(bloomFilter,url,myVector),1);
}
//option1 tests.
TEST(option1Test3, pass){
    vector <string> myVector;
    int bloomFilter[8];
    for(int i=0;i<8;i++){
        bloomFilter[i]=0;
    }
    string url = "www.example.com4";
    int firstInputSize = 2;
    int hashTimes = 2;
    int hash1 = 0;
    int hash2 = 0;
    int arraySize = 8;
    Option1 option1(firstInputSize, hashTimes, hash1, hash2, arraySize);
    EXPECT_EQ(option1.pushToArray(bloomFilter,url,myVector),1);
}

//these are old tests that we used before refactoring to classes.
/*
// here is the code that we used to check our code by TDD.





// here we check the tests.

#include "../src/functions.cpp" 
TEST(doHashTest, pass) {
EXPECT_EQ(doHash("www.example.com"), 1);
}
TEST(doDoubleHashTest2, pass) {
EXPECT_EQ(doHash("www.example.com"), 2);
}
TEST(keletTest, failedTest) {
EXPECT_EQ(firstInputCheck("234  876a"), 0);
}
TEST(keletTest1, failedTest1) {
EXPECT_EQ(firstInputCheck("234  564"), 0);
}
TEST(keletTest2, passedTest) {
EXPECT_EQ(firstInputCheck("234  2"), 2);
}
TEST(keletTest3, passedTest) {
EXPECT_EQ(firstInputCheck("234  2 1"), 3);
}

TEST(KeletTest4, passedTest1) {
EXPECT_EQ(checkStringFun("ofek"), 0);
}
TEST(KeletTest5, falseTest2) {
EXPECT_EQ(checkStringFun("2 www.example.com1"), 1);
} 

TEST(mainTest, passedTest) {
EXPECT_EQ(checkStringFun("2 www.example.com1"), 1);
}
*/
