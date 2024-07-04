#ifndef BLOOMFILTER_H
#define BLOOMFILTER_H

#include <string>
#include <vector>
#include "Option1.h"
#include "Option2.h"


class BloomFilter {
    std::vector<std::string> stringVector;
    int arraySize;
    int hash1;
    int hash2;
    int result;
    int* bloomFilter;
    int hashTimes;

public:
    BloomFilter(int size, int hash1, int hash2, int hashTimes);
    ~BloomFilter();

// class BloomFilter {
//     int bloomFilter[128]; 
//     std::vector<std::string> stringVector;
//     int arraySize;
//     int result;
//     int hash1, hash2;


// public:
//     BloomFilter();

    int execute(int choice, const std::string url,int fis);
    // bool checkIfUrlExist(std::string newUrl);
    // long int doHash(int digit, std::string s);
    // bool pushToArray(std::string url);
    // int checking2(std::string url);

    std::vector<std::string> getStringVector();
    int getArraySize();
    int getResult();

    // Getter methods
    int getHash1();
    int getHash2();

    // Setter methods
    void setHash1(int newHash1);
    void setHash2(int newHash2);
    void setArraySize(int newArraySize);
};

#endif // BLOOMFILTER_H