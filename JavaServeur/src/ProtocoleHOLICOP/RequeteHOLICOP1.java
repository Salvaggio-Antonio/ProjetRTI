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

public class RequeteHOLICOP1 implements Requete, Serializable {

    private String chargeUtile;
    private int type;

    public static final int HSEND = 1;
    public static final int AUTH = 0;
    
    InetAddress adresseGroupe;
    MulticastSocket socketGroupe;

    File currentDirectory = new File(System.getProperty("user.dir"));
    public String path = currentDirectory+"\\src\\Config\\Config.config";
    Configuration config;


    public RequeteHOLICOP1(int t, String chu) {
        type = t;
        chargeUtile = chu;
    }

    public String getChargeUtile() {
        return chargeUtile;
    }

    /**
     * @param chargeUtile the chargeUtile to set
     */
    public void setChargeUtile(String chargeUtile) {
        this.chargeUtile = chargeUtile;
    }

    private String calculeTimestamp(String type) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();

        return type + dtf.format(now);
    }

    public Runnable createRunnable(Socket s, ConsoleServeur cs) {
        switch (type) {
            case HSEND:
                return new Runnable() {
                    public void run() {
                        try {
                            traiteRequete(s, cs);
                        } catch (IOException ex) {
                            Logger.getLogger(RequeteHOLICOP1.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            default:
                throw new AssertionError();
        }

    }

    private void traiteRequete(Socket s, ConsoleServeur cs) throws IOException {
        try {
            String tmp = getChargeUtile();         
            switch (tmp) {
                case "QUEST":
                    traiteReponse(s, cs, "q");
                    break;
                case "EVENT":
                    traiteReponse(s, cs, "e");
                    break;
                case "REP":
                    traiteReponse(s, cs, "r");
                    break;
                case "AUTH":
                    System.out.println("OOOOOOOOOOOOOOOOOOOOOH  "+tmp);
                    traiteAuth(s,cs);
                    break;
                    
                default:
                    
                    break;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(RequeteHOLICOP1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void traiteReponse(Socket sock, ConsoleServeur cs, String type) throws SQLException, ClassNotFoundException {
                System.out.println("zzzzzzzzz");

        String adresseDistante = sock.getRemoteSocketAddress().toString();
        cs.TraceEvenements(adresseDistante + "#" + type + "#" + Thread.currentThread().getName());

        String message = calculeTimestamp(type);
        ObjectOutputStream oos;
        ReponseHOLICOP rep = new ReponseHOLICOP(ReponseHOLICOP.OK, message);
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }

    private void traiteAuth(Socket s, ConsoleServeur cs) throws IOException {
                        System.out.println("aaaaaaaa");

        String adresseDistante = s.getRemoteSocketAddress().toString();
        cs.TraceEvenements(adresseDistante + "#" + "AUTH" + "#" + Thread.currentThread().getName());
        
        String contenu = getChargeUtile();
        String[] authInfo = contenu.split(":");
        String pseudo = authInfo[0];
        String receivedDigest = authInfo[1];
        
        //verif
        
        //
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        //if (AuthenticationSuccess)
        config = new Configuration(path, "PORT_GROUP");
        config.setAdresse(Utils.getItemConfig(path, "adress_multicast"));
        
        System.out.println("Adresse multicast: " + config.getAdresse());
        System.out.println("Port groupe: " + config.getPort());         
        System.out.println(pseudo);
        System.out.println(receivedDigest);


        //rep
        

    }
    
    
}