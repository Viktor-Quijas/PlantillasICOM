#include <iostream>
#include "Lista.h"
using namespace std;

int main()
{
    Lista* l=new Lista();
    int i, dato;
    for(i=0;i<18;i++)
    {
        cout << "Numero: ";
        cin >> dato;
        l->InsertarInicio(new Nodo(dato));
    }

    l->MostrarTodo();
    l->InsertionSort();
    l->MostrarTodo();
    return 0;
}
