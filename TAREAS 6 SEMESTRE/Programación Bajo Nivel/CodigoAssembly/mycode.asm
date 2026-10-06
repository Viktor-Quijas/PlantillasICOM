.model small                      ; Define el modelo de memoria pequeño (64KB de código, 64KB de datos)
        .data                     ; Marca el inicio del segmento de datos del programa
            var1 db 0Ah           ; Reserva 1 byte de memoria llamado var1 e inicializa su valor en 0Ah (10 en decimal)
            
;desplazamiento      0        
            arreglo1 db 0Ah,0Bh,0Fh ; Reserva 3 bytes contiguos de memoria e inicializa sus valores con 0Ah, 0Bh y 0Fh
;desplazamiento          1   2   3

        .code                     ; Marca el inicio del segmento de código fuente ejecutable
        mov ax,@data              ; Carga en AX la dirección base donde se encuentra el segmento de datos
        mov ds,ax                 ; Mueve la dirección base desde AX hacia DS para inicializar el segmento de datos
        mov al,arreglo1[3]
        
        ;Modos de direccionamiento
        
        ;Inmediato        
        
        mov bl,01h                ; Asigna directamente el valor literal constante 01h al registro BL
                            
        ;De registro
        
        mov ax,bx                 ; Copia todo el contenido del registro BX dentro del registro AX
            
        ;Directo
        
        mov [014h],ax               ; Intenta mover el valor de AX a la direccion de memoria 014h 
        
        ;De registro indirecto
        
        mov [bx],ax               ; Copia el contenido de AX a la direccion de memoria cuya direccion está guardada en BX
        
        ;De base mas indice
        
        mov [bx+si],bp            ; Copia el contenido del registro BP a la direccion calculada sumando BX y SI
        
        ;De registro relativo
        
        mov cl, [bx+4]            ; Carga en el registro CL el byte ubicado en la direccion desplazada BX + 4
        
        ;De base relativa mas indice
        
        mov arreglo1[bx+si],al    ; Copia el valor del registro AL en la direccion calculada por la base de arreglo1 + BX + SI
        
        ;De indice escalado
        
        mov [ebx+2*esi],ax        ; Intenta copiar AL en la memoria usando direccionamiento de 32 bits pero no se puede.
        
        ret                       ; Retorna el control al programa principal o sistema operativo que invoca este proceso