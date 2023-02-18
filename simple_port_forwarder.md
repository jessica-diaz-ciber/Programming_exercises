## server.py
Ejecutar primero

```python
import socket

import signal, sys
def sigint_handler(signum, frame):
    print(' [*] Saliendo...')
    sys.exit(0)
signal.signal(signal.SIGINT, sigint_handler)


server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
shost = "127.0.0.1"; sport = 2340
print(f" [*] Servidor escuchando en {shost}:{sport}")
server.bind((shost, sport))
server.listen()
while True:
    client_socket, addr = server.accept()
    while True:
        data = client_socket.recv(1024)
        print(f" > Recibido: {data.decode()}")
        if not data:
            break
```
------------------------------------
## port_forwarder.py

Ejecutar segundo
```python
import socket

rhost = "127.0.0.1"; rport = 1225
shost = "127.0.0.1"; sport = 2340
print(f" [*] Proxy esuchando en {rhost}:{rport}")
print(f" [*] Mensaje trasnmitiendose a {shost}:{sport}")

import signal, sys
def sigint_handler(signum, frame):
    print(' [*] Saliendo...')
    sys.exit(0)
signal.signal(signal.SIGINT, sigint_handler)


server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server.bind((rhost, rport))
server.listen(1)
while True:
    client_socket, addr = server.accept()
    remote_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    remote_socket.connect((shost, sport))
    while True:
        data = client_socket.recv(1024)
        print(f"[Data enviada de {rhost}:{rport} a {shost}:{sport}] ->  {data.decode()}")
        remote_socket.sendall(data)
        if not data:
            break
```
-------------------------------
## client.py

Ejecutar el ultimo:
```python
import socket

import signal, sys
def sigint_handler(signum, frame):
    print(' [*] Saliendo...')
    sys.exit(0)
signal.signal(signal.SIGINT, sigint_handler)


rhost = "127.0.0.1"; rport = 1225
print(f" [*] Cliente trasnmitiendo desde {rhost}:{rport}")
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((rhost, rport))
while True:
    message = input(' > Ingrese el mensaje: ')
    if message == "salir":
        break
    else:
        client.sendall(message.encode())

client.close()
```
