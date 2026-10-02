org 100h        ; Indica al ensamblador que el codigo se cargara
                ; en la direccion 100h.

jmp inicio      ; Salta directo a donde se encuentra la etiqueta inicio.

msg0: db "Implementare un sumador de 2 numeros",0Dh,0Ah
      db "cada numero es de 1 solo digito",0Dh,0Ah,24h
                ; "db" Define Byte: reserva y almacena informacion en la memoria pero en
                ; un solo byte.               
                ; Define el mensaje de bienvenida.
                ; 0Dh retorno de carro y 0Ah salto de linea.
                ; 24h es el caracter '$' y se necesita para indicar el fin 
                ; de cadena a la interrupcion 21h/AH=9h.

msg1: db "Dame el 1er numero: ",24h ; Solicita el primer numero.                
msg2: db "Dame el 2do numero: ",24h ; Solicita el segundo numero.               
msg3: db "El resultado es: ",24h    ; Mensaje para dar el resultado.               
salto: db 0Dh,0Ah,24h               ; Cadena auxiliar para hacer un salto de linea en pantalla.
var1 db ?       ; Reserva 1 byte de memoria no inicializado para guardar el primer operando.
var2 db ?       ; el segundo operando y el resultado.
var3 db ?       ; 

inicio:         ; A esta etiqueta salta el programa.

; Imprimir mensaje de bienvenida.
mov ah,9h       ; Carga la funcion 09h en AH que viene siendo
                ; una interrupcion para mostrar una cadena terminada en '$'.
mov dx,msg0     ; Pone la direccion de 'msg0' en el registro DX.
int 21h         ; Llama a la interrupcion del sistema DOS para imprimir la cadena.

; Imprimir peticion del primer numero.
mov ah,9h       ; Repite el proceso de imprimir las cadenas.
mov dx,msg1     ; 
int 21h         ; 

; Leer el primer caracter desde el teclado
mov ah,1h       ; Carga la funcion 01h en AH que es la lectura de un carcter con eco en pantalla.
int 21h         ; Espera la tecla del usuario y lo guarda en el registro AL en codigo ASCII.
sub al,30h      ; Resta 30h (48 decimal) al codigo ASCII para convertirlo 
                ; a su valor numérico real.
mov var1,al     ; Guarda el valor numerico obtenido en la variable 'var1'.

; Imprimir salto de linea
mov ah,9h       ; Carga la instruccion de salto de linea en el acumulador.
mov dx,salto    ; Pone la direccion de 'salto' en DX.
int 21h         ; Ejecuta la interrupcion para bajar el cursor a una nueva línea.

; -- Repite el proceso de imprimir y pedir datos --
mov ah,9h       ; 
mov dx,msg2     ; 
int 21h         ;

mov ah,1h       ; 
int 21h         ; 
sub al,30h      ; 
mov var2,al     ; 

mov al,var1     ; 
mov bl,var2     ; 
add al,bl       ; 
mov var3,al     ; 

mov ah,9h       ; 
mov dx,salto    ; 
int 21h         ; 

; Imprimir mensaje de resultado
mov ah,9h       ; Carga la instruccion de imprimir cadena en el acumulador.
mov dx,msg3     ; Pone la direccion de 'msg3' en DX.
int 21h         ; Llama a la interrupcion para imprimir "El resultado es: ".

; Mostrar el digito resultante en pantalla
mov ah,2h       ; Carga la funcion 02h en el acumulador 
                ; interrupcion para imprimir un solo caracter desde DL.
mov dl,var3     ; Copia el resultado numerico guardado en 'var3' al registro DL.
add dl,30h      ; Le suma 30h (48 decimal) para reconvertir el numero a su caracter ASCII correspondiente.
int 21h         ; Llama a la interrupcion para mostrar el caracter del resultado en pantalla.

ret             ; Finaliza el programa y regresa el control al sistema operativo al DOS.