#include <iostream> 
#include <string>
#include <vector>
#include "IMenu.h"
#include "OptionMenu.h"
using namespace std;
/*
This class represents the second menu, when the user choose option 1 or 2, and choose the url.
*/  
        string userURL;
        int choice;


    // Constructor
        OptionMenu::OptionMenu(){
            userURL = "";
            choice = -1;
        }
        /*
        This method gets a string and check if the string is in the form of "1\2 "some url".
        */
        bool OptionMenu::checkStringFun(string s){
            if (s[0]!='1'&& s[0]!='2'){
                return false;
            } 
            //some exceptions check
            if(s[1]!=' ' || s[2]==' ' || s[s.length()-1]==' '){
                return false;
            } 

            
            return true;
            
        }  
        /*
        This method runs the second menu , and gets what the user want to do next.
        */ 
        void OptionMenu::runMenu(string userInput){
            //string userInput;
            //getline(cin, userInput);

            //need to check the input
            while (!checkStringFun(userInput)) {
            // we will clear the input buffer
	        cin.clear();
            getline(cin, userInput);
            }
            //split the input into choice and URL.
            int splitIndex = userInput.find(' ');
            choice = stoi(userInput.substr(0, splitIndex));
            userURL = userInput.substr(splitIndex + 1);
        }
        /*
        Getters for the variables
        */
        int OptionMenu::getChoice(){
            return choice;
        }
        string OptionMenu::getUserUrl(){
            return userURL;
        }