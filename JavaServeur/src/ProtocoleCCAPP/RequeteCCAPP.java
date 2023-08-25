/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProtocoleCCAPP;

import Holidays.BDCard;
import Requete.Requete;
import Serveurs.ConsoleServeur;
import Utilities.Configuration;
import Utilities.CryptoUtils;
import Utilities.ServerKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.SQLException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Salva
 */
public class RequeteCCAPP implements Requete, Serializable {

    private int type;
    private Socket socketClient;
    public String chargeUtile;
        //File currentDirectory = new File(System.getProperty("user.dir"));
    public String currentDirectory = "C:\\Users\\Salva\\Documents\\ecole\\important\\3emeinformatique\\rti\\ProjetRTI\\JavaServeur";

    public String path = currentDirectory + "\\src\\Config\\Config.config";
    String pathserveur = currentDirectory + "\\..\\KeyStore\\Serveur\\card_store.jce";

    public static final int CHANDSHAKE = 1;
    public static final int CVERIF = 2;

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
            case CHANDSHAKE:
                return new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TraitementHANDSHAKE(s, cs);
                        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | NoSuchProviderException | InvalidKeySpecException | InvalidKeyException | SignatureException ex) {
                            Logger.getLogger(RequeteCCAPP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            case CVERIF:
                return new Runnable() {
                    public void run() {
                        try {
                            TraitementCVERIF(s, cs);
                        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | NoSuchProviderException | UnrecoverableKeyException | InvalidKeyException | SignatureException | ClassNotFoundException | SQLException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
                            Logger.getLogger(RequeteCCAPP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            default:
                return null;
        }
    }

    public void TraitementCVERIF(Socket s, ConsoleServeur cs) throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, NoSuchProviderException, UnrecoverableKeyException, InvalidKeyException, SignatureException, ClassNotFoundException, SQLException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        ObjectOutputStream oos;
        LocalDate currentDate = LocalDate.now();
        pk = CryptoUtils.getInstance().getPrivateKey(pathserveur, "JCEKS", "TOUNFLOUTCH", Utilities.Utils.getDayNameWithK(currentDate.getDayOfWeek()), Utilities.Utils.getDayNameWithK(currentDate.getDayOfWeek()));
        pubk = CryptoUtils.getInstance().getPublicKey(pathserveur, "DER", "TOUNFLOUTCH", Utilities.Utils.getDayNameWithK(currentDate.getDayOfWeek()));

        ReponseCCAPP re;
        Configuration config = new Configuration(path, "PORT_CARD");
        String[] tmp = getChargeUtile().split(":");
        byte[] messagechiffre = Base64.getDecoder().decode(tmp[0]);
        byte[] signatureSPAYMAP = Base64.getDecoder().decode(tmp[1]);

        byte[] messagedechiffre = CryptoUtils.getInstance().getDechiffrement(pk, messagechiffre);

        boolean ok = CryptoUtils.getInstance().checkSignature(ServerKey.getInstance().getPk(), signatureSPAYMAP, messagedechiffre);

        if (ok) {
            String[] user = new String(messagedechiffre).split(":");
            String name = user[0];
            String card = user[1];

            if (isValidBECreditCard(card)) {
                re = new ReponseCCAPP(ReponseCCAPP.OK, "ok");
                int userId = BDCard.getInstance().getUserIdByName(name);
                if (userId == -1) {
                    // Créer l'utilisateur s'il n'existe pas
                    BDCard.getInstance().createUser(name);
                }
                // Vérifier si la carte existe pour l'utilisateur
                if (!BDCard.getInstance().cardExistsForUser(name, card)) {
                    // Créer une nouvelle carte pour l'utilisateur
                    BDCard.getInstance().createCardForUser(name, card);
                }
            } else {
                re = new ReponseCCAPP(ReponseCCAPP.WRONG_CREDITCARD, "carte bancaire pas valide");
            }
        } else {
            re = new ReponseCCAPP(ReponseCCAPP.ERR_SIGNATURE, "SIGNATUR pas valide");
        }

        try {
            oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(re);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }

    public static boolean isValidBECreditCard(String cardNumber) {
        return cardNumber.matches("BE\\d{10}");
    }

    public void TraitementHANDSHAKE(Socket s, ConsoleServeur cs) throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, NoSuchProviderException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        LocalDate currentDate = LocalDate.now();
        System.out.println(Utilities.Utils.getDayNameWithK(currentDate.getDayOfWeek()));
        pk = CryptoUtils.getInstance().getPrivateKey(pathserveur, "JCEKS", "TOUNFLOUTCH", Utilities.Utils.getDayNameWithK(currentDate.getDayOfWeek()), Utilities.Utils.getDayNameWithK(currentDate.getDayOfWeek()));
        pubk = CryptoUtils.getInstance().getPublicKey(pathserveur, "DER", "TOUNFLOUTCH", Utilities.Utils.getDayNameWithK(currentDate.getDayOfWeek()));

        String[] tmp = getChargeUtile().split(":");

        PublicKey pubkSPAYMAP = CryptoUtils.getInstance().Base64ToPublicKey(tmp[0]);

        byte[] signatureClient = Base64.getDecoder().decode(tmp[1]);

        byte[] message = tmp[2].getBytes();

        boolean ok = CryptoUtils.getInstance().checkSignature(pubkSPAYMAP, signatureClient, message);
        ReponseCCAPP rep;
        if (ok) {
            ServerKey.getInstance().setPk(pubkSPAYMAP);

            String nvmessage = RandomStringUtils.randomAlphabetic(8);
            byte[] signature = CryptoUtils.getInstance().createSignature(pk, nvmessage.getBytes());
            //envoie de la clé publique au serveur
            String env = CryptoUtils.getInstance().PublicKeyToBase64(pubk) + ":" + Base64.getEncoder().encodeToString(signature) + ":" + nvmessage;
            rep = new ReponseCCAPP(ReponseCCAPP.OK, env);
        } else {
            rep = new ReponseCCAPP(ReponseCCAPP.ERREURSHAREPUBLICKEY, "erreur Verification clé publique");
        }

        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }
}
