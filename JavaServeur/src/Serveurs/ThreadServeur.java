/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serveurs;

import Holidays.BDHolidays;
import Requete.Requete;
import Utilities.Utils;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Salva
 */
public class ThreadServeur extends Thread {
    private int port;
    private SourceTaches tachesAExecuter;
    private ConsoleServeur guiApplication;
    private int maxthread;
    private ServerSocket SSocket = null;
    File currentDirectory = new File(System.getProperty("user.dir"));
    
    public String path = currentDirectory+"\\src\\Config\\Config.config";
    
    public ThreadServeur(int p, SourceTaches st, ConsoleServeur fs, int m)
    {
        port = p; 
        tachesAExecuter = st; 
        guiApplication = fs;
        maxthread = m;
    }
    public void run()
    {
        try
        {
            SSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]"); System.exit(1);
        }
        // Démarrage du pool de threads
        for (int i=0; i<maxthread ; i++) // 3 devrait être constante ou une propriété du fichier de config
        {
            ThreadClient thr = new ThreadClient (tachesAExecuter, "Thread du pool n°" +
                    String.valueOf(i));
            thr.start();
        }

        // Mise en attente du serveur
        
        Socket CSocket = null;
        while (!isInterrupted())
        {
            try
            {
                System.out.println("************ Serveur en attente");
                CSocket = SSocket.accept();
                System.out.println(CSocket);
                guiApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString()+"#accept#thread serveur");
            }
            catch (IOException e)
            {
                System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]"); System.exit(1);
            }
                ObjectInputStream ois=null;
                Requete req = null;
            try
            {
                ois = new ObjectInputStream(CSocket.getInputStream());
                req = (Requete)ois.readObject();
                System.out.println("Requete lue par le serveur, instance de " +
                req.getClass().getName());
            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
            }
            catch (IOException e)
            {
                System.err.println("Erreur ? [" + e.getMessage() + "]");
            }
            Runnable travail = req.createRunnable(CSocket, guiApplication);
            if (travail != null)
            {
                tachesAExecuter.recordTache(travail);
                System.out.println("Travail mis dans la file");
            }
            else System.out.println("Pas de mise en file");    
        }
    }
    
}
