/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Holidays;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Salva
 */
public class BDCard extends ConnectionBDMySQL implements Serializable{

    private static BDCard instance;
    
    public BDCard() throws ClassNotFoundException, SQLException {
        super();
        setConnection("root", "root", "bd_card");
    }
    
    public int getUserIdByName(String userName) {
        try {
            String query = "SELECT id_compte FROM comptes WHERE nom_titulaire = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_compte");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void createUser(String userName) {
        try {
            String query = "INSERT INTO comptes (nom_titulaire) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userName);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedUserId = generatedKeys.getInt(1);
                System.out.println("Created user with ID: " + generatedUserId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean cardExistsForUser(String userName, String cardNumber) {
        try {
            int userId = getUserIdByName(userName);
            if (userId != -1) {
                String query = "SELECT * FROM cartes WHERE id_compte = ? AND numero_carte = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, cardNumber);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createCardForUser(String userName, String cardNumber) {
        try {
            int userId = getUserIdByName(userName);
            if (userId != -1) {
                String query = "INSERT INTO cartes (id_compte, numero_carte) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, cardNumber);
                preparedStatement.executeUpdate();
                System.out.println("Created card for user: " + userName);
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static BDCard getInstance() throws ClassNotFoundException, SQLException {
        if(instance == null)
        {
            instance =  new BDCard();
        }
        return instance;
    }
}
