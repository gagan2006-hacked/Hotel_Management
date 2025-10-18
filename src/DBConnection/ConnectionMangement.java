package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMangement {
    public Connection formConnection() throws SQLException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            return null;
        }
        try{
            Connection connection;
            EncryptionV1 v=new EncryptionV1();
            connection = DriverManager.getConnection(AccessLevel.getUrl(),v.decrypt(AccessLevel.getAdmin()),v.decrypt(AccessLevel.getAdHashPass()));
            return connection;
        } catch (SQLException ex) {
            throw new SQLException("Invalid Request");
        }
    }
}
