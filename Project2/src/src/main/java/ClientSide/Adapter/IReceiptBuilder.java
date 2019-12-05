package ClientSide.Adapter;

import Model.UserModel;
import Model.ProductModel;
import Model.PurchaseModel;

public interface IReceiptBuilder {
    public void appendHeader(String s);
    public void appendCustomer(UserModel customer);
    public void appendProduct(ProductModel product);
    public void appendPurchase(PurchaseModel purchase);
    public void appendFooter(String s);
    public String toString();
}
