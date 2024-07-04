#include <iostream>
#include "App.h"

using namespace std; 
/*
this class represents the main and runs our program.
*/ 
int main() {
    App* ap = new App();
    ap->run();
    delete ap;
    return 0;
}