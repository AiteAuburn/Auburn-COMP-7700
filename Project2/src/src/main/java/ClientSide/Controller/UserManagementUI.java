package ClientSide.Controller;

import ClientSide.Adapter.IDataAccess;
import ClientSide.Adapter.StoreManager;
import Model.ResponseModel;
import Model.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class UserManagementUI {
    public final JFrame view = new JFrame();
    public final JTextField textViewUserID = new JTextField(20);
    public final JTextField textViewUserName = new JTextField(20);
    public final JTextField textViewUserAddress = new JTextField(20);
    public final JTextField textViewUserPhone = new JTextField(20);
    private String[] userTypes = { "Customer", "Cashier", "Manager", "Admin" };
    public final JComboBox comboBoxUserType = new JComboBox(userTypes);
    public final JButton btnAdd = new JButton("Save");
    public final JButton btnClear = new JButton("Clear");

    public UserManagementUI() {

        view.setTitle("Add User");
        // operation to do when the window is closed.
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        view.setSize(800, 400);
        Container pane = view.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        // Declare all the panel needed
        JPanel panelUserName = new JPanel(new FlowLayout());
        JPanel panelUserID = new JPanel(new FlowLayout());
        JPanel panelUserType = new JPanel(new FlowLayout());
        JPanel panelUserAddress = new JPanel(new FlowLayout());
        JPanel panelUserPhone = new JPanel(new FlowLayout());
        JPanel panelSaveAndClear = new JPanel(new FlowLayout());

        // Add labels, fields, and buttons to the panel
        panelUserID.add(new JLabel("User ID"));
        panelUserID.add(textViewUserID);
        textViewUserID.addFocusListener(new UserDataLoader());
        panelUserName.add(new JLabel("Full Name"));
        panelUserName.add(textViewUserName);
        panelUserType.add(new JLabel("User Type"));
        panelUserType.add(comboBoxUserType);
        comboBoxUserType.setPreferredSize(textViewUserID.getPreferredSize());
        panelUserAddress.add(new JLabel("Address"));
        panelUserAddress.add(textViewUserAddress);
        panelUserPhone.add(new JLabel("Phone"));
        panelUserPhone.add(textViewUserPhone);
        panelSaveAndClear.add(btnAdd);
        panelSaveAndClear.add(btnClear);

        pane.add(panelUserID);
        pane.add(panelUserName);
        pane.add(panelUserType);
        pane.add(panelUserAddress);
        pane.add(panelUserPhone);
        pane.add(panelSaveAndClear);
        btnAdd.addActionListener(new AddButtonController());
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent actionEvent){
                textViewUserID.setText("");
                textViewUserName.setText("");
                textViewUserAddress.setText("");
                textViewUserPhone.setText("");
                comboBoxUserType.setSelectedIndex(0);
            }
        });
    }

    class UserDataLoader implements FocusListener {

        @Override
        public void focusGained(FocusEvent focusEvent) {
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            String userID = textViewUserID.getText();
            UserModel user = null;
            IDataAccess adapter = StoreManager.getInstance().getDataAccess();
            if(userID.length() > 0){
                try {
                    user = adapter.loadUser(Integer.parseInt(userID));
                } catch (Exception e) {
                    return;
                }
            }
            if (user != null) {
                textViewUserName.setText(user.mName);
                textViewUserAddress.setText(user.mAddress);
                textViewUserPhone.setText(user.mPhone);
                comboBoxUserType.setSelectedIndex(user.mUserType - 1 );
            } else {
                textViewUserName.setText("");
                textViewUserAddress.setText("");
                textViewUserPhone.setText("");
                comboBoxUserType.setSelectedIndex(0);
            }

        }
    }

    class AddButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserModel user = new UserModel();

            String temp = textViewUserID.getText();

            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "UserID is required.");
                return;
            }
            try {
                user.mUserID = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "UserID is not a number.");
                return;
            }

            temp = textViewUserName.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Full Name is required.");
                return;
            }
            user.mName = temp;

            temp = textViewUserPhone.getText();
            user.mPhone = temp;

            temp = textViewUserAddress.getText();
            user.mAddress = temp;

            IDataAccess adapter = StoreManager.getInstance().getDataAccess();

            if (adapter.saveUser(user))
                if (adapter.getErrorCode() == ResponseModel.USER_SAVE_OK)
                    JOptionPane.showMessageDialog(null,
                            "User is added successfully!\n");
                else if(adapter.getErrorCode() == ResponseModel.USER_EDIT_OK)
                    JOptionPane.showMessageDialog(null,
                            "User is edited successfully!\n");
                else
                    JOptionPane.showMessageDialog(null,
                            ResponseModel.getErrorMessage(adapter.getErrorCode()));


        }
    }
}
