package Model;

import java.text.SimpleDateFormat;

public class PurchaseModel {
    public int mPurchaseID = -1;
    public int mCustomerID = -1;
    public int mProductID = -1;
    public String mProductName = "";
    public double mCost = 0.0;
    public double mTax = 0.0;
    public double mTotalCost = 0.0;
    public double mPrice = -1;
    public double mQuantity = -1;
    public long mDate = 0;
    /**
     * Give a summary of the purchase
     * @return a string representation of the purchase
     */
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String output = String.format("Purchase ID: %d\n" +
                                      "Customer ID: %s\n" +
                                      "Product ID: %s\n" +
                                      "Cost: %f\n" +
                                      "Tax: %f\n" +
                                      "TotalCost: %f\n" +
                                      "Price: %f\n" +
                                      "Quantity: %f\n" +
                                      "Date: %s",
                                      mPurchaseID,
                                      mCustomerID,
                                      mProductID,
                                      mCost,
                                      mTax,
                                      mTotalCost,
                                      mPrice,
                                      mQuantity,
                                      sdf.format(mDate));
        return output;
    }
}
