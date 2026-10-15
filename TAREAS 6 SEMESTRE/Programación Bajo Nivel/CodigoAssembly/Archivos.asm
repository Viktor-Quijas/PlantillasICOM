.model small
.stack 100h

.data
    ; --- Rutas (terminadas en 0, formato ASCIZ) ---
    dirAlumno   db "C:\emu8086\MyBuild\Victor",0     ; <-- tu primer nombre
    dirAnterior    db "..",0
    archivo     db "Quijas.txt",0                   ; <-- tu primer apellido

    texto       db "Hola Mundo"                    ; 10 bytes
    letraX      db "X"
    handle      dw ?
    bufferDir   db 64 dup(0)

    ; --- Mensajes ---
    msgDir      db 13,10,"El directorio actual es: "
    msgError    db 13,10,"ERROR en la operacion (AX = codigo de error DOS).$"

.code
main proc
    mov ax, @data
    mov ds, ax

    ; ----------------------------------------------------
    ; 1. Crear directorio con el primer nombre
    ; ----------------------------------------------------

    mov ah, 39h                 ; INT 21h / 39h = MKDIR
    lea dx, dirAlumno
    int 21h
    jc  error

    ; ----------------------------------------------------
    ; 2. Entrar al directorio
    ; ----------------------------------------------------
    mov ah, 3Bh                 ; INT 21h / 3Bh = CHDIR
    lea dx, dirAlumno
    int 21h
    jc  error

    ; ----------------------------------------------------
    ; 3. Mostrar en pantalla el directorio actual
    ; ----------------------------------------------------
    lea dx, msgDir
    mov ah, 09h
    int 21h

    mov ah, 47h                 ; INT 21h / 47h = GET CURRENT DIRECTORY
    mov dl, 0                   ; unidad por defecto
    lea si, bufferDir           ; DS:SI = buffer
    int 21h
    jc  error

    lea si, bufferDir
mostrar:
    lodsb
    cmp al, 0
    je  finMostrar
    mov dl, al
    mov ah, 2                   ; INT 21h / 02h = imprimir caracter
    int 21h
    jmp mostrar
finMostrar:

    ; ----------------------------------------------------
    ; 4. Crear archivo primer_apellido.txt
    ; ----------------------------------------------------
    mov ah, 3Ch                 ; INT 21h / 3Ch = CREATE FILE
    mov cx, 0                   ; atributos normales
    lea dx, archivo
    int 21h
    jc  error
    mov handle, ax              ; guardar handle

    ; ----------------------------------------------------
    ; 5. Escribir "Hola Mundo"
    ; ----------------------------------------------------
    mov ah, 40h                 ; INT 21h / 40h = WRITE
    mov bx, handle
    mov cx, 10                  ; longitud del texto
    lea dx, texto
    int 21h
    jc  error

    ; ----------------------------------------------------
    ; 6. Poner una X donde esta la M
    ;    "Hola Mundo": H(0) o(1) l(2) a(3) ' '(4) M(5)
    ;    Se mueve el puntero a la posicion 5 y se escribe 'X'
    ; ----------------------------------------------------
    mov ah, 42h                 ; INT 21h / 42h = LSEEK
    mov al, 0                   ; desde el inicio del archivo
    mov bx, handle
    mov cx, 0                   ; CX:DX = desplazamiento
    mov dx, 5
    int 21h
    jc  error

    mov ah, 40h                 ; escribir 1 byte: 'X'
    mov bx, handle
    mov cx, 1
    lea dx, letraX
    int 21h
    jc  error
    lea dx, msgX
    call imprimir

    ; ----------------------------------------------------
    ; 7. Cerrar el archivo
    ; ----------------------------------------------------
    mov ah, 3Eh                 ; INT 21h / 3Eh = CLOSE
    mov bx, handle
    int 21h
    jc  error
    lea dx, msgCerrado
    call imprimir

    ; ----------------------------------------------------
    ; 8. Borrar el archivo
    ; ----------------------------------------------------
    mov ah, 41h                 ; INT 21h / 41h = DELETE FILE
    lea dx, archivo
    int 21h
    jc  error
    lea dx, msgBorrado
    call imprimir

    ; ----------------------------------------------------
    ; 9. Salir del directorio (subir al directorio padre)
    ; ----------------------------------------------------
    mov ah, 3Bh                 ; CHDIR ".."
    lea dx, dirAnterior
    int 21h
    jc  error
    lea dx, msgSalio
    call imprimir

    ; ----------------------------------------------------
    ; 10. Borrar la carpeta
    ; ----------------------------------------------------
    mov ah, 3Ah                 ; INT 21h / 3Ah = RMDIR
    lea dx, dirAlumno
    int 21h
    jc  error
    lea dx, msgCarpeta
    call imprimir

    lea dx, msgFin
    call imprimir
    jmp salir

error:
    push ax
    lea dx, msgError
    call imprimir
    pop ax

salir:
    mov ah, 4Ch                 ; terminar programa
    int 21h
main endp

; Imprime cadena terminada en '$' apuntada por DX
imprimir proc
    mov ah, 9
    int 21h
    ret
imprimir endp

end main