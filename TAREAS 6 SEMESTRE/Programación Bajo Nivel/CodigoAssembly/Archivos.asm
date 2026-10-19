.model small
.stack 100h

.data
    ;Rutas en formato ASCIZ
    dirAlumno   db "C:\emu8086\MyBuild\Victor",0 
    dirAnterior db "C:\emu8086\MyBuild",0
    archivo     db "C:\emu8086\MyBuild\Victor\Quijas.txt",0                  

    texto       db "Hola Mundo"
    letraX      db "X"
    handle      dw ?
    bufferDir   db 64 dup(0)

    msgDir      db 13,10,"El directorio actual es: ",'$'

.code
    mov ax, @data
    mov ds, ax
    
    ;Crear directorio con el primer nombre

    mov ah, 39h                 ;39h = MKDIR
    lea dx, dirAlumno
    int 21h

    ;Entrar al directorio
    mov ah, 3Bh                 ;3Bh = CD
    lea dx, dirAlumno
    int 21h

    ;Mostrar en pantalla el directorio actual
    lea dx, msgDir
    mov ah, 09h
    int 21h

    mov ah, 47h                 ;47h = GET CURRENT DIRECTORY
    mov dl, 0                   ;unidad por defecto
    lea si, bufferDir           ;DS:SI = buffer
    int 21h

    lea si, bufferDir 
    
mostrar:
    lodsb
    cmp al, 0
    je  finMostrar
    mov dl, al
    mov ah, 02h                   ;02h = imprimir caracter
    int 21h
    jmp mostrar
finMostrar:

    ;Crear archivo primer_apellido.txt
    
    mov ah, 3Ch                 ;3Ch = CREATE FILE
    mov cx, 0                   ;atributos normales
    lea dx, archivo
    int 21h
    mov handle, ax              ;guardar handle

    ;Escribir "Hola Mundo"
    
    mov ah, 40h                 ;40h = WRITE
    mov bx, handle
    mov cx, 10                  ;longitud del texto
    lea dx, texto
    int 21h

    ;Se mueve el puntero a la posicion 5 y se escribe 'X'    
    
    mov ah, 42h                 ;42h = LSEEK
    mov al, 0                   ;desde el inicio del archivo
    mov bx, handle
    mov cx, 0                   ;CX:DX = desplazamiento
    mov dx, 5                   ;caracter 5 = M
    int 21h

    mov ah, 40h                 ; escribir 1 byte: 'X'
    mov bx, handle
    mov cx, 1
    lea dx, letraX
    int 21h


    ;Cerrar el archivo
    mov ah, 3Eh                 ;3Eh = CLOSE
    mov bx, handle
    int 21h

    ;Borrar el archivo
    mov ah, 41h                 ;41h = DELETE FILE
    lea dx, archivo
    int 21h

    ;Salir del directorio (subir al directorio padre)
    mov ah, 3Bh
    lea dx, dirAnterior
    int 21h

    ;Borrar la carpeta
    mov ah, 3Ah                 ; INT 21h / 3Ah = RMDIR
    lea dx, dirAlumno
    int 21h

    mov ah, 4Ch                 ; terminar programa
    int 21h