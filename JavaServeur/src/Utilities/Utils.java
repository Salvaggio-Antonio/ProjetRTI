/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Salva
 */
public class Utils {
    
    public static String getItemConfig(String path, String Element ) throws FileNotFoundException, IOException{
        
        String item = null;
        File file = new File(path);    
        // Créer l'objet File Reader
        FileReader fr = new FileReader(file);  
        // Créer l'objet BufferedReader        
        BufferedReader br = new BufferedReader(fr);  
        StringBuffer sb = new StringBuffer();    
        String line;
        while((line = br.readLine()) != null)
        {
            String []tmp = line.split(":");
            if(tmp[0].equals(Element)){
                item = tmp[1];
            }
          // ajoute la ligne au buffer 
        }
        fr.close();      
        return item;
        
    }
    
}
