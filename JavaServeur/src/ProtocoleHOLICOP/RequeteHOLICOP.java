/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleHOLICOP;

import ProtocoleSPAYMAP.ReponseSPAYMAP;
import Requete.Requete;
import Serveurs.ConsoleServeur;
import Utilities.RequeteUtils;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequeteHOLICOP implements Requete, Serializable {

    private String chargeUtile;
    private int type;

    public static final int HSEND = 1;

    public RequeteHOLICOP(int t, String chu) {
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddhhmmss");
        LocalDateTime now = LocalDateTime.now();

        return type + dtf.format(now);
    }

    public Runnable createRunnable(Socket s, ConsoleServeur cs) {
        switch (type) {
            case HSEND:
                return new Runnable() {
                    public void run() {
                        traiteRequete(s, cs);
                    }
                };
            default:
                throw new AssertionError();
        }

    }

    private void traiteRequete(Socket s, ConsoleServeur cs) {

        try {
            String tmp = getChargeUtile();

            switch (tmp) {
                case "QUEST":
                    AskQuestion(s, cs);
                    break;
                case "EVENT":
                    AskEvent(s, cs);
                    break;
                case "REP":
                    AskReponse(s, cs);
                    break;
                default:
                    break;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(RequeteHOLICOP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void AskQuestion(Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException {
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        cs.TraceEvenements(adresseDistante + "#" + "ASK_QUESTION" + "#" + Thread.currentThread().getName());

        String message = calculeTimestamp("q");
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

    private void AskEvent(Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException {
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        cs.TraceEvenements(adresseDistante + "#" + "EVENT" + "#" + Thread.currentThread().getName());

        String message = calculeTimestamp("e");
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

    private void AskReponse(Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException {
        String adresseDistante = sock.getRemoteSocketAddress().toString();
        cs.TraceEvenements(adresseDistante + "#" + "REP" + "#" + Thread.currentThread().getName());
        String message = calculeTimestamp("r");
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
}
