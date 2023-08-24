/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.security.PublicKey;
import javax.crypto.SecretKey;

/**
 *
 * @author Salva
 */
public class KeysUser {

    private SecretKey secretAuth;
    private SecretKey secretCrypt;
    private PublicKey publicHandshake;

    public KeysUser() {
    }
   

    public PublicKey getPublicHandshake() {
        return publicHandshake;
    }

    public void setPublicHandshake(PublicKey publicHandshake) {
        this.publicHandshake = publicHandshake;
    }

    public KeysUser(SecretKey secretAuth, SecretKey secretCrypt, PublicKey publicHandshake) {
        this.secretAuth = secretAuth;
        this.secretCrypt = secretCrypt;
        this.publicHandshake = publicHandshake;
    }

    public KeysUser(SecretKey secretAuth, SecretKey secretCrypt) {
        this.secretAuth = secretAuth;
        this.secretCrypt = secretCrypt;
    }

    public SecretKey getSecretAuth() {
        return secretAuth;
    }

    public void setSecretAuth(SecretKey secretAuth) {
        this.secretAuth = secretAuth;
    }

    public SecretKey getSecretCrypt() {
        return secretCrypt;
    }

    public void setSecretCrypt(SecretKey secretCrypt) {
        this.secretCrypt = secretCrypt;
    }

}
