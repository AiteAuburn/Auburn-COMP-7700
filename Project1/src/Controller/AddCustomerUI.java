package Controller;
import Adapter.IDataAccess;
import Adapter.StoreManager;
import Model.CustomerModel;

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
    public final JButton btnConfirm = new JButton("Confirm");
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
        JPanel panelConfirmAndClear = new JPanel(new FlowLayout());

        // Add labels, fields, and buttons to the panel
        panelCustomerID.add(new JLabel("CustomerID"));
        panelCustomerID.add(textViewCustomerID);
        panelCustomerName.add(new JLabel("CustomerName"));
        panelCustomerName.add(textViewCustomerName);
        panelCustomerPrice.add(new JLabel("CustomerAddress"));
        panelCustomerPrice.add(textViewCustomerAddress);
        panelCustomerQuantity.add(new JLabel("CustomerPhone"));
        panelCustomerQuantity.add(textViewCustomerPhone);
        panelConfirmAndClear.add(btnConfirm);
        panelConfirmAndClear.add(btnClear);

        pane.add(panelCustomerID);
        pane.add(panelCustomerName);
        pane.add(panelCustomerPrice);
        pane.add(panelCustomerQuantity);
        pane.add(panelConfirmAndClear);
        btnConfirm.addActionListener(new AddButtonController());
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
    class AddButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            CustomerModel customer = new CustomerModel();

            String temp = textViewCustomerID.getText();

            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "CustomerID could not be EMPTY!!!");
                return;
            }
            try {
                customer.mCustomerID = Integer.parseInt(temp);
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
                JOptionPane.showMessageDialog(null,
                        "Customer is saved successfully!\n" + customer);
            else
                JOptionPane.showMessageDialog(null,
                        "ERROR: " + adapter.getErrorMessage());


        }
    }
}
