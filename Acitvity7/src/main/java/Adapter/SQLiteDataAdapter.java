package Adapter;

import Model.ProductModel;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;


public class SQLiteDataAdapter {
    Connection conn = null;
    public boolean connect(){
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/Users/Ai-Te/store.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public ProductModel loadProduct(int productID) {
        ProductModel product = new ProductModel();
        try {
            String sql = "SELECT productID, productName, unitPrice, stockQuantity FROM Products WHERE ProductID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1,productID);
            ResultSet rs = pStmt.executeQuery();
            product.mProductID = rs.getInt("productID");
            product.mName = rs.getString("productName");
            product.mPrice = rs.getDouble("unitPrice");
            product.mQuantity = rs.getDouble("stockQuantity");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return product;
    }
    public boolean saveProduct(ProductModel product){
        try {
            String sql = "INSERT INTO Products (productID, productName, unitPrice, stockQuantity) VALUES (?,?,?,?)";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1,product.mProductID);
            pStmt.setString(2,product.mName);
            pStmt.setDouble(3,product.mPrice);
            pStmt.setDouble(4,product.mQuantity);
            pStmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
