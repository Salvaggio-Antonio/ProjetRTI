/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Application_Paiements;

import ProtocoleSPAYMAP.ReponseSPAYMAP;
import ProtocoleSPAYMAP.RequeteSPAYMAP;
import Utilities.Configuration;
import Utilities.Facture;
import Utilities.RequeteUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Salva
 */
public class PaiementReservation extends javax.swing.JFrame {

    /**
     * Creates new form PaiementReservation
     */
    private Configuration config;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    
    public PaiementReservation(Configuration c) {
        initComponents();
        config = c;
        try {
            fillTableAction_reservation();
        } catch (SQLException ex) {
            Logger.getLogger(PaiementReservation.class.getName()).log(Level.SEVERE, null, ex);
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
        table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N°réservation", "Name", "N°Chambre", "Date de début", "Prix", "Accompte"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(2).setResizable(false);
            table.getColumnModel().getColumn(3).setResizable(false);
            table.getColumnModel().getColumn(4).setResizable(false);
            table.getColumnModel().getColumn(5).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        int index = table.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Double prix =   (Double) model.getValueAt(index, 4);
        Double acc =  (Double) model.getValueAt(index, 5);
        Facture f= new Facture((String) model.getValueAt(index, 0),prix , acc, prix - acc);
        PayerReservation p = new PayerReservation(this, true, f, config, this);
        p.setVisible(true);
    }//GEN-LAST:event_tableMouseClicked

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    
    public void fillTableAction_reservation() throws SQLException {
        RequeteSPAYMAP req;
        try {
            req = new RequeteSPAYMAP(RequeteSPAYMAP.LGETRESERVATIONS,"");

            ois = null;
            oos = null;
            cliSock = null;
            try {
                cliSock = new Socket(config.getAdresse(), config.getPort());
                System.out.println(cliSock.getInetAddress().toString());
            } catch (IOException ex) {
                Logger.getLogger(PaiementReservation.class.getName()).log(Level.SEVERE, null, ex);
            }
            oos = new ObjectOutputStream(cliSock.getOutputStream());

            // Envoie de la requête
            RequeteUtils.SendRequest(req, "LGETRESERVATIONS", oos, cliSock);
            
            ois = new ObjectInputStream(cliSock.getInputStream());
            // Lecture de la réponse
            ReponseSPAYMAP rep;
            rep = (ReponseSPAYMAP) RequeteUtils.ReceiveRequest(cliSock, ois, "SPAYMAP");

            String[] tmp = rep.getChargeUtile().split(":");

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            double accompte;
            for (int i = 0; i < tmp.length; i++) {
                String[] champs = tmp[i].split(" ");
                accompte = Double.parseDouble(champs[4]) - Double.parseDouble(champs[5]); 
                model.addRow(new Object[]{champs[0], champs[1], champs[2], champs[3], Double.valueOf(champs[4]),accompte});
            }
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(PaiementReservation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}