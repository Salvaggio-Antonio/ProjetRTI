/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application_Chat;
import static Application_Chat.LoginWindow.model;
import static Application_Chat.LoginWindow.thr;
import ProtocoleHOLICOP.ReponseHOLICOP;
import ProtocoleHOLICOP.RequeteHOLICOP;
import Utilities.Configuration;
import Utilities.RequeteUtils;
import Utilities.Utils;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Nicolas
 */
public class ChatWindow extends javax.swing.JFrame {

    /**
     * 
     * Creates new form ChatWindow
     */
    File currentDirectory = new File(System.getProperty("user.dir"));
    
    public String path = currentDirectory+"\\src\\Config\\Config.config";
    int port;
    String adresse = "localhost";
    
    InetAddress adresseGroupe;
    MulticastSocket socketGroupe;
    String client;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    Configuration config;
    
    public static List<String> listeMessages = new ArrayList<String>();
            
    public ChatWindow(String nomCli, MulticastSocket socket, InetAddress ad) throws IOException {
        initComponents();
        port = Integer.parseInt(Utils.getItemConfig(path, "PORT_CARD"));
        Label_Titre.setText("Conversation ~ " + nomCli);
        List_Message.setModel(model);
        client = nomCli;
        socketGroupe = socket;
        adresseGroupe = ad;
        
        config = new Configuration(path, "PORT_CHAT");
    }
    
    public String getTagByType (String type) {
        Socket cliSock;
        String chaine="";
        RequeteHOLICOP req;
        
        try {
            cliSock = new Socket(config.getAdresse(), config.getPort());
            
            oos = new ObjectOutputStream(cliSock.getOutputStream());

            req = new RequeteHOLICOP(RequeteHOLICOP.HSEND, type);
            RequeteUtils.SendRequest(req, "HOLICOP", oos, cliSock);
            
            ois = new ObjectInputStream(cliSock.getInputStream());
            ReponseHOLICOP rep = (ReponseHOLICOP) RequeteUtils.ReceiveRequest(cliSock, ois, "HOLICOP");
            oos.close();
            ois.close();
            cliSock.close();
            
            chaine = rep.getChargeUtile();
            
            
        } catch (IOException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return chaine;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        List_Message = new javax.swing.JList<>();
        Label_Titre = new javax.swing.JLabel();
        Bouton_Envoyer = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        Field_Message = new javax.swing.JTextField();
        Bouton_Quitter = new javax.swing.JButton();
        Radio_Question = new javax.swing.JRadioButton();
        Radio_Reponse = new javax.swing.JRadioButton();
        Radio_Event = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        List_Message.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                List_MessageMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(List_Message);

        Label_Titre.setFont(new java.awt.Font("Footlight MT Light", 3, 18)); // NOI18N
        Label_Titre.setText("Conversation - DDDDDDDDDDDDDDDDDDDDDD");

        Bouton_Envoyer.setText("Envoyer");
        Bouton_Envoyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bouton_EnvoyerActionPerformed(evt);
            }
        });

        jLabel2.setText("Message :");

        Bouton_Quitter.setText("Quitter le chat");
        Bouton_Quitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bouton_QuitterActionPerformed(evt);
            }
        });

        buttonGroup1.add(Radio_Question);
        Radio_Question.setSelected(true);
        Radio_Question.setText("Question");

        buttonGroup1.add(Radio_Reponse);
        Radio_Reponse.setText("Réponse");

        buttonGroup1.add(Radio_Event);
        Radio_Event.setText("Event");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Bouton_Quitter)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Bouton_Envoyer))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Field_Message, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Radio_Reponse)
                            .addComponent(Radio_Event)
                            .addComponent(Radio_Question))
                        .addGap(44, 44, 44))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(Label_Titre, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(Label_Titre)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Bouton_Envoyer)
                            .addComponent(Bouton_Quitter))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(273, 273, 273)
                        .addComponent(Radio_Question)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Radio_Reponse)
                        .addGap(0, 0, 0)
                        .addComponent(Radio_Event)
                        .addContainerGap(43, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Field_Message, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Bouton_EnvoyerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bouton_EnvoyerActionPerformed
        if (Field_Message.getText().equals(""))
            return;
        String tag ="";
        
        if (Radio_Question.isSelected())
            tag = getTagByType("QUEST");
        else if (Radio_Event.isSelected())
            tag = getTagByType("EVENT");
        else if (Radio_Reponse.isSelected() && List_Message.getSelectedIndex() > -1)
            tag = getTagByType("REP");
        
        String texte = Field_Message.getText();
        
        String message = "<" + client + "> ("+ tag + ") " + texte;
        
        DatagramPacket dtg = new DatagramPacket(message.getBytes(), message.length(), adresseGroupe, 60060);
        try
        {
            socketGroupe.send(dtg);
        }
        catch (IOException e) { System.out.println("Erreur : " + e.getMessage()); }
        Field_Message.setText("");
    }//GEN-LAST:event_Bouton_EnvoyerActionPerformed

    private void Bouton_QuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bouton_QuitterActionPerformed
        String msg = client + " vient de quitter le groupe. ";
        DatagramPacket dtg = new DatagramPacket(msg.getBytes(), msg.length(), adresseGroupe, 60060);
        try
        {
            socketGroupe.send(dtg);
            thr.stopThread();
            socketGroupe.leaveGroup(adresseGroupe); System.out.println("Après leaveGroup");
            socketGroupe.close();
            System.out.println("Après close");
            
            this.dispose();
        }
        catch (IOException e){ System.out.println("Erreur : " + e.getMessage());}
    }//GEN-LAST:event_Bouton_QuitterActionPerformed

    private void List_MessageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_List_MessageMouseClicked
        if (evt.getClickCount() == 2) {
            String val = List_Message.getSelectedValue();
            StringTokenizer tk = new StringTokenizer(val, "(");
            String chaine = tk.nextToken();
            chaine = tk.nextToken();
            
            tk = new StringTokenizer(chaine, ")");
            chaine = tk.nextToken();
            Field_Message.setText("{REP to " + chaine + "} ");
        }
    }//GEN-LAST:event_List_MessageMouseClicked

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
            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ChatWindow(null, null, null).setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Bouton_Envoyer;
    private javax.swing.JButton Bouton_Quitter;
    private javax.swing.JTextField Field_Message;
    private javax.swing.JLabel Label_Titre;
    public static javax.swing.JList<String> List_Message;
    private javax.swing.JRadioButton Radio_Event;
    private javax.swing.JRadioButton Radio_Question;
    private javax.swing.JRadioButton Radio_Reponse;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
