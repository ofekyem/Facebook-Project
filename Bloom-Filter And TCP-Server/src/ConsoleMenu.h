#ifndef ConsoleMenu_H
#define ConsoleMenu_H
#include "IMenu.h"
#include <string>

using namespace std;  


class ConsoleMenu : public IMenu{
    private:
        int arraySize; //array size
        int hashTimes; //times of 1 function
        int hash1; //times of the first of the 2 functions
        int hash2; //times of the second of the 2 functions
        int firstInputSize; //represent if using one function or two

    public :
        ConsoleMenu();
        void runMenu(string userInput);
        int getArraySize();
        int getHashTimes();
        int getHash1();
        int getHash2();
        int getFirstInputSize();
        int firstInputCheck(const string& input);
};

#endif //ConsoleMenu_H
