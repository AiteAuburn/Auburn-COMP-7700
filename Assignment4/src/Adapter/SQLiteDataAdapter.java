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
    String url = null;
    public int errorCode = 0;

    public SQLiteDataAdapter() {
    }

    public SQLiteDataAdapter(String path) {
        url = "jdbc:sqlite:" + path;
    }

    public boolean connect(String path) {
        try {
            // db parameters
            url = "jdbc:sqlite:" + path;
            // create a connection to the database
            return true;
        } catch (Exception e) {
            errorCode = CONNECTION_OPEN_FAILED;
            return false;
        }
    }
    @Override
    public boolean disconnect() {
        return true;
    }

    public ProductModel loadProduct(int productID) {
        try {
            Connection conn = DriverManager.getConnection(url);
            ProductModel product = new ProductModel();
            String sql = "SELECT productID, productName, unitPrice, stockQuantity FROM Products WHERE productID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1,productID);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()) {
                product.mProductID = rs.getInt("productID");
                product.mName = rs.getString("productName");
                product.mPrice = rs.getDouble("unitPrice");
                product.mQuantity = rs.getDouble("stockQuantity");
                errorCode = PRODUCT_LOAD_OK;
            } else {
                product = null;
                errorCode = PRODUCT_LOAD_ID_NOT_FOUND;
            }
            rs.close();
            pStmt.close();
            conn.close();
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            errorCode = PRODUCT_LOAD_FAILED;
            return null;
        }
    }
    public boolean saveProduct(ProductModel product){
        try {
            boolean edit = false;
            Connection conn = DriverManager.getConnection(url);
            ProductModel model = loadProduct(product.mProductID);
            // Delete if exists
            Statement stmt = conn.createStatement();
            if (model != null) {
                stmt.execute("DELETE FROM Products WHERE ProductId = " + product.mProductID);
                edit = true;
            }
            // Insert record
            String sql = "INSERT INTO Products (productID, productName, unitPrice, stockQuantity) VALUES (?,?,?,?)";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, product.mProductID);
            pStmt.setString(2, product.mName);
            pStmt.setDouble(3, product.mPrice);
            pStmt.setDouble(4, product.mQuantity);
            errorCode = (edit) ? PRODUCT_EDIT_OK: PRODUCT_SAVE_OK;
            pStmt.executeUpdate();
            stmt.close();
            pStmt.close();
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = PRODUCT_SAVE_FAILED;
            return false;
        }
    }

    public CustomerModel loadCustomer(int customerID) {
        try {
            Connection conn = DriverManager.getConnection(url);
            CustomerModel customer = new CustomerModel();
            String sql = "SELECT customerID, name, phone, address FROM Customers WHERE customerID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, customerID);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()) {
                customer.mCustomerID = rs.getInt("customerID");
                customer.mName = rs.getString("name");
                customer.mAddress = rs.getString("address");
                customer.mPhone = rs.getString("phone");
                errorCode = CUSTOMER_LOAD_OK;
            } else {
                customer = null;
                errorCode = CUSTOMER_LOAD_ID_NOT_FOUND;
            }
            rs.close();
            pStmt.close();
            conn.close();
            return customer;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = CUSTOMER_LOAD_FAILED;
            return null;
        }
    }
    public boolean saveCustomer(CustomerModel customer){
        try {
            boolean edit = false;
            Connection conn = DriverManager.getConnection(url);
            CustomerModel model = loadCustomer(customer.mCustomerID);
            // Delete if exists
            Statement stmt = conn.createStatement();
            if (model != null) {
                stmt.execute("DELETE FROM Customers WHERE customerID = " + customer.mCustomerID);
                edit = true;
            }
            // Insert record
            String sql = "INSERT INTO Customers (customerID, name, address, phone) VALUES (?,?,?,?)";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, customer.mCustomerID);
            pStmt.setString(2, customer.mName);
            pStmt.setString(3, customer.mAddress);
            pStmt.setString(4, customer.mPhone);
            pStmt.executeUpdate();
            errorCode = (edit) ? CUSTOMER_EDIT_OK: CUSTOMER_SAVE_OK;
            stmt.close();
            pStmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            errorCode = CUSTOMER_SAVE_FAILED;
            return false;
        }
    }

    public PurchaseModel loadPurchase(int purchaseID) {
        try {
            Connection conn = DriverManager.getConnection(url);
            PurchaseModel purchase = new PurchaseModel();
            String sql = "SELECT purchaseID, customerID, productID, price, quantity, tax, totalCost, date FROM Purchases WHERE purchaseID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, purchaseID);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()) {
                purchase.mPurchaseID = rs.getInt("purchaseID");
                purchase.mCustomerID = rs.getInt("customerID");
                purchase.mProductID = rs.getInt("productID");
                purchase.mPrice = rs.getDouble("price");
                purchase.mQuantity = rs.getDouble("quantity");
                purchase.mTax = rs.getDouble("tax");
                purchase.mTotalCost = rs.getDouble("totalCost");
                purchase.mDate = rs.getString("date");
                errorCode = PURCHASE_LOAD_OK;
            } else {
                purchase = null;
                errorCode = PURCHASE_LOAD_ID_NOT_FOUND;
            }
            rs.close();
            pStmt.close();
            conn.close();
            return purchase;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = PURCHASE_LOAD_FAILED;
            return null;
        }
    }
    public boolean savePurchase(PurchaseModel purchase){
        try {
            boolean edit = false;
            Connection conn = DriverManager.getConnection(url);
            PurchaseModel model = loadPurchase(purchase.mPurchaseID);
            // Delete if exists
            Statement stmt = conn.createStatement();
            if (model != null) {
                stmt.execute("DELETE FROM Purchases WHERE purchaseID = " + purchase.mPurchaseID);
                edit = true;
            }
            // Insert record
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
            errorCode = (edit) ? PURCHASE_EDIT_OK: PURCHASE_SAVE_OK;
            stmt.close();
            pStmt.close();
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = PURCHASE_SAVE_FAILED;
            return false;
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        String output = "ErrorCode: " + errorCode + "\nMsg: " + errMsg.get(errorCode);
        return output;
    }
}
