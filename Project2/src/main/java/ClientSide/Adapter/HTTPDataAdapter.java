package ClientSide.Adapter;

import Model.UserModel;
import Model.ProductModel;
import Model.PurchaseModel;
import Model.ResponseModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sun.deploy.net.URLEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;


public class HTTPDataAdapter implements IDataAccess{
    Connection conn = null;
    public int errorCode = 0;
    private String requestURL = "";
    public boolean connect(String url) {
        requestURL = url;
        try {
            ResponseModel rs = (sendGet(requestURL));
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            System.out.println(result);
            String status = result.get("status").getAsString();
            boolean serverSideStatus = status.equalsIgnoreCase("online");
            return serverSideStatus;
        }catch(Exception e){}
        return false;
    }
    public boolean disconnect(){
        return true;
    }
    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replace("+", "%20");
        } catch (Exception e){
            return "";
        }
    }
    private ResponseModel sendGet(String url) throws Exception {

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();
        // optional default is GET
        httpClient.setRequestMethod("GET");
        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = httpClient.getResponseCode();

        ResponseModel rs = new ResponseModel();
        rs.setStatusCode(responseCode);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            rs.setResponse(response.toString());
        }
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        System.out.println(rs.getResponse());
        return rs;
    }
    public UserModel loginUser(String fullName, String password) {
        try {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("fullName", fullName);
            requestParams.put("password", password);
            String encodedURL = requestParams.keySet().stream()
                    .map(key -> key + "=" + encodeValue(requestParams.get(key)))
                    .collect(joining("&", requestURL + "user/login/?", ""));
            ResponseModel rs = sendGet(encodedURL);
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            if(success) {
                UserModel user = new UserModel();
                result = new Gson().fromJson(result.get("user").getAsString(), JsonObject.class);
                user.mName = result.get("mName").getAsString();
                user.mUserID = result.get("mUserID").getAsInt();
                user.mAddress = result.get("mAddress").getAsString();
                user.mPassword = result.get("mPassword").getAsString();
                user.mPhone = result.get("mPhone").getAsString();
                user.mUserType = result.get("mUserType").getAsInt();
                errorCode = ResponseModel.USER_LOGIN_OK;
                return user;
            } else {
                errorCode = ResponseModel.USER_LOGIN_NOT_MATCH;
                return null;
            }
        }catch(Exception e){
            errorCode = ResponseModel.USER_LOGIN_FAILED;
            return null;
        }
    }
    public ProductModel loadProduct(int productID) {
        try {
            ResponseModel rs = (sendGet(requestURL + "product?id=" + productID));
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            ProductModel product = new ProductModel();
            product.mProductID = result.get("mProductID").getAsInt();
            product.mName = result.get("mName").getAsString();
            product.mPrice = result.get("mPrice").getAsDouble();
            product.mQuantity = result.get("mQuantity").getAsDouble();
            errorCode = ResponseModel.PRODUCT_LOAD_OK;
            return product;
        }catch(Exception e){
            errorCode = ResponseModel.PRODUCT_LOAD_FAILED;
            return null;
        }
    }
    public boolean saveProduct(ProductModel product){
        try {
            String apiURL = requestURL + "product/change?id=" + product.mProductID + "&name=" + product.mName + "&price=" + product.mPrice + "&quantity=" + product.mQuantity;
            ResponseModel rs = sendGet(apiURL);
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            if (success)
                errorCode = ResponseModel.PRODUCT_SAVE_OK;
            else
                errorCode = ResponseModel.PRODUCT_SAVE_FAILED;
            return success;
        } catch (Exception e) {
            errorCode = ResponseModel.PRODUCT_SAVE_FAILED;
            return false;
        }
    }

    public UserModel loadUser(int userID) {
        try {
            ResponseModel rs = (sendGet(requestURL + "user?userID=" + userID));
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            errorCode = result.get("errorCode").getAsInt();
            if(success) {
                UserModel user = new UserModel();
                result = new Gson().fromJson(result.get("user").getAsString(), JsonObject.class);
                user.mName = result.get("mName").getAsString();
                user.mUserID = result.get("mUserID").getAsInt();
                user.mAddress = result.get("mAddress").getAsString();
                user.mPassword = result.get("mPassword").getAsString();
                user.mPhone = result.get("mPhone").getAsString();
                user.mUserType = result.get("mUserType").getAsInt();
                return user;
            } else {
                return null;
            }
        }catch(Exception e){
            errorCode = ResponseModel.USER_LOAD_FAILED;
            return null;
        }
    }
    public PurchaseModel[] loadUserPurchaseHistory(int userID) {
        try {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("userID", Integer.toString(userID));
            String encodedURL = requestParams.keySet().stream()
                    .map(key -> key + "=" + encodeValue(requestParams.get(key)))
                    .collect(joining("&", requestURL + "user/history/?", ""));
            ResponseModel rs = sendGet(encodedURL);
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            errorCode = result.get("errorCode").getAsInt();
            if(success) {
                JsonArray productArray = new Gson().fromJson(result.get("purchases").getAsString(), JsonArray.class);
                ArrayList<PurchaseModel> pmArray = new ArrayList<PurchaseModel>();
                for (int i = 0; i < productArray.size(); i++) {
                    JsonObject jt = productArray.get(i).getAsJsonObject();
                    PurchaseModel temp = new PurchaseModel();
                    temp.mPurchaseID = jt.get("mPurchaseID").getAsInt();
                    temp.mProductID = jt.get("mProductID").getAsInt();
                    temp.mProductName = jt.get("mProductName").getAsString();
                    temp.mCustomerID = jt.get("mCustomerID").getAsInt();
                    temp.mPrice = jt.get("mPrice").getAsDouble();
                    temp.mQuantity = jt.get("mQuantity").getAsDouble();
                    temp.mCost = jt.get("mCost").getAsDouble();
                    temp.mTax = jt.get("mTax").getAsDouble();
                    temp.mTotalCost = jt.get("mTotalCost").getAsDouble();
                    temp.mDate = jt.get("mDate").getAsLong();;
                    pmArray.add(temp);
                }
                PurchaseModel[] pm = new PurchaseModel[pmArray.size()];
                pmArray.toArray(pm);
                return pm;
            } else {
                return null;
            }
        }catch(Exception e){
            errorCode = ResponseModel.PURCHASE_LOAD_FAILED;
            return null;
        }
    }

    public boolean saveUser(UserModel user){
        try {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("userID", Integer.toString(user.mUserID));
            if(user.mName.length() != 0)
                requestParams.put("name", user.mName);
            if(user.mPassword.length() != 0)
                requestParams.put("password", user.mPassword);
            requestParams.put("phone", user.mPhone);
            requestParams.put("address", user.mAddress);
            String encodedURL = requestParams.keySet().stream()
                    .map(key -> key + "=" + encodeValue(requestParams.get(key)))
                    .collect(joining("&", requestURL + "user/change?", ""));
            ResponseModel rs = sendGet(encodedURL);
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            errorCode = result.get("errorCode").getAsInt();
            return success;
        } catch (Exception e) {
            errorCode = ResponseModel.USER_SAVE_FAILED;
            return false;
        }
    }
    public UserModel loadCustomer(int customerID) {
        try {
            ResponseModel rs = (sendGet(requestURL + "customer?customerID=" + customerID));
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            errorCode = result.get("errorCode").getAsInt();
            if(success) {
                UserModel user = new UserModel();
                result = new Gson().fromJson(result.get("customer").getAsString(), JsonObject.class);
                user.mName = result.get("mName").getAsString();
                user.mUserID = result.get("mUserID").getAsInt();
                user.mAddress = result.get("mAddress").getAsString();
                user.mPassword = result.get("mPassword").getAsString();
                user.mPhone = result.get("mPhone").getAsString();
                user.mUserType = result.get("mUserType").getAsInt();
                return user;
            } else {
                return null;
            }
        }catch(Exception e){
            errorCode = ResponseModel.CUSTOMER_LOAD_FAILED;
            return null;
        }
    }
    public ProductModel[] loadProductAll() {
        try {
            ResponseModel rs = (sendGet(requestURL + "product/all"));
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            errorCode = result.get("errorCode").getAsInt();
            if(success) {
                JsonArray productArray = new Gson().fromJson(result.get("products").getAsString(), JsonArray.class);
                ArrayList<ProductModel> pmArray = new ArrayList<ProductModel>();
                for (int i = 0; i < productArray.size(); i++) {
                    JsonObject jt = productArray.get(i).getAsJsonObject();
                    ProductModel temp = new ProductModel();
                    temp.mProductID = jt.get("mProductID").getAsInt();
                    temp.mName = jt.get("mName").getAsString();
                    temp.mPrice = jt.get("mPrice").getAsDouble();
                    temp.mQuantity = jt.get("mQuantity").getAsDouble();
                    pmArray.add(temp);
                }
                ProductModel[] pm = new ProductModel[pmArray.size()];
                pmArray.toArray(pm);
                return pm;
            } else {
                return null;
            }
        }catch(Exception e){
            errorCode = ResponseModel.PURCHASE_LOAD_FAILED;
            return null;
        }
    }
    public ProductModel[] searchProductByNameAndPrice(String productName, double minPrice, double maxPrice) {
        try {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("productName", productName);
            requestParams.put("minPrice", Double.toString(minPrice));
            requestParams.put("maxPrice", Double.toString(maxPrice));
            String encodedURL = requestParams.keySet().stream()
                    .map(key -> key + "=" + encodeValue(requestParams.get(key)))
                    .collect(joining("&", requestURL + "product/search?", ""));
            ResponseModel rs = sendGet(encodedURL);
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            errorCode = result.get("errorCode").getAsInt();
            if(success) {
                JsonArray productArray = new Gson().fromJson(result.get("products").getAsString(), JsonArray.class);
                ArrayList<ProductModel> pmArray = new ArrayList<ProductModel>();
                for (int i = 0; i < productArray.size(); i++) {
                    JsonObject jt = productArray.get(i).getAsJsonObject();
                    ProductModel temp = new ProductModel();
                    temp.mProductID = jt.get("mProductID").getAsInt();
                    temp.mName = jt.get("mName").getAsString();
                    temp.mPrice = jt.get("mPrice").getAsDouble();
                    temp.mQuantity = jt.get("mQuantity").getAsDouble();
                    pmArray.add(temp);
                }
                ProductModel[] pm = new ProductModel[pmArray.size()];
                pmArray.toArray(pm);
                errorCode = ResponseModel.CUSTOMER_LOAD_OK;
                return pm;
            } else {
                errorCode = ResponseModel.CUSTOMER_LOAD_FAILED;
                return null;
            }
        }catch(Exception e){
            errorCode = ResponseModel.CUSTOMER_LOAD_FAILED;
            return null;
        }
    }

    public PurchaseModel[] searchPurchaseByTimePeriod(long startTime, long endTime) {
        try {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("startTime" , Long.toString(startTime));
            requestParams.put("endTime", Long.toString(endTime));
            String encodedURL = requestParams.keySet().stream()
                    .map(key -> key + "=" + encodeValue(requestParams.get(key)))
                    .collect(joining("&", requestURL + "purchase/search?", ""));
            ResponseModel rs = sendGet(encodedURL);
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            errorCode = result.get("errorCode").getAsInt();
            if(success) {
                JsonArray purchaseArray = new Gson().fromJson(result.get("purchases").getAsString(), JsonArray.class);
                ArrayList<PurchaseModel> pmArray = new ArrayList<PurchaseModel>();
                for (int i = 0; i < purchaseArray.size(); i++) {
                    JsonObject jt = purchaseArray.get(i).getAsJsonObject();
                    PurchaseModel purchase = new PurchaseModel();
                    purchase.mPurchaseID = jt.get("mPurchaseID").getAsInt();
                    purchase.mCustomerID = jt.get("mCustomerID").getAsInt();
                    purchase.mProductID = jt.get("mProductID").getAsInt();
                    purchase.mProductName = jt.get("mProductName").getAsString();
                    purchase.mPrice = jt.get("mPrice").getAsDouble();
                    purchase.mQuantity = jt.get("mQuantity").getAsDouble();
                    purchase.mCost = jt.get("mCost").getAsDouble();
                    purchase.mTax = jt.get("mTax").getAsDouble();
                    purchase.mTotalCost = jt.get("mTotalCost").getAsDouble();
                    purchase.mDate = jt.get("mDate").getAsLong();
                    pmArray.add(purchase);
                }
                PurchaseModel[] pm = new PurchaseModel[pmArray.size()];
                pmArray.toArray(pm);
                errorCode = ResponseModel.PURCHASE_SEARCH_OK;
                return pm;
            } else {
                errorCode = ResponseModel.PURCHASE_SEARCH_FAILED;
                return null;
            }
        }catch(Exception e){
            errorCode = ResponseModel.PURCHASE_SEARCH_FAILED;
            return null;
        }
    }

    public boolean saveCustomer(UserModel customer){
        try {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("userID", Integer.toString(customer.mUserID));
            if(customer.mName.length() != 0)
                requestParams.put("name", customer.mName);
            if(customer.mPhone.length() != 0)
            requestParams.put("phone", customer.mPhone);
            if(customer.mAddress.length() != 0)
            requestParams.put("address", customer.mAddress);
            String encodedURL = requestParams.keySet().stream()
                    .map(key -> key + "=" + encodeValue(requestParams.get(key)))
                    .collect(joining("&", requestURL + "customer/change?", ""));
            ResponseModel rs = sendGet(encodedURL);
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            errorCode = result.get("errorCode").getAsInt();
            return success;
        } catch (Exception e) {
            errorCode = ResponseModel.USER_SAVE_FAILED;
            return false;
        }
    }

    public PurchaseModel loadPurchase(int purchaseID) {

        try {
            ResponseModel rs = (sendGet(requestURL + "purchase?purchaseID=" + purchaseID));
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            errorCode = result.get("errorCode").getAsInt();
            if(success) {
                PurchaseModel purchase = new PurchaseModel();
                result = new Gson().fromJson(result.get("purchase").getAsString(), JsonObject.class);
                purchase.mPurchaseID = result.get("mPurchaseID").getAsInt();
                purchase.mQuantity = result.get("mQuantity").getAsDouble();
                purchase.mDate = result.get("mDate").getAsLong();
                purchase.mTotalCost = result.get("mTotalCost").getAsDouble();
                purchase.mCost = result.get("mCost").getAsDouble();
                purchase.mPrice = result.get("mPrice").getAsDouble();
                purchase.mProductID = result.get("mProductID").getAsInt();
                purchase.mCustomerID = result.get("mCustomerID").getAsInt();
                purchase.mTax = result.get("mTax").getAsDouble();
                return purchase;
            } else {
                return null;
            }
        }catch(Exception e){
            errorCode = ResponseModel.PURCHASE_LOAD_FAILED;
            return null;
        }
    }
    public boolean savePurchase(PurchaseModel purchase){
        try {
            Map<String, String> requestParams = new HashMap<>();
            if(purchase.mPurchaseID != -1)
                requestParams.put("purchaseID", Integer.toString(purchase.mPurchaseID));
            if(purchase.mCustomerID != -1)
                requestParams.put("customerID", Integer.toString(purchase.mCustomerID));
            requestParams.put("productID", Integer.toString(purchase.mProductID));
            requestParams.put("price", Double.toString(purchase.mPrice));
            requestParams.put("quantity", Double.toString(purchase.mQuantity));
            if(purchase.mDate != 0)
                requestParams.put("date", Long.toString(purchase.mDate) );
            String encodedURL = requestParams.keySet().stream()
                    .map(key -> key + "=" + encodeValue(requestParams.get(key)))
                    .collect(joining("&", requestURL + "purchase/change?", ""));
            ResponseModel rs = sendGet(encodedURL);
            JsonObject result = new Gson().fromJson(rs.getResponse(), JsonObject.class);
            boolean success = result.get("result").getAsBoolean();
            errorCode = result.get("errorCode").getAsInt();
            return success;
        } catch (Exception e) {
            errorCode = ResponseModel.PURCHASE_SAVE_FAILED;
            return false;
        }
    }
    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        String output = "ErrorCode: " + errorCode + "\nMsg: " + ResponseModel.errMsg.get(errorCode);
        return output;
    }
}
