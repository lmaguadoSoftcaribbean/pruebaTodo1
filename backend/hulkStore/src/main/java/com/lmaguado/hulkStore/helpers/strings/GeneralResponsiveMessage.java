package com.lmaguado.hulkStore.helpers.strings;

public class GeneralResponsiveMessage {
    private GeneralResponsiveMessage() {
        throw new IllegalStateException("GeneralResponsiveMessage - Utility class");
    }

    // GENERAL
    public static final String REQUEST_SUCCESS = "Request successfully.";

    // USER
    public static final String USER_DATA_IS_NULL = "The user data is empty.";
    public static final String USER_ALREADY_EXISTS = "The user already exists.";
    public static final String USER_DOES_NOT_EXIST = "The user does not exist.";
    public static final String USER_CREATED_SUCCESSFULLY = "User created successfully.";
    public static final String USER_LOGIN_SUCCESSFULLY = "User login successfully.";

    // Params
    public static final String PARAMS_KEY_IS_NULL = "The param key value is empty.";

    // Products
    public static final String PRODUCT_CODE_IS_EMPTY = "The code is empty.";
    public static final String PRODUCT_CANT_BE_FOUND = "Product can't be found.";
    public static final String PRODUCT_OVERFLOW_UNIT_EXISTENT = "The product's units that you wanna buy overflow the units existent.";
    public static final String PRODUCT_SET_UNIT_ERROR = "Unexpected error them store the new units value.";
    public static final String PRODUCT_SET_UNIT_SUCCESS = "Product update successfully.";
    public static final String PRODUCT_MIN_1_UNIT = "The minimum number of units is 1.";
}
