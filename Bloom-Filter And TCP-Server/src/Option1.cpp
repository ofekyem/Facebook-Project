#include <iostream>
#include <string>
#include <vector>
#include "ICommand.h"
#include "Options.h"
#include "Option1.h"
using namespace std;
/*
this class represents the option 1, of insterting a url to the bloom filter.
*/

        //constructor
        Option1::Option1(int firstInputSize,int hashTimes,int hash1,int hash2,int arraySize): firstInputSize(firstInputSize),hashTimes(hashTimes),hash1(hash1),hash2(hash2),arraySize(arraySize){} 

        /*
        this method gets the url and push it into the vector, and put it in the bloom filter.
        return 1 in case the enter is completed else 0.
        */
        int Option1:: pushToArray(int* bloomFilter,string url,vector <string>& myVector){
            if (firstInputSize == 2) {

                //in case only one bit in the bloom filter need to be changed.
                int place1 = abs((doHash(hashTimes,url))%arraySize);
                bloomFilter[place1]=1; 
                // adding url to the vector.
                myVector.push_back(url);
                cout << url << endl;
                return 1;
            } 
            else {
                //in case two bits in the bloom filter need to be changed.
                //the first place.
                int place1 = abs((doHash(hash1,url))%arraySize);
                bloomFilter[place1]=1;

                //the second place.
                int place2 = abs((doHash(hash2,url))%arraySize);
                bloomFilter[place2]=1;
                // adding url to the vector.
                myVector.push_back(url);
                cout << url << endl;
                return 1;
            }

            return 0;
        }
        /*
        this method runs the first option.
        */
        int Option1::execute(int* bloomFilter,string url,vector <string>& myVector){
            // we wanted to TDD the function, so we sent it to pushToArray which return a value
            int result  = pushToArray(bloomFilter,url,myVector);
            return result;
        }   
