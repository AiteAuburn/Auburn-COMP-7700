package edu.auburn;

import javax.swing.*;

public class StoreManager {
    public static String DEFAULT_DB_FILE = "C:\\Users\\Ai-Te\\store.db";
    IDataAccess mAdapter;
    MainUI mMainUI;

    static StoreManager instance = null;

    public static StoreManager getInstance() {
        if (instance == null) {
            instance = new StoreManager();
            instance.setup("Server", true);
        }
        return instance;
    }

    public IDataAccess getDataAccess() {
        return mAdapter;
    }

    private void setup(String dbms, boolean cache) {
        String path = DEFAULT_DB_FILE;
        if (dbms.equals("SQLite")) {
            mAdapter = new SQLiteDataAdapter(path);
            if (path.length() == 0) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Select the DB file!!!");
                int r = fc.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION)
                    path = fc.getSelectedFile().getAbsolutePath();
            }
        }
        if (dbms.equals("Server")) {
            mAdapter = new ClientSideDataAdapter();
            path = "{ \"host\": \"localhost\", \"port\": 1000 }";
        }
        //        if (dbms.equals("Oracle"))
//            adapter = new OracleDataAdapter();

//        if (cache)
//            mAdapter = new CachedDataAdapter(mAdapter);

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
