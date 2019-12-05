package ClientSide.Controller;

import ClientSide.Adapter.IDataAccess;
import ClientSide.Adapter.StoreManager;
import ClientSide.Adapter.TXTReceiptBuilder;
import Model.ProductModel;
import Model.PurchaseModel;
import Model.ResponseModel;
import Model.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;

public class PurchaseUI_Customer {
    public JFrame view;
    public JLabel labProductID = new JLabel("Product ID: ");
    public JLabel labProductName = new JLabel("Product Name: ");
    public JLabel labProductPrice = new JLabel("Product Price: ");
    public JLabel labProductQuantity = new JLabel("Purchase Quantity: ");

    public JTextField txtProductID = new JTextField(10);
    public JTextField txtProductName = new JTextField(20);
    public JTextField txtPrice = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);
    public JTextField txtCost = new JTextField(20);
    public JTextField txtTax = new JTextField(20);
    public JTextField txtTotalCost = new JTextField(20);

    public JButton btnSave = new JButton("Purchase");
    public JButton btnClear = new JButton("Clear");

    ProductModel product;
    UserModel customer;
    PurchaseModel purchase;

    public PurchaseUI_Customer() {
        view = new JFrame();
        view.setTitle("Add Product");
        view.setSize(500, 400);
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(11,2, 0,6));
        view.getContentPane().add(pane);

        pane.add(labProductID); pane.add(txtProductID);
        pane.add(labProductName); pane.add(txtProductName);
        pane.add(labProductPrice); pane.add(txtPrice);
        pane.add(labProductQuantity); pane.add(txtQuantity);
        pane.add(new JLabel("Cost")); pane.add(txtCost);
        pane.add(new JLabel("Tax")); pane.add(txtTax);
        pane.add(new JLabel("Total Cost")); pane.add(txtTotalCost);


        pane = new JPanel();
        pane.setLayout(new FlowLayout());
        pane.add(btnSave);
        pane.add(btnClear);
        view.getContentPane().add(pane);

        purchase = new PurchaseModel();

        txtProductName.setEnabled(false);
        txtPrice.setEnabled(false);
        txtCost.setEnabled(false);
        txtTax.setEnabled(false);
        txtTotalCost.setEnabled(false);
        txtProductID.addFocusListener(new ProductNameLoader());
        txtQuantity.addFocusListener(new CostCalculator());
        btnSave.addActionListener(new AddButtonController());
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent actionEvent){
                txtProductID.setText("");
                txtProductName.setText("");
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

            IDataAccess adapter = StoreManager.getInstance().getDataAccess();
            purchase.mCustomerID = StoreManager.getInstance().getUser().mUserID;
            String temp = "";

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

            if (adapter.savePurchase(purchase)) {
                TXTReceiptBuilder tb = new TXTReceiptBuilder();
                tb.appendHeader("STORE MANAGEMENT SYSTEM");
                tb.appendCustomer(customer);
                tb.appendProduct(product);
                tb.appendPurchase(purchase);
                tb.appendFooter("");
                if (adapter.getErrorCode() == ResponseModel.PURCHASE_SAVE_OK)
                    JOptionPane.showMessageDialog(null,
                            "Purchase is made!");
            }
            else {
                JOptionPane.showMessageDialog(null,
                        ResponseModel.getErrorMessage(adapter.getErrorCode()));
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
            if (txtProductID.getText().length() > 0 && productQuantity.length() > 0 & product != null) {
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
                    purchase.mQuantity = 0;
                    txtQuantity.setText("");
                    JOptionPane.showMessageDialog(null, "No Product with ID = " + productID);
                    return;
                }

                txtProductName.setText(product.mName);
                txtPrice.setText(Double.toString(product.mPrice));

                if( txtQuantity.getText().length() != 0){
                    purchase.mQuantity = Double.parseDouble(txtQuantity.getText());
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
                txtQuantity.setText("");
                txtPrice.setText("");
                txtCost.setText("");
                txtTax.setText("");
                txtTotalCost.setText("");
            }

        }
    }

}
