package ServerSide;

public class UserModel {
    public int mUserID = -1;
    public String mPassword = "";
    public int mUserType = 1;
    public String mName = "";
    public String mAddress = "";
    public String mPhone = "";

    /**
     * Give a summary of the customer
     * @return a string representation of the customer
     */
    public String toString(){
        String output = String.format("User ID: %d\n" +
                        "User Name: %s\n" +
                        "User Address: %s\n" +
                        "User Phone: %s\n",
                mUserID,
                mName,
                mAddress,
                mPhone);
        return output;
    }
}
