package edu.auburn;

import com.google.gson.Gson;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientSideDataAdapter implements IDataAccess {


    class SocketAddress {
        String host;
        int port;
    }

    Gson gson = new Gson();
    SocketAddress serverAddr;

    @Override
    public boolean connect(String path) {
        serverAddr = gson.fromJson(path, SocketAddress.class);
        return true;
    }

    @Override
    public boolean disconnect() {
        return false;
    }

    @Override
    public int getErrorCode() {
        return 0;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    public String netSend(int code, String data) {
        String json = null;
        try {
            Socket link = new Socket(serverAddr.host, serverAddr.port);
            Scanner in = new Scanner(link.getInputStream());
            PrintWriter out = new PrintWriter(link.getOutputStream(), true);
            MessageModel message = new MessageModel(code, data);
            json = gson.toJson(message);
            System.out.println("Sent: " + json);
            System.out.println(link.getKeepAlive());
            out.println(json);
            json = in.nextLine();
            in.close();
            out.close();
            link.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Received: " + json);
        return json;
    }

    @Override
    public ProductModel loadProduct(int id) {
        String json = netSend(MessageModel.GET_PRODUCT, Integer.toString(id));
        MessageModel message = gson.fromJson(json, MessageModel.class);
        if (message.code == IDataAccess.PRODUCT_LOAD_OK)
            return gson.fromJson(message.data, ProductModel.class);
        else
            return null;
    }

    @Override
    public boolean saveProduct(ProductModel product) {
        String json = netSend(MessageModel.PUT_PRODUCT, gson.toJson(product));
        MessageModel message = gson.fromJson(json, MessageModel.class);
        if (message.code == IDataAccess.PRODUCT_SAVE_OK)
            return true;
        else
            return false;
    }

    @Override
    public CustomerModel loadCustomer(int mProductID) {
        return null;
    }
}
