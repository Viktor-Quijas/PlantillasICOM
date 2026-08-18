#include "Menu.h"
#include <iostream>

using namespace std;

void Menu::Menu_principal(){
    system("cls");
    cout << "\t---Lista Simplemente Ligada---" << endl << endl;
    cout << "1. Inicializar lista." << endl;
    cout << "2. Insertar un elemento." << endl;
    cout << "3. Mostrar la lista." << endl;
    cout << "4. Buscar un elemento." << endl;
    cout << "5. Eliminar un elemento." << endl;
    cout << "6. Eliminar lista." << endl;
    cout << "7. Salir." << endl;
    cout << "-> ";
    cin >> eleccion;

    switch (eleccion){
    case 1:
        if (Validacion()){
            Inicializar_lista();
            band = true;
            system("pause");
            Menu_principal();
        }
        else {
            cout << "La lista ya fue creada." << endl;
            system("pause");
            Menu_principal();
        }
        break;
    case 2:
        if (Validacion()){
            cout << endl << "Es ESTRICTAMENTE necesario inicializar la lista primero." << endl;
            system("pause");
            Menu_principal();
        }
        else {
            Insertar_elemento();
            system("pause");
            Menu_principal();
        }
        break;
    case 3:
        if (Validacion()){
            cout << endl << "Es ESTRICTAMENTE necesario inicializar la lista primero." << endl;
            system("pause");
            Menu_principal();
        }
        else {
            Mostrar_lista();
            system("pause");
            Menu_principal();
        }
        break;
    case 4:
        if (Validacion()){
            cout << endl << "Es ESTRICTAMENTE necesario inicializar la lista primero." << endl;
            system("pause");
            Menu_principal();
        }
        else {
            Buscar_elemento();
            system("pause");
            Menu_principal();
        }
        break;
    case 5:
        if (Validacion()){
            cout << endl << "Es ESTRICTAMENTE necesario inicializar la lista primero." << endl;
            system("pause");
            Menu_principal();
        }
        else {
            Eliminar_elemento();
            system("pause");
            Menu_principal();
        }
        break;
    case 6:
        if (Validacion()){
            cout << endl << "Es ESTRICTAMENTE necesario inicializar la lista primero." << endl;
            system("pause");
            Menu_principal();
        }
        else {
            Eliminar_lista();
            system("pause");
            Menu_principal();
        }
        break;
    case 7:
        Salir();
        system("pause");
        break;
    default:
        cout << "Ingrese un digito valido." << endl;
        system("pause");
        Menu_principal();
        break;
    }
    return;
}

void Menu::Inicializar_lista(){
    l = new Lista();
    cout << "Lista creada correctamente!" << endl;
    return;
}

void Menu::Insertar_elemento(){
    system("cls");
    cout << "Indique el dato que va a insertar: ";
    cin >> dato;
    cout << endl << "Indique la posicion donde va insertar el dato." << endl << endl;
    cout << "1. Insertar al inicio." << endl;
    cout << "2. Insertar al final." << endl;
    cout << "3. Insertar en posicion." << endl;
    cout << "4. Regresar al menu." << endl;
    cout << "-> ";
    cin >> eleccion;
    cout << endl;

    switch(eleccion){
    case 1:
        l->Insert_inicio(dato);
        break;
    case 2:
        l->Insert_final(dato);
        break;
    case 3:
        l->Mostrar_todo();
        cout << endl << "Ingrese la posicion: ";
        cin >> posicion;
        l->Insertar_posicion(posicion,dato);
        break;
    case 4:
        cout << "Seguro que deseas regresar?\t1.Si\t2.Noo" << endl;
        cout << "-> ";
        cin >> eleccion;
        if (eleccion == 2){
            Insertar_elemento();
        }
        break;
    default:
        cout << "Ingrese un digito valido." << endl;
        system("pause");
        Insertar_elemento();
        break;
    }
    return;
}

void Menu::Buscar_elemento(){
    system("cls");
    cout << "Indique lo que va a buscar. " << endl << endl;
    cout << "1. Busqueda." << endl;
    cout << "2. Buscar el primero." << endl;
    cout << "3. Buscar el ultimo." << endl;
    cout << "4. Buscar el siguiente a." << endl;
    cout << "5. Buscar el anterior a." << endl;
    cout << "6. Regresar al menu principal." << endl;
    cout << "-> ";
    cin >> eleccion;
    cout << endl;


    switch(eleccion){
    case 1:
        cout << "Ingrese el metodo por el cual buscar. " << endl;
        cout << "1. Buscar por posicion." << endl;
        cout << "2. Buscar por dato." << endl;
        cout << "-> ";
        cin >> eleccion;
            if (eleccion == 1){
                cout << "Ingrese la posicion: ";
                cin >> posicion;
                l->Buscar(posicion,0,1);
            }
            else if (eleccion == 2){
                cout << "Ingrese el valor a buscar: " << endl;
                cin >> dato;
                l->Buscar(0,dato,2);
            } else {
                cout << "Ingrese un digito valido." << endl;
                Buscar_elemento();
            }
        break;
    case 2:
        l->Mostrar_enPosicion(l->Primero());
        break;
    case 3:
        l->Mostrar_enPosicion(l->Ultimo());
        break;
    case 4:
        cout << "Buscar al siguiente." << endl;
        cout << "Ingrese la posicion de la referencia: ";
        cin >> posicion;
        l->Mostrar_enPosicion(l->Siguiente(l->Buscar(posicion,0,1)));
        break;
    case 5:
        cout << "Buscar al anterior." << endl;
        cout << "Ingrese la posicion de la referencia: ";
        cin >> posicion;
        l->Mostrar_enPosicion(l->Anterior(l->Buscar(posicion,0,1)));
        break;
    case 6:
        cout << "Seguro que deseas regresar?\t1.Si\t2.Noo" << endl;
        cout << "-> ";
        cin >> eleccion;
        if (eleccion == 2){
            Buscar_elemento();
        }
        break;
    default:
        cout << "Ingrese un digito valido." << endl;
        system("pause");
        Buscar_elemento();
        break;
    }
    return;
}

void Menu::Eliminar_elemento(){
    system("cls");
    l->Mostrar_todo();
    cout << "Ingresa la posicion del elemento que deseas eliminar: ";
    cin >> posicion;

    cout << endl << "***Estas seguro de eliminar este elemento?***" << endl << "No puedes deshacer la accion." << endl;
    cout << "1.Si\t2.No" << endl;
    cout << "-> ";
    cin >> eleccion;

    switch (eleccion){
    case 1:
        l->Eliminar(posicion);
        break;
    case 2:
        cout << "Transfiriendote a Menu Principal..." << endl;
        system("pause");
        Menu_principal();
        break;
    default:
        cout << "Ingrese un digito valido." << endl;
        system("pause");
        Eliminar_elemento();
        break;
    }
    return;
}

void Menu::Eliminar_lista(){
    system("cls");
    cout << endl << "***Estas seguro de ELIMINAR la lista por COMPLETO?***" << endl << "No puedes deshacer la accion." << endl;
    cout << "1.Si\t2.No" << endl;
    cout << "-> ";
    cin >> eleccion;

    switch (eleccion){
    case 1:
        l->Eliminar_Todo();
        band = false;
        break;
    case 2:
        cout << "Transfiriendote a Menu Principal..." << endl;
        system("pause");
        Menu_principal();
        break;
    default:
        cout << "Ingrese un digito valido." << endl;
        system("pause");
        Eliminar_elemento();
        break;
    }
    return;
}

void Menu::Salir(){
    system("cls");
    cout << "Seguro que deseas salir del programa?\t1.Si\t2.Noo" << endl;
    cout << "-> ";
    cin >> eleccion;

    if (eleccion == 1){
        cout << "Gracias por utilizar mi programa!" << endl;
        return;
    }
    else if (eleccion == 2){
        Menu_principal();
    } else {
        cout << "Ingrese un digito valido." << endl;
        system("pause");
        Salir();
    }
}

bool Menu::Validacion(){
    return band == false;
}

void Menu::Mostrar_lista(){
        system("cls");
        cout << "\t---Lista---" << endl;
        l->Mostrar_todo();
        return;
}

Menu::Menu()
{
    eleccion = 0;
    band = false;
    dato = 0;
    posicion = 0;
}

Menu::~Menu()
{

}

