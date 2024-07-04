#include <iostream>
#include <string>
#include <vector>
#include "IMenu.h"
#include "ConsoleMenu.h"

using namespace std; 
/*
this class runs the first menu of the task, when the user chooses his arr size , and his hash functions.
*/ 

        ConsoleMenu:: ConsoleMenu(){
            arraySize=0;
            hashTimes=0;
            hash1=0;
            hash2=0;
            firstInputSize=0;
        } 
        /*
        this method checks the user input, if its vaild or not.
        */
        int ConsoleMenu:: firstInputCheck(const string& input) {
            if(input[0]-'0'==0|| input[0]== ' '){
                return 0;
            }
            int countNumbers = 0;
            bool inNumber = false;
            bool isCorrectDigit=false;
            
            bool isSpaceNow=false;
            int lastDigit=0;

            for (char c : input) {
                if( c== ' ' && isSpaceNow==true){
                    //if there is a double space
                    return 0;
                }
                if (c == ' ') { //case of space
                    inNumber = false;
                    isSpaceNow=true;
                    if(isCorrectDigit==true){
                        isCorrectDigit=false;
                    }
                } 

                else if (isdigit(c)) { // case that the char is a number
                    isSpaceNow=false;
                    if (!inNumber) {
                        if(lastDigit!=(c-'0')){
                            ++countNumbers;
                        }
                        
                        inNumber = true;
                    }
                    if(countNumbers>=2){  // in case we are in the second or three number of the input
                        if(isCorrectDigit==false && (c=='2'||c=='1')){ // here we make sure that the second and third input has only one digit: 2 or 1.
                            isCorrectDigit=true;
                            
                        }

                        else{
                            return 0;
                        } 
                    }
                    if(countNumbers!=1){
                        lastDigit=c-'0';
                    } 
                    
                         
                } 
                //in case there is a non space or digit char
                else {
                    return 0;
                }
            }
            ///check if there is useless space at the end of the input
            if(input[input.length()-1]==' '){
                return 0;
            }

            if(countNumbers==0||countNumbers==1){ //we return 0 in case that there were 0 or 1 values in the input
                return 0;
            } 
            // in this case we have 2 different inputs: array size and 1 or 2,we will check if its 1 or 2 and return it. we have only one hash in this case
            if(countNumbers==2){ 
                isSpaceNow=true;
                int valueNum=0;
                for(char c : input){
                    if( c != ' '&& valueNum==1){
                        return c-'0';
                    }

                    if (c != ' '){
                        isSpaceNow=false;
                    }
                    else if(c == ' ' && isSpaceNow==false){
                        valueNum++; 
                        isSpaceNow=true; 
                    } 
                    
                } 
                return 0;
            }
            //in this case we have 3 different inputs values: first one is array size and the second or third one is 1 or 2: we will return 3 for 2 hash functions
            else{
                return 3;
            }

        }  
        /*
        this method gets the first input from the user, and initialize the variables.
        */
        void ConsoleMenu::runMenu(string userInput){
            int splitIndex2;
            //string userInput;
            // getline(cin, userInput);
            // //get a valid input.
            // while (firstInputCheck(userInput) == 0) {
            //     getline(cin, userInput);
            // } 
            // // we will clear the input buffer
            // cin.clear();

            //this is the right input and it indicates 1 2 or 3 where 1 is one function with 1 as value, 2 is one function with 2 as value, and three is 2 functions with both 1 and 2 as values.
            firstInputSize = firstInputCheck(userInput); 
            int splitIndex = userInput.find(' ');
            arraySize = stoi(userInput.substr(0, splitIndex));
            //in this case there are 2 hash functions where one must have 2 and the other 1.
            if(firstInputSize==3){
                hash1=1;
                hash2=2;
            }
            //in this case we have one hash function, the value depends on the var.
            else{
                hashTimes=firstInputSize;
                firstInputSize=2;
            }  

        }  
        /*
        getters for the variables.
        */
        int ConsoleMenu::getArraySize(){
            return arraySize;
        } 
        int ConsoleMenu::getHashTimes(){
            return hashTimes;
        }  
        int ConsoleMenu::getHash1(){
            return hash1;
        } 
        int ConsoleMenu::getHash2(){
            return hash2;
        } 
        int ConsoleMenu::getFirstInputSize(){
            return firstInputSize;
        }