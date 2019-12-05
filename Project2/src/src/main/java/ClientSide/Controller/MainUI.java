package ClientSide.Controller;


import ClientSide.Adapter.IDataAccess;
import ClientSide.Adapter.StoreManager;
import Model.ResponseModel;
import Model.UserModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI {
    public JFrame view;
    private JPanel titlePane = new JPanel(new FlowLayout());
    private JTextField textViewUserName = new JTextField(20);
    private JTextField textViewUserPassword = new JTextField(20);
    public MainUI() {
        view = new JFrame();
        textViewUserName.setText("customer");
        textViewUserPassword.setText("customer");
        view.setTitle("Store Management System");
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setSize(500, 250);
        StoreManager instance = StoreManager.getInstance();
        IDataAccess adapter = instance.getDataAccess();
        JLabel title = new JLabel("Store Management System");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        titlePane.add(title);
        switchToLoginUI();
    }
    public JPanel getLoginPanel(){
        JPanel loginPanel = new JPanel(new GridLayout(7,2, 0,1));
        JPanel panelUserName = new JPanel(new FlowLayout());
        JPanel panelUserPassword = new JPanel(new FlowLayout());
        JPanel panelBtn = new JPanel(new FlowLayout());
        JButton btnLogin = new JButton("Login");
        JButton btnClear = new JButton("Clear");
        panelUserName.add(new JLabel("Name"));
        panelUserName.add(textViewUserName);
        panelUserPassword.add(new JLabel("Password"));
        panelUserPassword.add(textViewUserPassword);
        panelBtn.add(btnLogin);
        panelBtn.add(btnClear);
        loginPanel.add(titlePane, BorderLayout.PAGE_START);
        loginPanel.add(panelUserName);
        loginPanel.add(panelUserPassword);
        loginPanel.add(panelBtn);
        btnLogin.addActionListener(new LoginButtonController());
        btnClear.addActionListener(new ClearButtonController());
        return loginPanel;

    }
    public JPanel getMainPanel(){
        JPanel mainPanel = new JPanel(new BorderLayout());
        StoreManager instance = StoreManager.getInstance();
        IDataAccess adapter = instance.getDataAccess();
        mainPanel.add(titlePane, BorderLayout.PAGE_START);
        JTabbedPane tabbedPane = new JTabbedPane();
        AddProductUI productFrame = new AddProductUI();
        AddCustomerUI customerFrame = new AddCustomerUI();
        PurchaseUI_Cashier purchaseFrame = new PurchaseUI_Cashier();
        ProfileUI profileFrame = new ProfileUI();;
        ProductUI productInfoFrame = new ProductUI();
        PurchaseHistoryUI purchaseHistoryFrame = new PurchaseHistoryUI();
        UserManagementUI userManagementFrame = new UserManagementUI();
        SummaryReportUI summaryReportFrame = new SummaryReportUI();
        PurchaseUI_Customer customerPurchaseFrame = new PurchaseUI_Customer();
        UserModel user = instance.getUser();
        tabbedPane.addTab("Profile", null, profileFrame.view.getContentPane());
        switch (user.mUserType) {
            case UserModel.USER_TYPE_MANAGER:
                tabbedPane.addTab("Product", null, productFrame.view.getContentPane());
                tabbedPane.addTab("Sales", null, summaryReportFrame.view.getContentPane());
                break;
            case UserModel.USER_TYPE_CASHIER:
                tabbedPane.addTab("Customer", null, customerFrame.view.getContentPane());
                tabbedPane.addTab("Purchase", null, purchaseFrame.view.getContentPane());
                break;
            case UserModel.USER_TYPE_CUSTOMER:
                tabbedPane.addTab("Purchase", null, customerPurchaseFrame.view.getContentPane());
                tabbedPane.addTab("Product", null, productInfoFrame.view.getContentPane());
                tabbedPane.addTab("History", null, purchaseHistoryFrame.view.getContentPane());
                break;
            case UserModel.USER_TYPE_ADMIN:
                tabbedPane.addTab("User", null, userManagementFrame.view.getContentPane());
        }
        mainPanel.add(tabbedPane);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                switch(tabbedPane.getSelectedIndex()){
                    case 3:
                        if(user.mUserType == UserModel.USER_TYPE_CUSTOMER)
                            purchaseHistoryFrame.refreshTable();
                    default:
                        break;
                }
            }
        });
        return mainPanel;
    }
    public void switchToMainUI(){
        Container pane = view.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        pane.removeAll();
        pane.add(getMainPanel());
        view.setSize(1000, 800);
        view.invalidate();
        view.validate();
    }
    public void switchToLoginUI(){
        Container pane = view.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        pane.removeAll();
        pane.add(getLoginPanel());
        view.setSize(500, 250);
        view.invalidate();
        view.validate();
    }
    class LoginButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            StoreManager instance = StoreManager.getInstance();
            IDataAccess adapter = instance.getDataAccess();
            String userFullName = textViewUserName.getText();
            String userPassword = textViewUserPassword.getText();
            if (userFullName.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Full Name Required");
                return;
            }
            if (userPassword.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Password Required.");
                return;
            }
            UserModel user = adapter.loginUser(userFullName, userPassword);
            if (user != null && adapter.getErrorCode() == ResponseModel.USER_LOGIN_OK) {
                JOptionPane.showMessageDialog(null,
                        "Welcome back! " + user.mName);
                instance.setUser(user);
                switchToMainUI();
            } else {
                JOptionPane.showMessageDialog(null,
                        ResponseModel.getErrorMessage(adapter.getErrorCode()));
            }

        }
    }
    class ClearButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            textViewUserName.setText("");
            textViewUserPassword.setText("");
        }
    }
}