package com.lmaguado.hulkStore.helpers.strings;

public class DatabaseStrings {
    private DatabaseStrings()  {
        throw new IllegalStateException("Constants - Utility class");
    }
    // Database Name
    public static final String GET_DATABASE_NAME = "hulk_store_db";

    // General
    public static final String GET_RESULT = "RESULT";

    // Store procedure -> GET_USER
    public static final String GET_USER_ID = "USER_ID";
    public static final String GET_USER_NAME = "USER_NAME";
    public static final String GET_USER_EMAIL = "USER_EMAIL";
    public static final String GET_USER_PERM_LABEL = "PERM_LABEL";
    public static final String GET_USER_PERM_WEIGHT = "PERM_WEIGHT";

    // Store procedure -> GET_PERMISSION
    public static final String GET_PERMISSION_ID = "PERMIT_ID";
    public static final String GET_PERMISSION_LABEL = "PERMIT_LABEL";
    public static final String GET_PERMISSION_WEIGHT = "PERMIT_WEIGHT";

    // Store procedure -> GET_USER_PASS
    public static final String GET_USER_PASS_PASSWORD = "USER_PASSWORD";

    // Store procedure -> GET_TOKEN_BLACKLIST
    public static final String GET_TOKEN_BLACKLIST_TOKEN = "TOKEN_TOKEN";
    public static final String GET_TOKEN_BLACKLIST_OWN = "USER_OWN";
    public static final String GET_TOKEN_BLACKLIST_REASON = "TOKEN_REASON";
    public static final String GET_TOKEN_BLACKLIST_BLOCKED = "TOKEN_BLOCKED_BY";
    public static final String GET_TOKEN_BLACKLIST_EXPIRED = "TOKEN_EXPIRED";
}
