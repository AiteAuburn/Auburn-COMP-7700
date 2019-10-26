package edu.auburn;

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
        int port = 1000;
        try {
            // not a permanent connection!

            ServerSocket server = new ServerSocket(port);
            while (true) {
                try {
                    Socket pipe = server.accept();
                    Scanner in = new Scanner(pipe.getInputStream());
                    PrintWriter out = new PrintWriter(pipe.getOutputStream(), true);

                    String msg = in.nextLine();
                    in.close();

                    MessageModel request = gson.fromJson(msg, MessageModel.class);
                    String res = gson.toJson(process(request));

                    out.println(res);
                    System.out.println("WriteBack: " + res);
                    out.close();
                    pipe.close(); // close this socket!!!
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
            System.out.println("GET command from Client with id = " + id);
            ProductModel model = dao.loadProduct(id);
            if (model == null)
                response.code = IDataAccess.PRODUCT_LOAD_ID_NOT_FOUND;
            else {
                response.code = IDataAccess.PRODUCT_LOAD_OK;
                response.data = gson.toJson(model);
            }
        }

        if (request.code == MessageModel.PUT_PRODUCT) {
            ProductModel model = gson.fromJson(request.data, ProductModel.class);
            System.out.println("PUT command from Client with product = " + model);
            boolean saved = dao.saveProduct(model);
            if (saved) // save successfully!
                response.code = IDataAccess.PRODUCT_SAVE_OK;
            else
                response.code = IDataAccess.PRODUCT_SAVE_FAILED;
        }
        return response;
    }
}