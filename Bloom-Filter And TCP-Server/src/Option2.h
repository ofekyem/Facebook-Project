#ifndef OPTION2_H
#define OPTION2_H
#include <iostream>
#include <string>
#include <vector>
#include "ICommand.h"
#include "Options.h"

using namespace std; 

class Option2 : public ICommand, Options{
    private :
        int firstInputSize;
        int hashTimes;
        int hash1;
        int hash2; 
        int arraySize;
        bool checkIfUrlExist(vector<string> urls, string newUrl);
       
    public:
        // Constructor.
        Option2(int firstInputSize,int hashTimes,int hash1,int hash2,int arraySize);
        int getHash1();
        int getHash2();
        //return 0 in case of false, 2 true true, 3 true false, error 1
        int checking1(int* bloomFilter,string url,vector <string>& myVector);
        //return 0 in case of false, 2 true true, 3 true false, error 1
        int checking2(int* bloomFilter,string url,vector <string>& myVector);
        int execute(int* bloomFilter,string url,vector <string>& myVector);

};
#endif // OPTION2_H