#include "Lista.h"
#include <iostream>
using namespace std;

Lista::Lista()
{
    cabeza = nullptr;
}

Lista::~Lista()
{
    Eliminar_Todo();
}


    /*Funciones de insertar*/

void Lista::Insert_inicio(int dato){
    Nodo* aux;
    aux = new Nodo(dato,cabeza);
    cabeza = aux;
    if (aux){
        cout << "Dato '" << dato << "' insertado correctamente al inicio!" << endl;  //Informa si el nodo fue creado correctamente
    } else {
        cout << "ERROR: falla en la asignacion de memoria" << endl;
    }
}

void Lista::Insert_final(int dato){
    Nodo *aux, *ultimo;
    aux = new Nodo(dato);

    if (cabeza){    //Después de crearse el nodo, checa si existe una lista y busca el final de la lista.
        ultimo = cabeza;
        while (ultimo->ptr)
            ultimo = ultimo->ptr;
        ultimo->ptr = aux;
    } else
        cabeza = aux;
    if (aux){
        cout << "Dato '" << dato << "' insertado correctamente al final!" << endl;  //Informa si el nodo fue creado correctamente
    } else {
        cout << "ERROR: falla en la asignacion de memoria" << endl;
    }
}

void Lista::Insertar_posicion(int posicion, int dato){
    Nodo* aux;
    aux = cabeza;

    if (Vacio()){ // Comprueba si la lista esta vacia, si es así, inserta el dato al inicio.
        cout << "Esta lista actualmente se encuentra vacia: Se creara la lista y el dato '" << dato <<"' se insertara en la primera posicion." << endl;
        Insert_inicio(dato);
    }
    else if(posicion >= Tamanio() + 1){ //Si la posicion es mayor, inserta el dato al final.
        Insert_final(dato);
        cout << "Dato '" << dato << "' correctamente insertado en la posicion: " << Tamanio() << endl;
    }
    else if (posicion == 1){ //Si la posicion es 1, inserta al inicio
        Insert_inicio(dato);
        cout << "Dato '" << dato << "' ingresado en la primer posicion." << endl;
    }
    else {  //Inserta nodo en la posicion indicada
        int i;
        Nodo *nueva_posicion = nullptr, *temp = nullptr; //temp crea el nuevo nodo, nueva posicion, guarda el nodo anterior.
        for (i = 1; i < posicion; i++)
            aux = aux->ptr;
        nueva_posicion = Anterior(aux);
        temp = new Nodo(dato,aux);
        nueva_posicion->ptr = temp;
        cout << "Dato '" << dato << "' correctamente insertado en la posicion: " << posicion << endl;
    }

    return;
}

    /*Funciones de Mostrar*/

void Lista::Mostrar_todo(){
    if (Vacio()){
        cout << "Esta lista actualmente se encuentra vacia: no hay nada por mostrar." << endl;
    } else {
        Nodo* aux;
        aux = cabeza;
        while (aux){
            cout << aux->info << "->";
            aux = aux->ptr;
        }
        cout << "x" << endl;
    }
}

bool Lista::Vacio(){
    return cabeza == nullptr;
}

int Lista::Tamanio(){
    int tamanio = 0;
    Nodo* aux = cabeza;

    while (aux){
        tamanio++;
        aux = aux->ptr;
    }
    return tamanio;
}

bool Lista::Validar_posicion(int posicion){
    return posicion > 0;
}

void Lista::Mostrar_enPosicion(Nodo* direccion){
    if (direccion == nullptr){
        cout << "Direccion apuntando a nulo, ingrese una direccion valida. Puede ultilizar la funcion mostrar todo si desea verificar informacion de esta lista." << endl;
    }
    else {
        cout << "'" << direccion->info << "'" << endl;
    }
    return;
}

    /*Funciones de buscar*/

Nodo* Lista::Buscar(int posicion,int dato, int metodo){
    if (Vacio()){
        cout << "Esta lista actualmente se encuentra vacia, verifica si buscabas en otra lista." << endl;
    } else {
        Nodo* aux;
        aux = cabeza;
        int i;

        switch (metodo){
        case 1:     // Primer método para solo buscar por posición.

            if (Validar_posicion(posicion)){
                for (i = 1; i < posicion && aux->ptr; i++)
                    aux = aux->ptr;
                if (i == posicion){
                    cout << "Elemento encontrado: " << aux->info << endl;
                    return aux;
                }
                else if (posicion > i){ // Indica si la posicion ingresada es un numero mayor a la cantidad de elmentos que hay en la lista.
                    cout << "La posicion es mayor al numero de elementos actuales de esta lista." << endl;
                    return nullptr;
                }
            } else {
                cout << "Posicion NO valida, ingreso un numero negativo." << endl;
                return nullptr;
            }
            break;

        case 2:     // Segundo método para solo buscar por dato (regresa la direccion de la primer coincidencia).

            i = 1;

            while (dato != aux->info && aux->ptr){
                aux = aux->ptr;
                i++;
            }
            if (dato == aux->info){
                cout << "Dato '" << dato << "' encontrado en la posicion: " << i << endl;
                return aux;
            }
            else {
                cout << "Dato '" << dato << "' no se encuentra en esta lista." << endl;
                return nullptr;
            }
            break;

        default:
            cout << "Metodo ingresado no valido, en el apartado de metodos de busqueda ingrese: 1)Buscar por posicion\t 2)Buscar por dato" << endl;
            return nullptr;
            break;
        }
    }
    return nullptr;
}

Nodo* Lista::Primero(){
    if (Vacio())
        cout << "Esta lista actualmente se encuentra vacia." << endl;
    else {
        cout << "Primer nodo encontrado!" << endl;
        return cabeza;
    }
    return nullptr;
}

Nodo* Lista::Ultimo(){
    if (Vacio())
        cout << "Esta lista actualmente se encuentra vacia." << endl;
    else {
        Nodo* aux;
        aux = cabeza;
        while (aux->ptr){
            aux = aux->ptr;
        }
        cout << "Ultimo nodo encontrado!" << endl;
        return aux;
    }
    return nullptr;
}

Nodo* Lista::Siguiente(Nodo* actual){
    if (Vacio())
        cout << "Esta lista actualmente se encuentra vacia." << endl;
    else if (actual->ptr == nullptr){
        cout << "No existe ningun elemento posterior al indicado, en esta lista." << endl;
        return nullptr;
    }
    else {
        cout << "Nodo siguiente encontrado! " << endl;
        return actual->ptr;
    }
    return nullptr;
}

Nodo* Lista::Anterior(Nodo* actual){
    if (Vacio()){
        cout << "Esta lista actualmente esta vacia." << endl;
        return nullptr;
    }
    else if (cabeza == actual){
        cout << "No existe ningun elemento anterior al indicado, en esta lista." << endl;
        return nullptr;
    }
    else if (actual == nullptr){
        cout << "La direccion ingresada apunta a nulo. Ingrese otra direccion." << endl;
        return nullptr;
    }
    else {
        Nodo* aux;
        aux = cabeza;
        while (aux->ptr != actual && aux->ptr)
            aux = aux->ptr;
        if (aux->ptr == actual){
            cout << "Nodo anterior encontrado!" << endl;
            return aux;
        }
        else {
            cout << "No existe ningun elemento anterior al indicado, en esta lista." << endl;
            return nullptr;
        }
    }
    return nullptr;
}

    /*Funciones de eliminar*/

void Lista::Eliminar(int posicion){
    if (Vacio()) // Si la lista no exst, no actua.
        cout << "Esta lista actualmente se encuentra vacia." << endl;

    else if (Buscar(posicion, 0, BUSCAR_POSICION) == nullptr)  //Verifica la posición
        cout << "No existe un elemento en esta lista en la posicion indicada." << endl;

    else if (posicion == 1) {   //Elimina específicamente el primer elemento
        Nodo* aux;
        aux = cabeza;

        cabeza = aux->ptr;
        delete aux;
        aux = nullptr;
        cout << "Elemento eliminado con exito!" << endl;
    }
    else {  //Elimina en la posición indicada.
        Nodo *aux, *auxR;
        aux = cabeza;
        auxR = nullptr;
        int i;
        for (i = 1; i < posicion; i++)
            aux = aux->ptr;
        auxR = Anterior(aux);
        auxR->ptr = aux->ptr;
        delete aux;
        aux = nullptr;
        cout << "Elemento eliminado con exito!" << endl;
    }

    return;
}

void Lista::Eliminar_Todo(){
    if (Vacio()){
        cout << "No existe ninguna lista, nada por eliminar." << endl;
    } else {
        Nodo *aux, *auxR;
        aux = cabeza;
        auxR = nullptr;
        cabeza = nullptr;

        while (aux){
            auxR = aux;
            aux = aux->ptr;
            delete auxR;
        }

        cout << "Esta lista ha sido borrada exitosamente!" << endl;
    }
    return;
}
