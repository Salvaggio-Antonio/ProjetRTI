/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProtocoleCCAPP;

import Requete.Reponse;
import java.io.Serializable;

/**
 *
 * @author Salva
 */
public class ReponseCCAPP implements Reponse, Serializable {
    public static int LOGIN_OK = 201;
    public static int EMAIL_NOT_FOUND = 501;
    public static int WRONG_CREDITCARD = 401;
    public static int ERREURSHAREPUBLICKEY =400; 
    public static int ERR_SIGNATURE = 402;
   

 
    public static int OK = 202;
    
    
    private int codeRetour;
    private String chargeUtile;
    
    public ReponseCCAPP(int c, String chu)
    {
        codeRetour = c; setChargeUtile(chu);
    }
    @Override
    public int getCode() { return codeRetour; }
    @Override
    public String getChargeUtile() { return chargeUtile; }
    public void setChargeUtile(String chargeUtile) { this.chargeUtile = chargeUtile; }
}
