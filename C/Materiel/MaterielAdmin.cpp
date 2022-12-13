#include "../Librairies/ServeurClient/protocol.h"
#include "../Librairies/ServeurClient/SocketsUtilities.h"


void handlerSigint(int sig);
int hSocket; /* handle de la socket */

int main()
{

    struct hostent *infosHost; /* pour gethostbyname */
    struct in_addr adresseIP;  /* Adresse Internet au format reseau */
    struct sockaddr_in adresseSocket;
    /* Structure de type sockaddr contenant les infos adresses - ici, cas de TCP */
    unsigned int tailleSockaddr_in;
    int ret; /* valeur de retour */
    char msgClient[LONG_GROS_MSG];
    char msgServeur[LONG_GROS_MSG], buf[LONG_GROS_MSG];
    int nbreEnv, nbreRecv;
    int t, cpt = 0;
    struct sigaction act;

    /* 2. Préparation de la structure sockaddr_in */
    memset(&adresseSocket, 0, sizeof(struct sockaddr_in));
    adresseSocket.sin_family = AF_INET;   /* Domaine */
    adresseSocket.sin_port = htons(PORTADMIN); /* conversion port au format réseau */
    adresseSocket.sin_addr.s_addr = inet_addr(ADRESSE);
    printf("Adresse IP = %s\n", ADRESSE);
    tailleSockaddr_in = sizeof(struct sockaddr_in);

    if((hSocket =SocketClient( PORTADMIN, &adresseSocket , tailleSockaddr_in))==-1)
    {
        exit(1);
    }

    /* 4. Armement sur le signal SIGINT */
    act.sa_handler = handlerSigint;
    sigemptyset(&act.sa_mask);
    act.sa_flags = 0;
    sigaction(SIGINT, &act, 0);

    /* 5.Envoi d'un message client */
    printf("-----------------MENU ADMIN----------------------\n ");
    printf("\t1) DECONNEXION DU SERVEUR\n");
    printf("\t2) QUITTER\n");
    do
    {
        printf("REPONSE : ");
        scanf("%s",buf);
        strcpy(msgClient, buf);
        t = strlen(msgClient);
        buf[t]=0;
    } while (strcmp(msgClient,"1")!=0 && strcmp(msgClient,"2")!=0);
    
    

    if ((nbreEnv = send(hSocket, msgClient, strlen(msgClient) + 1, 0)) == -1) /* pas message urgent */
    {
        printf("Erreur sur le send de la socket %d\n", errno);
        close(hSocket); /* Fermeture de la socket */
        exit(1);
    }
    else
        printf("Send socket OK\n");


    close(hSocket);
    printf("Socket client fermee\n");
    printf("%d messages envoyes !", cpt);

    return 0;
}
void handlerSigint(int sig)
{
    printf("Socket client fermee\n");
    close(hSocket);
    exit(0);
}