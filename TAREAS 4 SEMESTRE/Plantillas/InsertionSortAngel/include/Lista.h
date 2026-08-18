#ifndef LISTA_H
#define LISTA_H
#include "Nodo.h"

class Lista
{
    public:
        Nodo* h;
        Nodo* t;

        Lista();

        void Inicializar(Nodo* insertar);
        void InsertarInicio(Nodo* insertar);
        void InsertarFinal(Nodo* insertar);
        void InsertarDespuesDe(Nodo* insertar,Nodo* previo);
        void InsertarAntesDe(Nodo* insertar,Nodo* posterior);

        void MostrarTodo();

        void QuitarNodo(Nodo* quitar);
        void EliminarNodo(Nodo* eliminar);
        void EliminarTodo();

        void InsertionSort();
};

#endif // LISTA_H
