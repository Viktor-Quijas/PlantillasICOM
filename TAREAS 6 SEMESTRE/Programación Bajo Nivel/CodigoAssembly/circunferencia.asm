.model small  
	.data   
		seno db 80, 90, 100, 109, 119, 127, 135, 142, 148, 152, 156, 159, 160, 160, 159, 156, 152, 148, 142, 135, 127, 119, 109, 100, 90, 80, 70, 60, 51, 41, 33, 25, 18, 12, 8, 4, 1, 0, 0, 1, 4, 8, 12, 18, 25, 33, 41, 51, 60, 70
        coseno db 160, 159, 157, 154, 150, 145, 138, 131, 123, 114, 105, 95, 85, 75, 65, 55, 46, 37, 29, 22, 15, 10, 6, 3, 1, 0, 1, 3, 6, 10, 15, 22, 29, 37, 46, 55, 65, 75, 85, 95, 105, 114, 123, 131, 138, 145, 150, 154, 157, 159
        n equ 50                  ; Longitud del arreglo
		COLOR  	EQU   0fh 
		tmp dw 0
	.code

main:	
		mov ax,@data
		mov ds,ax 
		mov ah, 0       
		mov al, 13h     
		int 10h         
		mov cx,n 
		mov si,1 
repite:	    	mov tmp,cx     
		mov cl,seno(si-1)
		mov dl,coseno(si-1)        
		mov al,COLOR     
		mov ah,0Ch      
		int 10h             ; AL = Color, BH = Pagina, CX = X, DX = Y
		inc si 
		mov cx,tmp
		loop repite

end main




