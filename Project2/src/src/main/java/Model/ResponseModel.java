package Model;

import java.util.HashMap;

public class ResponseModel {
    private int statusCode = 0;
    private String response = "";

    public static final int CONNECTION_OPEN_OK = 0;
    public static final int CONNECTION_OPEN_FAILED = 1;

    public static final int PRODUCT_SAVE_OK = 100;
    public static final int PRODUCT_SAVE_FAILED = 101;
    public static final int PRODUCT_SAVE_DUPLICATE = 102;
    public static final int PRODUCT_EDIT_OK = 103;

    public static final int PRODUCT_LOAD_OK = 200;
    public static final int PRODUCT_LOAD_FAILED = 201;
    public static final int PRODUCT_LOAD_ID_NOT_FOUND = 202;;

    public static final int USER_LOAD_OK = 300;
    public static final int USER_LOAD_FAILED = 301;
    public static final int USER_LOAD_ID_NOT_FOUND = 302;
    public static final int USER_HISTORY_LOAD_OK = 303;
    public static final int USER_HISTORY_LOAD_FAILED = 304;
    public static final int USER_HISTORY_LOAD_ID_NOT_FOUND = 305;
    public static final int USER_LOGIN_OK = 306;
    public static final int USER_LOGIN_FAILED = 307;
    public static final int USER_LOGIN_NOT_MATCH= 308;

    public static final int USER_SAVE_OK = 400;
    public static final int USER_SAVE_FAILED = 401;
    public static final int USER_SAVE_DUPLICATE = 402;
    public static final int USER_EDIT_OK = 403;

    public static final int PURCHASE_LOAD_OK = 500;
    public static final int PURCHASE_LOAD_FAILED = 501;
    public static final int PURCHASE_LOAD_ID_NOT_FOUND = 502;

    public static final int PURCHASE_SAVE_OK = 600;
    public static final int PURCHASE_SAVE_FAILED = 601;
    public static final int PURCHASE_SAVE_PRODUCT_ID_NOT_FOUND = 602;
    public static final int PURCHASE_SAVE_DUPLICATE = 603;
    public static final int PURCHASE_EDIT_OK = 604;
    public static final int PURCHASE_SEARCH_OK = 605;
    public static final int PURCHASE_SEARCH_FAILED = 606;

    public static final int CUSTOMER_LOAD_OK = 700;
    public static final int CUSTOMER_LOAD_FAILED = 701;
    public static final int CUSTOMER_LOAD_ID_NOT_FOUND = 702;

    public static final int CUSTOMER_SAVE_OK = 800;
    public static final int CUSTOMER_SAVE_FAILED = 801;
    public static final int CUSTOMER_SAVE_DUPLICATE = 802;
    public static final int CUSTOMER_EDIT_OK = 803;

    public static final int PRODUCT_SEARCH_OK = 901;
    public static final int PRODUCT_SEARCH_FAILED = 902;

    public static final HashMap<Integer, String> errMsg = new HashMap<Integer, String>() {
        {
            put(CONNECTION_OPEN_FAILED, "Connection is not opened!");
            put(PURCHASE_LOAD_FAILED, "Cannot load the purchase!");
            put(USER_LOAD_FAILED, "Cannot load the user");
            put(CUSTOMER_LOAD_FAILED, "Cannot load the customer");
            put(PRODUCT_LOAD_FAILED, "Cannot load the product!");
            put(PURCHASE_SAVE_FAILED, "Cannot save the purchase!");
            put(USER_SAVE_FAILED, "Cannot save the user");
            put(CUSTOMER_SAVE_FAILED, "This id is taken by the admin, manager, or cashier.\nPlease use another customer id.");
            put(PRODUCT_SAVE_FAILED, "Cannot save the product!");
            put(PURCHASE_LOAD_ID_NOT_FOUND, "Purchase ID not found!");
            put(PURCHASE_SAVE_PRODUCT_ID_NOT_FOUND, "Product does not exist with the ID provided.");
            put(USER_LOAD_ID_NOT_FOUND, "USER ID not found!");
            put(PRODUCT_LOAD_ID_NOT_FOUND, "Product ID not found!");
            put(USER_HISTORY_LOAD_FAILED, "Cannot load the user history");
            put(USER_HISTORY_LOAD_ID_NOT_FOUND, "No purchase record found");
            put(USER_LOGIN_FAILED, "Check the params format.");
            put(USER_LOGIN_NOT_MATCH, "Incorrect name or password");
        }};

    public static String getErrorMessage(int errorCode) {
        String output = "ErrorCode: " + errorCode + "\nMsg: " + ResponseModel.errMsg.get(errorCode);
        return output;
    }
    public void setStatusCode(int statusCodeIn){
        statusCode = statusCodeIn;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public void setResponse(String responseIn){
        response = responseIn;
    }
    public String getResponse() {
        return response;
    }
}
