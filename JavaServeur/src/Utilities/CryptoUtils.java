/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import javax.crypto.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Salva
 */
public class CryptoUtils {

    public static String codeProvider = "BC";
    public static String codeAsym = "RSA";
    public static String codeSym = "DES";
    private static CryptoUtils instance;

    private CryptoUtils() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public byte[] createMessageDigest(String user, String password, long temps, double alea) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {

        MessageDigest md = MessageDigest.getInstance("SHA-1", codeProvider);
        md.update(user.getBytes());
        md.update(password.getBytes());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream bdos = new DataOutputStream(baos);
        bdos.writeLong(temps);
        bdos.writeDouble(alea);
        md.update(baos.toByteArray());
        return md.digest();
    }

    public PrivateKey getPrivateKey(String path, String typeKeystore, String pwdkeystore, String alias, String pwdkey) throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        KeyStore ks;
        ks = KeyStore.getInstance(typeKeystore);
        ks.load(new FileInputStream(path),
                pwdkeystore.toCharArray());
        PrivateKey clePrivee;
        clePrivee = (PrivateKey) ks.getKey(alias, pwdkey.toCharArray());

        return clePrivee;
    }

    public PublicKey getPublicKey(String path, String typeKeystore, String pwdkeystore, String alias) throws KeyStoreException, NoSuchProviderException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore ksv;
        ksv = KeyStore.getInstance("JCEKS");

        ksv.load(new FileInputStream(path),
                pwdkeystore.toCharArray());
        X509Certificate certif = (X509Certificate) ksv.getCertificate(alias);
        PublicKey clePublique = certif.getPublicKey();

        return clePublique;
    }

    public String PublicKeyToBase64(PublicKey pubk) {
        byte[] publicKeyBytes = pubk.getEncoded();
        return Base64.getEncoder().encodeToString(publicKeyBytes);
    }

    public PublicKey Base64ToPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(key);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(keySpec);
    }

    public byte[] createSignature(PrivateKey pk, byte []message) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature s = Signature.getInstance("SHA1withRSA", codeProvider);
        s.initSign(pk);
        s.update(message);
        return s.sign();
    }

    public boolean checkSignature(PublicKey pk, byte[] signature, byte[] message) {
        try {
            Signature s = Signature.getInstance("SHA1withRSA", codeProvider);
            s.initVerify(pk);
            s.update(message);
            return s.verify(signature);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public SecretKey generateSecretKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator cleGen = KeyGenerator.getInstance(codeSym, codeProvider);
        cleGen.init(new SecureRandom());
        
        return cleGen.generateKey();
    }

    public byte[] getChiffrement(SecretKey cle, byte[] message) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher chiffrement = Cipher.getInstance(codeSym,
                codeProvider);
        chiffrement.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrement.doFinal(message);
    }
    
    public byte[] getChiffrement(PublicKey cle, byte[] message) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher chiffrement = Cipher.getInstance(codeAsym,
                codeProvider);
        chiffrement.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrement.doFinal(message);
    }
    
    public byte[] getDechiffrement(SecretKey cle, byte[] message) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher chiffrement = Cipher.getInstance(codeSym,
                codeProvider);
        chiffrement.init(Cipher.DECRYPT_MODE, cle);
        return chiffrement.doFinal(message);
    }
    
    public byte[] getDechiffrement(PrivateKey cle, byte[] message) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher chiffrement = Cipher.getInstance(codeAsym,
                codeProvider);
        chiffrement.init(Cipher.DECRYPT_MODE, cle);
        return chiffrement.doFinal(message);
    }
    
    public byte[] createHMAC(SecretKey secretKey, byte [] message) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256", codeProvider);
            hmac.init(secretKey);
            hmac.update(message);
            
            return hmac.doFinal();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

        


    public static CryptoUtils getInstance() {
        if (instance == null) {
            instance = new CryptoUtils();
        }
        return instance;
    }
}
