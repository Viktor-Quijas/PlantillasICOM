        .MODEL small
        .STACK
        .DATA
        
        jmp inicio:
        
        n1 db ?
        n2 db ?
        n3 db ?
        
        uni1 db ?
        dece1 db ?
        cent1 db ?
        uni2 db ?
        dece2 db ?
        cent2 db ?
        
        unir db ?
        decer db ?
        centr db ?
        milr db ?
        
        carry db ?
        
        msg1 db 'Ingrese el primer operando: ','$'
        msg2 db 0Dh, 0Ah,'Ingrese el segundo operando: ','$'
        msg3 db 0Dh, 0Ah,'Resultado: ','$'
        
        .CODE
        
inicio:

        mov ax, @data
        mov ds, ax
                
; Captura del primer numero
        lea dx,msg1
        mov ah,09h
        int 21h        
                
        mov ah,01h
        int 21h
        
        sub al,30h
        mov cent1,al
        
        int 21h
        
        sub al,30h
        mov dece1,al
        
        int 21h
        
        sub al,30h
        mov uni1,al
        
; Captura del segundo numero
        lea dx,msg2
        mov ah,09h
        int 21h        
                
        mov ah,01h
        int 21h
        
        sub al,30h
        mov cent2,al
        
        int 21h
        
        sub al,30h
        mov dece2,al
        
        int 21h
        
        sub al,30h
        mov uni2,al
            
; Suma de dos numeros

        mov al,uni1
        add al,uni2        
        aam 
        mov unir,al
        mov carry,ah
        
        mov al,dece1
        add al,carry
        add al,dece2
        aam
        mov decer,al
        mov carry,ah
        
        mov al,cent1
        add al,carry
        add al,cent2
        aam
        mov centr,al
        mov carry,ah
        
        mov al,carry
        mov milr,al
        
;Imprimir numero de 4 digitos        
        mov ah,09h
        lea dx,msg3
        int 21h
        
        mov ah,02h
        mov dl,milr
        add dl,30h
        int 21h
        
        mov dl,centr
        add dl,30h
        int 21h
        
        mov dl,decer
        add dl,30h
        int 21h
        
        mov dl,unir
        add dl,30h
        int 21h
                                 
        ret