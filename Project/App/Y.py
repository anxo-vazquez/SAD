import socket

socket = socket.socket()
host = "localhost"
port = 12345
socket.connect((host, port))

while(1){
message = socket.recv(100000)
print(message.decode())

socket.sendall("Hello Server".encode())
}
