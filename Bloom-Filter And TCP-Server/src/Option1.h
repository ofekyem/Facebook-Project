#ifndef OPTION1_H
#define OPTION1_H
#include <iostream>
#include <string>
#include <vector>
#include "ICommand.h"
#include "Options.h"

using namespace std; 

class Option1 : public Options ,ICommand{
    private :
        int firstInputSize;
        int hashTimes;
        int hash1;
        int hash2; 
        int arraySize; 

    public : 
        Option1(int firstInputSize,int hashTimes,int hash1,int hash2,int arraySize);
        int execute(int* bloomFilter,string url,vector <string>& myVector);
        int pushToArray(int* bloomFilter,string url,vector <string>& myVector);

};
#endif // OPTION1_H