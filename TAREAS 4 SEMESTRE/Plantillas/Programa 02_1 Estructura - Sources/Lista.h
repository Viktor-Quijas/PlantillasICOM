#ifndef LISTA_H
#define LISTA_H
#include "Nodo.h"


class Lista
{
    public:
        static const int BUSCAR_POSICION = 1;
        static const int BUSCAR_DATO = 2;

        Nodo* cabeza;

        /* Funciones de insertar*/
        void Insert_inicio(int dato);
        void Insert_final(int dato);
        void Insertar_posicion(int posicion, int dato);

        /* Funciones de Mostrar*/
        void Mostrar_todo();
        bool Vacio();
        int Tamanio();
        bool Validar_posicion(int posicion);
        void Mostrar_enPosicion(Nodo* posicion);

        /* Funciones para buscar */
        Nodo* Buscar(int posicion, int dato, int metodo);
        Nodo* Primero();
        Nodo* Ultimo();
        Nodo* Anterior(Nodo* actual);
        Nodo* Siguiente(Nodo* actual);

        /* Funciones de eleminar */
        void Eliminar(int posicion);
        void Eliminar_Todo();

        Lista();
        ~Lista();

    protected:

    private:
};

#endif // LISTA_H
