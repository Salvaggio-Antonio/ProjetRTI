import database.facility.*;

import java.sql.SQLException;

public class Main {
    public static BDHolidays con;
    public static void main(String[] args) {
        try {
             con = new BDHolidays("root", "root", "bd_holidays");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Connexion échouée...");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Connexion échouée...");
        }
        if(con != null)
            System.out.println("Connexion réussie!");
    }
}