/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

/**
 *
 *
 * @author Salva
 */
public class User {
    
    private static User instance;
    private KeysUser mykeys;
    private String username;
    
    private User()
    {
        
    }
    
    public synchronized void  setUser(String name, KeysUser keys)
    {
        setUsername(name);
        setMykeys(keys);
    }

    public synchronized String getUsername() {
        return username;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized KeysUser getMykeys() {
        return mykeys;
    }

    public synchronized void setMykeys(KeysUser mykeys) {
        this.mykeys = mykeys;
    }
    
    public static User getInstance()
    {
        if(instance == null)
        {
            instance = new User();
        }
        return instance;
    }
}
