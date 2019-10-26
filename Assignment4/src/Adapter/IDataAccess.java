package Adapter;

import Model.CustomerModel;
import Model.ProductModel;
import Model.PurchaseModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface IDataAccess {
    public static final int CONNECTION_OPEN_OK = 0;
    public static final int CONNECTION_OPEN_FAILED = 1;

    public static final int PRODUCT_SAVE_OK = 100;
    public static final int PRODUCT_SAVE_FAILED = 101;
    public static final int PRODUCT_SAVE_DUPLICATE = 102;
    public static final int PRODUCT_EDIT_OK = 103;

    public static final int PRODUCT_LOAD_OK = 200;
    public static final int PRODUCT_LOAD_FAILED = 201;
    public static final int PRODUCT_LOAD_ID_NOT_FOUND = 202;;

    public static final int CUSTOMER_LOAD_OK = 300;
    public static final int CUSTOMER_LOAD_FAILED = 301;
    public static final int CUSTOMER_LOAD_ID_NOT_FOUND = 302;

    public static final int CUSTOMER_SAVE_OK = 400;
    public static final int CUSTOMER_SAVE_FAILED = 401;
    public static final int CUSTOMER_SAVE_DUPLICATE = 402;
    public static final int CUSTOMER_EDIT_OK = 403;

    public static final int PURCHASE_LOAD_OK = 500;
    public static final int PURCHASE_LOAD_FAILED = 501;
    public static final int PURCHASE_LOAD_ID_NOT_FOUND = 502;

    public static final int PURCHASE_SAVE_OK = 600;
    public static final int PURCHASE_SAVE_FAILED = 601;
    public static final int PURCHASE_SAVE_DUPLICATE = 602;
    public static final int PURCHASE_EDIT_OK = 603;
    public static final HashMap<Integer, String> errMsg = new HashMap<Integer, String>() {
        {
            put(CONNECTION_OPEN_FAILED, "Connection is not opened!");
            put(PURCHASE_LOAD_FAILED, "Cannot load the purchase!");
            put(CUSTOMER_LOAD_FAILED, "Cannot load the customer");
            put(PRODUCT_LOAD_FAILED, "Cannot load the product!");
            put(PURCHASE_SAVE_FAILED, "Cannot save the purchase!");
            put(CUSTOMER_SAVE_FAILED, "Cannot save the customer");
            put(PRODUCT_SAVE_FAILED, "Cannot save the product!");
            put(PURCHASE_LOAD_ID_NOT_FOUND, "Purchase ID not found!");
            put(CUSTOMER_LOAD_ID_NOT_FOUND, "Customer ID not found!");
            put(PRODUCT_LOAD_ID_NOT_FOUND, "Product ID not found!");
        }};

    public boolean connect(String path);
    public boolean disconnect();
    public ProductModel loadProduct(int id);
    public boolean saveProduct(ProductModel product);
    public CustomerModel loadCustomer(int id);
    public boolean saveCustomer(CustomerModel product);
    public PurchaseModel loadPurchase(int id);
    public boolean savePurchase(PurchaseModel purchase);

    public int getErrorCode();
    public String getErrorMessage();
}
