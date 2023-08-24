/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Application_Paiements;

import ProtocoleSPAYMAP.ReponseSPAYMAP;
import ProtocoleSPAYMAP.RequeteSPAYMAP;
import Utilities.Configuration;
import Utilities.CryptoUtils;
import Utilities.Facture;
import Utilities.RequeteUtils;
import Utilities.User;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;
import org.bouncycastle.crypto.CryptoException;

/**
 *
 * @author Salva
 */
public class PayerReservation extends javax.swing.JDialog {

    /**
     * Creates new form PayerReservation
     */
    private Facture facture;
    public File currentDirectory = new File(System.getProperty("user.dir"));
    public Configuration config;
    private Socket cliSock;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private PaiementReservation pr;
    
    public PayerReservation(java.awt.Frame parent, boolean modal, Facture f, Configuration c, PaiementReservation p) {
        super(parent, modal);
        initComponents();
        config = c;
        facture = f;
        pr = p;
        initFacture();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jcreditcard = new javax.swing.JTextField();
        jmontant = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        laccompte = new javax.swing.JLabel();
        lreste = new javax.swing.JLabel();
        lprix = new javax.swing.JLabel();
        lreservation = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("N°Réservation");

        jLabel3.setText("Prix");

        jLabel6.setText("Accompte");

        jLabel7.setText("Reste à payer");

        jLabel11.setText("Montant ");

        jLabel12.setText("Carte de crédit ");

        jcreditcard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcreditcardActionPerformed(evt);
            }
        });

        jmontant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmontantActionPerformed(evt);
            }
        });

        jButton2.setText("PAYER");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(laccompte, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jmontant, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcreditcard, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lreste, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lprix, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lreservation, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addComponent(jButton2)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel11, jLabel12, jLabel3, jLabel6, jLabel7});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jmontant, laccompte, lprix, lreservation, lreste});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(37, 37, 37))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lreservation))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lprix)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(laccompte)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lreste)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jmontant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcreditcard))
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(10, 10, 10))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jmontant, laccompte, lprix, lreservation, lreste});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jcreditcardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcreditcardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcreditcardActionPerformed

    private void jmontantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmontantActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jmontantActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            //PAYER
            double montant = Double.parseDouble(jmontant.getText());
            double reste = facture.getResteapayer();
            String pathClient = currentDirectory + "\\..\\KeyStore\\Client\\client_keystore.jce";
            PrivateKey pk = CryptoUtils.getInstance().getPrivateKey(pathClient, "JCEKS", "TOUNFLOUTCH", "client_key", "client_key");
            RequeteSPAYMAP req;
            ois = null;
            oos = null;
            System.out.println(montant+"::"+reste);
            if(montant <= reste)
            {
                ois = null;
                oos = null;
                double restaapayer = reste - montant;
                //chiffrer symétrique
                String message =facture.getReservation()+";"+restaapayer +";"+ jcreditcard.getText();
                try {
                    byte[] encryptmessage = CryptoUtils.getInstance().getChiffrement(User.getInstance().getMykeys().getSecretCrypt(), message.getBytes());
                    //créer signature
                    byte[] signature = CryptoUtils.getInstance().createSignature(pk, message.getBytes());
                    
                    String env = User.getInstance().getUsername()+":"+Base64.getEncoder().encodeToString(encryptmessage) + ":" + Base64.getEncoder().encodeToString(signature);
                    cliSock = new Socket(config.getAdresse(), config.getPort());
                    req = new RequeteSPAYMAP(RequeteSPAYMAP.LPAYRESERVATIONS, env);
                    oos = new ObjectOutputStream(cliSock.getOutputStream());
                    RequeteUtils.SendRequest(req, "LPAYRESERVATIONS", oos, cliSock);
                    ois = new ObjectInputStream(cliSock.getInputStream());
                    ReponseSPAYMAP rep = (ReponseSPAYMAP) RequeteUtils.ReceiveRequest(cliSock, ois, "SPAYMAP");
                    
                    cliSock.close();
                    oos.close();
                    ois.close();
                    
                    if(rep.getCode() == ReponseSPAYMAP.OK)
                    {
                        JOptionPane.showMessageDialog(null, "Le paiement a été effectué !", "CAUTION ! ", JOptionPane.INFORMATION_MESSAGE);
                        pr.fillTableAction_reservation();      
                        this.dispose();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, rep.getChargeUtile(), "CAUTION ! ", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | SignatureException | ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(PayerReservation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Veuillez saisir un montant inférieur ou égal au reste à payer ", "CAUTION ! ", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException ex) {
            Logger.getLogger(PayerReservation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField jcreditcard;
    private javax.swing.JTextField jmontant;
    private javax.swing.JLabel laccompte;
    private javax.swing.JLabel lprix;
    private javax.swing.JLabel lreservation;
    private javax.swing.JLabel lreste;
    // End of variables declaration//GEN-END:variables

    private void initFacture() {
        lreservation.setText(facture.getReservation());
        lprix.setText(Double.toString(facture.getPrix()));
        laccompte.setText(Double.toString(facture.getAccompte()));
        lreste.setText(Double.toString(facture.getResteapayer()));
        
    }  
}
