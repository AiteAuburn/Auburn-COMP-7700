package Model;

public class UserModel {
    public int mUserID = -1;
    public String mPassword = "";
    public int mUserType = 1;
    public String mName = "";
    public String mAddress = "";
    public String mPhone = "";

    public static final int USER_TYPE_CUSTOMER = 1;
    public static final int USER_TYPE_CASHIER = 2;
    public static final int USER_TYPE_MANAGER = 3;
    public static final int USER_TYPE_ADMIN = 4;
    /**
     * Give a summary of the customer
     * @return a string representation of the customer
     */
    public String toString(){
        String output = String.format("User ID: %d\n" +
                        "User Name: %s\n" +
                        "User Address: %s\n" +
                        "User Phone: %s",
                mUserID,
                mName,
                mAddress,
                mPhone);
        return output;
    }
}
