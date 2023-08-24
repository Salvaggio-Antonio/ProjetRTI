/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serveurs;

import Serveurs.ConsoleServeur;
import Serveurs.ListeTaches;
import Serveurs.SourceTaches;
import Serveurs.ThreadServeur;
import Utilities.Utils;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Anto
 */

public class FenAppServeur_Reservations extends javax.swing.JFrame implements ConsoleServeur {

    private int port;
    
    File currentDirectory = new File(System.getProperty("user.dir"));
    
    public String path = currentDirectory+"\\src\\Config\\Config.config";

    public FenAppServeur_Reservations() throws IOException {
        initComponents();
        
        port = Integer.parseInt(Utils.getItemConfig(path, "PORT_RESERVATIONS"));
        
    }

        @Override
        public void TraceEvenements
        (String commentaire
        
            ) {
        Vector ligne = new Vector();
            StringTokenizer parser = new StringTokenizer(commentaire, "#");
            while (parser.hasMoreTokens()) {
                ligne.add(parser.nextToken());
            }
            DefaultTableModel dtm = (DefaultTableModel) TableauEvenements.getModel();
            dtm.insertRow(dtm.getRowCount(), ligne);
        }

        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        TableauEvenements = new javax.swing.JTable();
        BStart = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TableauEvenements.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane1.setViewportView(TableauEvenements);

        BStart.setText("Start");
        BStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BStartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 632, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BStart))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(BStart)
                .addContainerGap(193, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BStartActionPerformed
      
        try {
            TraceEvenements("serveur#acquisition du port#main");
            int maxthread = Integer.parseInt(Utils.getItemConfig(path, "maxthreadPaiement"));
            ThreadServeur ts = new ThreadServeur(port,  new ListeTaches(), this, maxthread);
            ts.start();
        } catch (IOException ex) {
            Logger.getLogger(FenAppServeur_Reservations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_BStartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FenAppServeur_Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FenAppServeur_Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FenAppServeur_Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FenAppServeur_Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new FenAppServeur_Reservations().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(FenAppServeur_Reservations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BStart;
    private javax.swing.JTable TableauEvenements;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
