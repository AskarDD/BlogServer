package Model.connection;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Сайида on 06.07.2017.
 */
public class ConnectionManager {
    private ConnectionManager() {
    }
    final static Logger LOGGER = Logger.getLogger(ConnectionManager.class);
    static {
        BasicConfigurator.configure();
    }

    public static class Holder{
        private static final ConnectionManager INSTANCE = new ConnectionManager();
    }

    public static ConnectionManager getInstance(){
        return Holder.INSTANCE;
    }

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.debug("ClassNotFoundException for Class.forName(\"org.postgresql.Driver\") in metod getConnection");
            e.printStackTrace();
        }

        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "zollton");
        } catch (SQLException e) {
            LOGGER.debug("SQLException for DriverManager.getConnection(\"jdbc:postgresql://localhost:5432/postgres\", \"postgres\", \"password\") in metod getConnection");
            e.printStackTrace();
        }
        return connection;
    }
}
