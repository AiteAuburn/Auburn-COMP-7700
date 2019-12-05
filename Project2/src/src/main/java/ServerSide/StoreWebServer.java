package ServerSide;

import Model.ProductModel;
import Model.PurchaseModel;
import Model.ResponseModel;
import Model.UserModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class StoreWebServer {
    private static final String DBPath = "C:\\Users\\Ai-Te\\store.db";
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(StoreWebServer::handleRequest);

        context = server.createContext("/product");
        context.setHandler(StoreWebServer::handleProductRequest);
        context = server.createContext("/product/all");
        context.setHandler(StoreWebServer::handleProductAllRequest);
        context = server.createContext("/product/search");
        context.setHandler(StoreWebServer::handleProductSearchRequest);
        context = server.createContext("/product/change");
        context.setHandler(StoreWebServer::handleProductChangeRequest);
        context = server.createContext("/customer");
        context.setHandler(StoreWebServer::handleCustomerRequest);
        context = server.createContext("/customer/change");
        context.setHandler(StoreWebServer::handleCustomerChangeRequest);
        context = server.createContext("/user");
        context.setHandler(StoreWebServer::handleUserRequest);
        context = server.createContext("/user/login");
        context.setHandler(StoreWebServer::handleUserLoginRequest);
        context = server.createContext("/user/history");
        context.setHandler(StoreWebServer::handleUserHistoryRequest);
        context = server.createContext("/user/change");
        context.setHandler(StoreWebServer::handleUserChangeRequest);
        context = server.createContext("/purchase");
        context.setHandler(StoreWebServer::handlePurchaseRequest);
        context = server.createContext("/purchase/change");
        context.setHandler(StoreWebServer::handlePurchaseChangeRequest);
        context = server.createContext("/purchase/search");
        context.setHandler(StoreWebServer::handlePurchaseSearchRequest);

        System.out.println("WebServer is listening at port " + 8888);
        server.start();
    }
    private static HashMap<String, String> getParamsFromQuery(String queryString){
        String[] par = queryString.split("&");
        HashMap<String, String> params = new HashMap<String, String>();
        for (String st : par) {
            int p = st.indexOf("=");
            String key = st.substring(0, p);
            String value = st.substring(p + 1);
            params.put(key, value);
        }
        return params;
    }
    private static void handleRequest(HttpExchange exchange) throws IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("status","online");
        String response = new Gson().toJson(obj);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }

    private static void handleProductRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);
        ProductModel product = new ProductModel();
        JsonObject obj = new JsonObject();
        boolean result = false;
        try {
            int id = Integer.parseInt(params.get("id"));
            product = adapter.loadProduct(id);
            if(product != null){
                result = true;
                obj.addProperty("product", new Gson().toJson(product));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        obj.addProperty("result", result);
        String response = new Gson().toJson(obj);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }
    private static void handleProductAllRequest(HttpExchange exchange) throws IOException {
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);

        JsonObject obj = new JsonObject();
        try {

            ProductModel[] productArray = adapter.loadAllProduct();
            if(productArray != null) {
                obj.addProperty("result",true);
                obj.addProperty("products",new Gson().toJson(productArray));
            }
            else
                obj.addProperty("result",false);
        } catch (Exception e) {
            obj.addProperty("result",false);
        }

        obj.addProperty("errorCode",adapter.getErrorCode());
        obj.addProperty("errorMessage", ResponseModel.getErrorMessage(adapter.getErrorCode()));
        String response = new Gson().toJson(obj);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }
    private static void handleProductSearchRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);
        JsonObject obj = new JsonObject();
        try {
            String productName = params.get("productName");
            double minPrice = Double.parseDouble(params.get("minPrice"));
            double maxPrice = Double.parseDouble(params.get("maxPrice"));
            ProductModel[] productArray = adapter.searchProductByNameAndPrice(productName, minPrice, maxPrice);
            if(productArray != null) {
                obj.addProperty("result",true);
                obj.addProperty("products",new Gson().toJson(productArray));
            }
            else
                obj.addProperty("result",false);
        } catch (Exception e) {
            obj.addProperty("result",false);
        }

        obj.addProperty("errorCode",adapter.getErrorCode());
        obj.addProperty("errorMessage", ResponseModel.getErrorMessage(adapter.getErrorCode()));
        String response = new Gson().toJson(obj);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }
    private static void handleProductChangeRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);
        JsonObject obj = new JsonObject();
        try {
            int id = Integer.parseInt(params.get("id"));
            ProductModel product = adapter.loadProduct(id);
            if(product == null)
                product = new ProductModel();
            product.mProductID = id;
            if(params.containsKey("price"))
                product.mPrice = Double.parseDouble(params.get("price"));
            if(params.containsKey("quantity"))
                product.mQuantity = Double.parseDouble(params.get("quantity"));
            if(params.containsKey("name"))
                product.mName = params.get("name");
            boolean result = adapter.saveProduct(product);
            if(result) {
                obj.addProperty("result",true);
            }
            else
                obj.addProperty("result",false);
        } catch (Exception e) {
            obj.addProperty("result",false);
            obj.addProperty("errorMessage", e.toString());
        }

        String response = new Gson().toJson(obj);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }

    private static void handleUserRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);

        JsonObject obj = new JsonObject();
        UserModel user = new UserModel();
        boolean result = false;
        try {
            if(params.containsKey("userID"))
                user.mUserID = Integer.parseInt(params.get("userID"));

            user = adapter.loadUser(user.mUserID);
            if(user != null) {
                result = true;
                obj.addProperty("user", new Gson().toJson(user));
            }
        } catch (Exception e) {
        }
        obj.addProperty("result", result);
        obj.addProperty("errorCode",adapter.getErrorCode());
        obj.addProperty("errorMessage", ResponseModel.getErrorMessage(adapter.getErrorCode()));
        String response = new Gson().toJson(obj);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }
    private static void handleUserLoginRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);

        JsonObject obj = new JsonObject();
        UserModel user = new UserModel();
        boolean result = false;
        try {
            if(params.containsKey("fullName"))
                user.mName = params.get("fullName");
            if(params.containsKey("password"))
                user.mPassword = params.get("password");
            user = adapter.loginUser(user.mName, user.mPassword);
            if(user != null) {
                result = true;
                obj.addProperty("user", new Gson().toJson(user));
            }
        } catch (Exception e) {
            obj.addProperty("result",false);
        }
        obj.addProperty("result", result);
        obj.addProperty("errorCode",adapter.getErrorCode());
        obj.addProperty("errorMessage", ResponseModel.getErrorMessage(adapter.getErrorCode()));
        String response = new Gson().toJson(obj);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }
    private static void handleUserHistoryRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);
        JsonObject obj = new JsonObject();
        try {
            int userID = Integer.parseInt(params.get("userID"));
            PurchaseModel[] purchaseArray = adapter.loadUserPurchaseHistory(userID);
            if(purchaseArray != null) {
                obj.addProperty("result",true);
                obj.addProperty("purchases",new Gson().toJson(purchaseArray));
            }
            else
                obj.addProperty("result",false);
        } catch (Exception e) {
            obj.addProperty("result",false);
        }

        obj.addProperty("errorCode",adapter.getErrorCode());
        obj.addProperty("errorMessage", ResponseModel.getErrorMessage(adapter.getErrorCode()));
        String response = new Gson().toJson(obj);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }
    private static void handleCustomerRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);

        JsonObject obj = new JsonObject();
        UserModel user = new UserModel();
        boolean result = false;
        try {
            if(params.containsKey("customerID"))
                user.mUserID = Integer.parseInt(params.get("customerID"));

            user = adapter.loadCustomer(user.mUserID);
            if(user != null) {
                result = true;
                obj.addProperty("customer", new Gson().toJson(user));
            }
        } catch (Exception e) {
        }
        obj.addProperty("result", result);
        obj.addProperty("errorCode",adapter.getErrorCode());
        obj.addProperty("errorMessage", ResponseModel.getErrorMessage(adapter.getErrorCode()));
        String response = new Gson().toJson(obj);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }

    private static void handleCustomerChangeRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);
        JsonObject obj = new JsonObject();
        boolean result = false;
        try {
            UserModel user = new UserModel();
            if(params.containsKey("userID"))
                user.mUserID = Integer.parseInt(params.get("userID"));
            else
                obj.addProperty("result", false);
            if(params.containsKey("name"))
                user.mName = params.get("name");
            if(params.containsKey("phone"))
                user.mPhone = params.get("phone");
            if(params.containsKey("address"))
                user.mAddress = params.get("address");

            result = adapter.saveCustomer(user);
        } catch (Exception e) {
        }

        obj.addProperty("result", result);
        obj.addProperty("errorCode",adapter.getErrorCode());
        obj.addProperty("errorMessage", ResponseModel.getErrorMessage(adapter.getErrorCode()));
        String response = new Gson().toJson(obj);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }
    private static void handleUserChangeRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);
        JsonObject obj = new JsonObject();
        boolean result = false;
        try {
            UserModel user = new UserModel();
            if(params.containsKey("userID"))
                user.mUserID = Integer.parseInt(params.get("userID"));
            else
                obj.addProperty("result", false);
            if(params.containsKey("name"))
                user.mName = params.get("name");
            if(params.containsKey("userType"))
                user.mUserType = Integer.parseInt(params.get("userType"));
            if(params.containsKey("phone"))
                user.mPhone = params.get("phone");
            if(params.containsKey("address"))
                user.mAddress = params.get("address");
            if(params.containsKey("password"))
                user.mPassword = params.get("password");
            result = adapter.saveUser(user);
        } catch (Exception e) {
        }

        obj.addProperty("result", result);
        obj.addProperty("errorCode",adapter.getErrorCode());
        obj.addProperty("errorMessage", ResponseModel.getErrorMessage(adapter.getErrorCode()));
        String response = new Gson().toJson(obj);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }
    private static void handlePurchaseRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);

        JsonObject obj = new JsonObject();
        PurchaseModel purchase = new PurchaseModel();
        boolean result = false;
        try {
            if(params.containsKey("purchaseID"))
                purchase.mPurchaseID = Integer.parseInt(params.get("purchaseID"));

            purchase = adapter.loadPurchase(purchase.mPurchaseID);
            if(purchase != null) {
                result = true;
                obj.addProperty("purchase", new Gson().toJson(purchase));
            }
        } catch (Exception e) {
        }
        obj.addProperty("result", result);
        obj.addProperty("errorCode",adapter.getErrorCode());
        obj.addProperty("errorMessage", ResponseModel.getErrorMessage(adapter.getErrorCode()));
        String response = new Gson().toJson(obj);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }

    private static void handlePurchaseChangeRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);
        JsonObject obj = new JsonObject();
        boolean result = false;
        try {
            PurchaseModel purchase = new PurchaseModel();
            boolean checkPassed = true;
            if(params.containsKey("purchaseID"))
                purchase.mPurchaseID = Integer.parseInt(params.get("purchaseID"));

            if(params.containsKey("customerID"))
                purchase.mCustomerID = Integer.parseInt(params.get("customerID"));
            else
                checkPassed = false;

            if(params.containsKey("productID"))
                purchase.mProductID = Integer.parseInt(params.get("productID"));
            else
                checkPassed = false;

            if(params.containsKey("price"))
                purchase.mPrice = Double.parseDouble(params.get("price"));
            else
                checkPassed = false;

            if(params.containsKey("quantity"))
                purchase.mQuantity = Double.parseDouble(params.get("quantity"));
            else
                checkPassed = false;
            System.out.println("purchase"+purchase);
            if(checkPassed)
                result = adapter.savePurchase(purchase);
        } catch (Exception e) {
        }

        obj.addProperty("result", result);
        obj.addProperty("errorCode", adapter.getErrorCode());
        obj.addProperty("errorMessage", ResponseModel.getErrorMessage(adapter.getErrorCode()));
        String response = new Gson().toJson(obj);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }
    private static void handlePurchaseSearchRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);
        JsonObject obj = new JsonObject();
        try {
            long startTime = Long.parseLong(params.get("startTime"));
            long endTime = Long.parseLong(params.get("endTime"));
            PurchaseModel[] purchaseArray = adapter.searchPurchaseByTimePeriod(startTime, endTime);
            if(purchaseArray != null) {
                obj.addProperty("result",true);
                obj.addProperty("purchases",new Gson().toJson(purchaseArray));
            }
            else {
                obj.addProperty("result", false);
            }
        } catch (Exception e) {
            System.out.println(e);
            obj.addProperty("result",false);
        }

        obj.addProperty("errorCode", adapter.getErrorCode());
        obj.addProperty("errorMessage", ResponseModel.getErrorMessage(adapter.getErrorCode()));
        String response = new Gson().toJson(obj);
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        os.write(response.getBytes());
        os.close();
    }
}

