package Server;

import Adapter.IDataAccess;
import Adapter.SQLiteDataAdapter;
import Model.CustomerModel;
import Model.MessageModel;
import Model.ProductModel;
import Model.PurchaseModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class StoreServer {
    static Gson gson = new Gson();
    static String dbfile = "C:\\Users\\Ai-TE\\store.db";
    static IDataAccess dao = new SQLiteDataAdapter(dbfile);

    public static void main(String[] args) {
        int port = 1001;
        try {
            // not a permanent connection!
            ServerSocket server = new ServerSocket(port);
            while (true) {
                try (Socket pipe = server.accept()){
                    Scanner in = new Scanner(pipe.getInputStream());
                    PrintWriter out = new PrintWriter(pipe.getOutputStream(), true);
                    String msg = in.nextLine();
                    MessageModel request = gson.fromJson(msg, MessageModel.class);
                    String res = gson.toJson(process(request));
                    out.println(res);
                    in.close();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MessageModel process(MessageModel request) {
        MessageModel response = new MessageModel();

        if (request.code == MessageModel.GET_PRODUCT) {
            int id = Integer.parseInt(request.data);
            System.out.println("GET command (LOAD) from Client with productID = " + id);
            ProductModel model = dao.loadProduct(id);
            response.code = dao.getErrorCode();
            if (model != null)
                response.data = gson.toJson(model);
        }

        if (request.code == MessageModel.PUT_PRODUCT) {
            ProductModel model = gson.fromJson(request.data, ProductModel.class);
            System.out.println("PUT command (PUT) from Client with productID = " + model);
            dao.saveProduct(model);
            response.code = dao.getErrorCode();
        }


        if (request.code == MessageModel.GET_CUSTOMER) {
            int id = Integer.parseInt(request.data);
            System.out.println("GET command (LOAD) from Client with customerID = " + id);
            CustomerModel model = dao.loadCustomer(id);
            response.code = dao.getErrorCode();
            if (model != null)
                response.data = gson.toJson(model);
        }

        if (request.code == MessageModel.PUT_CUSTOMER) {
            CustomerModel model = gson.fromJson(request.data, CustomerModel.class);
            System.out.println("PUT command (PUT) from Client with customerID = " + model);
            dao.saveCustomer(model);
            response.code = dao.getErrorCode();
        }


        if (request.code == MessageModel.GET_PURCHASE) {
            int id = Integer.parseInt(request.data);
            System.out.println("GET command (LOAD) from Client with purchaseID = " + id);
            PurchaseModel model = dao.loadPurchase(id);
            response.code = dao.getErrorCode();
            if (model != null)
                response.data = gson.toJson(model);
        }

        if (request.code == MessageModel.PUT_PURCHASE) {
            PurchaseModel model = gson.fromJson(request.data, PurchaseModel.class);
            System.out.println("PUT command (PUT) from Client with purchaseID = " + model);
            dao.savePurchase(model);
            response.code = dao.getErrorCode();
        }
        return response;
    }
}