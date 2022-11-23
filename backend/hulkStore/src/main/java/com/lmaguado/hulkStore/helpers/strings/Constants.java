package com.lmaguado.hulkStore.helpers.strings;

public class Constants {
    private Constants()  {
        throw new IllegalStateException("Constants - Utility class");
    }

    public static String getRandomUUID() {
        return java.util.UUID.randomUUID().toString();
    }
    public static final String FOLDER_SECRET_PATH = "secret";
    public static final String PROPERTIES_DATABASE_NAME = "database";
    public static final String PROPERTIES_JWT_NAME = "jwt";
    public static final String PROPERTY_FILE_DEFAULT_VALUE = "{Not_Implement}";
    public static final String PROPERTY_FILE_JWT_EXPIRATION_TIME = "3600";
}
