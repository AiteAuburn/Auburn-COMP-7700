package Adapter;

import Model.CustomerModel;
import Model.ProductModel;
import Model.PurchaseModel;
import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;


public class SQLiteDataAdapter implements IDataAccess{
    Connection conn = null;
    public int errorCode = 0;

    public boolean connect(String path) {
        try {
            // db parameters
            String url = "jdbc:sqlite:" + path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            return true;
        } catch (SQLException e) {
            errorCode = CONNECTION_OPEN_FAILED;
            return false;
        }
    }
    public ProductModel loadProduct(int productID) {
        try {
            ProductModel product = new ProductModel();
            String sql = "SELECT productID, productName, unitPrice, stockQuantity FROM Products WHERE productID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1,productID);
            ResultSet rs = pStmt.executeQuery();
            product.mProductID = rs.getInt("productID");
            product.mName = rs.getString("productName");
            product.mPrice = rs.getDouble("unitPrice");
            product.mQuantity = rs.getDouble("stockQuantity");
            return product;
        } catch (Exception e) {
            errorCode = PRODUCT_LOAD_FAILED;
            return null;
        }
    }
    public boolean saveProduct(ProductModel product){
        try {
            String sql = "INSERT INTO Products (productID, productName, unitPrice, stockQuantity) VALUES (?,?,?,?)";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, product.mProductID);
            pStmt.setString(2, product.mName);
            pStmt.setDouble(3, product.mPrice);
            pStmt.setDouble(4, product.mQuantity);
            pStmt.executeUpdate();
            return true;
        } catch (Exception e) {
            errorCode = PRODUCT_SAVE_FAILED;
            return false;
        }
    }

    public CustomerModel loadCustomer(int customerID) {
        try {
            CustomerModel customer = new CustomerModel();
            String sql = "SELECT customerID, name, phone, address FROM Customers WHERE customerID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, customerID);
            ResultSet rs = pStmt.executeQuery();
            customer.mCustomerID = rs.getInt("customerID");
            customer.mName = rs.getString("name");
            customer.mAddress = rs.getString("address");
            customer.mPhone = rs.getString("phone");
            return customer;
        } catch (Exception e) {
            System.out.println("hey");
            errorCode = CUSTOMER_LOAD_FAILED;
            return null;
        }
    }
    public boolean saveCustomer(CustomerModel customer){
        try {
            String sql = "INSERT INTO Customers (customerID, name, address, phone) VALUES (?,?,?,?)";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, customer.mCustomerID);
            pStmt.setString(2, customer.mName);
            pStmt.setString(3, customer.mAddress);
            pStmt.setString(4, customer.mPhone);
            pStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            errorCode = CUSTOMER_SAVE_FAILED;
            return false;
        }
    }

    public PurchaseModel loadPurchase(int purchaseID) {
        try {
            PurchaseModel purchase = new PurchaseModel();
            String sql = "SELECT purchaseID, customerID, productID, price, quantity, tax, totalCost, date FROM Purchases WHERE purchaseID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            ResultSet rs = pStmt.executeQuery();
            purchase.mPurchaseID = rs.getInt("purchaseID");
            purchase.mCustomerID = rs.getInt("customerID");
            purchase.mProductID = rs.getInt("productID");
            purchase.mPrice = rs.getDouble("price");
            purchase.mQuantity = rs.getDouble("quantity");
            purchase.mTax = rs.getDouble("tax");
            purchase.mTotalCost = rs.getDouble("totalCost");
            purchase.mDate = rs.getString("date");
            return purchase;
        } catch (Exception e) {
            errorCode = PURCHASE_LOAD_FAILED;
            return null;
        }
    }
    public boolean savePurchase(PurchaseModel purchase){
        try {
            String sql = "INSERT INTO Purchases (purchaseID, customerID, productID, price, quantity, cost, tax, totalCost, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, purchase.mPurchaseID);
            pStmt.setInt(2, purchase.mCustomerID);
            pStmt.setInt(3, purchase.mProductID);
            pStmt.setDouble(4, purchase.mPrice);
            pStmt.setDouble(5, purchase.mQuantity);
            pStmt.setDouble(6, purchase.mCost);
            pStmt.setDouble(7, purchase.mTax);
            pStmt.setDouble(8, purchase.mTotalCost);
            pStmt.setString(9, purchase.mDate);
            pStmt.executeUpdate();
            return true;
        } catch (Exception e) {
            errorCode = PURCHASE_SAVE_FAILED;
            return false;
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        switch (errorCode) {
            case CONNECTION_OPEN_FAILED: return "Connection is not opened!";
            case PURCHASE_LOAD_FAILED: return "Cannot load the purchase!";
            case CUSTOMER_LOAD_FAILED: return "Cannot load the customer";
            case PRODUCT_LOAD_FAILED: return "Cannot load the product!";
            case PURCHASE_SAVE_FAILED: return "Cannot save the purchase!";
            case CUSTOMER_SAVE_FAILED: return "Cannot save the customer";
            case PRODUCT_SAVE_FAILED: return "Cannot save the product!";
        };
        return "";
    }
}
