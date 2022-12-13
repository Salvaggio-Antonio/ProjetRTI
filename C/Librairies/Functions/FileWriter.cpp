#include "FileWriter.h"

int addToFile(const char * nomFichier, char *action ,  const char * sep )
{
    char buff[100] , envoie[100];
    int cpt=0;
    int tmp;
    FILE *inputFile = fopen(nomFichier, "r+");
    if (inputFile == NULL)
    {
        printf("Cannot open file %s\n", nomFichier);
        return -1;
    }
    while (fgets(buff, 100, inputFile) != NULL)
    {
        tmp=atoi(strtok(buff, sep));
        if(cpt < tmp)
        {
            cpt =tmp;
        }
    }
    cpt++;

    sprintf(envoie,"%d%s%s%s\n", cpt,sep,action,sep);
    if(1!= fwrite(envoie , strlen(envoie),1, inputFile )){
        printf("stderr","Cannot write in file\n");
        return -1;
    }
    printf("je viens d'écrire sur le fichier \n");
    
    fclose(inputFile);
    return cpt;
    

}
