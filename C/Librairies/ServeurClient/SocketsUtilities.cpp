#include "SocketsUtilities.h"

int SocketServeur(int port, struct sockaddr_in *adresse)
{
    int hSocketServ;

    hSocketServ = socket(AF_INET, SOCK_STREAM, 0);
    if (hSocketServ == -1)
    {
        printf("Erreur de creation de la socket %d\n", errno);
        return -1;
    }
    else
        printf("Creation de la socket OK\n");

    if (bind(hSocketServ, (struct sockaddr *)adresse, sizeof(struct sockaddr_in)) == -1)
    {
        printf("Erreur sur le bind de la socket %d\n", errno);
        return -1;
    }
    else
        printf("Bind adresse et port socket OK\n");

    return hSocketServ;
}

int SocketClient(int port, struct sockaddr_in *adresse, socklen_t taille)
{
    int hSocketClient, ret;

    hSocketClient = socket(AF_INET, SOCK_STREAM, 0);
    if (hSocketClient == -1)
    {
        printf("Erreur de creation de la socket %d\n", errno);
        return -1;
    }
    else
        printf("Creation de la socket OK\n");
    printf("ADRESSE : %s", ADRESSE);
    if ((ret = connect(hSocketClient, (struct sockaddr *)adresse, taille)) == -1)
    {
        // VERIFIER QUEL TYPE D'ERREUR GRACE AU ERRNO
        printf("Erreur sur connect de la socket %d\n", errno);
        switch (errno)
        {
        case EBADF:
            printf("EBADF - hSocketEcouten'existe pas\n");
            break;
        case ENOTSOCK:
            printf("ENOTSOCK - hSocketEcouteidentifie un fichier\n");
            break;
        case EAFNOSUPPORT:
            printf("EAFNOTSUPPORT - adresse ne correspond pas famille\n");
            break;
        case EISCONN:
            printf("EISCONN - socket deja connectee\n");
            break;
        case ECONNREFUSED:
            printf("ECONNREFUSED - connexion refusee par le serveur\n");
            break;
        case ETIMEDOUT:
            printf("ETIMEDOUT - time out sur connexion \n");
            break;
        case ENETUNREACH:
            printf("ENETUNREACH - cible hors d'atteinte\n");
            break;
        case EINTR:
            printf("EINTR - interruption par signal\n");
            break;
        default:
            printf("Erreur inconnue ?\n");
        }
        
        return -1;
    }
    else
        printf("Connect socket OK\n");

    return hSocketClient;
}

int Accept(int s, struct sockaddr_in *adresse, socklen_t *taille)
{
    puts("En attente d'une connexion");
    int hSocketDup;

    if (listen(s, SOMAXCONN) == -1)
    {
        printf("Erreur sur lel isten de la socket %d\n", errno);
        return -1;
    }
    else
        printf("Listen socket OK\n");

    /* 6. Acceptation d'une connexion */

    if ((hSocketDup = accept(s, (struct sockaddr *)adresse, taille)) == -1)
    {
        printf("Erreur sur l'accept de la socket %d\n", errno);
        return -1;
    }
    else
        printf("Accept socket OK\n");

    return hSocketDup;
}

int Send(int s, char *data, int n)
{
    int nb;
    if (nb = send(s, data, n, 0) == -1)
    {
        switch (errno)
        {
        case EBADF:
            printf("<Erreur> BAD FILE DESCRIPTOR\n");
            break;
        case ENOTSOCK:
            printf("<Erreur> SOCKET OPERATION ON NON SOCKET\n");
            break;
        case EINTR:
            printf("<Erreur> EINTR\n");
            break;
        }
        return -1;
    }
    else
    {
        printf("<OK> Le message a bien ete envoye\n");
        return nb;
    }
}

char* Receive(int s, int taille, int tailleMax , int* nb)
{
    int tailleMsgRecu, finDetectee, nbreRecv;
    char buff[taille], msg[taille], *m;

    tailleMsgRecu = 0;
    finDetectee = 0;
    memset(buff, 0, sizeof(buff));

    /* 7.Reception d'un message client */
    do
    {

        puts("Passage boucle de reception");
        if ((nbreRecv = recv(s, buff, tailleMax, 0)) == -1) /* pas message urgent */
        {
            printf("Erreur sur le recv de la socket %d\n", errno);
            return NULL;
        }
        else
        {
            finDetectee = marqueurRecu(buff, nbreRecv);
            memcpy((char *)msg + tailleMsgRecu, buff, nbreRecv);
            tailleMsgRecu += nbreRecv;
            printf("finDetecteee = %d\n", finDetectee);
            printf("Nombre de bytes recus = %d\n", nbreRecv);
            printf("Taile totale msg recu = %d\n", tailleMsgRecu);
        }

    } while (nbreRecv != 0 && nbreRecv != -1 && !finDetectee);
    
    *nb = nbreRecv;
    msg[strlen(msg)]=0;
    m = (char*) malloc(strlen(msg)+1);
    strcpy(m , msg);

    return m;
}

char marqueurRecu(char *m, int nc)
/* Recherche de la sequence \r\n */
{
    static char demiTrouve = 0;
    int i;
    char trouve = 0; 
    if (demiTrouve == 1 && m[0] == '\n')
        return 1;
    else
        demiTrouve = 0;

    for (i = 0; i < nc - 1 && !trouve; i++)
        if (m[i] == '\r' && m[i + 1] == '\n')
            trouve = 1;

    if (trouve)
        return 1;
    else if (m[nc] == '\r')
    {
        demiTrouve = 1;
        return 0;
    }
    else
        return 0;
}
