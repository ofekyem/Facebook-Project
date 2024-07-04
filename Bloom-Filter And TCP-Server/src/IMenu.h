#ifndef IMENU_H
#define IMENU_H

class IMenu {
public:
    // the abstract class
    virtual void runMenu(std::string userInput) = 0;

    // Virtual destructor (recommended when dealing with polymorphism)
    virtual ~IMenu() = default;
}; 
#endif // IMENU_H