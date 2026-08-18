#ifndef NODO_H
#define NODO_H
#include <iostream>
#include <string>
#include <stdlib.h>
using namespace std;
class Nodo
{
    public:
        Nodo* sig;
        Nodo* ant;
        int dato;

        Nodo();
        Nodo(int dato);
        Nodo(Nodo* sig,Nodo* ant);
        Nodo(int dato,Nodo* sig,Nodo* ant);
};

#endif // NODO_H
