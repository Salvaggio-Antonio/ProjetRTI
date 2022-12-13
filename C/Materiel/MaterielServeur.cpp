#include "../Librairies/ServeurClient/protocol.h"
#include "../Librairies/ServeurClient/SocketsUtilities.h"
#include "../Librairies/Functions/FunctionUtilities.h"


// MUTEX
pthread_mutex_t mutexIndiceCourant;
// VARIABLE DE CONDITION
pthread_cond_t condIndiceCourant;
// threads
pthread_t threadHandle[NBRE_MAX_CLIENTS]; /* Threads pour clients*/
pthread_t threandeHandleAdmin;

// fonctions threads
void *fctThreadAdmin();
void *fctThread(int param);

// fonctions
int Connexion(int);

int hSocketEcoute, /* Handle de la socket d'écoute */
    hSocketDupliquee,
    hSocketAdmin,                     /* Handle de la socket de service connectee au client */
    hSocketService[NBRE_MAX_CLIENTS]; /* Handle de la socket de service connectee au client */
int cpt = 0;                          /* Compteur de clients acceptés */
int indiceCourant = -1;

int main()
{
    int posLibre, i, j, ret;
    struct hostent *infosHost; /*Infos sur le host : pour gethostbyname */
    struct in_addr adresseIP;  /* Adresse Internet au format reseau */
    struct sockaddr_in adresseSocket;
    /* Structure de type sockaddr contenant les infos adresses - ici, cas de TCP */
    socklen_t tailleSockaddr_in = sizeof(struct sockaddr_in);
    char buf[LONG_GROS_MSG], msgServeur[LONG_GROS_MSG];

    /*initialisations */
    puts("* Thread principal serveur demarre *");
    printf("identite = %d.%u\n", getpid(), pthread_self());

    pthread_mutex_init(&mutexIndiceCourant, NULL);
    pthread_cond_init(&condIndiceCourant, NULL);

    /* Si la socket n'est pas utilisee, le descripteur est a -1 */
    for (i = 0; i < NBRE_MAX_CLIENTS; i++)
        hSocketService[i] = -1;

    /* 2. Acquisition des informations sur l'ordinateur local */
    if ((infosHost = gethostbyname("zeus")) == 0)
    {
        printf("Erreur d'acquisition d'infos sur le host %d\n", errno);
        exit(1);
    }
    else
        printf("Acquisition infos host OK\n");

    /* 3. Préparation de la structure sockaddr_in */
    memset(&adresseSocket, 0, sizeof(struct sockaddr_in));
    adresseSocket.sin_family = AF_INET; /* Domaine */
    adresseSocket.sin_port = htons(PORT);
    /* conversion numéro de port au format réseau _*/
    memcpy(&adresseSocket.sin_addr, infosHost->h_addr, infosHost->h_length);
    

    // Socket + Bind
    if ((hSocketEcoute = SocketServeur(PORT, &adresseSocket)) == -1)
        exit(1);

    printf("Thread secondaire Admin se lance !\n");
    ret = pthread_create(&threandeHandleAdmin, NULL, (void *(*)(void *))fctThreadAdmin, NULL);
    ret = pthread_detach(threandeHandleAdmin);

    /* 6. Lancement des threads */
    for (i = 0; i < NBRE_MAX_CLIENTS; i++)
    {
        ret = pthread_create(&threadHandle[i], NULL, (void *(*)(void *))fctThread, (void *)i);
        printf("Thread secondaire %d lance !\n", i);
        ret = pthread_detach(threadHandle[i]);
    }

    do
    {
        /* 5. Mise a l'ecoute d'une requete de connexion */
        puts("Thread principal : en attente d'une connexion");

        if ((hSocketDupliquee = Accept(hSocketEcoute, &adresseSocket, &tailleSockaddr_in)) == -1)
        {
            close(hSocketEcoute);
            exit(1);
        }

        printf("Recherche d'une socket connecteee libre ...\n");
        for (j = 0; j < NBRE_MAX_CLIENTS && hSocketService[j] != -1; j++)
            ;

        if (j == NBRE_MAX_CLIENTS)
        {
            printf("Plus de connexion disponible\n");
            sprintf(msgServeur, DOC);
            if ((Send(hSocketDupliquee, msgServeur, MAXSTRING)) == -1)
            {
                close(hSocketDupliquee);
                exit(1);
            }
            else
                printf("Send socket refusee OK");
            close(hSocketDupliquee); /* Fermeture de la socket */
        }
        else
        {
            /* Il y a une connexion de libre */
            printf("Connexion sur la socket num. %d\n", j);
            pthread_mutex_lock(&mutexIndiceCourant);
            hSocketService[j] = hSocketDupliquee;
            indiceCourant = j;
            pthread_mutex_unlock(&mutexIndiceCourant);
            pthread_cond_signal(&condIndiceCourant);
        }
    } while (1);

    /* 11. Fermeture des sockets */
    close(hSocketEcoute); /* Fermeture de la socket */
    printf("Socket serveur fermee\n");
    puts("Fin du thread principal");
    return 0;
}

void *fctThread(int param)
{
    char *nomCli, buff[LONG_GROS_MSG], *buf = (char *)malloc(100);
    int vr = param, finDialogue = 0, iCliTraite, hSocketServ;
    int temps, retRecv;
    int num = vr;
    printf("vr : %d\n", num);
    char msgServeur[LONG_GROS_MSG], msgClient[LONG_GROS_MSG];
    int i;
    int tailleMsgRecu, nbreRecv;
    int finDetectee;
    char *m;
    bool finConnexion = false;
    socklen_t tailleO = sizeof(struct sockaddr_in);

    while (1)
    {
        /* 1. Attente d'un client à traiter */
        pthread_mutex_lock(&mutexIndiceCourant);
        while (indiceCourant == -1)
            pthread_cond_wait(&condIndiceCourant, &mutexIndiceCourant);
        iCliTraite = indiceCourant;
        indiceCourant = -1;
        hSocketServ = hSocketService[iCliTraite];
        pthread_mutex_unlock(&mutexIndiceCourant);
        sprintf(buf, "Je m'occupe du numero %d ...\n", iCliTraite);
        printf("TID : %d\n", pthread_self());
        finDialogue = 0;

        //verif connexion 
        if((Connexion(hSocketServ))==-1)
        {
            finDialogue = -1;
        }
        /* 2. Dialogue thread-client */
        

        do
        {
            if ((m = Receive(hSocketServ, LONG_GROS_MSG, MAXSTRING, &retRecv)) == NULL)
            {
                close(hSocketServ);
            }

            if (retRecv == 0)
            {
                sprintf(buf, "Le client est parti !!!");
                printf("TID : %d\n", pthread_self());
                finDialogue = 1;
                break;
            }
            if (strcmp(m, EOC) == 0)
            {
                finDialogue = 1;
                free(m);
                m = NULL;
                break;
            }
            strcpy(msgClient, m);
            free(m);
            m = NULL;
            tailleMsgRecu = strlen(msgClient);
            msgClient[tailleMsgRecu - 2] = 0; /* zero de fin de chaine */
            printf("Recv socket OK\n");
            printf("requete du client : %s\n",msgClient);

            pthread_mutex_lock(&mutexIndiceCourant);
            strcpy(msgServeur,HandlingClientRequest(msgClient,SEP));
            pthread_mutex_unlock(&mutexIndiceCourant);
            
            printf("%s\n", msgServeur);
            if ((Send(hSocketServ, msgServeur, LONG_GROS_MSG)) == -1)
            {
                close(hSocketServ);
            }
            else
            {
                sprintf(buf, "Send socket connectee OK\n");
                printf("TID : %d\n", pthread_self());
            }
        } while (!finDialogue);
        /* 3. Fin de traitement */
        pthread_mutex_lock(&mutexIndiceCourant);
        hSocketService[iCliTraite] = -1;
        pthread_mutex_unlock(&mutexIndiceCourant);
        printf("le cli a quitté et j'attend %d\n", pthread_self());
    }
    close(hSocketServ);

    return 0;
}
void *fctThreadAdmin()
{
    char buff[MAXSTRING];
    struct hostent *infosHost; /*Infos sur le host : pour gethostbyname */
    struct in_addr adresseIP;  /* Adresse Internet au format reseau */
    socklen_t tailleSockaddr_in = sizeof(struct sockaddr_in);
    struct sockaddr_in adresseSocket;
    int hSocketAdminDup;

    /* 2. Acquisition des informations sur l'ordinateur local */
    if ((infosHost = gethostbyname("zeus")) == 0)
    {
        printf("Erreur d'acquisition d'infos sur le host %d\n", errno);
        exit(1);
    }
    else
        printf("Acquisition infos host OK\n");

    /* 3. Préparation de la structure sockaddr_in */
    memset(&adresseSocket, 0, sizeof(struct sockaddr_in));
    adresseSocket.sin_family = AF_INET; /* Domaine */
    adresseSocket.sin_port = htons(PORTADMIN);
    /* conversion numéro de port au format réseau _*/
    memcpy(&adresseSocket.sin_addr, infosHost->h_addr, infosHost->h_length);

    if ((hSocketAdmin = SocketServeur(PORT, &adresseSocket)) == -1)
    {
        exit(1);
    }
    do
    {
        if ((hSocketAdminDup = Accept(hSocketAdmin, &adresseSocket, &tailleSockaddr_in)) == -1)
        {
            exit(1);
        }

        if ((recv(hSocketAdminDup, buff, MAXSTRING, 0)) == -1) /* pas message urgent */
        {
            printf("Erreur sur le recv de la socket %d\n", errno);
            close(hSocketAdminDup); /* Fermeture de la socket */
        }
        if (strcmp(buff, "1") == 0)
        {
            printf("LE SERVEUR SE FERME !\n");

            pthread_mutex_lock(&mutexIndiceCourant);
            for (int j = 0; j < NBRE_MAX_CLIENTS; j++)
            {
                if (hSocketService[j] != -1)
                {
                    printf("fermeture de la socket : %d\n", hSocketService[j]);
                    sprintf(buff, DOC);
                    if (send(hSocketService[j], buff, MAXSTRING, 0) == -1)
                    {
                        printf("Erreur sur le send de refus%d\n", errno);
                        close(hSocketService[j]); /* Fermeture de la socket */
                        exit(1);
                    }

                    close(hSocketService[j]);
                }
            }
            printf("fermeture de la socket ADMIN \n");
            close(hSocketAdmin);
            printf("fermeture de la socket principale\n");
            close(hSocketEcoute);
            pthread_mutex_unlock(&mutexIndiceCourant);
            exit(0);
        }

    } while (true);
}

int Connexion(int s)
{
    char *m;

    int retRecv, tailleMsgRecu, login;
    char msgClient[LONG_GROS_MSG], msgServeur[LONG_GROS_MSG];

    if ((m = Receive(s, LONG_GROS_MSG, MAXSTRING, &retRecv)) == NULL)
    {
        return -1;
    }

    if (retRecv == 0)
    {
        return -1;
    }
    if (strcmp(m, EOC) == 0)
    {
        return -1;
    }
    strcpy(msgClient, m);
    tailleMsgRecu = strlen(msgClient);
    msgClient[tailleMsgRecu - 2] = 0; /* zero de fin de chaine */
    printf("Recv socket OK\n");
    printf("%s\n", msgClient);
    char nomFichier[MAXSTRING];
    strcpy(nomFichier,"./Ressources/compteClient.csv");
    pthread_mutex_lock(&mutexIndiceCourant);
    if ((checkConnexion(nomFichier,msgClient, SEP)) == -1)
    {
        sprintf(msgServeur, "KO");
        printf("Connexion refusé !\n");
        login = -1;
    }
    else
    {
        sprintf(msgServeur, "OK");
        printf("Connexion Accepter !\n");
        login = 1;
    }
    pthread_mutex_unlock(&mutexIndiceCourant);

    if ((Send(s, msgServeur, LONG_GROS_MSG)) == -1)
    {
        return -1;
    }
    return login;
}