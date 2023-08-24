/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProtocoleCCAPP;

import Holidays.BDHolidays;
import ProtocoleSPAYMAP.ReponseSPAYMAP;
import ProtocoleSPAYMAP.RequeteSPAYMAP;
import Requete.Requete;
import Serveurs.ConsoleServeur;
import Utilities.CryptoUtils;
import Utilities.RequeteUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.SQLException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Base64;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Salva
 */
public class RequeteCCAPP implements Requete, Serializable {
    private int type;
    private Socket socketClient;
    public String chargeUtile;
    File currentDirectory = new File(System.getProperty("user.dir"));

    public String path = currentDirectory + "\\src\\Config\\Config.config";
    String pathserveur = currentDirectory + "\\..\\KeyStore\\Serveur\\card_store.jce";
    
    public static final int CVERIF = 1;
    
    private PrivateKey pk;
    private PublicKey pubk;
    
    public RequeteCCAPP(int t, String chu) throws ClassNotFoundException, SQLException {
        type = t;
        chargeUtile = chu;
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
        switch (type) {
            case CVERIF:
                return new Runnable() {
                    public void run() {
                        
                    }
                };
            default:
                return null;
        }
    }
    
    public void TraitementCVERIF(Socket s, ConsoleServeur cs) throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, NoSuchProviderException, UnrecoverableKeyException, InvalidKeyException, SignatureException, ClassNotFoundException, SQLException, InvalidKeySpecException
    {
        String[] tmp = getChargeUtile().split(":");
        String user = tmp[0];
        String card = tmp[1];
        
        LocalDate currentDate = LocalDate.now();
        pk = CryptoUtils.getInstance().getPrivateKey(pathserveur, "JCEKS", "TOUNFLOUTCH", Utilities.Utils.getDayNameWithK(currentDate.getDayOfWeek()), Utilities.Utils.getDayNameWithK(currentDate.getDayOfWeek()));
        pubk = CryptoUtils.getInstance().getPublicKey(pathserveur, "DER", "TOUNFLOUTCH", Utilities.Utils.getDayNameWithK(currentDate.getDayOfWeek()));

        String message = RandomStringUtils.randomAlphabetic(8);
        byte[] signature = CryptoUtils.getInstance().createSignature(pk, message.getBytes());

        String env = CryptoUtils.getInstance().PublicKeyToBase64(pubk) + ":" + Base64.getEncoder().encodeToString(signature) + ":" + message + ":" + "SERVEUR_CARD";
        RequeteSPAYMAP req = new RequeteSPAYMAP(RequeteSPAYMAP.LSHAREPUBLICKEY, env);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        RequeteUtils.SendRequest(req, "LSHAREPUBLICKEY", oos, s);

        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        ReponseSPAYMAP rep = (ReponseSPAYMAP) RequeteUtils.ReceiveRequest(s, ois, "SPAYMAP");
        
        if (rep.getCode() == ReponseSPAYMAP.OK) {
            tmp = rep.getChargeUtile().split(":");

            PublicKey pubkServeur = CryptoUtils.getInstance().Base64ToPublicKey(tmp[0]);

            byte[] signatureServeur = Base64.getDecoder().decode(tmp[1]);

            byte[] messageserveur = tmp[2].getBytes();

            boolean ok = CryptoUtils.getInstance().checkSignature(pubkServeur, signatureServeur, messageserveur);
            
            if(ok)
            {
                //check bd 
                //check card 
                //SI OK renvoie ok
                
            }
        }
    }
}
