        
        org 100h     
        
        #start=led_display.exe#
        #make_bin#
        name "led" 
        
        jmp inicio
        cociente db ?
        resto db ?
        
        
inicio: mov ax,-9   ; ax = 9 
        mov cl,2   ; bl = 2
        iDIV cl     ; al = cociente, ah = resto; AX / cl
        mov cociente, al
        mov resto, ah
        xor ax,ax
        mov al, cociente
        cbw
        
        out 199, ax
        xor ax, ax
        mov al,resto
        out 199, ax
        
        

ret




