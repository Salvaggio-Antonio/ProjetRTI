#include "../Librairies/ServeurClient/protocol.h"
#include "../Librairies/ServeurClient/SocketsUtilities.h"
#include "../Librairies/Functions/FunctionUtilities.h"

void handlerSigint(int sig);
int hSocket; /* handle de la socket */
int ConnexionClient(int s);

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
    adresseSocket.sin_port = htons(PORT); /* conversion port au format réseau */
    adresseSocket.sin_addr.s_addr = inet_addr(ADRESSE);
    printf("Adresse IP = %s\n", ADRESSE);
    /* 3. Tentative de connexion */
    tailleSockaddr_in = sizeof(struct sockaddr_in);
    /*1. Création de la socket + connect*/
    if ((hSocket = SocketClient(PORT, &adresseSocket, tailleSockaddr_in)) == -1)
    {
        exit(1);
    }
    /* 4. Armement sur le signal SIGINT */
    act.sa_handler = handlerSigint;
    sigemptyset(&act.sa_mask);
    act.sa_flags = 0;
    sigaction(SIGINT, &act, 0);

    //La connexion (pseudo , pass)
    if ((ConnexionClient(hSocket)) == -1)
    {
        close(hSocket);
        printf("Socket client fermee\n");
        return 0;
    }
    do
    {   
        //MENU PRINCIPALE      
        strcpy(msgClient, menuPrincipalClient(SEP));
        t = strlen(msgClient);
        printf("taille msgClient = %d\n", t);

        msgClient[t] = '\r';
        msgClient[t + 1] = '\n';
        msgClient[t + 2] = 0;

        if ((nbreEnv = send(hSocket, msgClient, strlen(msgClient) + 1, 0)) == -1) /* pas message urgent */
        {
            printf("Erreur sur le send de la socket %d\n", errno);
            close(hSocket); /* Fermeture de la socket */
            exit(1);
        }
        else
            printf("Send socket OK\n");
        printf("Message envoye = %s\n", msgClient);
        printf("Nbre de bytes envoyes = %d\n", nbreEnv);

        if (strcmp(msgClient, EOC))
        {
            /* 6. Reception de l'ACK du serveur au client */
            if ((nbreRecv = recv(hSocket, msgServeur, LONG_GROS_MSG, 0)) == -1)
            {
                printf("Erreur sur le recv de la socket %d\n", errno);
                close(hSocket); /* Fermeture de la socket */
                exit(1);
            }
            else
                printf("Recv socket OK\n");
            printf("Message recu en ACK = %s\n", msgServeur);
            cpt++;
        }
    } while (strcmp(msgClient, EOC) && strcmp(msgServeur, DOC));
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

int ConnexionClient(int s)
{
    char pseudo[LONG_GROS_MSG], pass[LONG_GROS_MSG], msgClient[LONG_GROS_MSG], msgServeur[LONG_GROS_MSG];
    int t;
    printf("--------------LOGIN-------------\n");
    printf("Pseudo : ");
    scanf("%s", pseudo);
    printf("Password : ");
    scanf("%s", pass);
    sprintf(msgClient, "%s;%s", pseudo, pass);

    t = strlen(msgClient);
    printf("taille msgClient = %d\n", t);
    msgClient[t] = '\r';
    msgClient[t + 1] = '\n';
    msgClient[t + 2] = 0;

    // ENVOIE DU LOGIN AU SERVEUR

    if ((send(s, msgClient, strlen(msgClient) + 1, 0)) == -1) /* pas message urgent */
    {
        printf("Erreur sur le send de la socket %d\n", errno);
        return -1;
    }
    else
        printf("Send socket OK\n");

    // EN ATTENTE DE LA REPONSE DU SERVEUR

    if ((recv(s, msgServeur, LONG_GROS_MSG, 0)) == -1)
    {
        printf("Erreur sur le recv de la socket %d\n", errno);
        return -1;
    }
    else
        printf("Recv socket OK\n");

    if (strcmp(msgServeur, "OK") == 0)
    {
        printf("Vous vous etes connecte\n");
        return 1;
    }
    else
    {
        printf("Login incorrect !\n");
        return -1;
    }
}