/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.io.IOException;

/**
 *
 * @author Salva
 */
public class Configuration {
    private String _adresse;
    private int _port;
    

    public Configuration(String path, String port) throws IOException
    {
        setAdresse(Utils.getItemConfig(path, "adresse"));
        setPort(Integer.parseInt(Utils.getItemConfig(path, port))); 
    }
    /**
     * @return the _adresse
     */
    public String getAdresse() {
        return _adresse;
    }

    /**
     * @param _adresse the _adresse to set
     */
    public void setAdresse(String _adresse) {
        this._adresse = _adresse;
    }

    /**
     * @return the _port
     */
    public int getPort() {
        return _port;
    }

    /**
     * @param _port the _port to set
     */
    public void setPort(int _port) {
        this._port = _port;
    }
    
}
