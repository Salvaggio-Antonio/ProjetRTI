package Holidays;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BDHolidays extends ConnectionBDMySQL implements Serializable {
    
    private static BDHolidays instance;
    

    private BDHolidays() throws ClassNotFoundException, SQLException {
        super();
    }
   

    public synchronized ResultSet getUserByEmail(String Email, String mdp) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs where mail = ? AND motDepasse = ?;");
            preparedStatement.setString(1, Email);
            preparedStatement.setString(2, mdp);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized ResultSet checkUserByCredit(String Email, String mdp, String carte) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs where mail = ? AND motDepasse = ? and creditCard = ?;");
            preparedStatement.setString(1, Email);
            preparedStatement.setString(2, mdp);
            preparedStatement.setString(3, carte);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized ResultSet getAllActivities() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM activites;");
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        }
    }

    public synchronized ResultSet getAllParticipantsByActivite(int idActivite) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT idvoyageurs, nom, prenom, mail, date_debut FROM voyageurs inner join reservations on (voyageurs.idvoyageurs = reservations.id_titulaire ) "
                    + "where id_activite = ?;");
            preparedStatement.setInt(1, idActivite);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized ResultSet getAllChambreReserve() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs inner join reservations on (voyageurs.idvoyageurs = reservations.id_titulaire ) where id_chambre is not null ");

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public synchronized ResultSet getAllChambreReservePasPaye() {
    try {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs " +
                                                                         "INNER JOIN reservations ON (voyageurs.idvoyageurs = reservations.id_titulaire) " +
                                                                         "WHERE reservations.id_chambre IS NOT NULL " +
                                                                         "AND reservations.resteapayer > 0");
        return preparedStatement.executeQuery();
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}


    public synchronized ResultSet getAllVoyageurs() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs;");
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized ResultSet getNbVoyageurs() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(idvoyageurs) FROM voyageurs;");
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized int updateVoyageurById(int id, String nom, String prenom, String adresse, int codepostal) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update voyageurs set nom =?, prenom =?, adresse =?, codepostal =? WHERE idvoyageurs = ?;");
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, adresse);
            preparedStatement.setInt(4, codepostal);
            preparedStatement.setInt(5, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public synchronized ResultSet getVoyageursById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs WHERE idvoyageurs = ?;");
            preparedStatement.setInt(1, id);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized ResultSet getVoyageursByEmailandName(String Email, String Nom) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs WHERE mail = ? and nom = ?;");
            preparedStatement.setString(1, Email);
            preparedStatement.setString(2, Nom);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized boolean insertVoyageur(String mail, String mdp) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Voyageurs(Mail,motDepasse) VALUES (?, ?);");
            preparedStatement.setString(1, mail);
            preparedStatement.setString(2, mdp);
            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized ResultSet getVoyageursByEmail(String Email) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs WHERE mail = ?;");
            preparedStatement.setString(1, Email);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized boolean insertVoyageurs(int id, String nom, String prenom, String adresse, int codepostal) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Voyageurs(idVoyageurs, nom, prenom, adresse, codepostal) VALUES (?, ?, ?, ?, ?);");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, nom);
            preparedStatement.setString(3, prenom);
            preparedStatement.setString(4, adresse);
            preparedStatement.setInt(5, codepostal);
            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized ResultSet getChambreById(String idChambre) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chambres WHERE idchambres = ? ");
            preparedStatement.setString(1, idChambre);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized ResultSet getChambreLibre(String categorie, String TypeChambre, String date, int nombreNuit) {
        try ( ResultSet rs = getChambreIdByCategorieAndType(categorie, TypeChambre)) {
            while (rs.next()) {
                String idChambre = rs.getString("idchambres");
                if (chambreEstDisponible(idChambre, date, nombreNuit)) {
                    // Si la chambre est disponible, récupérez les autres détails de la chambre à partir de la base de données
                    ResultSet chambreDetails = getChambreById(idChambre); // Vous devez implémenter cette méthode pour récupérer les détails de la chambre par son idChambre.
                    if (chambreDetails.next()) {
                        return chambreDetails;
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour vérifier si une chambre est disponible pour une période spécifiée
    private boolean chambreEstDisponible(String idChambre, String date, int nombreNuit) {

        try ( PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM reservations WHERE id_chambre = ? AND ((date_debut <= ? AND date_fin >= ?) OR (date_debut <= DATE_ADD(?, INTERVAL ? DAY) AND date_fin >= DATE_ADD(?, INTERVAL ? DAY)))")) {
            preparedStatement.setString(1, idChambre);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, date);
            preparedStatement.setInt(5, nombreNuit);
            preparedStatement.setString(6, date);
            preparedStatement.setInt(7, nombreNuit);

            try ( ResultSet rs = preparedStatement.executeQuery()) {
                rs.next();
                int count = rs.getInt("count");
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized ResultSet getChambreIdByCategorieAndType(String categorie, String TypeChambre) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT idchambres from chambres where typeChambre = ? AND typeLieu = ?;");
            preparedStatement.setString(1, TypeChambre);
            preparedStatement.setString(2, categorie);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized ResultSet getReservationChambreByEMailNonPaye(String email) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM voyageurs inner join reservations on (voyageurs.idvoyageurs = reservations.id_titulaire)"
                    + " WHERE mail = ? and resteapayer > 0 and id_chambre is not null");
            preparedStatement.setString(1, email);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized boolean insertReservationChambre(int idchambre, int id_titulaire, String date, int nombreNuit, double prix) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservations(id_titulaire, id_chambre, date_debut, date_fin, prix_net, NombreNuit, paye, resteapayer) VALUES (?, ?, ?,DATE_ADD(?, INTERVAL ? DAY), ?, ?, 0 ,?);");
            preparedStatement.setInt(1, id_titulaire);
            preparedStatement.setInt(2, idchambre);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, date);
            preparedStatement.setInt(5, nombreNuit);
            preparedStatement.setDouble(6, prix);
            preparedStatement.setInt(7, nombreNuit);
            preparedStatement.setDouble(8, prix);

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean insertReservationActivite(int idActivite, int id_titulaire, String date, int duree) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservations(id_titulaire, id_activite, date_debut, date_fin) VALUES (?, ?, ?,DATE_ADD(?, INTERVAL ? DAY) );");
            preparedStatement.setInt(1, id_titulaire);
            preparedStatement.setInt(2, idActivite);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, date);
            preparedStatement.setInt(5, duree);

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean checkReservationActivite(int idActivite, int id_titulaire, String date) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM reservations where id_titulaire = ? AND id_activite = ? AND date_debut = ?;");
            preparedStatement.setInt(1, id_titulaire);
            preparedStatement.setInt(2, idActivite);
            preparedStatement.setString(3, date);

            ResultSet result = preparedStatement.executeQuery();

            return !result.next();

        } catch (SQLException ex) {
            Logger.getLogger(BDHolidays.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public synchronized boolean insertReservationChambre(int idActivite, int id_titulaire, String date, int duree) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservations(id_titulaire, id_chambre, date_debut, date_fin) VALUES (?, ?, ?,DATE_ADD(?, INTERVAL ? DAY) );");
            preparedStatement.setInt(1, id_titulaire);
            preparedStatement.setInt(2, idActivite);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, date);
            preparedStatement.setInt(5, duree);

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean PaiementReservation(int idchambre, int id_titulaire, String date) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update reservations set paye =true  where id_chambre= ? and id_titulaire = ? and date_debut = ?; ");
            preparedStatement.setInt(1, idchambre);
            preparedStatement.setInt(2, id_titulaire);
            preparedStatement.setString(3, date);

            int rowCount = preparedStatement.executeUpdate();
            return rowCount > 0;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public synchronized boolean PaiementReservation(int reservation, int id_titulaire) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update reservations set paye =true  where idreservations = ? and id_titulaire =?; ");
            preparedStatement.setInt(1, reservation);
            preparedStatement.setInt(2, id_titulaire);

            int rowCount = preparedStatement.executeUpdate();
            return rowCount > 0;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }
    
    public synchronized boolean PayReservation(int reservation, double resteapayer) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update reservations set resteapayer = ?  where idreservations = ? ");
            preparedStatement.setDouble(1, resteapayer);
            preparedStatement.setInt(2, reservation);

            int rowCount = preparedStatement.executeUpdate();
            return rowCount > 0;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public synchronized boolean SuppressionReservation(int idchambre, int id_titulaire, String date) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from  reservations where id_chambre= ? and id_titulaire = ? and date_debut = ? ");
            preparedStatement.setInt(1, idchambre);
            preparedStatement.setInt(2, id_titulaire);
            preparedStatement.setString(3, date);

            int rowcount = preparedStatement.executeUpdate();

            return rowcount > 0;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }
    
    public synchronized boolean SuppressionReservationById(int idreservation) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from  reservations where idreservations = ?");
            preparedStatement.setInt(1, idreservation);

            int rowcount = preparedStatement.executeUpdate();

            return rowcount > 0;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public synchronized boolean DeleteReservationActivite(int idActivite, int id_titulaire, String date) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reservations where id_titulaire = ? and id_activite = ? and date_debut = ?");
            preparedStatement.setInt(1, id_titulaire);
            preparedStatement.setInt(2, idActivite);
            preparedStatement.setString(3, date);

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public synchronized ResultSet getAdmin(String login) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM admin where login = ?;");
            preparedStatement.setString(1, login);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toString(ResultSet rs) {

        StringBuilder buf = new StringBuilder();
        buf.append("[");
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int nColumns = metaData.getColumnCount();
            for (int i = 1; i <= nColumns; ++i) {
                buf.append(metaData.getColumnName(i));
                buf.append(" = ");
                buf.append(rs.getString(i));
                if (i < nColumns) {
                    buf.append(" , ");
                }
            }
        } catch (SQLException e) {
            buf.append(e.getMessage());
            e.printStackTrace();
        }
        buf.append("]");

        return buf.toString();
    }
    
    

    /**
     * @return the instance
     */
    public static BDHolidays getInstance() throws ClassNotFoundException, SQLException {
        if(instance == null)
        {
            instance =  new BDHolidays();
        }
        return instance;
    }

}
