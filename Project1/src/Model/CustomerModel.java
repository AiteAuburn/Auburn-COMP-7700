package Model;

public class CustomerModel {
    public int mCustomerID = -1;
    public String mName = "";
    public String mAddress = "";
    public String mPhone = "";

    /**
     * Give a summary of the customer
     * @return a string representation of the customer
     */
    public String toString(){
        String output = String.format("Customer ID: %d\n" +
                                      "Customer Name: %s\n" +
                                      "Customer Address: %s\n" +
                                      "Customer Phone: %s\n",
                                      mCustomerID,
                                      mName,
                                      mAddress,
                                      mPhone);
        return output;
    }
}
