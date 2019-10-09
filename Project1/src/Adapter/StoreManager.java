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
    private String databasePath = "";
    private int adapterType = 0;
    public static final int ADAPTER_TYPE_CACHED = 0;
    public static final int ADAPTER_TYPE_SQL = 1;
    public static final int ADAPTER_TYPE_TXT = 2;
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
                mAdapter.connect(databasePath);
                break;
            case ADAPTER_TYPE_SQL:
                mAdapter = new SQLiteDataAdapter();
                mAdapter.connect(databasePath);
                break;
            case ADAPTER_TYPE_TXT:
                mAdapter = new TXTDataAdapter();
                mAdapter.connect(databasePath);
                break;
        }
    }
    public void setDatabasePath(String databasePathIn){
        databasePath = databasePathIn;
    }
    private void setup() {
        setAdapterType(ADAPTER_TYPE_SQL);
        String dbFile = DEFAULT_DB_FILE;
        if (dbFile.length() == 0) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Select the DB file!!!");
            int r = fc.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION)
                dbFile = fc.getSelectedFile().getAbsolutePath();
        }
        if (mAdapter.connect(dbFile))
            System.out.println("Connection to SQLite has been established.");
        else
            System.out.println(mAdapter.getErrorMessage());
        mMainUI = new MainUI();
    }

    public void run() {
        mMainUI.view.setVisible(true);
    }

}
