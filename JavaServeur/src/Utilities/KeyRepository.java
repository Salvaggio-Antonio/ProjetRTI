/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.util.Hashtable;

/**
 *
 * @author Salva
 */
public class KeyRepository {
    
    private static KeyRepository instance;
    
    private Hashtable<String , KeysUser> repo;
       
    
    private KeyRepository()
    {
        repo = new Hashtable();
    }
    
    public synchronized void addKeysRepo(String user, KeysUser keys)
    {
        repo.put(user, keys);
    }
    
    public synchronized KeysUser getKeysRepoByUser(String user)
    {
        return repo.get(user);
    }
    
    public static KeyRepository getInstance()
    {
        if(instance == null)
        {
            instance = new KeyRepository();
        }
        return instance;
    }
    
}
