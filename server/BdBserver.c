/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <xdo.h>

void error(const char *msg)
{
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[])
{
     bool opened = false;
     int sockfd, newsockfd, portno;
     socklen_t clilen;
     char buffer[256];
     struct sockaddr_in serv_addr, cli_addr;
     int n;
     if (argc < 2) {
         fprintf(stderr,"ERROR, no port provided\n");
         exit(1);
     }
     portno = atoi(argv[1]) - 1;
     xdo_t* x = xdo_new(":0.0");
     while (1) {
	if (!opened) {
		portno++;
		printf("Opening socket on port %i...\n", portno);
		sockfd = socket(AF_INET, SOCK_STREAM, 0);
		if (sockfd < 0) 
		    error("ERROR opening socket");
		bzero((char *) &serv_addr, sizeof(serv_addr));
		serv_addr.sin_family = AF_INET;
		serv_addr.sin_addr.s_addr = INADDR_ANY;
		serv_addr.sin_port = htons(portno);
		if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0)
			error("ERROR on binding");
		listen(sockfd,5);
		clilen = sizeof(cli_addr);
		newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);
		if (newsockfd < 0) 
			error("ERROR on accept");
		opened = true;
		printf("...ueila! (socket opened)\n");
	}
	bzero(buffer,256);
	n = read(newsockfd,buffer,255);
	if (n < 0) error("ERROR reading from socket");
	printf("received string: %s\n", buffer);
	if (strcmp(buffer, "quit") == 0) {
		printf("Closing socket...\n");
		opened = false;
		close(newsockfd);
		close(sockfd);
		//sleep(90);
		printf("socket closed\n");
	} else if (strcmp(buffer, "-999999999") == 0) {
		//break;
		printf("Closing socket...\n");
		opened = false;
		close(newsockfd);
		close(sockfd);
		//sleep(90);
		printf("socket closed\n");
	} else if (strncmp(buffer, "_p", 2) == 0) {
		int xyVal[2];
		char *xyMovements = &buffer[2];
		char *xy;
		xy = strtok (xyMovements, ",");
		int i;
		for (i = 0; i < 2; i++) {
			xyVal[i] = atoi(xy);
			xy = strtok (NULL, ",");
		}
		//printf("mouse %i,%i\n", xyVal[0], xyVal[1]);
		xdo_move_mouse_relative(x, xyVal[0], xyVal[1]);
	} else if (strcmp(buffer, "_l") == 0) {
		xdo_mouse_down(x, CURRENTWINDOW, 1);
		xdo_mouse_up(x, CURRENTWINDOW, 1);
	} else if (strcmp(buffer, "_m") == 0) {
		xdo_mouse_down(x, CURRENTWINDOW, 2);
		xdo_mouse_up(x, CURRENTWINDOW, 2);
	} else if (strcmp(buffer, "_r") == 0) {
		xdo_mouse_down(x, CURRENTWINDOW, 3);
		xdo_mouse_up(x, CURRENTWINDOW, 3);
	} else if (strcmp(buffer, "_u") == 0) {
		xdo_mouse_down(x, CURRENTWINDOW, 4);
		xdo_mouse_up(x, CURRENTWINDOW, 4);
	} else if (strcmp(buffer, "_d") == 0) {
		xdo_mouse_down(x, CURRENTWINDOW, 5);
		xdo_mouse_up(x, CURRENTWINDOW, 5);
	} else {
		xdo_send_keysequence_window(x, CURRENTWINDOW, buffer, 0);
	}
	//n = write(newsockfd,"I got your message",18);
	//if (n < 0) error("ERROR writing to socket");
     }
     return 0; 
}
