package Adapter;

import Model.CustomerModel;
import Model.ProductModel;
import Model.PurchaseModel;

public class TXTReceiptBuilder implements IReceiptBuilder {
    StringBuilder sb = new StringBuilder();

    @Override
    public void appendHeader(String headerIn){
        sb.append(headerIn);
        sb.append("\n------------------\n");
    }

    @Override
    public void appendCustomer(CustomerModel customer){
        sb.append(customer);
    }

    @Override
    public void appendProduct(ProductModel product){
        sb.append(product);
    }

    @Override
    public void appendPurchase(PurchaseModel purchase){
        sb.append(purchase);
    }
    @Override
    public void appendFooter(String footerIn){
        sb.append(footerIn);
    }
    @Override
    public String toString() {
        return sb.toString();
    }
}
