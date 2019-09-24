import Adapter.SQLiteDataAdapter;
import Controller.AddProductController;
import View.AddProductView;

public class StoreManager {
    public static void main(String args[]){
        SQLiteDataAdapter adapter = new SQLiteDataAdapter();
        adapter.connect();
        AddProductView mProductView = new AddProductView();
        AddProductController addProductController = new AddProductController(mProductView, adapter);
        mProductView.setVisible(true) ;
    }
}
