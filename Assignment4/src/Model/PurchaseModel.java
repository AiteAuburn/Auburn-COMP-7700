package Model;

public class PurchaseModel {
    public int mPurchaseID = -1;
    public int mCustomerID = -1;
    public int mProductID = -1;
    public double mCost = 0.0;
    public double mTax = 0.0;
    public double mTotalCost = 0.0;
    public double mPrice = 0.0;
    public double mQuantity = 0.0;
    public String mDate = "";
    /**
     * Give a summary of the purchase
     * @return a string representation of the purchase
     */
    public String toString() {
        String output = String.format("Purchase ID: %d\n" +
                                      "Customer ID: %s\n" +
                                      "Product ID: %s\n" +
                                      "Cost: %f\n" +
                                      "Tax: %f\n" +
                                      "TotalCost: %f\n" +
                                      "Price: %f\n" +
                                      "Quantity: %f\n" +
                                      "Date: %s\n",
                                      mPurchaseID,
                                      mCustomerID,
                                      mProductID,
                                      mCost,
                                      mTax,
                                      mTotalCost,
                                      mPrice,
                                      mQuantity,
                                      mDate);
        return output;
    }
}
