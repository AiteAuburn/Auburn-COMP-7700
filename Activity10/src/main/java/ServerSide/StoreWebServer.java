package ServerSide;

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
        context = server.createContext("/user/change");
        context.setHandler(StoreWebServer::handleUserChangeRequest);

        System.out.println("WebServer is listening at port " + 8888);
        server.start();
    }
    private String getHeader(){
        return "a";
    }
    private static HashMap<String, String> getParamsFromQuery(String queryString){
        String[] par = queryString.split("&");
        HashMap<String, String> params = new HashMap<String, String>();
        for (String st : par) {
            System.out.println(st);
            int p = st.indexOf("=");
            String key = st.substring(0, p);
            String value = st.substring(p + 1);
            System.out.println(key + " ---> " + value);
            params.put(key, value);
        }
        return params;
    }
    private static void handleRequest(HttpExchange exchange) throws IOException {
        System.out.println("Request in / context - Welcome!!!");
        String response = "<html><body><h1>Hi there!</body></html>";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleProductRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        StringBuilder response = new StringBuilder();
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);
        response.append("<html><body>");
        try {
            int id = Integer.parseInt(params.get("id"));
            ProductModel product = adapter.loadProduct(id);
            response.append(convertProductToString(product));
        } catch (Exception e) {
        }
        response.append("</body></html>");


        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
    private static void handleProductSearchRequest(HttpExchange exchange) throws IOException {

        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        StringBuilder response = new StringBuilder();
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect("");
        response.append("<html><body>");
        try{
            String productName = params.get("name");
            ProductModel[] productArray = adapter.searchProductByName(productName);
            response.append(convertProductToString(productArray));
        } catch (Exception e) {
        }
        response.append("</body></html>");


        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
    private static void handleProductChangeRequest(HttpExchange exchange) throws IOException {

        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        StringBuilder response = new StringBuilder();
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);
        response.append("<html><body>");
        try {
            int id = Integer.parseInt(params.get("id"));
            ProductModel product = adapter.loadProduct(id);
            double price,quantity;
            String name;
            if(params.containsKey("price"))
                product.mPrice = Double.parseDouble(params.get("price"));
            if(params.containsKey("quantity"))
                product.mQuantity = Double.parseDouble(params.get("quantity"));
            if(params.containsKey("name"))
                product.mName = params.get("name");
            boolean result = adapter.saveProduct(product);
            if(result)
                response.append(convertProductToString(product));
            else
                response.append("Update failed");
        } catch (Exception e) {
        }

        try{
            System.out.println("product");
            String productName = params.get("name");
            ProductModel[] productArray = adapter.searchProductByName(productName);
            response.append(convertProductToString(productArray));
        } catch (Exception e) {
        }
        response.append("</body></html>");


        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
    private static String convertProductToString (ProductModel product){
        StringBuilder response = new StringBuilder();
        if(product != null) {
            response.append("<h1>Product Information</h1>");
            response.append("<table border=\"1\">");
            response.append("<tbody>");
            response.append("<tr><td>ProductID</td><td>" + product.mProductID + "</td>");
            response.append("<tr><td>Product Name</td><td>" + product.mName + "</td>");
            response.append("<tr><td>Price</td><td>" + product.mPrice + "</td>");
            if (product.mQuantity == 0)
                response.append("<tr><td>Availability</td><td>Out of stock</td>");
            else
                response.append("<tr><td>Availability</td><td>On stock</td>");
            response.append("</tbody>");
            response.append("</table>");
        } else {
            response.append("<h1>Not Available</h1>");
        }
        return response.toString();
    }
    private static String convertProductToString (ProductModel[] productArray){
        StringBuilder response = new StringBuilder();
        if(productArray.length != 0) {
            response.append("<h1>Product Information</h1>");
            response.append("<table border=\"1\">");
            response.append("<thead>");
            response.append("<tr>");
            response.append("<td>ProductID</td>");
            response.append("<td>Product Name</td>");
            response.append("<td>Price</td>");
            response.append("<td>Availablity</td></tr>");
            response.append("</tr>");
            response.append("</thead>");
            response.append("<tbody>");

            for (ProductModel p : productArray) {
                response.append("<tr>");
                response.append("<td>" + p.mProductID + "</td>");
                response.append("<td>" + p.mName + "</td>");
                response.append("<td>" + p.mPrice + "</td>");
                if (p.mQuantity == 0)
                    response.append("<td>Out of stock</td>");
                else
                    response.append("<td>On stock</td>");
                response.append("</tr>");
            }
            response.append("</tbody>");
            response.append("</table>");
        } else {
            response.append("<h1>Not Available</h1>");
        }
        return response.toString();
    }
    private static void handleProductAllRequest(HttpExchange exchange) throws IOException {
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);

        StringBuilder response = new StringBuilder();
        ProductModel[] productArray = adapter.loadAllProduct();
        response.append("<html><body>");
        response.append(convertProductToString(productArray));

        response.append("</body></html>");


        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

    private static void handleCustomerRequest(HttpExchange exchange) throws IOException {
        String response = "<html><body><h1>You try read Customer information!!! </body></html>";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleUserChangeRequest(HttpExchange exchange) throws IOException {
        HashMap<String, String> params = getParamsFromQuery(exchange.getRequestURI().getQuery());
        StringBuilder response = new StringBuilder();
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect(DBPath);
        response.append("<html><body>");
        try {
            UserModel user = new UserModel();
            String newPassword = "";
            if(params.containsKey("userID"))
                user.mUserID = Integer.parseInt(params.get("userID"));
            else
                response.append("Param userID Required.<br/>");

            if(params.containsKey("oldPassword"))
                user.mPassword = params.get("oldPassword");
            else
                response.append("Param oldPassword Required.<br/>");

            if(params.containsKey("newPassword"))
                newPassword = params.get("newPassword");
            else
                response.append("Param newPassword Required.<br/>");

            boolean result = adapter.changeUserPassword(user, newPassword);

            if(result)
                response.append("Successfully");
            else
                response.append("Update failed");
        } catch (Exception e) {
            System.out.println(e);
        }

        try{
            System.out.println("product");
            String productName = params.get("name");
            ProductModel[] productArray = adapter.searchProductByName(productName);
            response.append(convertProductToString(productArray));
        } catch (Exception e) {
        }

        response.append("</body></html>");


        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

}

