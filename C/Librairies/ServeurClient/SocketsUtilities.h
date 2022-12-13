#include "protocol.h"


int SocketServeur (int , struct sockaddr_in *);//socket bind
int SocketClient( int port, struct sockaddr_in * adresse , socklen_t taille);//socket connect
int Accept (int s, struct sockaddr_in * adresse , socklen_t* taille );//listen accept
int Send (int s, char* data, int n );
char* Receive (int s, int taille, int tailleMax , int *nb);
char marqueurRecu(char *m, int nc);



 