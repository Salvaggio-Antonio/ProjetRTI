/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProtocoleSPAYMAP;

import Holidays.BDHolidays;
import Requete.Requete;
import Serveurs.ConsoleServeur;
import Utilities.CryptoUtils;
import Utilities.KeyRepository;
import Utilities.KeysUser;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Salva
 */
public class RequeteSPAYMAP implements Requete, Serializable {

    private int type;
    private Socket socketClient;
    public String chargeUtile;
    File currentDirectory = new File(System.getProperty("user.dir"));

    public String path = currentDirectory + "\\src\\Config\\Config.config";
    String pathserveur = currentDirectory + "\\..\\KeyStore\\Serveur\\Serveur_keystore.jce";
    public static final int LPAY = 1;
    public static final int LSHAREPUBLICKEY = 2;
    public static final int LSHARESECRETKEYAUTH = 3;
    public static final int LSHARESECRETKEYCRYPT = 4;
    public static final int LGETRESERVATIONS = 5;
    public static final int LPAYRESERVATIONS = 6;

    private PrivateKey pk;
    private PublicKey pubk;

    public RequeteSPAYMAP(int t, String chu) throws ClassNotFoundException, SQLException {
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
            case LPAY:
                return new Runnable() {
                    public void run() {
                        try {
                            TraitementLPAY(s, cs);
                        } catch (SQLException | IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchProviderException ex) {
                            Logger.getLogger(RequeteSPAYMAP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };

            case LSHAREPUBLICKEY:
                return new Runnable() {
                    public void run() {
                        try {
                            TraitementLSHAREPUBLICKEY(s, cs);
                        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | NoSuchProviderException | InvalidKeySpecException | SignatureException | InvalidKeyException ex) {
                            Logger.getLogger(RequeteSPAYMAP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            case LSHARESECRETKEYAUTH:
                return new Runnable() {
                    public void run(){
                        try {
                            traitementLSHARESECRETKEYAUTH(s,cs);
                        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
                            Logger.getLogger(RequeteSPAYMAP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            case LSHARESECRETKEYCRYPT:
                return new Runnable() {
                    public void run(){
                        try {
                            TraitementLSHARESECRETKEYCRYPT(s,cs);
                        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException | UnrecoverableKeyException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
                            Logger.getLogger(RequeteSPAYMAP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            case LGETRESERVATIONS:
                return new Runnable(){
                    public void run()
                    {
                        try {
                            TraitementLGETRESERVATIONS(s,cs);
                        } catch (SQLException | ClassNotFoundException ex) {
                            Logger.getLogger(RequeteSPAYMAP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            case LPAYRESERVATIONS:
                return new Runnable(){
                    public void run()
                    {
                        try {
                            TraitementLPAYRESERVATIONS(s,cs);
                        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | ClassNotFoundException | SQLException ex) {
                            Logger.getLogger(RequeteSPAYMAP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            default:
                return null;
        }
    }

    public void TraitementLPAY(Socket sock, ConsoleServeur cs) throws SQLException, IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException {

        String[] tmp = getChargeUtile().split(":");

        String user = tmp[0];
        long temps = Long.parseLong(tmp[1]);
        double alea = Double.parseDouble(tmp[2]);
        ReponseSPAYMAP rep;

        String hashString = tmp[3]; // Chaîne de hachage
        byte[] digest = Base64.getDecoder().decode(hashString);

        ResultSet r = BDHolidays.getInstance().getAdmin(user);

        if (r != null) {
            r.next();
            String passwordbd = r.getString("password");
            byte[] newdigest = CryptoUtils.getInstance().createMessageDigest(user, passwordbd, temps, alea);
            if (MessageDigest.isEqual(digest, newdigest)) {
                rep = new ReponseSPAYMAP(ReponseSPAYMAP.LOGIN_OK, "L'admin a été connecté !");
                KeyRepository.getInstance().addKeysRepo(user, new KeysUser());
            } else {
                rep = new ReponseSPAYMAP(ReponseSPAYMAP.WRONG_PASSWORD, "Mot de passe éroné");
            }
        } else {
            rep = new ReponseSPAYMAP(ReponseSPAYMAP.ADMIN_NOT_FOUND, "Erreur de login");
        }

        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }

    }

    public void TraitementLSHAREPUBLICKEY(Socket sock, ConsoleServeur cs) throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, NoSuchProviderException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        pk = CryptoUtils.getInstance().getPrivateKey(pathserveur, "JCEKS", "TOUNFLOUTCH", "serveur_key", "serveur_key");
        pubk = CryptoUtils.getInstance().getPublicKey(pathserveur, "DER", "TOUNFLOUTCH", "serveur_key");

        String[] tmp = getChargeUtile().split(":");
        
        PublicKey  pubkClient = CryptoUtils.getInstance().Base64ToPublicKey(tmp[0]);
        
        byte[] signatureClient =  Base64.getDecoder().decode(tmp[1]);
        
        byte[] message = tmp[2].getBytes();
        
        
        
        boolean ok = CryptoUtils.getInstance().checkSignature(pubkClient, signatureClient, message);
        ReponseSPAYMAP rep;            
        if(ok)
        {
            String user = tmp[3];
            KeyRepository.getInstance().getKeysRepoByUser(user).setPublicHandshake(pubkClient);
            String nvmessage = RandomStringUtils.randomAlphabetic(8);
            byte[] signature = CryptoUtils.getInstance().createSignature(pk,nvmessage.getBytes());
            //envoie de la clé publique au serveur
            String env = CryptoUtils.getInstance().PublicKeyToBase64(pubk)+":"+Base64.getEncoder().encodeToString(signature)+":"+nvmessage;   
            rep = new ReponseSPAYMAP(ReponseSPAYMAP.OK, env );
        }
        else{
            rep = new ReponseSPAYMAP(ReponseSPAYMAP.ERREURSHAREPUBLICKEY, "erreur Verification clé publique" );
        }
        
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
        
    }
    public void traitementLSHARESECRETKEYAUTH(Socket sock, ConsoleServeur cs) throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        pk = CryptoUtils.getInstance().getPrivateKey(pathserveur, "JCEKS", "TOUNFLOUTCH", "serveur_key", "serveur_key");
        pubk = CryptoUtils.getInstance().getPublicKey(pathserveur, "DER", "TOUNFLOUTCH", "serveur_key");
        
        String[] tmp = getChargeUtile().split(":");
        
        byte[]  key = CryptoUtils.getInstance().getDechiffrement(pk,Base64.getDecoder().decode(tmp[0]));
        
        byte[] signatureClient =  Base64.getDecoder().decode(tmp[1]);
        
        String user = tmp[2];
        
        PublicKey pubkClient = KeyRepository.getInstance().getKeysRepoByUser(user).getPublicHandshake();
        
        boolean ok = CryptoUtils.getInstance().checkSignature(pubkClient, signatureClient, key);
        ReponseSPAYMAP rep;
        if(ok)
        {
            KeyRepository.getInstance().getKeysRepoByUser(user).setSecretAuth(new SecretKeySpec(key, CryptoUtils.codeSym));
            rep = new ReponseSPAYMAP(ReponseSPAYMAP.OK, "La clé secrète pour l'authentification a été partagé avec succès" );
        }
        else{
            rep = new ReponseSPAYMAP(ReponseSPAYMAP.ERREURAUTHENTIFICATIONKEY, "erreur verification clé publique" );
        }
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }
    public void TraitementLSHARESECRETKEYCRYPT(Socket sock, ConsoleServeur cs) throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, CertificateException, UnrecoverableKeyException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        pk = CryptoUtils.getInstance().getPrivateKey(pathserveur, "JCEKS", "TOUNFLOUTCH", "serveur_key", "serveur_key");
        pubk = CryptoUtils.getInstance().getPublicKey(pathserveur, "DER", "TOUNFLOUTCH", "serveur_key");
        
        String[] tmp = getChargeUtile().split(":");
        
        byte[]  key = CryptoUtils.getInstance().getDechiffrement(pk,Base64.getDecoder().decode(tmp[0]));
        
        byte[] signatureClient =  Base64.getDecoder().decode(tmp[1]);
        
        String user = tmp[2];
        
        PublicKey pubkClient = KeyRepository.getInstance().getKeysRepoByUser(user).getPublicHandshake();
        
        boolean ok = CryptoUtils.getInstance().checkSignature(pubkClient, signatureClient, key);
        ReponseSPAYMAP rep;
        if(ok)
        {
            KeyRepository.getInstance().getKeysRepoByUser(user).setSecretCrypt(new SecretKeySpec(key, CryptoUtils.codeAsym));
            rep = new ReponseSPAYMAP(ReponseSPAYMAP.OK, "La clé secrète pour l'authentification a été partagé avec succès" );
        }
        else{
            rep = new ReponseSPAYMAP(ReponseSPAYMAP.ERREURAUTHENTIFICATIONKEY, "erreur verification clé publique" );
        }
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }
    public void TraitementLGETRESERVATIONS(Socket sock, ConsoleServeur cs) throws SQLException, ClassNotFoundException
    {
        System.out.println("JE SUIS DANS TraitementLROOMS");
        ReponseSPAYMAP rep;

        ResultSet rs = BDHolidays.getInstance().getAllChambreReservePasPaye();
        String s = "";

        while (rs.next()) {
            s = s + rs.getString("idreservations") + " " + rs.getString("nom") + " " + rs.getString("id_chambre") + " " + rs.getString("date_debut") + " " +rs.getString("prix_net")+" "+rs.getString("resteapayer")+":";
        }
        rep = new ReponseSPAYMAP(ReponseSPAYMAP.OK, s);
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }
    
    public void TraitementLPAYRESERVATIONS(Socket sock, ConsoleServeur cs) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, SQLException
    {
        System.out.println("JE SUIS DANS TraitementLPAYRESERVATIONS");
        ReponseSPAYMAP rep;
        String[] tmp = getChargeUtile().split(":");
        
        String user = tmp[0];
        byte[] encryptMessage = Base64.getDecoder().decode(tmp[1]);
        byte[] signature = Base64.getDecoder().decode(tmp[2]);
        
        
        SecretKey skc = KeyRepository.getInstance().getKeysRepoByUser(user).getSecretCrypt();
        byte[] message = CryptoUtils.getInstance().getDechiffrement(skc, encryptMessage);
        
        if(CryptoUtils.getInstance().checkSignature(KeyRepository.getInstance().getKeysRepoByUser(user).getPublicHandshake(), signature, message))
        {
            String[] newMessage = new String(message).split(";");
            if(BDHolidays.getInstance().PayReservation( Integer.parseInt(newMessage[0]) , Double.parseDouble(newMessage[1]) ))
            {
                rep = new ReponseSPAYMAP(ReponseSPAYMAP.OK, "Le paiement a été effectué avec succès");
            }
            else
            {
                rep = new ReponseSPAYMAP(ReponseSPAYMAP.PAIEMENTREFUSE, "Le paiement n'a pas pu se faire");
            }
        }
        else
        {
            rep = new ReponseSPAYMAP(ReponseSPAYMAP.PAIEMENTREFUSE, "La vérification de la signature a été refusé");
        }
        
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
        
    }

}
