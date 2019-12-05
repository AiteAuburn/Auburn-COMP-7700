package ServerSide;

import java.sql.*;

import java.util.ArrayList;

public class SQLiteDataAdapter {
    Connection conn = null;
    int errorCode = 0;

    public boolean connect(String path) {
        try {
            // db parameters
            String url = "jdbc:sqlite:" + path;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            errorCode = 201;
            return false;
        }

    }

    public boolean disconnect() {
        return true;
    }

    public ProductModel loadProduct(int productID) {
        try {
            ProductModel product = new ProductModel();

            String sql = "SELECT productID, productName, unitPrice, stockQuantity FROM Products WHERE productID = " + productID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            product.mProductID = rs.getInt("productID");
            product.mName = rs.getString("productName");
            product.mPrice = rs.getDouble("unitPrice");
            product.mQuantity = rs.getDouble("stockQuantity");
            return product;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = 101;
            return null;
        }
    }
    public ProductModel[] loadAllProduct() {
        try {
            String sql = "SELECT productID, productName, unitPrice, stockQuantity FROM Products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int size = 0;
            int i = 0;
            ArrayList<ProductModel> pmArray = new ArrayList<ProductModel>();
            while(rs.next()){
                ProductModel product = new ProductModel();
                product.mProductID = rs.getInt("productID");
                product.mName = rs.getString("productName");
                product.mPrice = rs.getDouble("unitPrice");
                product.mQuantity = rs.getDouble("stockQuantity");
                pmArray.add(product);
            }
            ProductModel[] pm = new ProductModel[pmArray.size()];
            pmArray.toArray(pm);
            return pm;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = 101;
            return null;
        }
    }
    public boolean changeUserPassword(UserModel user, String newPasswordIn) {
        try {
            String sql = "SELECT userID FROM Users WHERE userID = ? and password = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, user.mUserID);
            pStmt.setString(2, user.mPassword);
            ResultSet rs = pStmt.executeQuery();
            boolean userFound = false;
            if(rs.next())
                userFound = true;
            if(userFound) {
                sql = "UPDATE Users SET password = ? WHERE userID = ?";
                pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, newPasswordIn);
                pStmt.setInt(2, user.mUserID);
                pStmt.executeUpdate();
            } else
                return false;
            pStmt.close();
            conn.close();
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = 101;
            return false;
        }
    }
    public boolean saveProduct(ProductModel product) {
        try {
            boolean edit = false;
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
            pStmt.executeUpdate();
            stmt.close();
            pStmt.close();
            conn.close();
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = 101;
            return false;
        }
    }
    public ProductModel[] searchProductByName(String productName) {
        try {
            String sql = "SELECT productID, productName, unitPrice, stockQuantity FROM Products WHERE productName LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, '%' + productName + '%');
            ResultSet rs = stmt.executeQuery();
            ArrayList<ProductModel> pmArray = new ArrayList<ProductModel>();
            while(rs.next()){
                ProductModel product = new ProductModel();
                product.mProductID = rs.getInt("productID");
                product.mName = rs.getString("productName");
                product.mPrice = rs.getDouble("unitPrice");
                product.mQuantity = rs.getDouble("stockQuantity");
                pmArray.add(product);
            }
            ProductModel[] pm = new ProductModel[pmArray.size()];
            pmArray.toArray(pm);
            return pm;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = 101;
            return null;
        }
    }

}
