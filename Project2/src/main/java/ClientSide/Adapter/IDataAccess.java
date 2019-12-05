package ClientSide.Adapter;

import Model.UserModel;
import Model.ProductModel;
import Model.PurchaseModel;

public interface IDataAccess {
    public boolean connect(String path);
    public ProductModel loadProduct(int productID);
    public ProductModel[] loadProductAll();
    public boolean saveProduct(ProductModel product);
    public UserModel loadCustomer(int customerID);
    public boolean saveCustomer(UserModel product);
    public UserModel loginUser(String fullName, String password);
    public UserModel loadUser(int userID);
    public PurchaseModel[] loadUserPurchaseHistory(int userID);
    public boolean saveUser(UserModel product);
    public PurchaseModel loadPurchase(int purchaseID);
    public boolean savePurchase(PurchaseModel purchase);
    public ProductModel[] searchProductByNameAndPrice(String productName, double minPrice, double maxPrice);
    public PurchaseModel[] searchPurchaseByTimePeriod(long startTime, long endTime);

    public int getErrorCode();
}
