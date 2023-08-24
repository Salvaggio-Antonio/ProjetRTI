/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleHOLICOP;

import ProtocoleSPAYMAP.ReponseSPAYMAP;
import Requete.Requete;
import Serveurs.ConsoleServeur;
import Utilities.Configuration;
import Utilities.RequeteUtils;
import Utilities.Utils;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequeteHOLICOP implements Requete, Serializable {

    private String chargeUtile;
    File currentDirectory = new File(System.getProperty("user.dir"));
    public String path = currentDirectory+"\\src\\Config\\Config.config";
    Configuration config;


    public RequeteHOLICOP(String chu) {
        chargeUtile = chu;
    }

    public String getChargeUtile() {
        return chargeUtile;
    }

    public void setChargeUtile(String chargeUtile) {
        this.chargeUtile = chargeUtile;
    }



    public Runnable createRunnable(Socket s, ConsoleServeur cs) {
      
                return new Runnable() {
                    public void run() {
                        try {
                            traiteRequete(s, cs);
                        } catch (IOException ex) {
                            Logger.getLogger(RequeteHOLICOP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };              
    }

    private void traiteRequete(Socket s, ConsoleServeur cs) throws IOException {
        String adresseDistante = s.getRemoteSocketAddress().toString();
        cs.TraceEvenements(adresseDistante + "#" + "AUTH" + "#" + Thread.currentThread().getName());
        
        String contenu = getChargeUtile();
        String[] authInfo = contenu.split(":");
        String pseudo = authInfo[0];
        String receivedDigest = authInfo[1];
        long temps = Long.parseLong(authInfo[2]);
        double alea = Double.parseDouble(authInfo[3]);
        boolean authSuccess = true;
        
                //verif
        //String expectedDigest = ;  BD
        //boolean authSuccess = receivedDigest.equals(expectedDigest);

        //
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        //if (AuthenticationSuccess)
       
        
        if (authSuccess) {
            config = new Configuration(path, "PORT_CHAT");
            config.setAdresse(Utils.getItemConfig(path, "adress_multicast"));
            oos.writeObject("ok#" + config.getPort() + "#" + config.getAdresse());

        } else {
            oos.writeObject("ko");
        }
        oos.flush();
        
         
       
    }

    private void traiteReponse(Socket sock, ConsoleServeur cs, String type) throws SQLException, ClassNotFoundException {
       
    }
}