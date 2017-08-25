from socket import socket, AF_INET, SOCK_DGRAM, SOL_SOCKET, SO_BROADCAST
from netifaces import ifaddresses
import json
from xdo import Xdo
from time import sleep

ID_STOP = -1;
ID_SETUP = 0;
ID_START = 1;
ID_KEY = 2;
ID_MOUSE_MOVE = 3;
ID_MOUSE_CLICK = 4;

MOUSE_BUTTON_LEFT = 1;
MOUSE_BUTTON_MIDDLE = 2;
MOUSE_BUTTON_RIGHT = 3;
MOUSE_BUTTON_WHEEL_UP = 4;
MOUSE_BUTTON_WHEEL_DOWN = 5;

UDP_PORT_SEND = 1846;
UDP_PORT_RECV = 1847;
UDP_BUFFER_SIZE = 1024;

print 'UEILA: starting up'

net_info = ifaddresses('wlp5s0')
udp_ip = net_info[2][0]['addr']
broadcast_ip = net_info[2][0]['broadcast']

sock_send = socket(AF_INET,  # Internet
    SOCK_DGRAM)  # UDP
sock_recv = socket(AF_INET,  # Internet
    SOCK_DGRAM)  # UDP
sock_recv.bind(('', UDP_PORT_RECV))
# sock_recv.setblocking(0)
sock_recv.settimeout(1)

msg_recv = {
        'id': []
    }
msg_send = {
    'id': ID_SETUP,
    'ip': udp_ip
}
msg_send_string = json.dumps(msg_send)

print 'Waiting for setting up communication...'

while msg_recv['id'] != ID_START:
    sock_send.setsockopt(SOL_SOCKET, SO_BROADCAST, 1)
    sock_send.sendto(msg_send_string, (broadcast_ip, UDP_PORT_SEND))
    # print 'send raw: ', msg_send_string
    # sleep(1)  # not needed, there is a 1s timeout on the socket for receiving
    try:
        msg_recv_string, addr = sock_recv.recvfrom(UDP_BUFFER_SIZE)
        msg_recv = json.loads(msg_recv_string)
        # print 'recv ', msg_recv_string
    except Exception as e:
        # print e
        pass

print '...done'

xdo = Xdo()

while msg_recv['id'] != ID_STOP:
    msg_recv_string = ''
    msg_recv = {
        'id': []
    }
    try:
        msg_recv_string, addr = sock_recv.recvfrom(UDP_BUFFER_SIZE)
        msg_recv = json.loads(msg_recv_string)
    except Exception as e:
        # print e
        pass
    print 'recv raw: ', msg_recv_string
    
    if 'x' in msg_recv:
        xdo.move_mouse_relative(msg_recv["x"],msg_recv["y"])

print 'UEILA: shutting down'
