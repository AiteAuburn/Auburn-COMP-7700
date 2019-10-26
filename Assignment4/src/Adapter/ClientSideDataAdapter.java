package Adapter;


import Model.CustomerModel;
import Model.MessageModel;
import Model.ProductModel;
import Model.PurchaseModel;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientSideDataAdapter implements IDataAccess {

    public int errorCode = 0;


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
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        String output = "ErrorCode: " + errorCode + "\nMsg: " + errMsg.get(errorCode);
        return output;
    }

    public MessageModel netSend(int code, String data) {
        String json = null;
        String serverResponse = "";
        try {
            Socket link = new Socket(serverAddr.host, serverAddr.port);
            Scanner in = new Scanner(link.getInputStream());
            PrintWriter out = new PrintWriter(link.getOutputStream(), true);
            MessageModel message = new MessageModel(code, data);
            json = gson.toJson(message);
            System.out.println("Sent: " + json);
            out.println(json);
            out.flush();
            serverResponse = in.nextLine();
            in.close();
            out.close();
            link.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println("Received: " + serverResponse);
        MessageModel result = gson.fromJson(serverResponse, MessageModel.class);
        errorCode = result.code;
        return result;
    }

    @Override
    public ProductModel loadProduct(int productID) {
        MessageModel message = netSend(MessageModel.GET_PRODUCT, Integer.toString(productID));
        if (message.code == IDataAccess.PRODUCT_LOAD_OK)
            return gson.fromJson(message.data, ProductModel.class);
        else
            return null;
    }

    @Override
    public boolean saveProduct(ProductModel product) {
        MessageModel message = netSend(MessageModel.PUT_PRODUCT, gson.toJson(product));
        if (message.code == IDataAccess.PRODUCT_SAVE_OK || message.code == IDataAccess.PRODUCT_EDIT_OK)
            return true;
        else
            return false;
    }

    @Override
    public CustomerModel loadCustomer(int customerID) {
        MessageModel message = netSend(MessageModel.GET_CUSTOMER, Integer.toString(customerID));
            if (message.code == IDataAccess.CUSTOMER_LOAD_OK)
                return gson.fromJson(message.data, CustomerModel.class);
            else
                return null;
    }
    @Override
    public boolean saveCustomer(CustomerModel customer){
        MessageModel message = netSend(MessageModel.PUT_CUSTOMER, gson.toJson(customer));
        if (message.code == IDataAccess.CUSTOMER_SAVE_OK || message.code == IDataAccess.CUSTOMER_EDIT_OK)
            return true;
        else
            return false;
    }
    @Override
    public PurchaseModel loadPurchase(int purchaseID){
        MessageModel message = netSend(MessageModel.GET_PURCHASE, Integer.toString(purchaseID));
        if (message.code == IDataAccess.PURCHASE_LOAD_OK)
            return gson.fromJson(message.data,PurchaseModel.class);
        else
            return null;
    }
    @Override
    public boolean savePurchase(PurchaseModel purchase) {
        MessageModel message = netSend(MessageModel.PUT_PURCHASE, gson.toJson(purchase));
            if (message.code == IDataAccess.PURCHASE_SAVE_OK || message.code == IDataAccess.PURCHASE_EDIT_OK)
                return true;
            else
                return false;
    }
}
