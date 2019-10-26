package edu.auburn;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataAdapter implements IDataAccess {
    String url = null;
    int errorCode = 0;

    public SQLiteDataAdapter(String path) {
        url = "jdbc:sqlite:" + path;
    }

    public boolean connect(String path) {
        try {
            // db parameters
            // create a connection to the database
            //conn =
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = CONNECTION_OPEN_FAILED;
            return false;
        }

    }

    @Override
    public boolean disconnect() {
        return true;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        switch (errorCode) {
            case CONNECTION_OPEN_FAILED:
                return "Connection is not opened!";
            case PRODUCT_LOAD_FAILED:
                return "Cannot load the product!";
        }
        ;
        return "OK";
    }

    public ProductModel loadProduct(int productID) {
        ProductModel product = null;
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Products WHERE ProductId = " + productID);
            if (rs.next()) {
                product = new ProductModel();
                product.mProductID = rs.getInt("ProductId");
                product.mName = rs.getString("Name");
                product.mPrice = rs.getDouble("Price");
                product.mQuantity = rs.getDouble("Quantity");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            errorCode = PRODUCT_LOAD_FAILED;
        }
        return product;
    }

    public boolean saveProduct(ProductModel product) {
        try {
            ProductModel model = loadProduct(product.mProductID);
            Connection conn = DriverManager.getConnection(url);
            String sql;
            Statement stmt = conn.createStatement();
            if (model != null)  // delete this record
                stmt.execute("DELETE FROM Products WHERE ProductId = " + product.mProductID);
            stmt.execute("INSERT INTO Products VALUES " + product);
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = PRODUCT_LOAD_FAILED;
            return false;
        }
        return true;
    }

    public CustomerModel loadCustomer(int id) {
        try {
            CustomerModel c = new CustomerModel();
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customers WHERE CustomerID = " + id);
            c.mCustomerID = rs.getInt("CustomerID");
            c.mName = rs.getString("Name");
            c.mPhone = rs.getString("Phone");
            c.mAddress = rs.getString("Address");
            return c;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = CUSTOMER_LOAD_FAILED;
            return null;
        }
    }



}
