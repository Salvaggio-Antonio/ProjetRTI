/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.security.PublicKey;

/**
 *
 * @author Salva
 */
public class ServerKey {
    
    private static ServerKey instance;
    private PublicKey pk;

    public PublicKey getPk() {
        return pk;
    }

    public void setPk(PublicKey pk) {
        this.pk = pk;
    }
    
    private ServerKey()
    {
    }
    
    public static ServerKey getInstance()
    {
        if(instance == null)
        {
            instance = new ServerKey();
        }
        return instance;
    }
}
