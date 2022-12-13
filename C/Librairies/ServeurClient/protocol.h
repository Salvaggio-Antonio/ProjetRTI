#include <stdio.h>
#include <stdlib.h> /* pour exit */
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h> /* pour les types de socket */
#include <netdb.h> /* pour la structure hostent */
#include <errno.h>
#include <string.h>
#include<stdbool.h>
#include<cstdint>  
#include<signal.h>
#include <pthread.h>
#include <time.h> /* pour select et timeval */
#include <netinet/in.h> /* pour la conversion adresse reseau->format dot
 ainsi que le conversion format local/format reseau */
#include <netinet/tcp.h> /* pour la conversion adresse reseau->format dot */
#include <arpa/inet.h> /* pour la conversion adresse reseau->format dot */

#include "../Functions/FunctionUtilities.h"
#include "../Functions/ConfReader.h"


#define PATH_FILES "././Ressources/Applic.conf"
#define PORT atoi(getConfiguration(PATH_FILES,"PORT")) /* Port d'ecoute de la socket serveur */
#define PORTADMIN atoi(getConfiguration(PATH_FILES,"PORTADMIN")) /* Port d'ecoute de la socket serveur pour Admin */
#define MAXSTRING atoi(getConfiguration(PATH_FILES,"MAXSTRING")) /* Longueur des messages */
#define ADRESSE getConfiguration(PATH_FILES,"ADRESSE") /* ADRESSE IP */
#define LONG_GROS_MSG atoi(getConfiguration(PATH_FILES,"LONG_GROS_MSG"))/* Taille gros message */
#define NBRE_MAX_CLIENTS 4
#define SEP getConfiguration(PATH_FILES,"SEP")

#define EOC getConfiguration(PATH_FILES,"EOC")  /* vérifier la fin de connexion */
#define DOC getConfiguration(PATH_FILES,"DOC") /* */






