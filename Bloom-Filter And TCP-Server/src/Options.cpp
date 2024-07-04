#include <iostream>
#include <string>
#include "Options.h"
/*
this class is a father class of option 1 and option 2 ,
because both of them use the fucntion do hash, and its to avoid duplicates.
*/
using namespace std;  
    /*
    this method gets a number and a string and does hashing or double hashing depends on the number the user chose.
    */
    long int Options::doHash (int digit, string s){
        hash<string> hashF;
        long int val=hashF(s);
        if(digit==1){
            return val;
        } 
        if(digit==2){
            string second = to_string(val);  
            return hashF(second);
        }
        else{
            return -1;
        }
    } 

