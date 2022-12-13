package database.facility;
import java.sql.*;


public class BDHolidays extends ConnectionBDMySQL {

    public BDHolidays() throws ClassNotFoundException {

        super();
    }

    public BDHolidays(String user, String password, String db) throws ClassNotFoundException, SQLException {

        super(user, password, db);
    }

    public synchronized ResultSet getAllVoyageurs()
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs;");
            return preparedStatement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public synchronized ResultSet getNbVoyageurs()
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(idvoyageurs) FROM voyageurs;");
            return preparedStatement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public synchronized int updateVoyageurById(int id, String nom, String prenom, String adresse, int codepostal)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("update voyageurs set nom =?, prenom =?, adresse =?, codepostal =? WHERE idvoyageurs = ?;");
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, adresse);
            preparedStatement.setInt(4, codepostal);
            preparedStatement.setInt(5, id);
            return preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }


    public synchronized ResultSet getVoyageursById(int id)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs WHERE idvoyageurs = ?;");
            preparedStatement.setInt(1, id);

            return preparedStatement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized boolean insertVoyageurs(int id, String nom, String prenom, String adresse, int codepostal)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Voyageurs(idVoyageurs, nom, prenom, adresse, codepostal) VALUES (?, ?, ?, ?, ?);");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, nom);
            preparedStatement.setString(3, prenom);
            preparedStatement.setString(4, adresse);
            preparedStatement.setInt(5, codepostal);
            preparedStatement.execute();

            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static String toString(ResultSet rs) {

        StringBuffer buf = new StringBuffer();
        buf.append("[");
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int nColumns = metaData.getColumnCount();
            for (int i = 1; i <= nColumns; ++i) {
                buf.append(metaData.getColumnName(i));
                buf.append(" = ");
                buf.append(rs.getString(i));
                if (i < nColumns)
                    buf.append(" , ");
            }
        } catch (SQLException e) {
            buf.append(e.getMessage());
            e.printStackTrace();
        }
        buf.append("]");

        return buf.toString();
    }
}
