#include <iostream> 
#include <string>
#include <vector>
#include "IMenu.h"
#include "ConsoleMenu.h"
#include "OptionMenu.h"
#include "Options.h"
#include "Option1.h"
#include "Option2.h"
#include "App.h"

using namespace std; 

/*
This class runs the program. and uses all the proper classes
*/
        void App::run(){
            // create a menu that get the first input

            IMenu* a = new ConsoleMenu();
            a->runMenu(); 
            ConsoleMenu* cm = dynamic_cast<ConsoleMenu*>(a);
            int bloomFilter[cm->getArraySize()]; 
            //initialize the array.
            for (int i = 0; i < cm->getArraySize(); ++i) {
                bloomFilter[i] = 0;
            }

            IMenu* b = new OptionMenu;
            vector <string> myVector;
            Option1* op1 = new Option1(cm->getFirstInputSize(),cm->getHashTimes(),cm->getHash1(),cm->getHash2(),cm->getArraySize()); 
            Option2* op2 = new Option2(cm->getFirstInputSize(),cm->getHashTimes(),cm->getHash1(),cm->getHash2(),cm->getArraySize()); 
            // this while loop runs for infinity, get the next commands from the user.
            while (true)
            {
                b->runMenu();
                OptionMenu* pm = dynamic_cast<OptionMenu*>(b);
                // check which option the user chose to run.
                if(pm->getChoice()==1){
                    int res1 = op1->execute(bloomFilter,pm->getUserUrl(),myVector); 
                } else {
                    int res2 = op2->execute(bloomFilter,pm->getUserUrl(),myVector);
                }
                
            }
        // delete all the meomory.
        delete op2;
        delete op1;
        delete b;
        delete a;
        }

