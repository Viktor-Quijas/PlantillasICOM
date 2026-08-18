#ifndef NODO_H
#define NODO_H


class Nodo
{
    public:
        int info;
        Nodo* ptr;

        Nodo();
        Nodo(int info);
        Nodo(int info,Nodo* ptr);
        ~Nodo();

    protected:

    private:
};

#endif // NODO_H
