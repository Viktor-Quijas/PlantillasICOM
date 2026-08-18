#include <iostream>
#include "Menu.h"

using namespace std;

int main()
{
    Menu* menu;
    menu = new Menu();
    menu->Menu_principal();
    delete menu;
    return 0;
}
