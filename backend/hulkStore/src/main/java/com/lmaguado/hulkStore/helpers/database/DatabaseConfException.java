package com.lmaguado.hulkStore.helpers.database;

public class DatabaseConfException extends Exception {

    public DatabaseConfException(String message) {
        super(message);
    }

    public static class DatabaseConfExceptionMessage {
        private DatabaseConfExceptionMessage() {
            throw new IllegalStateException("DatabaseConfExceptionMessage - Utility class");
        }

        public static final String ERROR_TO_CREATE_THE_POOL_CONNECTION_VALIDATE_PROPERTIES = "The database properties file is not change initials values.";
        public static final String ERROR_TO_CREATE_THE_POOL_CONNECTION_PARSING_VALUES = "Was generate a error to parse the values from database properties file.";
        public static final String ERROR_TO_CREATE_THE_POOL_CONNECTION_THE_MAX_VALUE_CANT_BE_LOWER_TO_INITIAL = "Was detected that value \"maxConnections\" is lower to \"initConnections\" on database file properties.";
    }
}
