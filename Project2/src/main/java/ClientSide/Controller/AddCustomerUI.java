package ClientSide.Controller;
import ClientSide.Adapter.IDataAccess;
import ClientSide.Adapter.StoreManager;
import Model.ResponseModel;
import Model.UserModel;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AddCustomerUI {
    public final JFrame view = new JFrame();
    public final JTextField textViewCustomerID = new JTextField(20);
    public final JTextField textViewCustomerName = new JTextField(20);
    public final JTextField textViewCustomerAddress = new JTextField(20);
    public final JTextField textViewCustomerPhone = new JTextField(20);
    public final JButton btnSave = new JButton("Save");
    public final JButton btnLoad = new JButton("Load");
    public final JButton btnClear = new JButton("Clear");

    public AddCustomerUI() {

        view.setTitle("Add Customer");
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

        // Add labels, fields, and buttons to the panel
        panelCustomerID.add(new JLabel("CustomerID"));
        panelCustomerID.add(textViewCustomerID);
        panelCustomerName.add(new JLabel("CustomerName"));
        panelCustomerName.add(textViewCustomerName);
        panelCustomerPrice.add(new JLabel("CustomerAddress"));
        panelCustomerPrice.add(textViewCustomerAddress);
        panelCustomerQuantity.add(new JLabel("CustomerPhone"));
        panelCustomerQuantity.add(textViewCustomerPhone);
        panelSaveAndClear.add(btnSave);
        panelSaveAndClear.add(btnLoad);
        panelSaveAndClear.add(btnClear);

        pane.add(panelCustomerID);
        pane.add(panelCustomerName);
        pane.add(panelCustomerPrice);
        pane.add(panelCustomerQuantity);
        pane.add(panelSaveAndClear);
        btnSave.addActionListener(new AddButtonController());
        btnLoad.addActionListener(new LoadButtonController());
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent actionEvent){
                textViewCustomerID.setText("");
                textViewCustomerName.setText("");
                textViewCustomerAddress.setText("");
                textViewCustomerPhone.setText("");
            }
        });
    }
    class LoadButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserModel customer;
            int customerID = -1;
            String temp = textViewCustomerID.getText();

            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "CustomerID cannot be empty.");
                return;
            }
            try {
                customerID = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "CustomerID is invalid!!!");
                return;
            }
            IDataAccess adapter = StoreManager.getInstance().getDataAccess();
            customer = adapter.loadCustomer(customerID);
            System.out.println("hey" + adapter.getErrorCode());
            if (customer != null) {
                textViewCustomerID.setText(customer.mUserID + "");
                textViewCustomerName.setText(customer.mName);
                textViewCustomerAddress.setText(customer.mAddress);
                textViewCustomerPhone.setText(customer.mPhone);
            }
            else
                JOptionPane.showMessageDialog(null,
                        ResponseModel.getErrorMessage(adapter.getErrorCode()));


        }
    }
    class AddButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserModel customer = new UserModel();

            String temp = textViewCustomerID.getText();

            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "CustomerID could not be EMPTY!!!");
                return;
            }
            try {
                customer.mUserID = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "ProductID is INVALID (not a number)!!!");
                return;
            }

            temp = textViewCustomerName.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Product Name could not be EMPTY!!!");
                return;
            }
            customer.mName = temp;

            temp = textViewCustomerPhone.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Phone could not be EMPTY!!!");
                return;
            }
            customer.mPhone = temp;

            temp = textViewCustomerAddress.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Address could not be EMPTY!!!");
                return;
            }

            customer.mAddress = temp;

            IDataAccess adapter = StoreManager.getInstance().getDataAccess();

            if (adapter.saveCustomer(customer))
                if (adapter.getErrorCode() == ResponseModel.CUSTOMER_SAVE_OK)
                    JOptionPane.showMessageDialog(null,
                            "Customer is saved successfully!\n" + customer);
                else if(adapter.getErrorCode() == ResponseModel.CUSTOMER_EDIT_OK)
                    JOptionPane.showMessageDialog(null,
                            "Customer is edited successfully!\n" + customer);
                else
                    JOptionPane.showMessageDialog(null,
                            ResponseModel.getErrorMessage(adapter.getErrorCode()));


        }
    }
}
