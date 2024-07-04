#include <iostream> 
#include <string>
#include <vector>
#include "BloomFilter.h"
#include "Option1.h"
#include "Option2.h"

using namespace std;

BloomFilter::BloomFilter(int size, int hash1 , int hash2 , int hashTimes) : result(0) {
    bloomFilter = new int[size]; // Allocate memory for bloomFilter
    for(int i = 0; i < size; i++) {
        bloomFilter[i] = 0;
    }
    arraySize = size;
    this->hash1 = hash1;
    this->hash2 = hash2;
    this->hashTimes = hashTimes;
}

BloomFilter::~BloomFilter() {
    delete[] bloomFilter; // Deallocate memory when the object is destroyed
}

int BloomFilter::execute(int choice, const std::string url,int fis) {
    Option1 op1 = Option1(fis,hashTimes,hash1,hash2,arraySize); 
    Option2 op2 = Option2(fis,hashTimes,hash1,hash2,arraySize); 

    if(choice==1){
        this->result = op1.execute(this->bloomFilter,url,this->stringVector);
    }
    else{
        this->result = op2.execute(this->bloomFilter,url,this->stringVector);
    }
    cout<<"this is the result: "<< this->result<<endl; 
    return this->result;
}


vector<string> BloomFilter::getStringVector() {
    return stringVector;
}

int BloomFilter::getArraySize() {
    return arraySize;
}

int BloomFilter::getResult() {
    return result;
}

// Getter methods
int BloomFilter::getHash1() {
    return hash1;
}

int BloomFilter::getHash2() {
    return hash2;
}

// Setter methods
void BloomFilter::setHash1(int newHash1) {
    hash1 = newHash1;
}

void BloomFilter::setHash2(int newHash2) {
    hash2 = newHash2;
}

void BloomFilter::setArraySize(int newArraySize) {
    arraySize = newArraySize;
}
