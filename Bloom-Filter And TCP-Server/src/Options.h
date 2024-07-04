#ifndef OPTIONS_H
#define OPTIONS_H
#include <iostream>
#include <string>

using namespace std; 

class Options {
public:
    // the abstract class
    long int doHash (int digit, string s);
    
    // Virtual destructor (recommended when dealing with polymorphism)
    virtual ~Options() = default;
};
#endif // OPTIONS_H