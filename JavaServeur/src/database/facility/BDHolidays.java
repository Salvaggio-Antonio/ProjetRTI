package database.facility;
import java.io.Serializable;
import java.sql.*;


public class BDHolidays extends ConnectionBDMySQL implements Serializable {

    public BDHolidays() throws ClassNotFoundException {

        super();
    }
    
    

    public BDHolidays(String user, String password, String db) throws ClassNotFoundException, SQLException {

        super(user, password, db);
    }
    
    public synchronized ResultSet getUserByEmail(String Email, String mdp)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs where mail = ? AND motDepasse = ?;");
            preparedStatement.setString(1, Email);
            preparedStatement.setString(2, mdp);
            
            return preparedStatement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public synchronized ResultSet checkUserByCredit(String Email, String mdp, String carte)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs where mail = ? AND motDepasse = ? and creditCard = ?;");
            preparedStatement.setString(1, Email);
            preparedStatement.setString(2, mdp);
            preparedStatement.setString(3, carte);
            
            return preparedStatement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public synchronized ResultSet getAllActivities()
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM activites;");
            return preparedStatement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
            
        }
    }
    
    public synchronized ResultSet getAllParticipantsByActivite(int idActivite)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT idvoyageurs, nom, prenom, mail, date_debut FROM voyageurs inner join reservations on (voyageurs.idvoyageurs = reservations.id_titulaire ) "
                                                                              + "where id_activite = ?;");
            preparedStatement.setInt(1, idActivite);
            return preparedStatement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public synchronized ResultSet getAllChambreReserve()
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id_chambre, nom,date_debut FROM voyageurs inner join reservations on (voyageurs.idvoyageurs = reservations.id_titulaire ) where id_chambre is not null ");
                                                                              
            return preparedStatement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
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
    
    public synchronized ResultSet getVoyageursByEmailandName(String Email, String Nom)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs WHERE mail = ? and nom = ?;");
            preparedStatement.setString(1, Email);
            preparedStatement.setString(2, Nom);

            return preparedStatement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public synchronized boolean insertVoyageur( String mail, String mdp)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Voyageurs(Mail,motDepasse) VALUES (?, ?);");
            preparedStatement.setString(1, mail);
            preparedStatement.setString(2, mdp);
            preparedStatement.execute();

            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    public synchronized ResultSet getVoyageursByEmail(String Email)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs WHERE mail = ?;");
            preparedStatement.setString(1, Email);

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
    
    public synchronized ResultSet getChambreLibre(String categorie, String TypeChambre, String date, int nombreNuit)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chambres left join reservations on (chambres.idchambres = reservations.id_chambre)"
                    + " WHERE typelieu = ? and typechambre = ? and (( DATE_ADD(?, INTERVAL ? DAY)<date_debut or ?>date_fin ) or reservations.id_chambre is null)");
            preparedStatement.setString(1, categorie);
            preparedStatement.setString(2, TypeChambre);
            preparedStatement.setString(3, date);
            preparedStatement.setInt(4, nombreNuit);
            preparedStatement.setString(5, date);
            

            return preparedStatement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
     public synchronized ResultSet getReservationChambreByEMailNonPaye(String email)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs inner join reservations on (voyageurs.idvoyageurs = reservations.id_titulaire)"
                    + " WHERE mail = ? and paye = false and id_chambre is not null");
            preparedStatement.setString(1, email);

            return preparedStatement.executeQuery();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public synchronized boolean insertReservationChambre(int idchambre, int id_titulaire, String date , int nombreNuit, double prix)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservations(id_titulaire, id_chambre, date_debut, date_fin, prix_net, NombreNuit, paye) VALUES (?, ?, ?,DATE_ADD(?, INTERVAL ? DAY), ?, ?, 0 );");
            preparedStatement.setInt(1, id_titulaire);
            preparedStatement.setInt(2, idchambre);
            preparedStatement.setString(3, date);        
            preparedStatement.setString(4, date);
            preparedStatement.setInt(5, nombreNuit);
            preparedStatement.setDouble(6, prix);
            preparedStatement.setInt(7, nombreNuit);

            preparedStatement.execute();

            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    public synchronized boolean insertReservationActivite(int idActivite, int id_titulaire, String date , int duree)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservations(id_titulaire, id_activite, date_debut, date_fin) VALUES (?, ?, ?,DATE_ADD(?, INTERVAL ? DAY) );");
            preparedStatement.setInt(1, id_titulaire);
            preparedStatement.setInt(2, idActivite);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, date);
            preparedStatement.setInt(5, duree);
            
        
            preparedStatement.execute();

            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public synchronized boolean insertReservationChambre(int idActivite, int id_titulaire, String date , int duree)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservations(id_titulaire, id_chambre, date_debut, date_fin) VALUES (?, ?, ?,DATE_ADD(?, INTERVAL ? DAY) );");
            preparedStatement.setInt(1, id_titulaire);
            preparedStatement.setInt(2, idActivite);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, date);
            preparedStatement.setInt(5, duree);
            
        
            preparedStatement.execute();

            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    public synchronized boolean PaiementReservation(int idchambre, int id_titulaire, String date)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("update reservations set paye =true  where id_chambre= ? and id_titulaire = ? and date_debut = ?; ");
            preparedStatement.setInt(1, idchambre);
            preparedStatement.setInt(2, id_titulaire);
            preparedStatement.setString(3, date);

            int rowCount = preparedStatement.executeUpdate();
            return rowCount > 0;

            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            
            return false;
        }
    }
    
    public synchronized boolean PaiementReservation(int reservation , int id_titulaire)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("update reservations set paye =true  where idreservations = ? and id_titulaire =?; ");
            preparedStatement.setInt(1, reservation);
            preparedStatement.setInt(2, id_titulaire);


            int rowCount = preparedStatement.executeUpdate();
            return rowCount > 0;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            
            return false;
        }
    }
    
    public synchronized boolean SuppressionReservation(int idchambre, int id_titulaire, String date)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from  reservations where id_chambre= ? and id_titulaire = ? and date_debut = ? ");
            preparedStatement.setInt(1, idchambre);
            preparedStatement.setInt(2, id_titulaire);
            preparedStatement.setString(3, date);
            
            int rowcount =preparedStatement.executeUpdate();

            return rowcount>0;

             
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            
            return false;
        }
    }
    
    
    
    
    public synchronized boolean DeleteReservationActivite(int idActivite, int id_titulaire, String date)
    {
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reservations where id_titulaire = ? and id_activite = ? and date_debut = ?");
            preparedStatement.setInt(1, id_titulaire);
            preparedStatement.setInt(2, idActivite);
            preparedStatement.setString(3, date);

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
