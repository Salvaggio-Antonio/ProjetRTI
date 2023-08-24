package testjdbc;

import Holidays.BDHolidays;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
       
        
        //ResultSet s = bd.getAllVoyageurs();
        //s.next();
        //System.out.println(s.getString("mail"));

        ResultSet s = BDHolidays.getInstance().getAllActivities();
        
        while (s.next()){      
            System.out.println(s.getString("nom"));
        }
        
    }
}
