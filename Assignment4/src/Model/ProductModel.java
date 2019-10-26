package Model;

public class ProductModel {
    public int mProductID = -1;
    public String mName = "";
    public double mPrice = 0.0;
    public double mQuantity = 0.0;

    /**
     * Give a summary of the product
     * @return a string representation of the product
     */
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
