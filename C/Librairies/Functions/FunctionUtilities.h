#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#include "FileWriter.h"

#define MAX 200

//vérifier si les données concernant la connexion est bonne
int checkConnexion(char * nomFichier,char * msg, char* sep);

//le menu du client
char *menuPrincipalClient(const char * sep);

//rentrer information du type de matériel pour le client
char *menuHandlingMaterial();

//Ajout de la commande de matériel du client 
const char *addCommandMaterial(const char* nomFichier,const char* sep, char* request );

//La gestion des requêtes du client par le serveur
const char*  HandlingClientRequest(char *request,char *sep);

//La gestion des actions du client par le serveur
const char * HandlingClientAction( int a, const char* sep);

//Ajouter une action du client dans le serveur
int addAction(const char * sep, const char* etat, const char* type);

//Récupérer les données d'un fichier
const char* getFile(const char *sep, const char* nomFichier);

//suppression d'une action grace a l'id
int deleteActionById(const char * sep, const char* nomFichier , char* id);

//récupérer la date actuel sous forme de seconde
time_t getDate();

//vérifier la date de l'action 
int checkDateFromAction(char* request, const char* sep);

//menu pour le Ask Matériel du client
char * menuAskMat();

//suppression d'un token d'une chaine de caractère
const char* removeToken( char* msg, const char* sep, int n );