import socket

rhost = "127.0.0.1"; rport = 1225
shost = "127.0.0.1"; sport = 2340
print(f" [*] Proxy esuchando en {rhost}:{rport}")
print(f" [*] Mensaje trasnmitiendose a {shost}:{sport}")

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
        if not data:
            break
        remote_socket.sendall(data)
        data = remote_socket.recv(1024)
        if not data:
            break
        client_socket.sendall(data)

# └─$ nc 127.0.0.1 1224    asdasd
# └─$ sudo nc -nlvp 2334   connect to [127.0.0.1] from (UNKNOWN) [127.0.0.1] 58946 -> asdasd
