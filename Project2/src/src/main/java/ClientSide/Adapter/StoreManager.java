package ClientSide.Adapter;

import ClientSide.Controller.AddCustomerUI;
import ClientSide.Controller.AddProductUI;
import ClientSide.Controller.MainUI;
import Model.ResponseModel;
import Model.UserModel;

import javax.swing.*;

public class StoreManager {
    private IDataAccess mAdapter;
    private MainUI mMainUI;
    String dataURL = "http://localhost:8888/";
    UserModel user = new UserModel();
    private String databasePath = "";


    static StoreManager instance = null;

    public static StoreManager getInstance() {
        if (instance == null) {
            instance = new StoreManager();
            instance.setup();
        }
        return instance;
    }
    public void logout(){
        user = new UserModel();
        mMainUI.switchToLoginUI();
    }
    public UserModel getUser(){
        return user;
    }
    public void setUser(UserModel userIn) {
        user = userIn;
    }
    public IDataAccess getDataAccess() {
        return mAdapter;
    }
    private void setup() {
        mAdapter = new HTTPDataAdapter();
        if (mAdapter.connect(dataURL))
            System.out.println("Connection to SQLite has been established.");
        else
            System.out.println(ResponseModel.getErrorMessage(mAdapter.getErrorCode()));
        mMainUI = new MainUI();
    }

    public void run() {
        mMainUI.view.setVisible(true);
    }

}
