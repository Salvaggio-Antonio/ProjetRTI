package testjdbc;

import Holidays.BDHolidays;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private BDHolidays con;

    public GUIBdHoliday() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        seConnecterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                     con = new BDHolidays("root", "TOUNFLOUTCH", "bd_holidays");
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                    connectionResult.setText("Connexion échouée...");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    connectionResult.setText("Connexion échouée...");
                }
                if(con != null)
                    connectionResult.setText("Connexion réussie!");
            }
        });

        modifButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                con.updateVoyageurById(Integer.parseInt(textField1.getText()),textField3.getText(),textField5.getText(),textField2.getText(),Integer.parseInt(textField4.getText()));

                textArea1.setText(String.valueOf(con));

            }
        });
        rechercherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                ResultSet result = con.getVoyageursById(Integer.parseInt(textField6.getText()));
                try {


                    result.next();
                    textArea1.setText(BDHolidays.toString(result));


                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        nombreDeVoyageursButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ResultSet result = con.getNbVoyageurs();
                try {


                    result.next();
                    textArea1.setText(BDHolidays.toString(result));


                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                con.insertVoyageurs(Integer.parseInt(textField1.getText()),textField3.getText(),textField5.getText(),textField2.getText(),Integer.parseInt(textField4.getText()));

                textArea1.setText("salut");
            }
        });
    }


}
