package testjdbc;

import Holidays.BDHolidays;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUIBdHoliday extends JFrame {
    private JTextField textField1;//id
    private JTextField textField2;//nom
    private JTextField textField3;//prenom
    private JTextField textField4;//addr
    private JTextField textField5;//cp
    private JButton modifButton;
    private JButton seConnecterButton;
    private JTextArea textArea1;
    private JLabel connectionResult;
    private JLabel insertStatut;
    private JPanel mainPanel;
    private JButton rechercherButton;
    private JTextField textField6;
    private JButton nombreDeVoyageursButton;
    private JButton ajouterButton;

    public GUIBdHoliday() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        

        modifButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BDHolidays.getInstance().updateVoyageurById(Integer.parseInt(textField1.getText()),textField3.getText(),textField5.getText(),textField2.getText(),Integer.parseInt(textField4.getText()));
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GUIBdHoliday.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(GUIBdHoliday.class.getName()).log(Level.SEVERE, null, ex);
                }


            }
        });
        rechercherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try {
                    
                    
                    ResultSet result = BDHolidays.getInstance().getVoyageursById(Integer.parseInt(textField6.getText()));
                    try {
                        
                        
                        result.next();
                        textArea1.setText(BDHolidays.toString(result));
                        
                        
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GUIBdHoliday.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(GUIBdHoliday.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        nombreDeVoyageursButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    
                    ResultSet result = BDHolidays.getInstance().getNbVoyageurs();
                    try {
                        
                        
                        result.next();
                        textArea1.setText(BDHolidays.toString(result));
                        
                        
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GUIBdHoliday.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(GUIBdHoliday.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BDHolidays.getInstance().insertVoyageurs(Integer.parseInt(textField1.getText()),textField3.getText(),textField5.getText(),textField2.getText(),Integer.parseInt(textField4.getText()));
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GUIBdHoliday.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(GUIBdHoliday.class.getName()).log(Level.SEVERE, null, ex);
                }

                textArea1.setText("salut");
            }
        });
    }


}
