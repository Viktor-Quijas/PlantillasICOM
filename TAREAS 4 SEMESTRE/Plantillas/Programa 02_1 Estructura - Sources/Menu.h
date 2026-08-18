#ifndef MENU_H
#define MENU_H
#include "Lista.h"


class Menu
{
    public:
        Lista* l;

        void Menu_principal();
        bool Validacion();
        void Salir();

        int eleccion;
        bool band;
        int dato;
        int posicion;

        /*Menu para cada accion?*/
        void Inicializar_lista();
        void Insertar_elemento();
        void Mostrar_lista();
        void Buscar_elemento();
        void Eliminar_elemento();
        void Eliminar_lista();


        Menu();
        ~Menu();

    protected:

    private:
};

#endif // MENU_H

