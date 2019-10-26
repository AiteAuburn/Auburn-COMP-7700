package Model;

public class MessageModel {

    public static final int GET_PRODUCT = 100;
    public static final int PUT_PRODUCT = 101;
    public static final int GET_CUSTOMER = 200;
    public static final int PUT_CUSTOMER = 201;
    public static final int GET_PURCHASE = 300;
    public static final int PUT_PURCHASE = 301;

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
