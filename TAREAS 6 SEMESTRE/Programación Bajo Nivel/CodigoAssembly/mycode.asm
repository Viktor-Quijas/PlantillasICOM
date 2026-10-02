.model small  
        .data  
            var1 db 0Ah  
;desplazamiento 0        
            arreglo1 db 1,5,10,15,0Bh,0Ch,0Dh,0Fh,0FFh
;desplazamiento     1 2  3  4  5   6   7   8    9

        .code
        mov ax,@data
        mov ds,ax
        
        ;Modos de direccionamiento
        
        ;
        
         
        mov bx,2     
        mov al,[bx] ; AL = 5 
        
        ret