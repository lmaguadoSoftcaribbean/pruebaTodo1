package com.lmaguado.hulkStore.models.implement.exceptions;

public class ConnectionPoolException extends Exception {
    public ConnectionPoolException(String message) {
        super(message);
    }

    public static class ConnectionPoolExceptionMessage {
        private ConnectionPoolExceptionMessage() {
            throw new IllegalStateException("ConnectionPoolExceptionMessage - Utility class");
        }

        public static final String ERROR_TO_CREATING_THE_CONNECTION_FOR_POOL = "Was throw a error to creating a new connection for the pool.";
    }
}
