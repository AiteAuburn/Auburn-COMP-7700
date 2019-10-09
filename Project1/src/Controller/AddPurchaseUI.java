package Controller;

import Adapter.IDataAccess;
import Adapter.StoreManager;
import Adapter.TXTDataAdapter;
import Adapter.TXTReceiptBuilder;
import Model.CustomerModel;
import Model.ProductModel;
import Model.PurchaseModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;

public class AddPurchaseUI {
    public JFrame view;

    public JLabel labPurchaseDate = new JLabel("Purchase Date: ");
    public JLabel labPurchaseID = new JLabel("Purchase ID: ");
    public JLabel labCustomerID = new JLabel("Customer ID: ");
    public JLabel labCustomerName = new JLabel("Customer Name: ");
    public JLabel labProductID = new JLabel("Product ID: ");
    public JLabel labProductName = new JLabel("Product Name: ");
    public JLabel labProductPrice = new JLabel("Product Price: ");
    public JLabel labProductQuantity = new JLabel("Purchase Quantity: ");

    public JTextField txtProductID = new JTextField(10);
    public JTextField txtPurchaseID = new JTextField(10);
    public JTextField txtCustomerID = new JTextField(10);
    public JTextField txtPurchaseDate = new JTextField(10);
    public JTextField txtProductName = new JTextField(20);
    public JTextField txtCustomerName = new JTextField(20);
    public JTextField txtPrice = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);
    public JTextField txtCost = new JTextField(20);
    public JTextField txtTax = new JTextField(20);
    public JTextField txtTotalCost = new JTextField(20);

    public JButton btnConfirm = new JButton("Confirm");
    public JButton btnClear = new JButton("Clear");

    ProductModel product;
    CustomerModel customer;
    PurchaseModel purchase;

    public AddPurchaseUI() {
        view = new JFrame();
        view.setTitle("Add Product");
        view.setSize(500, 400);
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(11,2, 0,6));
        view.getContentPane().add(pane);

        pane.add(new JLabel("Purchase Date:")); pane.add(txtPurchaseDate);
        pane.add(labPurchaseID); pane.add(txtPurchaseID);
        pane.add(labCustomerID); pane.add(txtCustomerID);
        pane.add(labCustomerName); pane.add(txtCustomerName);
        pane.add(labProductID); pane.add(txtProductID);
        pane.add(labProductName); pane.add(txtProductName);
        pane.add(labProductPrice); pane.add(txtPrice);
        pane.add(labProductQuantity); pane.add(txtQuantity);
        pane.add(new JLabel("Cost")); pane.add(txtCost);
        pane.add(new JLabel("Tax")); pane.add(txtTax);
        pane.add(new JLabel("Total Cost")); pane.add(txtTotalCost);


        pane = new JPanel();
        pane.setLayout(new FlowLayout());
        pane.add(btnConfirm); pane.add(btnClear);
        view.getContentPane().add(pane);

        purchase = new PurchaseModel();

        txtPurchaseDate.setText(Calendar.getInstance().getTime().toString());
        txtCustomerName.setEnabled(false);
        txtProductName.setEnabled(false);
        txtCost.setEnabled(false);
        txtTax.setEnabled(false);
        txtTotalCost.setEnabled(false);
        txtPurchaseDate.setEnabled(false);
        txtCustomerID.addFocusListener(new CustomerNameLoader());
        txtProductID.addFocusListener(new ProductNameLoader());
        txtQuantity.addFocusListener(new CostCalculator());
        btnConfirm.addActionListener(new AddButtonController());
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent actionEvent){
                txtProductID.setText("");
                txtPurchaseID.setText("");
                txtCustomerID.setText("");
                txtPurchaseDate.setText(Calendar.getInstance().getTime().toString());
                txtProductName.setText("");
                txtCustomerName.setText("");
                txtPrice.setText("");
                txtQuantity.setText("");
                txtCost.setText("");
                txtTax.setText("");
                txtTotalCost.setText("");
            }
        });

    }
    class AddButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            String temp = txtPurchaseID.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Purchase ID could not be EMPTY!!!");
                return;
            }

            try {
                purchase.mPurchaseID = Integer.parseInt(temp);
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "PurchaseID is INVALID (not a number)!!!");
                return;
            }


            temp = txtCustomerID.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "CustomerID could not be EMPTY!!!");
                return;
            }

            try {
                purchase.mCustomerID = Integer.parseInt(temp);
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "CustomerID is INVALID (not a number)!!!");
                return;
            }

            temp = txtProductID.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Product ID could not be EMPTY!!!");
                return;
            }

            try {
                purchase.mProductID = Integer.parseInt(temp);
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "ProductID is INVALID (not a number)!!!");
                return;
            }


            temp = txtQuantity.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Quantity could not be EMPTY!!!");
                return;
            }

            try {
                purchase.mQuantity = Double.parseDouble(temp);
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Quantity is INVALID (not a number)!!!");
                return;
            }

            temp = txtPrice.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Price could not be EMPTY!!!");
                return;
            }

            try {
                purchase.mPrice = Double.parseDouble(temp);
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Price is INVALID (not a number)!!!");
                return;
            }

            temp = txtPurchaseDate.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Date could not be EMPTY!!!");
                return;
            } else {
                purchase.mDate = txtPurchaseDate.getText();
            }

            IDataAccess adapter = StoreManager.getInstance().getDataAccess();

            if (adapter.savePurchase(purchase)) {
                TXTReceiptBuilder tb = new TXTReceiptBuilder();
                tb.appendHeader("STORE MANAGEMENT SYSTEM");
                tb.appendCustomer(customer);
                tb.appendProduct(product);
                tb.appendPurchase(purchase);
                tb.appendFooter("");
                JOptionPane.showMessageDialog(null,
                        "Purchase is saved successfully!\n" + tb);
            }
            else {
                JOptionPane.showMessageDialog(null,
                        "ERROR: " + adapter.getErrorMessage());
            }


        }
    }

    class CostCalculator implements FocusListener  {

        @Override
        public void focusGained(FocusEvent focusEvent) {

        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            String productQuantity = txtQuantity.getText();
            if (productQuantity.length() > 0 & product != null) {
                try {
                    purchase.mQuantity = Double.parseDouble(productQuantity);
                } catch (NumberFormatException ex) {
                    txtQuantity.setText("");
                    JOptionPane.showMessageDialog(null, "Invalid Quantity");
                    return;
                }
                if (product.mProductID == -1) {
                    txtQuantity.setText("");
                    JOptionPane.showMessageDialog(null, "Please Enter ProductID First!");
                    return;
                }
                if (purchase.mQuantity > product.mQuantity) {
                    txtQuantity.setText("");
                    txtCost.setText("");
                    txtTax.setText("");
                    txtTotalCost.setText("");
                    JOptionPane.showMessageDialog(null, "Purchase Quantity > Available Quantity!");
                    return;
                }
                purchase.mPrice = product.mPrice;
                purchase.mCost = purchase.mQuantity * product.mPrice;
                txtCost.setText(Double.toString(purchase.mCost));
                purchase.mTax = purchase.mCost * 0.09;
                txtTax.setText(Double.toString(purchase.mTax));
                purchase.mTotalCost = purchase.mCost + purchase.mTax;
                txtTotalCost.setText(Double.toString(purchase.mTotalCost));
            } else {
                purchase.mQuantity = 0;
                purchase.mCost = 0;
                purchase.mTax = 0;
                purchase.mTotalCost = 0;
                txtCost.setText("");
                txtTax.setText("");
                txtTotalCost.setText("");
            }

        }
    }

    class CustomerNameLoader implements FocusListener  {

        @Override
        public void focusGained(FocusEvent focusEvent) {

        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            String customerID = txtCustomerID.getText();
            if(customerID.length() > 0) {
                try {
                    purchase.mCustomerID = Integer.parseInt(customerID);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid CustomerID");
                    return;
                }
                if (customer == null || customer.mCustomerID != purchase.mCustomerID)
                    customer = StoreManager.getInstance().getDataAccess().loadCustomer(purchase.mCustomerID);
                if (customer == null) {
                    txtCustomerID.setText("");
                    txtCustomerName.setText("");
                    JOptionPane.showMessageDialog(null, "No Customer with ID = " + customerID);
                    return;
                }
                txtCustomerName.setText(customer.mName);
            } else {
                txtCustomerName.setText("");
            }

        }
    }
    class ProductNameLoader implements FocusListener  {

        @Override
        public void focusGained(FocusEvent focusEvent) {

        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            String productID = txtProductID.getText();
            if ( productID.length() > 0 ) {
                try {
                    purchase.mProductID = Integer.parseInt(productID);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid ProductID");
                    return;
                }
                if (product == null || product.mProductID != purchase.mProductID)
                    product = StoreManager.getInstance().getDataAccess().loadProduct(purchase.mProductID);
                if (product == null) {
                    txtProductID.setText("");
                    txtProductName.setText("");
                    txtPrice.setText("");
                    purchase.mCost = 0;
                    txtCost.setText("");
                    purchase.mTax = 0;
                    txtTax.setText("");
                    purchase.mTotalCost = 0;
                    txtTotalCost.setText("");
                    txtQuantity.setText("");
                    purchase.mQuantity = 0;
                    JOptionPane.showMessageDialog(null, "No Product with ID = " + productID);
                    return;
                }

                txtProductName.setText(product.mName);
                txtPrice.setText(Double.toString(product.mPrice));

                if( txtQuantity.getText().length() != 0){
                    purchase.mCost = purchase.mQuantity * product.mPrice;
                    txtCost.setText(Double.toString(purchase.mCost));
                    purchase.mTax = purchase.mCost * 0.09;
                    txtTax.setText(Double.toString(purchase.mTax));
                    purchase.mTotalCost = purchase.mCost + purchase.mTax;
                    txtTotalCost.setText(Double.toString(purchase.mTotalCost));
                }
            } else {
                purchase.mQuantity = 0;
                purchase.mCost = 0;
                purchase.mTax = 0;
                purchase.mTotalCost = 0;
                txtProductName.setText("");
                txtPrice.setText("");
                txtCost.setText("");
                txtTax.setText("");
                txtTotalCost.setText("");
            }

        }
    }

}
