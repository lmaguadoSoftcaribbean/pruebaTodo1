package com.lmaguado.hulkStore.models.implement;

import com.lmaguado.hulkStore.models.implement.exceptions.ConnectionPoolException;
import com.lmaguado.hulkStore.models.interfaces.ConnectionPoolInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConnectionPoolImp implements ConnectionPoolInt {

    private final Logger logger = LoggerFactory.getLogger(ConnectionPoolImp.class);
    public static final String LOGGER_HEADER = ">> Connection Pool Imp:\n\t";
    private List<Connection> connectionPool;
    private final List<Connection> usedConnections = new ArrayList<>();
    private String errMsg = "";
    private String url;
    private String username;
    private String password;
    private int maxConnections;

    @Override
    public void createConnectionPool(String url, String username, String password, int initConnections, int maxConnections) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxConnections = maxConnections;

        connectionPool = new ArrayList<>(initConnections);
        for (int i = 0; i < initConnections; i++) {
            try {
                connectionPool.add(createConnection());
            } catch (ConnectionPoolException e) {
                printError(e);
            }
        }
        errMsg = LOGGER_HEADER + "Was created the pool connection successfully. Num Connections: " + getSize();
        logger.info(errMsg);
        errMsg = null;
    }

    private Connection createConnection() throws ConnectionPoolException {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            printError(ex);
            throw new ConnectionPoolException(ConnectionPoolException.ConnectionPoolExceptionMessage.ERROR_TO_CREATING_THE_CONNECTION_FOR_POOL);
        }
    }

    @Override
    public Connection getConnection() throws ConnectionPoolException {
        Connection connection;
        if (!connectionPool.isEmpty()) {
            connection = connectionPool.remove(connectionPool.size() - 1);
            usedConnections.add(connection);
        } else if (getSize() <= maxConnections) {
            try {
                connection = createConnection();
                usedConnections.add(connection);
            } catch (ConnectionPoolException e) {
                throw new ConnectionPoolException(e.getMessage());
            }
        } else {
            connection = null;
        }
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    @Override
    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    private void printError(Exception ex) {
        errMsg = LOGGER_HEADER + ex;
        logger.error(errMsg);
        errMsg = null;
    }
}
