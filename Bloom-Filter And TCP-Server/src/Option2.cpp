#include <iostream>
#include <string>
#include <vector>
#include "ICommand.h"
#include "Options.h"
#include "Option2.h"
/*
this class represents the option 2, when the user checks if the url is already in the bloom filter.
*/
using namespace std; 
        
        bool Option2::checkIfUrlExist(vector<string> urls, string newUrl){
            for (string i : urls) {
                if(i.compare(newUrl)==0){
                    return true;
                }
            }
            return false;
        }
    
    // Constructor.
    Option2::Option2(int firstInputSize,int hashTimes,int hash1,int hash2,int arraySize): firstInputSize(firstInputSize),hashTimes(hashTimes),hash1(hash1),hash2(hash2),arraySize(arraySize){} 


    int Option2::getHash1(){
        return hash1;
    }
    int Option2::getHash2(){
        return hash2;
    }

    /*
    This method check in one place in the bloom filter.
    */
    int Option2::checking1(int* bloomFilter,string url,vector <string>& myVector) {
    int place;
    //need to check the url exists in one place.
        place = abs((doHash(hashTimes,url))%arraySize);
        if (bloomFilter[place] != 1) {
            cout << "false" << endl;
            return 0;
        } 
            else {
            // bloom filter was true;
                cout << "true" << " ";
            //checking false positive situation.
                if (checkIfUrlExist(myVector, url)) {
                    cout << "true" << endl;
                    return 2;
                    } 
                    else {
                        cout << "false" << endl;
                        return 3;
                    }
            }
        return 1;
    }

    /*
    This method check in two places in the bloom filter.
    */
    int Option2::checking2(int* bloomFilter,string url,vector <string>& myVector) {
    //need to check the url exists in two places.
    int place1;
    int place2;
    //the first place.
    place1 = abs((doHash(hash1,url))%arraySize);
    //the second place.
    place2 = abs((doHash(hash2,url))%arraySize);
    if (bloomFilter[place1] != 1 || bloomFilter[place2] != 1) {
        cout << "false" << endl;
        return 0;
        } else {
            // bloom filter was true;
            cout << "true" << " ";

            //checking false positive situation.
            if (checkIfUrlExist(myVector, url)) {
                cout << "true" << endl;
                return 2;
                } else {
                    cout << "false" << endl;
                    return 3;
                    }
                }
    return 1;
} 
/*
this method runs the second option.
*/
int Option2::execute(int* bloomFilter,string url,vector <string>& myVector) {
        //checking in the bloom filter one or two times.
        int result=-1;
        switch (firstInputSize) {
        case 2:
            //need to check the url exists in one place.
            result = checking1(bloomFilter, url, myVector);   
            break;
        case 3:
            //need to check the url exists in two places.
            result = checking2(bloomFilter, url, myVector);
            break;
        default:
            break;
        } 
        return result;
}
