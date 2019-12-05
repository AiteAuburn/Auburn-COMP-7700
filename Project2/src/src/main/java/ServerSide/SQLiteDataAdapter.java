package ServerSide;

import Model.ProductModel;
import Model.PurchaseModel;
import Model.ResponseModel;
import Model.UserModel;

import java.sql.*;

import java.util.ArrayList;
import java.util.Calendar;

public class SQLiteDataAdapter {
    Connection conn = null;
    private int errorCode = 0;
    private static final double taxRate = 0.09;

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

    public UserModel loginUser(String fullName, String password) {
        try {
            UserModel user = new UserModel();
            String sql = "SELECT userID, password, userType, name, phone, address FROM Users WHERE name = ? and password = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, fullName);
            pStmt.setString(2, password);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()){
                user.mUserID = rs.getInt("userID");
                user.mPassword = rs.getString("password");
                user.mName = rs.getString("name");
                user.mAddress = rs.getString("address");
                user.mPhone = rs.getString("phone");
                user.mUserType = rs.getInt("userType");
                errorCode = ResponseModel.USER_LOGIN_OK;
                pStmt.close();
            } else{
                errorCode = ResponseModel.USER_LOGIN_NOT_MATCH;
                pStmt.close();
                return null;
            }
            return user;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = ResponseModel.USER_LOGIN_FAILED;
            return null;
        }
    }
    public ProductModel loadProduct(int productID) {
        try {
            String sql = "SELECT productID, productName, unitPrice, stockQuantity FROM Products WHERE productID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, productID);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()) {
                ProductModel product = new ProductModel();
                product.mProductID = rs.getInt("productID");
                product.mName = rs.getString("productName");
                product.mPrice = rs.getDouble("unitPrice");
                product.mQuantity = rs.getDouble("stockQuantity");
                errorCode = ResponseModel.PRODUCT_LOAD_OK;
                pStmt.close();
                return product;
            } else {
                errorCode = ResponseModel.PRODUCT_LOAD_FAILED;
                pStmt.close();
                return null;
            }

        } catch (Exception e) {
            errorCode = ResponseModel.PRODUCT_LOAD_FAILED;
            return null;
        }
    }

    public UserModel loadCustomer(int customerID) {
        try {
            UserModel user = new UserModel();
            String sql = "SELECT userID, password, userType, name, phone, address FROM Users WHERE userID = ? and userType = 1";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, customerID);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()){
                user.mUserID = rs.getInt("userID");
                user.mPassword = rs.getString("password");
                user.mName = rs.getString("name");
                user.mAddress = rs.getString("address");
                user.mPhone = rs.getString("phone");
                user.mUserType = rs.getInt("userType");
                errorCode = ResponseModel.USER_LOAD_OK;
                pStmt.close();
            } else{
                errorCode = ResponseModel.USER_LOAD_ID_NOT_FOUND;
                pStmt.close();
                return null;
            }
            return user;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = ResponseModel.USER_LOAD_FAILED;
            return null;
        }
    }
    public boolean saveCustomer(UserModel userIn) {
        try {
            UserModel user = loadUser(userIn.mUserID);
            if(user != null) {
                if (user.mUserID == -1)
                    user.mUserID = userIn.mUserID;
                if (userIn.mName.length() != 0)
                    user.mName = userIn.mName;
                if (userIn.mPassword.length() != 0)
                    user.mPassword = userIn.mPassword;
                user.mPhone = userIn.mPhone;
                user.mAddress = userIn.mAddress;
            }

            if(user != null && user.mUserType == 1) {
                    // UPDATE USER
                    System.out.println("----------");
                    System.out.println("UPDATE USER");
                    System.out.println(user);
                    System.out.println("----------");
                    String sql;
                    PreparedStatement pStmt;
                    sql = "UPDATE Users SET password = ?, userType = ?, name = ?, phone = ?, address = ? WHERE userID = ? and userType = 1";
                    pStmt = conn.prepareStatement(sql);
                    pStmt.setString(1, user.mPassword);
                    pStmt.setDouble(2, user.mUserType);
                    pStmt.setString(3, user.mName);
                    pStmt.setString(4, user.mPhone);
                    pStmt.setString(5, user.mAddress);
                    pStmt.setInt(6, user.mUserID);
                    errorCode = ResponseModel.CUSTOMER_EDIT_OK;
                    pStmt.executeUpdate();
                    pStmt.close();
                return true;
            } else if (user == null) {
                // INSERT USER
                System.out.println("----------");
                System.out.println("INSERT USER");
                System.out.println(user);
                System.out.println("----------");
                String sql = "INSERT INTO Users VALUES (?,?,?,?,?,?)";
                PreparedStatement pStmt = conn.prepareStatement(sql);
                pStmt.setInt(1, userIn.mUserID);
                pStmt.setString(2, "0");
                pStmt.setDouble(3, userIn.mUserType);
                pStmt.setString(4, userIn.mName);
                pStmt.setString(5, userIn.mPhone);
                pStmt.setString(6, userIn.mAddress);
                errorCode = ResponseModel.CUSTOMER_SAVE_OK;
                pStmt.executeUpdate();
                pStmt.close();
                return true;
            } else {
                errorCode = ResponseModel.CUSTOMER_SAVE_FAILED;
                return false;
            }

        } catch (Exception e) {
            errorCode = ResponseModel.CUSTOMER_SAVE_FAILED;
            return false;
        }
    }
    public UserModel loadUser(int userID) {
        try {
            UserModel user = new UserModel();
            String sql = "SELECT userID, password, userType, name, phone, address FROM Users WHERE userID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, userID);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()){
                user.mUserID = rs.getInt("userID");
                user.mPassword = rs.getString("password");
                user.mName = rs.getString("name");
                user.mAddress = rs.getString("address");
                user.mPhone = rs.getString("phone");
                user.mUserType = rs.getInt("userType");
                errorCode = ResponseModel.USER_LOAD_OK;
                pStmt.close();
            } else{
                errorCode = ResponseModel.USER_LOAD_ID_NOT_FOUND;
                pStmt.close();
                return null;
            }
            return user;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = ResponseModel.USER_LOAD_FAILED;
            return null;
        }
    }
    public PurchaseModel[] loadUserPurchaseHistory(int userID) {
        try {
            String sql = "SELECT purchaseID, userID, Purchases.productID, Products.productName, price, quantity, cost, tax, totalCost, date FROM Purchases LEFT JOIN Products ON Purchases.productID = Products.productID WHERE userID = ? ORDER BY date";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, userID);
            ResultSet rs = pStmt.executeQuery();
            ArrayList<PurchaseModel> pmArray = new ArrayList<PurchaseModel>();
            while(rs.next()){
                PurchaseModel purchase = new PurchaseModel();
                purchase.mPurchaseID = rs.getInt("purchaseID");
                purchase.mCustomerID = rs.getInt("userID");
                purchase.mProductName = rs.getString("productName");
                purchase.mProductID = rs.getInt("productID");
                purchase.mPrice = rs.getDouble("price");
                purchase.mQuantity = rs.getDouble("quantity");
                purchase.mCost = rs.getDouble("cost");
                purchase.mTax = rs.getDouble("tax");
                purchase.mTotalCost = rs.getDouble("totalCost");
                purchase.mDate = rs.getLong("date");
                pmArray.add(purchase);
            }
            PurchaseModel[] pm = new PurchaseModel[pmArray.size()];
            pmArray.toArray(pm);
            errorCode = ResponseModel.USER_HISTORY_LOAD_OK;
            return pm;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = ResponseModel.USER_HISTORY_LOAD_FAILED;
            return null;
        }
    }
    public boolean saveUser(UserModel userIn) {
        try {
            UserModel user = loadUser(userIn.mUserID);
            if (user == null)
                user = userIn;
            if(userIn.mName.length() != 0)
                user.mName = userIn.mName;
            if(userIn.mPassword.length() != 0)
                user.mPassword = userIn.mPassword;
            user.mPhone = userIn.mPhone;
            user.mAddress = userIn.mAddress;
            if(userIn.mUserType != -1)
                user.mUserType = userIn.mUserType;

            String sql = "SELECT 1 FROM Users WHERE userID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);

            pStmt.setInt(1, user.mUserID);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()) {
                // UPDATE USER
                System.out.println("----------");
                System.out.println("UPDATE USER");
                System.out.println(user);
                System.out.println("----------");
                sql = "UPDATE Users SET password = ?, userType = ?, name = ?, phone = ?, address = ? WHERE userID = ?";
                pStmt.close();
                pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, user.mPassword);
                pStmt.setDouble(2, user.mUserType);
                pStmt.setString(3, user.mName);
                pStmt.setString(4, user.mPhone);
                pStmt.setString(5, user.mAddress);
                pStmt.setInt(6, user.mUserID);
                errorCode = ResponseModel.USER_EDIT_OK;
            } else {
                // INSERT USER
                System.out.println("----------");
                System.out.println("INSERT USER");
                System.out.println(user);
                System.out.println("----------");
                sql = "INSERT INTO Users(userID, userType, name, phone, address) VALUES (?,?,?,?,?)";
                pStmt.close();
                pStmt = conn.prepareStatement(sql);
                pStmt.setInt(1, user.mUserID);
                pStmt.setDouble(2, user.mUserType);
                pStmt.setString(3, user.mName);
                pStmt.setString(4, user.mPhone);
                pStmt.setString(5, user.mAddress);
                errorCode = ResponseModel.USER_SAVE_OK;
            }
            pStmt.executeUpdate();
            pStmt.close();
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = ResponseModel.USER_SAVE_FAILED;
            return false;
        }
    }
    public PurchaseModel loadPurchase(int purchaseID) {
        try {
            PurchaseModel purchase = new PurchaseModel();
            String sql = "SELECT userID, productID, price, quantity, cost, tax, totalCost, date FROM Purchases WHERE purchaseID = ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, purchaseID);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()){
                purchase.mPurchaseID = purchaseID;
                purchase.mCustomerID = rs.getInt("userID");
                purchase.mProductID = rs.getInt("productID");
                purchase.mPrice = rs.getDouble("price");
                purchase.mQuantity = rs.getDouble("quantity");
                purchase.mCost = rs.getDouble("cost");
                purchase.mTax = rs.getDouble("tax");
                purchase.mTotalCost = rs.getDouble("totalCost");
                purchase.mDate = rs.getLong("date");
                errorCode = ResponseModel.PURCHASE_LOAD_OK;
                pStmt.close();
            } else{
                errorCode = ResponseModel.PURCHASE_LOAD_ID_NOT_FOUND;
                pStmt.close();
                return null;
            }
            return purchase;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = ResponseModel.PURCHASE_LOAD_FAILED;
            return null;
        }
    }
    public boolean savePurchase(PurchaseModel purchaseIn) {
        try {
            PurchaseModel purchase = loadPurchase(purchaseIn.mPurchaseID);
            if (purchase != null) {
                if (purchaseIn.mPurchaseID != -1)
                    purchase.mPurchaseID = purchaseIn.mPurchaseID;
                if (purchaseIn.mCustomerID != -1)
                    purchase.mCustomerID = purchaseIn.mCustomerID;
                if (purchaseIn.mPrice != -1)
                    purchase.mPrice = purchaseIn.mPrice;
                if (purchaseIn.mQuantity != -1)
                    purchase.mQuantity = purchaseIn.mQuantity;
            }

            if (purchase != null) {
                String sql = "UPDATE Purchases SET userID = ?, productID = ?, price = ?, quantity = ?, cost = ?, tax = ?, totalCost = ?, date = ? WHERE purchaseID = ?";
                PreparedStatement pStmt = conn.prepareStatement(sql);
                purchase.mCost = purchase.mPrice * purchase.mQuantity;
                purchase.mTax = purchase.mCost * taxRate;
                purchase.mTotalCost = purchase.mCost + purchase.mTax;
                purchase.mDate = Calendar.getInstance().getTimeInMillis() / 1000;
                pStmt.setInt(1, purchase.mCustomerID);
                pStmt.setInt(2, purchase.mProductID);
                pStmt.setDouble(3, purchase.mPrice);
                pStmt.setDouble(4, purchase.mQuantity);
                pStmt.setDouble(5, purchase.mCost);
                pStmt.setDouble(6, purchase.mTax);
                pStmt.setDouble(7, purchase.mTotalCost);
                pStmt.setLong(8, purchase.mDate);
                pStmt.setInt(9, purchase.mPurchaseID);
                pStmt.executeUpdate();
                pStmt.close();
                // UPDATE USER
                System.out.println("----------");
                System.out.println("UPDATE PURCHASE");
                System.out.println(purchase);
                System.out.println("----------");
                errorCode = ResponseModel.PURCHASE_EDIT_OK;
            } else {
                String sql = "";
                purchase = purchaseIn;
                if(purchase.mPurchaseID != -1)
                    sql = "INSERT INTO Purchases(purchaseID, userID, productID, price, quantity, cost, tax, totalCost, date) VALUES (?,?,?,?,?,?,?,?,?);";
                else
                    sql = "INSERT INTO Purchases(userID, productID, price, quantity, cost, tax, totalCost, date) VALUES (?,?,?,?,?,?,?,?);";

                PreparedStatement pStmt = conn.prepareStatement(sql);
                ProductModel product = loadProduct(purchaseIn.mProductID);
                if (product != null) {
                    if(purchase.mPrice == -1)
                        purchase.mPrice = product.mPrice;
                    purchase.mCost = purchase.mPrice * purchase.mQuantity;
                    purchase.mTax = purchase.mCost * taxRate;
                    purchase.mTotalCost = purchase.mCost + purchase.mTax;
                    purchase.mDate = Calendar.getInstance().getTimeInMillis() / 1000;
                    int counter = 1;
                    if(purchase.mPurchaseID != -1)
                        pStmt.setInt(counter++, purchase.mPurchaseID);
                    pStmt.setInt(counter++, purchase.mCustomerID);
                    pStmt.setInt(counter++, purchase.mProductID);
                    pStmt.setDouble(counter++, purchase.mPrice);
                    pStmt.setDouble(counter++, purchase.mQuantity);
                    pStmt.setDouble(counter++, purchase.mCost);
                    pStmt.setDouble(counter++, purchase.mTax);
                    pStmt.setDouble(counter++, purchase.mTotalCost);
                    pStmt.setLong(counter++, purchase.mDate);
                    pStmt.executeUpdate();
                    pStmt.close();
                    // INSERT PURCHASE
                    System.out.println("----------");
                    System.out.println("Add Purchase");
                    System.out.println(purchase);
                    System.out.println("----------");
                    errorCode = ResponseModel.PURCHASE_SAVE_OK;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e);
            errorCode = ResponseModel.USER_SAVE_FAILED;
            return false;
        }
    }
    public ProductModel[] loadAllProduct() {
        try {
            String sql = "SELECT productID, productName, unitPrice, stockQuantity FROM Products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
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
    public boolean saveProduct(ProductModel productIn) {
        try {
            ProductModel product = loadProduct(productIn.mProductID);
            if(product != null) {
                // UPDATE PRODUCT
                String sql = "UPDATE Products SET productName = ?, unitPrice = ?, stockQuantity = ? WHERE productID = ?";
                PreparedStatement pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, productIn.mName);
                pStmt.setDouble(2, productIn.mPrice);
                pStmt.setDouble(3, productIn.mQuantity);
                pStmt.setInt(4, productIn.mProductID);
                pStmt.executeUpdate();
                pStmt.close();
                errorCode = ResponseModel.PRODUCT_EDIT_OK;
            } else {
                // INSERT PRODUCT
                String sql = "INSERT INTO Products (productID, productName, unitPrice, stockQuantity) VALUES (?,?,?,?)";
                PreparedStatement pStmt = conn.prepareStatement(sql);
                pStmt.setInt(1, productIn.mProductID);
                pStmt.setString(2, productIn.mName);
                pStmt.setDouble(3, productIn.mPrice);
                pStmt.setDouble(4, productIn.mQuantity);
                pStmt.executeUpdate();
                pStmt.close();
                errorCode = ResponseModel.PRODUCT_SAVE_OK;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = ResponseModel.PRODUCT_SAVE_FAILED;
            return false;
        }
    }

    public PurchaseModel[] searchPurchaseByTimePeriod(long startTime, long endTime) {
        try {
            String sql = "SELECT purchaseID, userID, Purchases.productID, Products.productName, price, quantity, cost, tax, totalCost, date FROM Purchases LEFT JOIN Products ON Purchases.productID = Products.productID WHERE date >= ? and date <= ? ORDER BY date";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, startTime);
            stmt.setLong(2, endTime);
            ResultSet rs = stmt.executeQuery();
            ArrayList<PurchaseModel> pmArray = new ArrayList<PurchaseModel>();
            while(rs.next()){
                PurchaseModel purchase = new PurchaseModel();
                purchase.mPurchaseID = rs.getInt("purchaseID");
                purchase.mCustomerID = rs.getInt("userID");
                purchase.mProductName = rs.getString("productName");
                purchase.mProductID = rs.getInt("productID");
                purchase.mPrice = rs.getDouble("price");
                purchase.mQuantity = rs.getDouble("quantity");
                purchase.mCost = rs.getDouble("cost");
                purchase.mTax = rs.getDouble("tax");
                purchase.mTotalCost = rs.getDouble("totalCost");
                purchase.mDate = rs.getLong("date");
                pmArray.add(purchase);
            }
            PurchaseModel[] pm = new PurchaseModel[pmArray.size()];
            pmArray.toArray(pm);
            errorCode = ResponseModel.PURCHASE_SEARCH_OK;
            return pm;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = ResponseModel.PURCHASE_SEARCH_FAILED;
            return null;
        }
    }
    public ProductModel[] searchProductByNameAndPrice(String productName, double minPrice, double maxPrice) {
        try {
            String sql = "SELECT productID, productName, unitPrice, stockQuantity FROM Products WHERE productName LIKE ? and unitPrice >= ? and unitPrice <= ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, '%' + productName + '%');
            stmt.setDouble(2, minPrice);
            stmt.setDouble(3, maxPrice);
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
            errorCode = ResponseModel.PRODUCT_SEARCH_OK;
            return pm;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorCode = ResponseModel.PRODUCT_SEARCH_FAILED;
            return null;
        }
    }
    public int getErrorCode(){
        return errorCode;
    }
}
