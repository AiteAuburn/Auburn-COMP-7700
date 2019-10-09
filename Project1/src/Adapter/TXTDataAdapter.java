package Adapter;

import Model.CustomerModel;
import Model.ProductModel;
import Model.PurchaseModel;

import java.io.File;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TXTDataAdapter implements IDataAccess {
    public int errorCode = 0;

    Map<Integer, ProductModel> products = new HashMap<Integer, ProductModel>();
    Map<Integer, CustomerModel> customers = new HashMap<Integer, CustomerModel>();
    Map<Integer, PurchaseModel> purchases = new HashMap<Integer, PurchaseModel>();

    public boolean connect(String path) {
        try {
            Scanner scanner = new Scanner(new FileReader(new File(path)));
            ProductModel product = new ProductModel();

            while (scanner.hasNext()) {
                product.mProductID = scanner.nextInt();
                product.mName = scanner.nextLine();
                product.mPrice = scanner.nextDouble();
                product.mQuantity = scanner.nextDouble();
                System.out.println(product);
                products.put(product.mProductID, product);
            }

            scanner.close();
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public ProductModel loadProduct(int productID) {
        if (products.containsKey(productID))
            return products.get(productID);
        else
            return null;
    }

    @Override
    public boolean saveProduct(ProductModel product) {

        return false;
    }


    @Override
    public CustomerModel loadCustomer(int customerID) {
        if (customers.containsKey(customerID))
            return customers.get(customerID);
        else
            return null;
    }

    @Override
    public boolean saveCustomer(CustomerModel product) {

        return false;
    }
    public PurchaseModel loadPurchase(int purchaseID) {
        if (purchases.containsKey(purchaseID))
            return purchases.get(purchaseID);
        else
            return null;
    }
    public boolean savePurchase(PurchaseModel purchase){

        return false;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        switch (errorCode) {
            case CONNECTION_OPEN_FAILED: return "Connection is not opened!";
            case PRODUCT_LOAD_FAILED: return "Cannot load the product!";
        };
        return "";
    }
}
