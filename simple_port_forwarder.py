import socket

rhost = "127.0.0.1"; rport = 1224
shost = "127.0.0.1"; sport = 2335

print(f" [*] Proxy esuchando en {rhost}:{rport}")
print(f" [*] Mensaje trasnmitido a {shost}:{sport}")

sender = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sender.connect((shost, sport))
reciever = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
reciever.bind((rhost, rport))
reciever.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
reciever.listen(5)
conn, addr = reciever.accept()
message = conn.recv(1024)
sender.send(message)
sender.close()

# └─$ nc 127.0.0.1 1224    asdasd
# └─$ sudo nc -nlvp 2334   connect to [127.0.0.1] from (UNKNOWN) [127.0.0.1] 58946 -> asdasd
