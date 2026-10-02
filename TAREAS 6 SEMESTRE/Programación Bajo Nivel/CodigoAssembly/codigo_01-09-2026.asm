#fasm#

org 100h
    jmp inicio
    
    msg1  db "Dame el 1er numero: ", 24h
    msg2  db "Dame el 2do numero: ", 24h        
    msg3  db "El resultado es: ", 24h
    salto db 0Dh, 0Ah, 24h
    
    var1  db ?
    var2  db ? 
    var3  db ?

inicio:    
    ; --- Primer número ---
    mov dx, msg1
    mov ah, 9h
    int 21h
    
    mov ah, 1h
    int 21h      
    sub al, 30h
    mov var1, al
    
    ; --- Salto ---
    mov dx, salto
    mov ah, 9h
    int 21h
    
    ; --- Segundo número ---
    mov dx, msg2
    mov ah, 9h
    int 21h
    
    mov ah, 1h
    int 21h  
    sub al, 30h
    mov var2, al
    
    ; --- Operación ---
    mov al, var1
    add al, var2
    mov var3, al  
    
    ; --- Salto ---
    mov dx, salto
    mov ah, 9h
    int 21h
    
    ; --- Mensaje Resultado ---
    mov dx, msg3
    mov ah, 9h
    int 21h
    
    ; --- Mostrar Resultado ---
    mov dl, var3
    add dl, 30h
    mov ah, 2h
    int 21h        

ret