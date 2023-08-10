/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import ClientsActivite.LoginActivite;
import ProtocoleFUCAMP.ReponseFUCAMP;
import ProtocoleROMP.ReponseROMP;
import Requete.Reponse;
import Requete.Requete;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Salva
 */
public class RequeteUtils {
    public static void SendRequest(Requete r, String typeRequete, ObjectOutputStream oos, Socket socket)
    {
        // Envoie de la requête
        System.out.println("envoie requete "+typeRequete);
        try
        {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(r); oos.flush();
        }
        catch (IOException e)
        { System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); }
    }
    public static Reponse ReceiveRequest(Socket socket, ObjectInputStream ois, String protocole)
    {
        Reponse r = null;
        System.out.println("en attente d'une réponse !");
        try
        {
            ois = new ObjectInputStream(socket.getInputStream());
            if(protocole.equals("ROMP"))
            {
                r = (ReponseROMP)ois.readObject();
            }
            else if(protocole.equals("FUCAMP")){
                r = (ReponseFUCAMP)ois.readObject();
            }
            
            System.out.println(" *** Reponse reçue : " + r.getChargeUtile());

            return r;
        }
        catch (ClassNotFoundException e)
        { System.out.println("--- erreur sur la classe = " + e.getMessage()); }
        catch (IOException e)
        { System.out.println("--- erreur IO = " + e.getMessage()); }
        return r;
        
    }
    
}
