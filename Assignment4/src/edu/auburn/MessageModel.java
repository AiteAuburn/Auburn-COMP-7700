package edu.auburn;

public class MessageModel {

    public static final int GET_PRODUCT = 100;
    public static final int PUT_PRODUCT = 101;

    public int code;
    public String data;

    public MessageModel() {
        this.code = 0;
        this.data = "";
    }
    public MessageModel(int code, String data) {
        this.code = code;
        this.data = data;
    }

}
