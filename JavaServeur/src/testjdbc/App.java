package testjdbc;

import database.facility.BDHolidays;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
       
        BDHolidays bd =new BDHolidays("root", "root", "bd_holidays");
        
        //ResultSet s = bd.getAllVoyageurs();
        //s.next();
        //System.out.println(s.getString("mail"));

        ResultSet s = bd.getAllActivities();
        
        while (s.next()){      
            System.out.println(s.getString("nom"));
        }
        
    }
}
