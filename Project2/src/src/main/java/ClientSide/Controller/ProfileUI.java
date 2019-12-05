package ClientSide.Controller;

import ClientSide.Adapter.IDataAccess;
import ClientSide.Adapter.StoreManager;
import Model.ResponseModel;
import Model.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileUI {
    public final JFrame view = new JFrame();
    public final JTextField textViewCustomerName = new JTextField(20);
    public final JTextField textViewCustomerPassword = new JTextField(20);
    public final JButton btnSave = new JButton("Save");
    public final JButton btnLogout = new JButton("Logout");

    public ProfileUI() {

        view.setTitle("Profile");
        // operation to do when the window is closed.
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        view.setSize(800, 400);
        Container pane = view.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        // Declare all the panel needed
        JPanel panelCustomerName = new JPanel(new FlowLayout());
        JPanel panelCustomerID = new JPanel(new FlowLayout());
        JPanel panelCustomerPrice = new JPanel(new FlowLayout());
        JPanel panelCustomerQuantity = new JPanel(new FlowLayout());
        JPanel panelSaveAndClear = new JPanel(new FlowLayout());
        // Load User Data
        StoreManager instance = StoreManager.getInstance();
        IDataAccess adapter = instance.getDataAccess();
        // Add labels, fields, and buttons to the panel
        panelCustomerName.add(new JLabel("Full Name"));
        panelCustomerName.add(textViewCustomerName);
        textViewCustomerName.setText(instance.getUser().mName);
        panelCustomerPrice.add(new JLabel("Password"));
        panelCustomerPrice.add(textViewCustomerPassword);
        panelSaveAndClear.add(btnSave);
        panelSaveAndClear.add(btnLogout);

        pane.add(panelCustomerID);
        pane.add(panelCustomerName);
        pane.add(panelCustomerPrice);
        pane.add(panelCustomerQuantity);
        pane.add(panelSaveAndClear);
        btnSave.addActionListener(new SaveProfileButtonController());
        btnLogout.addActionListener(new LogoutButtonController());
    }
    class LogoutButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            StoreManager.getInstance().logout();
        }
    }
    class SaveProfileButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            StoreManager instance = StoreManager.getInstance();
            IDataAccess adapter = instance.getDataAccess();
            UserModel user = new UserModel();
            user.mUserID = instance.getUser().mUserID;
            String userFullName = textViewCustomerName.getText();
            String userPassword = textViewCustomerPassword.getText();
            if (userFullName.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Full Name cannot be empty!");
                return;
            } else
                user.mName = userFullName;

            if (userPassword.length() != 0)
                user.mPassword = userPassword;
            else
                user.mPassword = instance.getUser().mPassword;

            if (adapter.saveUser(user))
                if(adapter.getErrorCode() == ResponseModel.USER_EDIT_OK)
                    JOptionPane.showMessageDialog(null,
                            "Profile is edited successfully!\n");
                else
                    JOptionPane.showMessageDialog(null,
                            ResponseModel.getErrorMessage(adapter.getErrorCode()));


        }
    }
}
