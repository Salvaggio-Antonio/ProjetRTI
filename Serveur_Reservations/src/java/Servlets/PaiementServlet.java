/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlets;

import Application_Paiements.PaiementReservation;
import Holidays.BDHolidays;
import ProtocoleSPAYMAP.ReponseSPAYMAP;
import ProtocoleSPAYMAP.RequeteSPAYMAP;
import Utilities.Configuration;
import Utilities.CryptoUtils;
import Utilities.KeysUser;
import Utilities.RequeteUtils;
import Utilities.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Salva
 */
public class PaiementServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        try {

            response.setContentType("text/html;charset=UTF-8");
            HttpSession session = request.getSession(true);
            Boolean Trouve = false;
            String msg = "hello";
            String user = (String) request.getSession().getAttribute("user");
            String currentDirectory = "C:\\Users\\Salva\\Documents\\ecole\\important\\3emeinformatique\\rti\\ProjetRTI\\JavaServeur\\";
            String path = "C:\\Users\\Salva\\Documents\\ecole\\important\\3emeinformatique\\rti\\ProjetRTI\\JavaServeur" + ".\\src\\Config\\Config.config";
            Configuration config = new Configuration(path, "PORT_PAY");
            if (user == null || user.equals("")) {

                response.sendRedirect(request.getContextPath() + "/Login.jsp");
            }
            String mdp = (String) request.getSession().getAttribute("mdp");
            String carte = (String) request.getParameter("credit");
            Double montant = Double.valueOf((String) request.getParameter("montant"));
            ResultSet r = BDHolidays.getInstance().getVoyageursByEmail(user);
            int reservation = Integer.parseInt(request.getParameter("reservation"));

            long temps = (new Date()).getTime();
            double alea = Math.random();
            RequeteSPAYMAP req;
            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;

            String password = mdp;

            byte[] messageDigest = CryptoUtils.getInstance().createMessageDigest(user, password, temps, alea);
            String env = user + ":" + temps + ":" + alea + ":" + Base64.getEncoder().encodeToString(messageDigest);
            req = new RequeteSPAYMAP(RequeteSPAYMAP.LPAYCLIENT, env);

            Socket cliSock = new Socket(config.getAdresse(), config.getPort());
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            //---------------- LOGIN--------------------------//
            RequeteUtils.SendRequest(req, "LPAYCLIENT", oos, cliSock);

            ois = new ObjectInputStream(cliSock.getInputStream());
            ReponseSPAYMAP rep = (ReponseSPAYMAP) RequeteUtils.ReceiveRequest(cliSock, ois, "SPAYMAP");
            cliSock.close();
            oos.close();
            ois.close();

            if (rep.getCode() == ReponseSPAYMAP.LOGIN_OK) {
                if (r != null) {
                    ResultSet rs = BDHolidays.getInstance().getReservationById(reservation);
                    if (rs != null && rs.next()) {
                        Double resteapayer = rs.getDouble("resteapayer");
                        if (montant <= resteapayer) {

                            String pathClient = "C:\\Users\\Salva\\Documents\\ecole\\important\\3emeinformatique\\rti\\ProjetRTI\\KeyStore\\Client\\client_keystore.jce";
                            PrivateKey pk = CryptoUtils.getInstance().getPrivateKey(pathClient, "JCEKS", "TOUNFLOUTCH", "client_key", "client_key");
                            PublicKey pubk = CryptoUtils.getInstance().getPublicKey(pathClient, "DER", "TOUNFLOUTCH", "client_key");
                            //signature
                            String message = RandomStringUtils.randomAlphabetic(8);
                            byte[] signature = CryptoUtils.getInstance().createSignature(pk, message.getBytes());

                            env = CryptoUtils.getInstance().PublicKeyToBase64(pubk) + ":" + Base64.getEncoder().encodeToString(signature) + ":" + message + ":" + user;
                            req = new RequeteSPAYMAP(RequeteSPAYMAP.LSHAREPUBLICKEY, env);
                            cliSock = new Socket(config.getAdresse(), config.getPort());
                            oos = new ObjectOutputStream(cliSock.getOutputStream());
                            RequeteUtils.SendRequest(req, "LSHAREPUBLICKEY", oos, cliSock);

                            ois = new ObjectInputStream(cliSock.getInputStream());
                            rep = (ReponseSPAYMAP) RequeteUtils.ReceiveRequest(cliSock, ois, "SPAYMAP");

                            if (rep.getCode() == ReponseSPAYMAP.OK) {

                                //vérifier la signature du serveur
                                String[] tmp = rep.getChargeUtile().split(":");

                                PublicKey pubkServeur = CryptoUtils.getInstance().Base64ToPublicKey(tmp[0]);

                                byte[] signatureServeur = Base64.getDecoder().decode(tmp[1]);

                                byte[] messageserveur = tmp[2].getBytes();

                                boolean ok = CryptoUtils.getInstance().checkSignature(pubkServeur, signatureServeur, messageserveur);

                                if (ok) {
                                    cliSock.close();
                                    oos.close();
                                    ois.close();
                                    //génération 2 clé de session (symétrique)
                                    KeysUser key = new KeysUser(CryptoUtils.getInstance().generateSecretKey(), CryptoUtils.getInstance().generateSecretKey(), pubkServeur);
                                    User.getInstance().setUser(user, key);
                                    //partage de la clé symétriqueAuth
                                    cliSock = new Socket(config.getAdresse(), config.getPort());
                                    byte[] m = User.getInstance().getMykeys().getSecretAuth().getEncoded();
                                    byte[] keysymmetric = CryptoUtils.getInstance().getChiffrement(pubkServeur, m);
                                    signature = CryptoUtils.getInstance().createSignature(pk, m);
                                    env = Base64.getEncoder().encodeToString(keysymmetric) + ":" + Base64.getEncoder().encodeToString(signature) + ":" + user;
                                    req = new RequeteSPAYMAP(RequeteSPAYMAP.LSHARESECRETKEYAUTH, env);
                                    oos = new ObjectOutputStream(cliSock.getOutputStream());
                                    RequeteUtils.SendRequest(req, "LSHARESECRETKEYAUTH", oos, cliSock);

                                    ois = new ObjectInputStream(cliSock.getInputStream());
                                    rep = (ReponseSPAYMAP) RequeteUtils.ReceiveRequest(cliSock, ois, "SPAYMAP");

                                    if (rep.getCode() == ReponseSPAYMAP.OK) {
                                        cliSock.close();
                                        oos.close();
                                        ois.close();
                                        //partage de la clé symétriqueCrypt
                                        cliSock = new Socket(config.getAdresse(), config.getPort());
                                        m = User.getInstance().getMykeys().getSecretCrypt().getEncoded();
                                        keysymmetric = CryptoUtils.getInstance().getChiffrement(pubkServeur, m);
                                        signature = CryptoUtils.getInstance().createSignature(pk, m);
                                        env = Base64.getEncoder().encodeToString(keysymmetric) + ":" + Base64.getEncoder().encodeToString(signature) + ":" + user;
                                        req = new RequeteSPAYMAP(RequeteSPAYMAP.LSHARESECRETKEYCRYPT, env);
                                        oos = new ObjectOutputStream(cliSock.getOutputStream());
                                        RequeteUtils.SendRequest(req, "LSHARESECRETKEYCRYPT", oos, cliSock);
                                        ois = new ObjectInputStream(cliSock.getInputStream());
                                        rep = (ReponseSPAYMAP) RequeteUtils.ReceiveRequest(cliSock, ois, "SPAYMAP");
                                        cliSock.close();
                                        oos.close();
                                        ois.close();
                                        if (rep.getCode() == ReponseSPAYMAP.OK) {
                                            ois = null;
                                            oos = null;
                                            //chiffrer symétrique
                                            message = Integer.valueOf(request.getParameter("reservation")) + ";" + montant + ";" + carte;

                                            byte[] encryptmessage = CryptoUtils.getInstance().getChiffrement(User.getInstance().getMykeys().getSecretCrypt(), message.getBytes());
                                            //créer signature
                                            signature = CryptoUtils.getInstance().createSignature(pk, message.getBytes());

                                            env = User.getInstance().getUsername() + ":" + Base64.getEncoder().encodeToString(encryptmessage) + ":" + Base64.getEncoder().encodeToString(signature);
                                            cliSock = new Socket(config.getAdresse(), config.getPort());
                                            req = new RequeteSPAYMAP(RequeteSPAYMAP.LPAYRESERVATIONS, env);
                                            oos = new ObjectOutputStream(cliSock.getOutputStream());
                                            RequeteUtils.SendRequest(req, "LPAYRESERVATIONS", oos, cliSock);
                                            ois = new ObjectInputStream(cliSock.getInputStream());
                                            rep = (ReponseSPAYMAP) RequeteUtils.ReceiveRequest(cliSock, ois, "SPAYMAP");

                                            cliSock.close();
                                            oos.close();
                                            ois.close();

                                            if (rep.getCode() == ReponseSPAYMAP.OK) {
                                                tmp = rep.getChargeUtile().split(":");
                                                byte[] mess = Base64.getDecoder().decode(tmp[0]);
                                                byte[] hmac = Base64.getDecoder().decode(tmp[1]);

                                                mess = CryptoUtils.getInstance().getDechiffrement(User.getInstance().getMykeys().getSecretCrypt(), mess);

                                                byte[] hmaclocal = CryptoUtils.getInstance().createHMAC(User.getInstance().getMykeys().getSecretAuth(), mess);

                                                if (MessageDigest.isEqual(hmac, hmaclocal)) {
                                                    String[] messNonChiffre = new String(mess).split(":");
                                                    msg =  "Le paiement a été effectué ! il reste : " + messNonChiffre[0] + " voici le numéro de transaction financière : " + messNonChiffre[1];
                                                    if (Double.parseDouble(messNonChiffre[0]) == 0) {
                                                        encryptmessage = CryptoUtils.getInstance().getChiffrement(User.getInstance().getMykeys().getSecretCrypt(), request.getParameter("reservation").getBytes());
                                                        hmac = CryptoUtils.getInstance().createHMAC(User.getInstance().getMykeys().getSecretAuth(), request.getParameter("reservation").getBytes());
                                                        message = User.getInstance().getUsername() + ":" + Base64.getEncoder().encodeToString(encryptmessage) + ":" + Base64.getEncoder().encodeToString(hmac);
                                                        cliSock = new Socket(config.getAdresse(), config.getPort());
                                                        req = new RequeteSPAYMAP(RequeteSPAYMAP.LGETFACTURE, message);
                                                        oos = new ObjectOutputStream(cliSock.getOutputStream());
                                                        RequeteUtils.SendRequest(req, "LGETFACTURE", oos, cliSock);

                                                        ois = new ObjectInputStream(cliSock.getInputStream());
                                                        rep = (ReponseSPAYMAP) RequeteUtils.ReceiveRequest(cliSock, ois, "SPAYMAP");

                                                        if (rep.getCode() == ReponseSPAYMAP.OK) {
                                                            tmp = rep.getChargeUtile().split(":");
                                                            mess = Base64.getDecoder().decode(tmp[0]);
                                                            signature = Base64.getDecoder().decode(tmp[1]);

                                                            message = new String(CryptoUtils.getInstance().getDechiffrement(pk, mess));

                                                            if (CryptoUtils.getInstance().checkSignature(User.getInstance().getMykeys().getPublicHandshake(), signature, mess)) {
                                                                msg = msg + "\n\n\n"+message;

                                                            } else {
                                                                msg = "erreur signature";
                                                            }
                                                        } else {
                                                            msg = "erreur facture";
                                                        }
                                                        cliSock.close();
                                                        oos.close();
                                                        ois.close();
                                                    }
                                                } else {
                                                    msg = "Erreur d'autthentification !";
                                                }
                                            }
                                        } else {
                                            msg = "Le montant est supérieur a ce qu'il reste à payer";
                                        }

                                    } else {
                                        msg = "IdReservation ";
                                    }
                                }

                                session.setAttribute("reppay", msg);
                                response.sendRedirect(request.getContextPath() + "/ReponsePaiement.jsp");

                            }
                        }

                    }

                }
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(PaiementServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet de paiement";
    }

}
