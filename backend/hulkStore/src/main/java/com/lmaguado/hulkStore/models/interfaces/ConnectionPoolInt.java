package com.lmaguado.hulkStore.models.interfaces;

import com.lmaguado.hulkStore.models.implement.exceptions.ConnectionPoolException;

import java.sql.Connection;

public interface ConnectionPoolInt {
    void createConnectionPool(String url, String username, String password, int initConnections, int maxConnections);
    Connection getConnection() throws ConnectionPoolException;
    boolean releaseConnection(Connection connection);
    int getSize();
}
