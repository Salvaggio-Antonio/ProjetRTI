#include "FunctionUtilities.h"
//vérifier si les données concernant la connexion est bonne
int checkConnexion(char *nomFichier, char *msg, char *sep)
{
    FILE *inputFile = fopen(nomFichier, "r");
    if (inputFile == NULL)
    {
        printf("Cannot open file %s\n", nomFichier);
        return -1;
    }
    char buff[MAX];
    char *pseudo = NULL;
    char *password = NULL;
    char *p = NULL;
    char *pa = NULL;
    int ok = -1;

    pseudo = strtok(msg, sep);
    password = strtok(NULL, sep);

    while (fgets(buff, MAX, inputFile) != NULL)
    {
        p = strtok(buff, sep);
        if (strcmp(pseudo, p) == 0)
        {
            pa = strtok(NULL, sep);
            if (strcmp(password, pa) == 0)
            {
                ok = 1;
            }
        }
    }
    fclose(inputFile);

    return ok;
}

//rentrer information du type de matériel pour le client
char *menuHandlingMaterial()
{
    char *reponse;
    char buf[100];
    char bufrep[100];

    /* 5.Envoi d'un message client */
    printf("\n1. livraison\n2. réparation\n3. déclassement\n-->");
    scanf("%s", buf);

    char nom[100];
    char id[100];
    char categorie[100];
    char jour[100];
    char mois[100];
    char annee[100];

    printf("Rentré les informations concernant l'appareil.\n");
    printf("categorie\n-->");
    scanf("%s", categorie);
    printf("nom\n-->");
    scanf("%s", nom);
    printf("id\n-->");
    scanf("%s", id);
    printf("Date\n-->");
    printf("\tjour\n\t--> ");
    scanf("%s", jour);
    printf("\tmois\n\t--> ");
    scanf("%s", mois);
    printf("\tannee\n\t--> ");
    scanf("%s", annee);

    sprintf(bufrep, "%s;%s;%s;%s;%s;%s;%s", buf, categorie, nom, id, jour, mois, annee);
    reponse = (char *)malloc(sizeof(bufrep) + 1);
    strcpy(reponse, bufrep);

    return reponse;
}

//le menu du client
char *menuPrincipalClient(const char *sep)
{
    int ok;
    char *reponse;
    char buf[100];
    char bufrep[100];
    int r;
    do
    {
        /* 5.Envoi d'un message client */
        ok = 1;

        printf("\n----MENU---\n1.Handling material\n2.List commands\n3.Cancel handling material\n4.Asking for materials\n-->");
        scanf("%d", &r);
        switch (r)
        {
        case 1:
            sprintf(bufrep, "%d", r);
            strcpy(buf, menuHandlingMaterial());
            sprintf(bufrep, "%s%s%s", bufrep, sep, buf);
            break;
        case 2:
            sprintf(bufrep, "%d%s", r, sep);
            break;
        case 3:
            sprintf(bufrep, "%d", r);
            printf("ID\n-->");
            scanf("%d", &r);
            sprintf(bufrep, "%s%s%d%s", bufrep, sep, r, sep);
            break;
        case 4:

            sprintf(bufrep, "%d;%s;", r, menuAskMat());

            break;
        default:
            printf("Reponse incorrect");
            ok = -1;
            break;
        }

    } while (ok == -1);
    reponse = (char *)malloc(sizeof(bufrep) + 1);
    strcpy(reponse, bufrep);
    reponse[strlen(reponse)] = 0;

    return reponse;
}

//La gestion des requêtes du client par le serveur
const char *HandlingClientRequest(char *request, char *sep)
{
    char *requestTok;

    const char *response;
    char buff[200];
    strcpy(buff, request);
    requestTok = strtok(request, sep);
    switch (atoi(requestTok))
    {
    case 1:
        response = HandlingClientAction(atoi(strtok(NULL, sep)), sep);
        break;

    case 2:

        response = getFile(sep, "././Ressources/handlingMateriel.csv");

        break;

    case 3:
        if ((deleteActionById(sep, "././Ressources/handlingMateriel.csv", strtok(NULL, sep))) == 1)
        {
            return "l'element a ete supprime";
        }
        else
        {
            return "probleme survenu ! ";
        }

        break;

    case 4: response = addCommandMaterial("././Ressources/materiels.csv",sep, buff);

        break;
    }

    return response;
}

//La gestion des actions du client par le serveur
const char *HandlingClientAction(int a, const char *sep)
{

    char *tmp;
    int act;

    switch (a)
    {
    case 1:
        act = addAction(sep, "OK", "Livraison");
        switch (act)
        {   
        case 0:
            return "le materiel n´existe pas ou il est en panne ";
            break;
        case -1:
            return "une erreur a eu lieu";
            break;
        default: return "La livraison a ete faites";
            break;
        }
        break;
    case 2:
        act = addAction(sep, "KO", "Reparation");
        switch (act)
        {
        
        case 0:
            return "le materiel n´existe pas ou il est déja ok";
            break;
        case -1:
            return "une erreur a eu lieu";
            break;
        default:
            return "le materiel sera en cours de réparation";
            break;
        }
        break;
    case 3:
        act = addAction(sep, "DES", "Declassement");
        switch (act)
        {
        
        case 0:
            return "le materiel n´existe pas ou il est déja ok";
            break;
        case -1:
            return "une erreur a eu lieu";
            break;
        default:
            return "le materiel sera déclassé";
            break;
        }
        break;

    default:
        break;
    }

    return NULL;
}

//Ajouter une action du client dans le serveur
int addAction(const char *sep, const char *etat, const char *type)
{
    char *categorie = strtok(NULL, sep);
    char *nom = strtok(NULL, sep);
    char *id = strtok(NULL, sep);
    char *jour = strtok(NULL, sep);
    char *mois = strtok(NULL, sep);
    char *annee = strtok(NULL, sep);
    char *e = NULL;
    char buff[MAX];

    FILE *inputFile = fopen("././Ressources/typemateriel.csv", "r");
    if (inputFile == NULL)
    {
        printf("Cannot open file %s\n", "././Ressources/typemateriel.csv");
        return -1;
    }
    while (fgets(buff, MAX, inputFile) != NULL)
    {

        if (strcmp(nom, strtok(buff, sep)) == 0)
        {
            if (strcmp(id, strtok(NULL, sep)) == 0)
            {
                if (strcmp(categorie, strtok(NULL, sep)) == 0)
                {
                    e = strtok(NULL, sep);
                    if (strcmp(etat, e) == 0)
                    {
                        fclose(inputFile);
                        sprintf(buff, "%s%s%s%s%s%s%s%s%s%s%s%s%s", type, sep, nom, sep, id, sep, categorie, sep, jour, sep, mois, sep, annee);

                        return addToFile("././Ressources/handlingMateriel.csv", buff, sep);
                    }
                }
            }
        }
    }
    fclose(inputFile);
    return 0;
}

//Récupérer les données d'un fichier
const char *getFile(const char *sep, const char *nomFichier)
{
    char *tmp = NULL, *rep = NULL;
    char buff[500], reponse[5000];
    int i = 0, cpt = 0;
    FILE *inputFile = fopen(nomFichier, "r+");
    if (inputFile == NULL)
    {
        printf("Cannot open file %s\n", nomFichier);
        return NULL;
    }
    sprintf(reponse, "resultat :\n");

    while (fgets(buff, 500, inputFile) != NULL)
    {
        i = 0;
        tmp = strtok(buff, sep);
        while (tmp != NULL)
        {
            sprintf(reponse, "%s%s\t", reponse, tmp);
            i++;
            tmp = strtok(NULL, sep);
        }
        sprintf(reponse, "%s\n", reponse);
        i++;
        cpt++;
    }
    fclose(inputFile);

    if (cpt == 0)
    {
        sprintf(reponse, "Aucune Action a ete enregistre !\n");
    }
    rep = new char[strlen(reponse) + 1];
    strcpy(rep, reponse);
    return rep;
}

//suppression d'une action grace a l'id
int deleteActionById(const char *sep, const char *nomFichier, char *id)
{
    FILE *current, *tmp;
    char buff[500], *toker, buff2[500];
    char fichierTmp[50];
    int trouve = 0;
    sprintf(fichierTmp, "tmp.csv");
    if ((current = fopen(nomFichier, "r")) == NULL)
    {
        printf("Cannot open file %s\n", nomFichier);
        return -1;
    }
    if ((tmp = fopen(fichierTmp, "w")) == NULL)
    {
        printf("Cannot open file %s\n", nomFichier);
        return -1;
    }
    while (!feof(current))
    {
        fgets(buff, 500, current);

        strcpy(buff2, buff);

        if (!feof(current))
        {
            toker = strtok(buff, sep);
            if (strcmp(id, toker) != 0)
            {
                fprintf(tmp, "%s", buff2);
            }
            else
            {
                if (checkDateFromAction(buff2, sep) == -1)
                {
                    // trop tard
                    fclose(current);
                    fclose(tmp);
                    return -2;
                }
                trouve = 1;
            }
        }
    }
    fclose(current);
    fclose(tmp);
    remove(nomFichier);
    rename(fichierTmp, nomFichier);

    return trouve;
}

//récupérer la date actuel sous forme de seconde
time_t getDate()
{
    int jour, mois, annee;
    char tab[20], *envoie;
    time_t now;

    time(&now);

    return now;
}

//suppression d'un token d'une chaine de caractère
const char *removeToken(char *msg, const char *sep, int n)
{
    char *tmp, *envoie;
    char resp[200];
    int i = 1;

    tmp = strtok(msg, sep);
    while (tmp != NULL)
    {
        if (i != n)
        {

            if (i == 2)
                sprintf(resp, "%s%s", tmp, sep);
            else
                sprintf(resp, "%s%s%s", resp, tmp, sep);
        }
        i++;
        tmp = strtok(NULL, sep);
    }

    envoie = new char[strlen(resp) + 1];
    strcpy(envoie, resp);
    envoie[strlen(envoie)] = 0;

    return envoie;
}

//menu pour le Ask Matériel du client
char *menuAskMat()
{
    char response[500];
    char *rep;
    char type[100], nom[100], marque[100], prix[50], accessoire[100];
    printf("Rentrez les informations concernant le materiel .\n");
    printf("type\n-->");
    scanf("%s", type);
    printf("nom\n-->");
    scanf("%s", nom);
    printf("marque\n-->");
    scanf("%s", marque);
    printf("prix\n-->");
    scanf("%s", prix);
    printf("accessioire :\n-->");
    scanf("%s", accessoire);

    sprintf(response, "%s;%s;%s;%s;%s", type, nom, marque, prix, accessoire);

    rep = new char[strlen(response) + 1];
    strcpy(rep, response);
    rep[strlen(rep)] = 0;
    return rep;
}

//vérifier la date de l'action 
int checkDateFromAction(char *request, const char *sep)
{
    char *jour, *mois, *annee;
    strtok(request, sep);

    for (int i = 0; i < 4; i++)
    {
        strtok(NULL, sep);
    }
    struct tm dateAction;
    time_t dateNow;

    char date[50];
    jour = strtok(NULL, sep);
    mois = strtok(NULL, sep);
    annee = strtok(NULL, sep);

    sprintf(date, "%s-%s-%s", jour, mois, annee);

    strptime(date, "%d-%m-%Y", &dateAction);
    dateNow = getDate();

    time_t a = mktime(&dateAction);

    double t = difftime(dateNow, a);

    if (t > 0 || t == 0)
    {
        printf("date Now est supéreiur a dateAction\n");
        return -1;
    }

    return 1;
}

//Ajout de la commande de matériel du client 
const char *addCommandMaterial(const char* nomFichier,const char* sep, char* request )
{
    int delai , id;
    char buff[200];
    srand(time(NULL));
    char* env; 

    delai = rand()%20;
    strcpy(buff, removeToken(request, sep, 1));
    sprintf(buff, "%s%djours", buff, delai);
    if ((id = addToFile(nomFichier, buff, sep)) < 1)
    {
        return "probleme survenu";
    }
    sprintf(buff, "votre commande numéro : %d sera faites dans %d jours",id,delai);
    env= new char[strlen(buff)+1];
    strcpy(env, buff);
    env[strlen(env)]=0;
    return env;

}