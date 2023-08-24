/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client_Reservation;

import Clients_Activite.LoginActivite;
import ProtocoleROMP.RequeteROMP;
import Requete.Reponse;
import Utilities.Configuration;
import Utilities.RequeteUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Salva
 */
public class AllChambresReserves extends javax.swing.JDialog {

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    public String id;
    public int duree;
    public Configuration config;
    /**
     * Creates new form AllChambresReserves
     */
    public AllChambresReserves(java.awt.Frame parent, boolean modal, Configuration c) {
        super(parent, modal);
        initComponents();
        config = c;


        initChambresReserve();
        
    }
    
    void initChambresReserve(){
        RequeteROMP req = null;
        try {
            req = new RequeteROMP(RequeteROMP.LROOMS,  "" );
            ois=null; oos=null; cliSock = null;
            try
            {
                cliSock = new Socket(config.getAdresse(), config.getPort());
                System.out.println(cliSock.getInetAddress().toString());
            }                       
            catch (IOException ex) {
                Logger.getLogger(LoginActivite.class.getName()).log(Level.SEVERE, null, ex);
            }
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            
            
            RequeteUtils.SendRequest(req, "LROOMS", oos, cliSock);
            
            // Lecture de la réponse
            Reponse rep;
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = RequeteUtils.ReceiveRequest(cliSock, ois, "ROMP");
            
            String[] recu = rep.getChargeUtile().split(":");
            DefaultListModel listModel = new DefaultListModel();
            for(int i=0; i< recu.length; i++){
                listModel.addElement(recu[i]);
            }
            jList1.setModel(listModel);

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LoginActivite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AllChambresReserves.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("Liste des reservations ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jLabel1)))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
