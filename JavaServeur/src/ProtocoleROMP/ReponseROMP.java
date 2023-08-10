/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProtocoleROMP;

import Requete.Reponse;
import java.io.Serializable;

/**
 *
 * @author Salva
 */
public class ReponseROMP implements Reponse, Serializable {
    public static int LOGIN_OK = 201;
    public static int EMAIL_NOT_FOUND = 501;
    public static int PARTICIPANTS_NOT_FOUND = 501;
    public static int WRONG_PASSWORD = 401;
    public static int WRONG_CREDITCARD = 402;
    public static int IDACTIVITE_NOT_FOUND = 502;
    public static int ERREURINSERTION = 301;
    public static int ERREURSUPPRESSION = 302;
    public static int Voyageur_NOT_FOUND = 502;
    
    public static int CHAMBRE_NON_DISPONIBLE=601;
    public static int PAIEMENTREFUSE=701;
            
    public static int OK = 202;
    
    
    private int codeRetour;
    private String chargeUtile;
    
    public ReponseROMP(int c, String chu)
    {
        codeRetour = c; setChargeUtile(chu);
    }
    @Override
    public int getCode() { return codeRetour; }
    @Override
    public String getChargeUtile() { return chargeUtile; }
    public void setChargeUtile(String chargeUtile) { this.chargeUtile = chargeUtile; }
}

