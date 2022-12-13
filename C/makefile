.SILENT:

GCC = g++ -m64 -lpthread
LIBFUN = Librairies/Functions/
LIBSERVEURCLIENT = Librairies/ServeurClient/
MATERIEL = Materiel/
all:	MaterielServeur MaterielClient MaterielAdmin

MaterielAdmin	:	$(MATERIEL)MaterielAdmin.cpp	$(LIBSERVEURCLIENT)protocol.h	SocketsUtilities.o ConfReader.o 
		echo compilation de MaterielAdmin
		$(GCC) -o MaterielAdmin $(MATERIEL)MaterielAdmin.cpp SocketsUtilities.o ConfReader.o 

MaterielClient	:	$(MATERIEL)MaterielClient.cpp	$(LIBSERVEURCLIENT)protocol.h	SocketsUtilities.o ConfReader.o FunctionUtilities.o FileWriter.o
		echo compilation de MaterielClient
		$(GCC) -o MaterielClient $(MATERIEL)MaterielClient.cpp SocketsUtilities.o ConfReader.o FunctionUtilities.o FileWriter.o

MaterielServeur	:	$(MATERIEL)MaterielServeur.cpp	$(LIBSERVEURCLIENT)protocol.h	SocketsUtilities.o FunctionUtilities.o ConfReader.o FileWriter.o
		echo compilation de MaterielServeur
		$(GCC) -o MaterielServeur $(MATERIEL)MaterielServeur.cpp SocketsUtilities.o FunctionUtilities.o ConfReader.o FileWriter.o

SocketsUtilities.o	:	$(LIBSERVEURCLIENT)SocketsUtilities.cpp $(LIBSERVEURCLIENT)SocketsUtilities.h $(LIBSERVEURCLIENT)protocol.h
		echo creation de SocketsUtilities
		$(GCC) -c $(LIBSERVEURCLIENT)SocketsUtilities.cpp

FunctionUtilities.o	:	$(LIBFUN)FunctionUtilities.cpp $(LIBFUN)FunctionUtilities.h
		echo creation de FunctionUtilities.o
		$(GCC) -c $(LIBFUN)FunctionUtilities.cpp

FileWriter.o	:	$(LIBFUN)FileWriter.cpp $(LIBFUN)FileWriter.h
		echo creation de FileWriter.o
		$(GCC) -c $(LIBFUN)FileWriter.cpp

ConfReader.o	:	$(LIBFUN)ConfReader.cpp	$(LIBFUN)ConfReader.h
		echo creation de ConfReader.o
		$(GCC) -c $(LIBFUN)ConfReader.cpp



 
