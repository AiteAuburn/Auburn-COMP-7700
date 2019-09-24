package Model;

public class ProductModel {
    public int mProductID = 0;
    public String mName = "", mVendor, mDescription ;
    public double mPrice = 0.0, mQuantity = 0.0;
    public String toString(){
        String output = String.format("Product ID: %d\n" +
                                      "Product Name: %s\n" +
                                      "Unit Price: %f\n" +
                                      "StockQuantity: %f\n",
                                      mProductID,
                                      mName,
                                      mPrice,
                                      mQuantity);
        return output;
    }
}
