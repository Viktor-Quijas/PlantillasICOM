#include "Lista.h"

Lista::Lista()
{
    h=nullptr;
    t=nullptr;
}

void Lista::Inicializar(Nodo* insertar)
{
    cout<<"-Inicializar ";
    if(!insertar)
    {
        cout<<"\tInsertar no Existe"<<endl;
        return;
    }

    h=insertar;
    t=h;
}
void Lista::InsertarInicio(Nodo* insertar)
{
    cout<<"InsertarInicio";
    if(!insertar)
    {
        cout<<"\tInsertar no Existe >.<"<<endl;
        return;
    }
    if(!h||!t)
    {
        Inicializar(insertar);
        cout<<">-<"<<endl;
        return;
    }

    cout<<"\t<"<<insertar->dato<<"> Insertado >3<"<<endl;
    insertar->sig=h;
    h=insertar;
    h->sig->ant=insertar;
}
void Lista::InsertarFinal(Nodo* insertar)
{
    cout<<"InsertarFinal";
    if(!insertar)
    {
        cout<<"\tInsertar no Existe >.<"<<endl;
        return;
    }
    if(!h||!t)
    {
        Inicializar(insertar);
        cout<<">-<"<<endl;
        return;
    }

    cout<<"\t<"<<insertar->dato<<"> Insertado >3<"<<endl;

    t->sig=insertar;
    insertar->ant=t;
    t=t->sig;
}
void Lista::InsertarDespuesDe(Nodo* insertar,Nodo* previo)
{
    cout<<"InsertarDespuesDe";
    if(!insertar)
    {
        cout<<"\tInsertar no Existe >.<"<<endl;
        return;
    }
    if(!h||!t)
    {
        Inicializar(insertar);
        cout<<"\t>-<"<<endl;
        return;
    }
    if(!previo)
    {
        cout<<"\tPosterior no Existe >.<"<<endl;
        return;
    }
    if(!previo->sig)
    {
        cout<<">w<"<<endl;
        cout<<"\t";
        InsertarFinal(insertar);
        return;
    }

    cout<<"\t<"<<insertar->dato<<"> <"<<previo->dato<<"> Insertardo >3<"<<endl;
    insertar->sig=previo->sig;
    insertar->ant=previo;
    previo->sig->ant=insertar;
    previo->sig=insertar;
}
void Lista::InsertarAntesDe(Nodo* insertar,Nodo* posterior)
{
    cout<<"InsertarAntesDe";
    if(!insertar)
    {
        cout<<"\tInsertar no Existe >.<"<<endl;
        return;
    }
    if(!h||!t)
    {
        Inicializar(insertar);
        cout<<">-<"<<endl;
        return;
    }
    if(!posterior)
    {
        cout<<"\tPosterior no Existe >.<"<<endl;
        return;
    }
    if(!posterior->ant)
    {
        cout<<">w<"<<endl;
        cout<<"\t";
        InsertarInicio(insertar);
        return;
    }

    cout<<"\t<"<<insertar->dato<<"> <"<<posterior->dato<<"> Insertardo >3<"<<endl;
    insertar->sig=posterior;
    insertar->ant=posterior->ant;
    posterior->ant->sig=insertar;
    posterior->ant=insertar;
}

void Lista::MostrarTodo()
{
    if(!h||!t)
    {
        cout<<"Lista no Existe"<<endl;
        return;
    }
    cout<<"-----Lista-----"<<endl;
    Nodo* tmp=h;
    while(tmp)
    {
        cout<<tmp->dato<<endl;
        tmp=tmp->sig;
    }
    cout<<"---------------"<<endl;
}

void Lista::QuitarNodo(Nodo* quitar)
{
    cout<<"Quitar";
    if(!quitar)
        return;
    if(!h||!t)
        return;
    if(quitar==h)
    {
        cout<<" Inicio"<<endl;
        h=h->sig;
        if(h)
            h->ant=nullptr;
        else
            t=nullptr;
    }
    else if (quitar==t)
    {
        cout<<" Final"<<endl;
        t=t->ant;
        if(t)
            t->sig=nullptr;
        else
            h=nullptr;
    }
    else
    {
        cout<<" Pos"<<endl;
        quitar->ant->sig=quitar->sig;
        quitar->sig->ant=quitar->ant;
    }
    quitar->sig=nullptr;
    quitar->ant=nullptr;
}
void Lista::EliminarNodo(Nodo* eliminar)
{
    QuitarNodo(eliminar);
    delete eliminar;
    cout<<"Eliminar*"<<endl;
}
void Lista::EliminarTodo()
{
    cout<<"EliminarTodo";
    if(!h||!t)
    {
        cout<<"\tLista no Existe >.<"<<endl;
        return;
    }
    Nodo* aux=h->sig;
    Nodo* auxR=h;

    while(aux)
    {
        delete auxR;
        auxR=aux;
        aux=aux->sig;
    }
    delete auxR;
    h=nullptr;
    t=nullptr;
    cout<<"\tLista Eliminada >3<"<<endl;
}

void Lista::InsertionSort()
{
    if(!h||!t)
    {
        cout<<"no hay elementos, naco"<<endl;
        return;
    }
    if(!h->sig)
    {
        cout<<"solo hay un elemento, naco"<<endl;
        return;
    }
    Nodo* mover = h->sig;

    while(mover)
    {
        Nodo* ancla=mover->sig;
        Nodo* recorrer=mover->ant;

        while(recorrer && mover->dato<recorrer->dato)
            recorrer=recorrer->ant;

        QuitarNodo(mover);

        if(recorrer)
            InsertarDespuesDe(mover, recorrer);
        else
            InsertarInicio(mover);

        mover=ancla;
    }
}
