; hello_world.asm
global _start

section .text:
; Seccion del codigo
; Utilizamos syscalls que son insutrcciones a nivel de kernel (SO) cada syscall equivale a un numero:
; locate unistd_32.h -> cat /usr/include/x86_64-linux-gnu/asm/unistd_32.h | grep "wirte" -> #define __NR_write 4
_start:
	; write(destino(fd), mensaje(varaible), tamaño) -> syscall 0x4(ebx, ecx, edx);
	mov eax, 0x4  ; #define __NR_write 4
	mov ebx, 1    ; stdout al descriptor de fichero 1 (stdout) -> es decir manda el resultado a la pantalla
	mov ecx, mensaje 		  ; usa la varaible "mensaje" como buffer
	mov edx, tamanyo_mensaje   ; el tamaño del buffer
	int 0x80    			  ;	ejecutar el syscall				
	
	mov eax, 0x1 ; exit
	mov ebx, 0   ; exit 0 -> exito
	int 0x80	 ; ejecutar el syscall   

section .data:
; variables  -> nombre : valor
; db -> define bytes -> string en bytes. 
; 0xA == "\n" salto de linea
	mensaje : db "Hello World!", 0xA
	tamanyo_mensaje equ $-mensaje

; nasm -f elf32 -o hello_world.o Helloworld.asm
; ld -m elf_i386 -o hello_world hello_world.o
;  ./hello_world   -> Hello World!
