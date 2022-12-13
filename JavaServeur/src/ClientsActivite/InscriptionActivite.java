/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientsActivite;

import ProtocoleFUCAMP.ReponseFUCAMP;
import ProtocoleFUCAMP.RequeteFUCAMP;
import Utilities.Utils;
import database.facility.BDHolidays;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Salva
 */
public class InscriptionActivite extends javax.swing.JDialog {
    
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    public String path = "C:\\Users\\Salva\\Documents\\ecole\\important\\3emeinformatique\\rti\\Inpres-Enjoy your holidays\\JavaServeur\\src\\Config\\Config.config";
    public String id;
    public int duree;

    /**
     * Creates new form NewJDialog
     */
    public InscriptionActivite(java.awt.Frame parent, boolean modal, String id, String nom, String type, String duree) throws IOException, ClassNotFoundException, SQLException {
        super(parent, modal);
        initComponents();
        this.id = id;
        jLabelId.setText("id: "+id);
        jLabelNom.setText("Nom: "+ nom);
        jLabelType.setText("type: "+type);
        jLabelDuree.setText(duree +" jours");
        this.duree = Integer.parseInt(duree);
       
        
        //Connexion();
        initParticipants();
    }

    private InscriptionActivite(JFrame jFrame, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelId = new javax.swing.JLabel();
        jLabelNom = new javax.swing.JLabel();
        jLabelType = new javax.swing.JLabel();
        jLabelDuree = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextEmail = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextNom = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextJour = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextMois = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextAnnee = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableP = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelId.setText("id");

        jLabelNom.setText("Nom");

        jLabelType.setText("Type ");

        jLabelDuree.setText("Duree");

        jLabel5.setText("Liste des participants");

        jLabel6.setText("Inscription :");

        jLabel7.setText("Email");

        jLabel8.setText("Nom");

        jLabel9.setText("Date : ");

        jTextJour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextJourActionPerformed(evt);
            }
        });

        jButton1.setText("Inscrire");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Jour");

        jLabel2.setText("Mois");

        jTextMois.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextMoisActionPerformed(evt);
            }
        });

        jLabel3.setText("Annee");

        jTableP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nom", "Prénom", "Email", "date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableP);
        if (jTableP.getColumnModel().getColumnCount() > 0) {
            jTableP.getColumnModel().getColumn(0).setResizable(false);
            jTableP.getColumnModel().getColumn(1).setResizable(false);
            jTableP.getColumnModel().getColumn(2).setResizable(false);
            jTableP.getColumnModel().getColumn(3).setResizable(false);
            jTableP.getColumnModel().getColumn(4).setResizable(false);
        }

        jButton2.setText("Supprimer");
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
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelId, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelNom, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelType, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)
                        .addComponent(jLabelDuree, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(126, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                                        .addComponent(jTextNom)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(90, 90, 90)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTextJour, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                                                    .addComponent(jTextMois))
                                                .addComponent(jTextAnnee, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jButton1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(189, 189, 189))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(187, 187, 187))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelId)
                    .addComponent(jLabelNom)
                    .addComponent(jLabelType)
                    .addComponent(jLabelDuree))
                .addGap(38, 38, 38)
                .addComponent(jLabel5)
                .addGap(3, 3, 3)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel1)
                            .addComponent(jTextJour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextMois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextAnnee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(54, 54, 54)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextJourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextJourActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextJourActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(jTextNom.getText() != null && !jTextNom.getText().equals("") && jTextEmail.getText() != null && !jTextEmail.getText().equals("") && jTextJour.getText() != null && !jTextJour.getText().equals("")
                && jTextMois.getText() != null && !jTextMois.getText().equals("") && jTextAnnee.getText() != null && !jTextAnnee.getText().equals("")){
            
            RequeteFUCAMP req = null;
        try {
            String env = jTextNom.getText()+":"+jTextEmail.getText()+":"+jTextAnnee.getText()+"-"+jTextMois.getText()+"-"+jTextJour.getText()+":"+duree+":"+id ;
            
            req = new RequeteFUCAMP(RequeteFUCAMP.RESERVATIONACTIVITE, env );
            
            ois=null; oos=null; cliSock = null;
            String adresse = Utils.getItemConfig(path, "adresse");
            int port = Integer.parseInt(Utils.getItemConfig(path, "PORT_ACTIVITES"));
            try
            {
                cliSock = new Socket(adresse, port);
                System.out.println(cliSock.getInetAddress().toString());
            }                       
            catch (IOException ex) {
                Logger.getLogger(LoginActivite.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Envoie de la requête
            System.out.println("envoie requete GetALLActivities !");
            try
            {
                oos = new ObjectOutputStream(cliSock.getOutputStream());
                oos.writeObject(req); oos.flush();
            }
            catch (IOException e)
            { System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); }
            
            // Lecture de la réponse
            ReponseFUCAMP rep = null;
            System.out.println("en attente d'une réponse !");
            try
            {
                ois = new ObjectInputStream(cliSock.getInputStream());
                rep = (ReponseFUCAMP)ois.readObject();
                System.out.println(" *** Reponse reçue : " + rep.getChargeUtile());
                
                JOptionPane.showMessageDialog(null,"code : "+ rep.getCode()+" "+rep.getChargeUtile() , "CAUTION ! ", JOptionPane.INFORMATION_MESSAGE);
                initParticipants();
            }
            catch (ClassNotFoundException e)
            { System.out.println("--- erreur sur la classe = " + e.getMessage()); }
            catch (IOException e)
            { System.out.println("--- erreur IO = " + e.getMessage()); }
            
            
            
            


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginActivite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginActivite.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (IOException ex) {
                Logger.getLogger(InscriptionActivite.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }else{
            JOptionPane.showMessageDialog(null, "Veuillez saisir tout les champs !!", "CAUTION ! ", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextMoisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextMoisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextMoisActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(jTableP.getSelectedRow() != -1){
                RequeteFUCAMP req = null;
        try {
            String env = id +":"+ jTableP.getValueAt(jTableP.getSelectedRow(), 0)+":" +jTableP.getValueAt(jTableP.getSelectedRow(), 4);
            
            req = new RequeteFUCAMP(RequeteFUCAMP.DELETERESERVATION, env );
            
            ois=null; oos=null; cliSock = null;
            String adresse = Utils.getItemConfig(path, "adresse");
            int port = Integer.parseInt(Utils.getItemConfig(path, "PORT_ACTIVITES"));
            try
            {
                cliSock = new Socket(adresse, port);
                System.out.println(cliSock.getInetAddress().toString());
            }                       
            catch (IOException ex) {
                Logger.getLogger(LoginActivite.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Envoie de la requête
            System.out.println("envoie requete DELETERESERVATION !");
            try
            {
                oos = new ObjectOutputStream(cliSock.getOutputStream());
                oos.writeObject(req); oos.flush();
            }
            catch (IOException e)
            { System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); }
            
            // Lecture de la réponse
            ReponseFUCAMP rep = null;
            System.out.println("en attente d'une réponse !");
            try
            {
                ois = new ObjectInputStream(cliSock.getInputStream());
                rep = (ReponseFUCAMP)ois.readObject();
                System.out.println(" *** Reponse reçue : " + rep.getChargeUtile());
                
                JOptionPane.showMessageDialog(null,"code : "+ rep.getCode()+" "+rep.getChargeUtile() , "CAUTION ! ", JOptionPane.INFORMATION_MESSAGE);
                initParticipants();
            }
            catch (ClassNotFoundException e)
            { System.out.println("--- erreur sur la classe = " + e.getMessage()); }
            catch (IOException e)
            { System.out.println("--- erreur IO = " + e.getMessage()); }
            
            
            
            


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginActivite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginActivite.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (IOException ex) {
                Logger.getLogger(InscriptionActivite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(InscriptionActivite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InscriptionActivite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InscriptionActivite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InscriptionActivite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InscriptionActivite dialog = new InscriptionActivite(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelDuree;
    private javax.swing.JLabel jLabelId;
    private javax.swing.JLabel jLabelNom;
    private javax.swing.JLabel jLabelType;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableP;
    private javax.swing.JTextField jTextAnnee;
    private javax.swing.JTextField jTextEmail;
    private javax.swing.JTextField jTextJour;
    private javax.swing.JTextField jTextMois;
    private javax.swing.JTextField jTextNom;
    // End of variables declaration//GEN-END:variables

    public void Connexion() throws IOException{
        ois=null; oos=null; cliSock = null;
        String adresse = Utils.getItemConfig(path, "adresse");
        int port = Integer.parseInt(Utils.getItemConfig(path, "PORT_ACTIVITES"));
        try
        {
            cliSock = new Socket(adresse, port);
            System.out.println(cliSock.getInetAddress().toString());
        }                       
        catch (IOException ex) {
            Logger.getLogger(LoginActivite.class.getName()).log(Level.SEVERE, null, ex);
        }
        oos = new ObjectOutputStream(cliSock.getOutputStream());
        
        ois = new ObjectInputStream(cliSock.getInputStream());
    }
    public void  initParticipants() throws ClassNotFoundException, SQLException, IOException {
        
        RequeteFUCAMP req = null;
        req = new RequeteFUCAMP(RequeteFUCAMP.GETALLPARTICIPANTSBYACTIVITE, id );
        ois=null; oos=null; cliSock = null;
        String adresse = Utils.getItemConfig(path, "adresse");
        int port = Integer.parseInt(Utils.getItemConfig(path, "PORT_ACTIVITES"));
        try
        {
            cliSock = new Socket(adresse, port);
            System.out.println(cliSock.getInetAddress().toString());
        }                       
        catch (IOException ex) {
            Logger.getLogger(LoginActivite.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("envoie requete !");
        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req); oos.flush();
        }
        catch (IOException e)
        { System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); }
        // Lecture de la réponse
        ReponseFUCAMP rep = null;
        System.out.println("en attente d'une réponse !");
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseFUCAMP)ois.readObject();
            System.out.println(" *** Reponse reçue : " + rep.getChargeUtile());

            if(rep.getCode()== ReponseFUCAMP.OK){
                
                DefaultTableModel model = (DefaultTableModel) jTableP.getModel();
                while (model.getRowCount() != 0)
                model.removeRow(0);
                
                String r = rep.getChargeUtile();
                String[] tmp = r.split(";");
            
                for(int i =0; i<tmp.length; i++){
                    String []champs = tmp[i].split(":");
                    model.addRow(new Object[]{champs[0], champs[1], champs[2], champs[3],champs[4]});
                }   
            }else if(rep.getCode()== ReponseFUCAMP.PARTICIPANTS_NOT_FOUND){
                DefaultTableModel model = (DefaultTableModel) jTableP.getModel();
                while (model.getRowCount() != 0)
                model.removeRow(0);
            }else{
                JOptionPane.showMessageDialog(null, "Probleme !", "CAUTION ! ", JOptionPane.INFORMATION_MESSAGE);
            }
            }
        catch (ClassNotFoundException e)
        { System.out.println("--- erreur sur la classe = " + e.getMessage()); }
        catch (IOException e)
        { System.out.println("--- erreur IO = " + e.getMessage()); }
        
        
    
    }


}







