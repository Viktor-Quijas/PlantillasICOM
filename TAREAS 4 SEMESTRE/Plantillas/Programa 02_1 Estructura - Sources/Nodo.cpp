#include "Nodo.h"
#include <iostream>
using namespace std;

Nodo::Nodo()
{
    info = 0;
    ptr = nullptr;
}

Nodo::Nodo(int info){
    this->info = info;
    ptr = nullptr;
}

Nodo::Nodo(int info,Nodo* ptr){
    this->info = info;
    this->ptr = ptr;
}

Nodo::~Nodo()
{
    //dtor
}
