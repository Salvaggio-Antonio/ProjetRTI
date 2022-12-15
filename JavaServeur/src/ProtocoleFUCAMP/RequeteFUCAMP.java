/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleFUCAMP;

import ProtocoleFUCAMP.ReponseFUCAMP;
import Requete.Requete;
import Serveurs.ConsoleServeur;
import Utilities.Utils;
import java.io.Serializable;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import database.facility.BDHolidays;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

/**
 *
 * @author Salva
 */
public class RequeteFUCAMP implements Requete, Serializable {
    
    public static final int LOGIN = 1;
    public static final int GETALLACTIVITIES =2;
    public static final int GETALLPARTICIPANTSBYACTIVITE=3;
    public static final int RESERVATIONACTIVITE=4;
    public static final int DELETERESERVATION=5;

    /**
     *
     */
    public BDHolidays bd ;
    private int type;
    private Socket socketClient;
    public String chargeUtile;
     File currentDirectory = new File(System.getProperty("user.dir"));
    
    public String path = currentDirectory+"\\src\\Config\\Config.config";
    
    
    public RequeteFUCAMP(int t, String chu) throws ClassNotFoundException, SQLException {
        type = t;
        setChargeUtile(chu);
        
    }
    public RequeteFUCAMP(int t) throws ClassNotFoundException, SQLException {
        type = t;
    }

    /**
     * @return the chargeUtile
     */
    public String getChargeUtile() {
        return chargeUtile;
    }

    /**
     * @param chargeUtile the chargeUtile to set
     */
    public void setChargeUtile(String chargeUtile) {
        this.chargeUtile = chargeUtile;
    }

    @Override
    public Runnable createRunnable(Socket s, ConsoleServeur cs) {
       
        switch(type){
            case LOGIN:return new Runnable() {
                    public void run() {
                        try {
                            TraitementLogin(s, cs);
                        } catch (SQLException ex) {
                            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            
            case GETALLACTIVITIES:return new Runnable() {
                    public void run() {
                        try {
                            getAllActivities(s, cs);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            case GETALLPARTICIPANTSBYACTIVITE :return new Runnable() {
                    public void run() {
                        try {
                            getAllParticipantsByActivite(s, cs);
                        } catch (SQLException ex) {
                            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            case RESERVATIONACTIVITE :return new Runnable() {
                    public void run() {
                        try {
                            reservationActivite(s,cs);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            case DELETERESERVATION :return new Runnable() {
                    public void run() {
                        try {
                            suppReservationActivite(s,cs);
                                    } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            default : return null;
        }
    }
    
    public void TraitementLogin(Socket sock, ConsoleServeur cs) throws SQLException, IOException, ClassNotFoundException{
        
        
        String[] tmp = getChargeUtile().split(";");
 
        String[] user = Utils.getItemConfig(path, "ADMIN").split(";");
        
        String log  = user[0];
        String pass = user[1];
        
        ReponseFUCAMP rep;
        if(log.equals(tmp[0]) && pass.equals(tmp[1]) ){
             rep = new ReponseFUCAMP(ReponseFUCAMP.LOGIN_OK, getChargeUtile() +" : " + "ca marche");
        }
        else{
             rep = new ReponseFUCAMP(ReponseFUCAMP.WRONG_PASSWORD, getChargeUtile() +" : " + "probleme !!");

        }
        
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
         
         
         
    }
    
    public void getAllActivities(Socket sock , ConsoleServeur cs) throws ClassNotFoundException, SQLException{
        
        BDHolidays bd =new BDHolidays("root", "root", "bd_holidays");
        
        System.out.println("JE SUIS DANS GETALLACTIVITIES");
        ResultSet s = bd.getAllActivities();
        
        chargeUtile = "";
        while (s.next()){
            
            chargeUtile= chargeUtile + s.getString("idactivites")+":"+s.getString("nom")+":"+s.getString("type")+":"+s.getString("duree")+";"; ;
           
        }
        System.out.println(chargeUtile);
        ReponseFUCAMP rep;
        rep = new ReponseFUCAMP(ReponseFUCAMP.OK, getChargeUtile());
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
  
    }
    
    public void getAllParticipantsByActivite(Socket sock, ConsoleServeur cs) throws SQLException{
        
        BDHolidays bd = null;
        try {
            bd = new BDHolidays("root", "root", "bd_holidays");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RequeteFUCAMP.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("JE SUIS DANS getAllParticipantsByActivite");
        ResultSet s = bd.getAllParticipantsByActivite(Integer.parseInt(getChargeUtile()));

        chargeUtile = "";
        while (s.next()){

            chargeUtile= chargeUtile + s.getString("idvoyageurs")+":"+s.getString("nom")+":"+s.getString("prenom")+":"+s.getString("mail")+":"+s.getString("date_debut")+";";

        }
        ReponseFUCAMP rep;
        if(!chargeUtile.equals("") && chargeUtile != null){
            rep = new ReponseFUCAMP(ReponseFUCAMP.OK, getChargeUtile());

        }else{
             rep = new ReponseFUCAMP(ReponseFUCAMP.PARTICIPANTS_NOT_FOUND, getChargeUtile());
        }
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
          
    }
    
    public void suppReservationActivite(Socket sock, ConsoleServeur cs) throws ClassNotFoundException, SQLException{
        
        
        BDHolidays bd =new BDHolidays("root", "root", "bd_holidays");
        System.out.println("JE SUIS DANS DELETERESERVATION"); 
        ReponseFUCAMP rep;
        
        String[] recu = getChargeUtile().split(":");
        
        Boolean  d = bd.DeleteReservationActivite(Integer.parseInt(recu[0]), Integer.parseInt(recu[1]), recu[2]);
        
        if(d){

            rep = new ReponseFUCAMP(ReponseFUCAMP.OK, "suppression reussi !");
        }else
        {
            rep = new ReponseFUCAMP(ReponseFUCAMP.ERREURSUPPRESSION, "suppression raté !!");
        }
      
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
        
        
    }
    
    public void reservationActivite(Socket sock, ConsoleServeur cs) throws ClassNotFoundException, SQLException{
        
        
        BDHolidays bd =new BDHolidays("root", "root", "bd_holidays");
        System.out.println("JE SUIS DANS RESERVATIONACTIVITE"); 
        ReponseFUCAMP rep;
        
        String[] recu = getChargeUtile().split(":");
        
        ResultSet s = bd.getVoyageursByEmailandName(recu[1], recu[0]);
        
        if(s.next()){
            
            if(bd.insertReservationActivite(Integer.parseInt(recu[4]), Integer.parseInt(s.getString("idvoyageurs")) , recu[2], Integer.parseInt(recu[3]))){
                rep = new ReponseFUCAMP(ReponseFUCAMP.OK, "insertion reussi !");
            }else{
                rep = new ReponseFUCAMP(ReponseFUCAMP.ERREURINSERTION, "insertion raté !!");
            }
            
        }else
        {
            rep = new ReponseFUCAMP(ReponseFUCAMP.EMAIL_NOT_FOUND, "l'adresse mail ou le nom du voyageur n'a pas été trouvé");
        }
        
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep); oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
        
        
    }
    
    
}
