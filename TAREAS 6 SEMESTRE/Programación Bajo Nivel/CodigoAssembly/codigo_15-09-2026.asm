        .model small
        .data
            var1 db 0Ah
            arreglo1 db 1,5,10,15,0Bh,0Ch,0Dh,0Fh,0FFh
            
        .code
        mov ax,@data
        mov ds,ax
        mov si, 5
        mov al, arreglo1 [si];AL = 0ch
                 
        ret                                