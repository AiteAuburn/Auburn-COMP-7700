package Adapter;

import Model.CustomerModel;
import Model.ProductModel;
import Model.PurchaseModel;

import java.util.HashMap;
import java.util.Map;

public class CachedDataAdapter implements IDataAccess {
    Map<Integer, ProductModel> cachedProducts = new HashMap<Integer, ProductModel>();
    Map<Integer, CustomerModel> cachedCustomers = new HashMap<Integer, CustomerModel>();
    Map<Integer, PurchaseModel> cachedPurchases = new HashMap<Integer, PurchaseModel>();
    IDataAccess adapter;
    public int errorCode = 0;

    public CachedDataAdapter(IDataAccess adapter) {
        this.adapter = adapter;
    }


    @Override
    public boolean connect(String path) {
        return this.adapter.connect(path);
    }

    @Override
    public boolean disconnect() {
        return true;
    }

    @Override
    public ProductModel loadProduct(int productID) {
        if (cachedProducts.containsKey(productID))
            return cachedProducts.get(productID);
        else {
            ProductModel product = adapter.loadProduct(productID);
            cachedProducts.put(productID, product);
            return product;
        }
    }

    @Override
    public boolean saveProduct(ProductModel product) {
        adapter.saveProduct(product);
        cachedProducts.put(product.mProductID, product);
        return true;
    }

    @Override
    public CustomerModel loadCustomer(int customerID) {
        if (cachedCustomers.containsKey(customerID))
            return cachedCustomers.get(customerID);
        else {
            CustomerModel customer = adapter.loadCustomer(customerID);
            cachedCustomers.put(customerID, customer);
            return customer;
        }
    }

    @Override
    public boolean saveCustomer(CustomerModel customer) {
        if(!adapter.saveCustomer(customer))
            return false;
        cachedCustomers.put(customer.mCustomerID, customer);
        return true;
    }

    @Override
    public PurchaseModel loadPurchase(int purchaseID) {
        if (cachedPurchases.containsKey(purchaseID))
            return cachedPurchases.get(purchaseID);
        else {
            PurchaseModel purchase = adapter.loadPurchase(purchaseID);
            cachedPurchases.put(purchaseID, purchase);
            return purchase;
        }
    }

    @Override
    public boolean savePurchase(PurchaseModel purchase) {
        if(!adapter.savePurchase(purchase))
            return false;
        cachedPurchases.put(purchase.mPurchaseID, purchase);
        return true;
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
