package database.facility;

import java.sql.*;

public class ConnectionBDMySQL{
    protected String user;
    protected String password;
    protected String db;
    protected Connection connection;

    public ConnectionBDMySQL() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    public ConnectionBDMySQL(String user, String password, String db) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.user = user;
        this.password = password;
        this.db = db;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db, user, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public synchronized Statement CreateUpdatableStatement() throws SQLException {
        return connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    public synchronized boolean Execute(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.execute();
    }

    public synchronized ResultSet ExecuteQuery(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.executeQuery();
    }

    public synchronized ResultSet ExecuteQuery(Statement statement, String query) throws SQLException {
        return statement.executeQuery(query);
    }



    public synchronized PreparedStatement getPreparedStatement(String request) throws SQLException {
        return connection.prepareStatement(request, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    public synchronized void UpdateResult(ResultSet resultSet) throws SQLException {
        resultSet.updateRow();
    }

}
