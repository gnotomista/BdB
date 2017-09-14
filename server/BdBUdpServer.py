from BdBUdpConstants import *

from socket import socket, AF_INET, SOCK_DGRAM, SOL_SOCKET, SO_BROADCAST, SO_REUSEADDR, SHUT_RDWR
from netifaces import ifaddresses
import json
from xdo import Xdo
from time import sleep
from ctypes import c_ulong

CURRENTWINDOW = 0

class BdBUdpServer:

    def __init__(self, interface, port_out=1846, port_in=1847):
        self.state = STATE_LINKING

        self.UDP_PORT_SEND = port_out
        self.UDP_PORT_RECV = port_in
        self.UDP_BUFFER_SIZE = 1024

        net_info = ifaddresses(interface)
        self.udp_ip = net_info[2][0]['addr']
        self.broadcast_ip = net_info[2][0]['broadcast']

        self.sock_send = socket(AF_INET,  # Internet
            SOCK_DGRAM)  # UDP
        self.sock_recv = socket(AF_INET,  # Internet
            SOCK_DGRAM)  # UDP

        self.sock_recv.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        self.sock_recv.bind(('', self.UDP_PORT_RECV))
        self.sock_recv.settimeout(1)

        self.xdo = Xdo()

    def initialize_link(self):

        msg_recv = {
            'id': []
        }
        msg_send = {
            'id': ID_SETUP,
            'ip': self.udp_ip
        }
        msg_send_string = json.dumps(msg_send)

        print 'Waiting for setting up link...'

        while True:
            self.sock_send.setsockopt(SOL_SOCKET, SO_BROADCAST, 1)
            self.sock_send.sendto(msg_send_string, (self.broadcast_ip, self.UDP_PORT_SEND))
            # print 'send raw: ', msg_send_string
            # sleep(1)  # not needed, there is a 1s timeout on the socket for receiving
            try:
                msg_recv_string, addr = self.sock_recv.recvfrom(self.UDP_BUFFER_SIZE)
                msg_recv = json.loads(msg_recv_string)
                # print 'recv ', msg_recv_string
            except Exception as e:
                # print e
                pass
            if 'id' in msg_recv:
                if msg_recv['id'] == ID_START:
                    self.state = STATE_RUNNING
                    break

        print '...done'

    def receive_and_process_command(self):
        msg_recv_string = ''
        msg_recv = {
            'id': []
        }

        try:
            msg_recv_string, addr = self.sock_recv.recvfrom(self.UDP_BUFFER_SIZE)
            msg_recv = json.loads(msg_recv_string)
        except Exception as e:
            pass

        if 'id' in msg_recv:
            if msg_recv['id'] == ID_STOP:
                self.state = STATE_LINKING
                print 'Link closed'
            else:
                self.process_command(msg_recv)

    def process_command(self, msg_recv):
        if msg_recv['id'] == ID_KEY:
            self.xdo.send_keysequence_window(CURRENTWINDOW, msg_recv['key'])
        elif msg_recv['id'] == ID_MOUSE_MOVE:
            self.xdo.move_mouse_relative(msg_recv['x'], msg_recv['y'])
        elif msg_recv['id'] == ID_MOUSE_CLICK:
            self.xdo.mouse_down(CURRENTWINDOW, msg_recv['button'])
            self.xdo.mouse_up(CURRENTWINDOW, msg_recv['button'])

    def run(self):
        while True:
            if self.state == STATE_LINKING:
                self.initialize_link()
            elif self.state == STATE_RUNNING:
                self.receive_and_process_command()
