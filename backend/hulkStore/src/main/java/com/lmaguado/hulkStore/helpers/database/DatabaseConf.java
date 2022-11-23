package com.lmaguado.hulkStore.helpers.database;

import com.lmaguado.hulkStore.helpers.fileManager.FileManager;
import com.lmaguado.hulkStore.helpers.fileManager.FileManagerException;
import com.lmaguado.hulkStore.helpers.strings.Constants;
import com.lmaguado.hulkStore.models.implement.ConnectionPoolImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DatabaseConf {
    private final FileManager fileManager;
    private final Logger logger = LoggerFactory.getLogger(DatabaseConf.class);
    private final ConnectionPoolImp connectionPool;

    @Autowired
    public DatabaseConf(FileManager fileManager, ConnectionPoolImp connectionPool) {
        this.fileManager = fileManager;
        this.connectionPool = connectionPool;
    }

    @PostConstruct
    public void createConnection() throws DatabaseConfException, FileManagerException {
        String errMsg;
        String loggerHeader = ">> DatabaseConf: \n\t";
        try {
            if (fileManager.createFileSecret(Constants.PROPERTIES_DATABASE_NAME)) {
                errMsg = loggerHeader + "The file " + Constants.PROPERTIES_DATABASE_NAME + " was created/founded successfully.";
                logger.info(errMsg);
                createPoolConnection(
                        fileManager.getPropertyByKey(Constants.PROPERTIES_DATABASE_NAME, "url"),
                        fileManager.getPropertyByKey(Constants.PROPERTIES_DATABASE_NAME, "username"),
                        fileManager.getPropertyByKey(Constants.PROPERTIES_DATABASE_NAME, "password"),
                        fileManager.getPropertyByKey(Constants.PROPERTIES_DATABASE_NAME, "initConnections"),
                        fileManager.getPropertyByKey(Constants.PROPERTIES_DATABASE_NAME, "maxConnections")
                );
            }
        } catch (FileManagerException e) {
            throw new FileManagerException(e.getMessage());
        }
    }

    private void createPoolConnection(String url, String username, String password, String initConnectionsStr, String maxConnectionsStr) throws DatabaseConfException {
        int initConnections;
        int maxConnections;
        if (
                url.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE) ||
                username.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE) ||
                password.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE) ||
                initConnectionsStr.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE) ||
                maxConnectionsStr.equals(Constants.PROPERTY_FILE_DEFAULT_VALUE)
        ) throw new DatabaseConfException(DatabaseConfException.DatabaseConfExceptionMessage.ERROR_TO_CREATE_THE_POOL_CONNECTION_VALIDATE_PROPERTIES);
        try {
            initConnections = Integer.parseInt(initConnectionsStr);
            maxConnections = Integer.parseInt(maxConnectionsStr);
        } catch (NumberFormatException ex) {
            throw new DatabaseConfException(DatabaseConfException.DatabaseConfExceptionMessage.ERROR_TO_CREATE_THE_POOL_CONNECTION_THE_MAX_VALUE_CANT_BE_LOWER_TO_INITIAL);
        }
        if (maxConnections < initConnections) throw new DatabaseConfException(DatabaseConfException.DatabaseConfExceptionMessage.ERROR_TO_CREATE_THE_POOL_CONNECTION_PARSING_VALUES);
        connectionPool.createConnectionPool(url, username, password, initConnections, maxConnections);
    }
}
