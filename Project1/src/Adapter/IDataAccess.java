package Adapter;

import Model.CustomerModel;
import Model.ProductModel;
import Model.PurchaseModel;

public interface IDataAccess {
    public static final int CONNECTION_OPEN_OK = 0;
    public static final int CONNECTION_OPEN_FAILED = 1;

    public static final int PRODUCT_SAVE_OK = 0;
    public static final int PRODUCT_SAVE_FAILED = 101;
    public static final int PRODUCT_SAVE_DUPLICATE = 102;

    public static final int PRODUCT_LOAD_OK = 0;
    public static final int PRODUCT_LOAD_FAILED = 201;
    public static final int PRODUCT_LOAD_ID_NOT_FOUND = 202;

    public static final int CUSTOMER_LOAD_OK = 0;
    public static final int CUSTOMER_LOAD_FAILED = 301;
    public static final int CUSTOMER_LOAD_ID_NOT_FOUND = 302;

    public static final int CUSTOMER_SAVE_OK = 0;
    public static final int CUSTOMER_SAVE_FAILED = 401;
    public static final int CUSTOMER_SAVE_DUPLICATE = 402;

    public static final int PURCHASE_LOAD_OK = 0;
    public static final int PURCHASE_LOAD_FAILED = 501;
    public static final int PURCHASE_LOAD_ID_NOT_FOUND = 502;

    public static final int PURCHASE_SAVE_OK = 0;
    public static final int PURCHASE_SAVE_FAILED = 601;
    public static final int PURCHASE_SAVE_DUPLICATE = 602;

    public boolean connect(String path);
    public ProductModel loadProduct(int id);
    public boolean saveProduct(ProductModel product);
    public CustomerModel loadCustomer(int id);
    public boolean saveCustomer(CustomerModel product);
    public PurchaseModel loadPurchase(int id);
    public boolean savePurchase(PurchaseModel purchase);

    public int getErrorCode();
    public String getErrorMessage();
}
