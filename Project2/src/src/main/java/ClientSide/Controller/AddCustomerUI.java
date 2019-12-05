package ClientSide.Controller;
import ClientSide.Adapter.IDataAccess;
import ClientSide.Adapter.StoreManager;
import Model.ResponseModel;
import Model.UserModel;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class AddCustomerUI {
    public final JFrame view = new JFrame();
    public final JTextField textViewCustomerID = new JTextField(20);
    public final JTextField textViewCustomerName = new JTextField(20);
    public final JTextField textViewCustomerAddress = new JTextField(20);
    public final JTextField textViewCustomerPhone = new JTextField(20);
    public final JButton btnSave = new JButton("Save");
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
        panelSaveAndClear.add(btnClear);

        textViewCustomerID.addFocusListener(new CustomerDataLoader());
        pane.add(panelCustomerID);
        pane.add(panelCustomerName);
        pane.add(panelCustomerPrice);
        pane.add(panelCustomerQuantity);
        pane.add(panelSaveAndClear);
        btnSave.addActionListener(new AddButtonController());
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
    class CustomerDataLoader implements FocusListener {

        @Override
        public void focusGained(FocusEvent focusEvent) {
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            String customerID = textViewCustomerID.getText();
            UserModel customer = null;
            IDataAccess adapter = StoreManager.getInstance().getDataAccess();
            if(customerID.length() > 0) {
                try {
                    customer = adapter.loadCustomer(Integer.parseInt(customerID));
                }catch (Exception e){
                    return ;
                }
            }
            if (customer != null) {
                textViewCustomerID.setText(customer.mUserID + "");
                textViewCustomerName.setText(customer.mName);
                textViewCustomerAddress.setText(customer.mAddress);
                textViewCustomerPhone.setText(customer.mPhone);
            } else{
                textViewCustomerName.setText("");
                textViewCustomerAddress.setText("");
                textViewCustomerPhone.setText("");
            }

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

            if (adapter.saveCustomer(customer)) {
                if (adapter.getErrorCode() == ResponseModel.CUSTOMER_SAVE_OK)
                    JOptionPane.showMessageDialog(null,
                            "Customer is saved successfully!\n" + customer);
                else if (adapter.getErrorCode() == ResponseModel.CUSTOMER_EDIT_OK)
                    JOptionPane.showMessageDialog(null,
                            "Customer is edited successfully!\n" + customer);
            } else {
                JOptionPane.showMessageDialog(null,
                        ResponseModel.getErrorMessage(adapter.getErrorCode()));
            }


        }
    }
}
