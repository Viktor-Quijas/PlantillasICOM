#include "Nodo.h"

Nodo::Nodo()
{
    sig=nullptr;
    ant=nullptr;
    dato=0;
}
Nodo::Nodo(Nodo* sig,Nodo* ant)
{
    this->sig=sig;
    this->ant=ant;
    dato=0;
}
Nodo::Nodo(int dato)
{
    sig=nullptr;
    ant=nullptr;
    this->dato=dato;
}
Nodo::Nodo(int dato,Nodo* sig,Nodo* ant)
{
    this->dato=dato;
    if(!sig)
        this->sig=nullptr;
    else
        this->sig=sig;
    if(!ant)
        this->ant=nullptr;
    else
        this->ant=ant;
}
