package dao.connection;

import dao.connection.abstraction.IConnectionPool;
import dao.exception.DAOException;
import dao.impl.AlbumDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionManager implements IConnectionPool {

    private static final Logger logger = LogManager.getLogger(ConnectionManager.class);
    private static volatile ConnectionManager instance;
    private static Properties properties;

    private static final int CONNECTIONS_AMOUNT = 10;
    private final ArrayBlockingQueue<Connection> pool;
    private final ArrayBlockingQueue<Connection> taken;

    private ConnectionManager() throws IOException, DAOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        pool = new ArrayBlockingQueue<>(CONNECTIONS_AMOUNT);
        taken = new ArrayBlockingQueue<>(CONNECTIONS_AMOUNT);
        properties = new Properties();
        properties.load(getClass().getResourceAsStream("/dbconfig.properties"));
        init();
        logger.info("ConnectionManager class object initialized.");
    }

    private void init() throws DAOException {
        String dbURL = properties.getProperty("db_url");
        String username = properties.getProperty("db_user");
        String password = properties.getProperty("db_password");
        for (int i = 0; i < CONNECTIONS_AMOUNT; i++) {
            try {
                Connection connection = DriverManager.getConnection(dbURL, username, password);
                pool.add(connection);
            } catch (SQLException e) {
                logger.error("Error initializing ConnectionManager class object.");
                throw new DAOException("Can't initialize connection", 0);
            }
        }
    }

    public static ConnectionManager getInstance() {
        if (instance == null){
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    try {
                        instance = new ConnectionManager();
                    } catch (IOException | DAOException e) {
                        logger.error("Error creating ConnectionManager class object");
                    }
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getFreeConnection() throws DAOException {
        Connection connection;
        try {
            connection = pool.take();
            taken.add(connection);
        } catch (InterruptedException e) {
            logger.error("Error trying to get free connection");
            throw new DAOException(e, 1);
        }
        return connection;
    }

    @Override
    public void retrieveConnection(Connection connection) {
        taken.remove(connection);
        pool.add(connection);
    }
}
