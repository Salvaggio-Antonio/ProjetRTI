/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProtocoleHOLICOP;

import Requete.Reponse;
import java.io.Serializable;

/**
 *
 * @author Salva
 */
public class ReponseHOLICOP implements Reponse, Serializable{
    public static int LOGIN_OK = 201;
    public static int EMAIL_NOT_FOUND = 501;
    public static int PARTICIPANTS_NOT_FOUND = 501;
    public static int WRONG_PASSWORD = 401;
    public static int IDACTIVITE_NOT_FOUND = 502;
    public static int ERREURINSERTION = 301;
    public static int ERREURSUPPRESSION = 302;
    public static int OK = 202;
    
    
    private int codeRetour;
    private String chargeUtile;
    
    public ReponseHOLICOP(int c, String chu)
    {
        codeRetour = c; setChargeUtile(chu);
    }
    @Override
    public int getCode() { return codeRetour; }
    @Override
    public String getChargeUtile() { return chargeUtile; }
    public void setChargeUtile(String chargeUtile) { this.chargeUtile = chargeUtile; }
}
