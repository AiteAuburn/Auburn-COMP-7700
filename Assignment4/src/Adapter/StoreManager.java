package Adapter;

import Controller.AddCustomerUI;
import Controller.AddProductUI;
import Controller.MainUI;

import javax.swing.*;

public class StoreManager {
    IDataAccess mAdapter;
    AddProductUI addProductUI;
    AddCustomerUI addCustomerUI;
    MainUI mMainUI;
    private String path = "";
    private int adapterType = 0;
    public static final int ADAPTER_TYPE_CACHED = 0;
    public static final int ADAPTER_TYPE_SQL = 1;
    public static final int ADAPTER_TYPE_TXT = 2;
    public static final int ADAPTER_TYPE_NETWORK = 3;
    public static String DEFAULT_DB_FILE = "C:\\Users\\Ai-Te\\store.db";


    static StoreManager instance = null;

    public static StoreManager getInstance() {
        if (instance == null) {
            instance = new StoreManager();
            instance.setup();
        }
        return instance;
    }

    public IDataAccess getDataAccess() {
        return mAdapter;
    }
    public void setAdapterType(int adapterTypeIn){
        adapterType = adapterTypeIn;
        switch (adapterType){
            default:
            case ADAPTER_TYPE_CACHED:
                mAdapter = new CachedDataAdapter(new SQLiteDataAdapter());
                break;
            case ADAPTER_TYPE_SQL:
                mAdapter = new SQLiteDataAdapter();
                break;
            case ADAPTER_TYPE_NETWORK:
                mAdapter = new ClientSideDataAdapter();
                path = "{ \"host\": \"localhost\", \"port\": 1001 }";
                break;
            case ADAPTER_TYPE_TXT:
                mAdapter = new TXTDataAdapter();
                break;
        }
    }
    private void setup() {
        path = DEFAULT_DB_FILE;
        if (path.length() == 0) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Select the DB file!!!");
            int r = fc.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION)
                path = fc.getSelectedFile().getAbsolutePath();
        }
        setAdapterType(ADAPTER_TYPE_NETWORK);
        if (mAdapter.connect(path))
            System.out.println("Connection to SQLite has been established.");
        else
            System.out.println(mAdapter.getErrorMessage());
        mMainUI = new MainUI();
    }

    public void run() {
        mMainUI.view.setVisible(true);
    }

}
