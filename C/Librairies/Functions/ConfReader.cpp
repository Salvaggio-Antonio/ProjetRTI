#include "ConfReader.h"


char* getConfiguration(const char* nomFichier, const char* c)
{
    int ok = -1;
    FILE * inputFile = fopen(nomFichier, "r");
    if(inputFile == NULL){
        printf("Cannot open file %s\n", nomFichier);
        return NULL;
    }
    char *token;
    char buff[200];
    char* value =  NULL;
    while (fgets(buff,200,inputFile)!= NULL && ok == -1 )
    {
        token = strtok(buff,"=");
        if(strcmp(c , token)==0)
        { 
            token = strtok(NULL, "\n");
            value = new char [strlen (token )+1];
            strcpy(value, token);
            ok =0;
            
        }
    }

    fclose(inputFile);
    return value;

}