/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleROMP;


import Requete.Requete;
import Serveurs.ConsoleServeur;
import Utilities.Utils;
import database.facility.BDHolidays;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Salva
 */
public class RequeteROMP implements Requete, Serializable {
    
    public BDHolidays bd ;
    private int type;
    private Socket socketClient;
    public String chargeUtile;
     File currentDirectory = new File(System.getProperty("user.dir"));
    
    public String path = currentDirectory+"\\src\\Config\\Config.config";
    public static final int LOGIN = 1;
    public static final int BROOM = 2;
    public static final int PROOM = 3;
    public static final int CROOM = 5;
    public static final int LROOMS = 6;
    
    
    public RequeteROMP(int t, String chu) throws ClassNotFoundException, SQLException {
        type = t;
        setChargeUtile(chu);
        
    }
    public RequeteROMP(int t) throws ClassNotFoundException, SQLException {
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
                            Logger.getLogger(RequeteROMP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(RequeteROMP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RequeteROMP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            
            case BROOM:return new Runnable() {
                    public void run() {
                        try {
                            TraitementBROOM(s, cs);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RequeteROMP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(RequeteROMP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            case PROOM :return new Runnable() {
                    public void run() {
                        try {
                            TraitementPROOM(s,cs);
                                    } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RequeteROMP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(RequeteROMP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            case CROOM :return new Runnable() {
                    public void run() {
                        try {
                            TraitementCROOM(s, cs);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RequeteROMP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(RequeteROMP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            case LROOMS :return new Runnable() {
                    public void run() {
                        try {
                            TraitementLROOMS(s, cs);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RequeteROMP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(RequeteROMP.class.getName()).log(Level.SEVERE, null, ex);
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
        
        ReponseROMP rep;
        if(log.equals(tmp[0]) && pass.equals(tmp[1]) ){
             rep = new ReponseROMP(ReponseROMP.LOGIN_OK, getChargeUtile() +" : " + "ca marche");
        }
        else{
             rep = new ReponseROMP(ReponseROMP.WRONG_PASSWORD, getChargeUtile() +" : " + "probleme !!");

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
    
    
    public void TraitementBROOM(Socket sock, ConsoleServeur cs) throws ClassNotFoundException, SQLException{
        
        
        BDHolidays bd =new BDHolidays("root", "root", "bd_holidays");
        System.out.println("JE SUIS DANS TraitementBROOM"); 
        ReponseROMP rep;
        
        String[] recu = getChargeUtile().split(":");
        
        ResultSet rs = bd.getVoyageursByEmailandName(recu[1], recu[0]);
        
        if(rs.next()){
            ResultSet  r = bd.getChambreLibre(recu[2], recu[3], recu[4], Integer.parseInt(recu[5]));
        
            if(r.next()){
                Boolean b = bd.insertReservationChambre(Integer.parseInt(r.getString("idchambres")), Integer.parseInt(rs.getString("idvoyageurs")), recu[4], Integer.parseInt(recu[5]), Double.parseDouble(r.getString("prix_htva")) *1.21*Integer.parseInt(recu[5]));
                if(b){
                    rep = new ReponseROMP(ReponseROMP.OK,"la chambre n°"+r.getString("idchambres")+" a été reservé pour le prix de : "+Double.parseDouble(r.getString("prix_htva")) *1.21*Integer.parseInt(recu[5]));
                }else{
                     rep = new ReponseROMP(ReponseROMP.ERREURINSERTION, "Erreur : la réservation n'a pas pu se faire  !!");
                }
            }else
            {
                rep = new ReponseROMP(ReponseROMP.CHAMBRE_NON_DISPONIBLE, "Pas de chambre disponible !!");
            }
        }else{
            rep = new ReponseROMP(ReponseROMP.Voyageur_NOT_FOUND, "Voyageur non trouvé !!");
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
    
    public void TraitementPROOM(Socket sock , ConsoleServeur cs) throws ClassNotFoundException, SQLException{
        BDHolidays bd =new BDHolidays("root", "root", "bd_holidays");
        System.out.println("JE SUIS DANS TraitementPROOM"); 
        ReponseROMP rep;
        
        String[] recu = getChargeUtile().split(":");
        
        ResultSet rs = bd.getVoyageursByEmailandName(recu[1], recu[0]);
        
        if(rs.next()){
            
            if(rs.getString("creditCard").equals(recu[2])){
        
                if(bd.PaiementReservation(Integer.parseInt(recu[3]), Integer.parseInt(rs.getString("idvoyageurs")), recu[4] )){
                    
                    rep = new ReponseROMP(ReponseROMP.OK,"Le paiement a été un succès !!");
                    }else{
                         rep = new ReponseROMP(ReponseROMP.ERREURINSERTION, "Erreur : le paiement n'a pas pu se faire !!");
                    }
                
            }else
            {
                rep = new ReponseROMP(ReponseROMP.WRONG_CREDITCARD, "Numéro de carte de crédit érroné !!");
            }
            
        }else{
            rep = new ReponseROMP(ReponseROMP.Voyageur_NOT_FOUND, "Voyageur non trouvé !!");
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
    
    public void TraitementCROOM(Socket sock , ConsoleServeur cs) throws ClassNotFoundException, SQLException{
        
        BDHolidays bd =new BDHolidays("root", "root", "bd_holidays");
        System.out.println("JE SUIS DANS TraitementCROOM"); 
        ReponseROMP rep;
        
        String[] recu = getChargeUtile().split(":");
        
        ResultSet rs = bd.getVoyageursByEmailandName(recu[1], recu[0]);
        
        if(rs.next()){
     

            if(bd.SuppressionReservation(Integer.parseInt(recu[2]), Integer.parseInt(rs.getString("idvoyageurs")), recu[3] )){
                rep = new ReponseROMP(ReponseROMP.OK,"La suppression a été un succès !!");
            }else{
                rep = new ReponseROMP(ReponseROMP.ERREURINSERTION, "Erreur : la suppression n'a pas pu se faire !!");
            }
           
            
        }else{
            rep = new ReponseROMP(ReponseROMP.Voyageur_NOT_FOUND, "Voyageur non trouvé !!");
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
    
    public void TraitementLROOMS(Socket sock , ConsoleServeur cs) throws ClassNotFoundException, SQLException{
        
        BDHolidays bd =new BDHolidays("root", "root", "bd_holidays");
        System.out.println("JE SUIS DANS TraitementLROOMS"); 
        ReponseROMP rep;
        
        
        
        
        ResultSet rs = bd.getAllChambreReserve();
        String s ="";
        
        while(rs.next()){
                
            s= s+rs.getString("id_chambre")+" "+rs.getString("nom")+" "+rs.getString("date_debut")+":";
        } 
        rep = new ReponseROMP(ReponseROMP.OK,s);
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
