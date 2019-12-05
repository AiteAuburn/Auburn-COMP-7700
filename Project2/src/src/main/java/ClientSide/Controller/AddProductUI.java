package ClientSide.Controller;
import ClientSide.Adapter.IDataAccess;
import ClientSide.Adapter.StoreManager;
import Model.ProductModel;
import Model.ResponseModel;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class AddProductUI {
    public final JFrame view = new JFrame();
    public final JTextField textViewProductID = new JTextField(20);
    public final JTextField textViewProductName = new JTextField(20);
    public final JTextField textViewProductPrice = new JTextField(20);
    public final JTextField textViewProductQuantity = new JTextField(20);
    public final JButton btnSave = new JButton("Save");
    public final JButton btnClear = new JButton("Clear");


    public AddProductUI() {
        view.setTitle("Add Product");
        // operation to do when the window is closed.
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        view.setSize(800, 400);

        Container pane = view.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        // Declare all the panel needed
        JPanel panelProductName = new JPanel(new FlowLayout());
        JPanel panelProductID = new JPanel(new FlowLayout());
        JPanel panelProductPrice = new JPanel(new FlowLayout());
        JPanel panelProductQuantity = new JPanel(new FlowLayout());
        JPanel panelSaveAndClear = new JPanel(new FlowLayout());

        // Add labels, fields, and buttons to the panel
        panelProductID.add(new JLabel("ProductID"));
        panelProductID.add(textViewProductID);
        panelProductName.add(new JLabel("ProductName"));
        panelProductName.add(textViewProductName);
        panelProductPrice.add(new JLabel("ProductPrice"));
        panelProductPrice.add(textViewProductPrice);
        panelProductQuantity.add(new JLabel("ProductQuantity"));
        panelProductQuantity.add(textViewProductQuantity);
        panelSaveAndClear.add(btnSave);
        panelSaveAndClear.add(btnClear);

        textViewProductID.addFocusListener(new ProductDataLoader());
        pane.add(panelProductID);
        pane.add(panelProductName);
        pane.add(panelProductPrice);
        pane.add(panelProductQuantity);
        pane.add(panelSaveAndClear);
        btnSave.addActionListener(new AddButtonController());
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent actionEvent){
                textViewProductID.setText("");
                textViewProductName.setText("");
                textViewProductPrice.setText("");
                textViewProductQuantity.setText("");
            }
        });
    }


    class ProductDataLoader implements FocusListener {

        @Override
        public void focusGained(FocusEvent focusEvent) {
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            String productID = textViewProductID.getText();
            ProductModel product = null;
            IDataAccess adapter = StoreManager.getInstance().getDataAccess();
            if(productID.length() > 0) {
                try {
                    product = adapter.loadProduct(Integer.parseInt(productID));
                } catch (Exception e) {
                    return;
                }
            }
            if (product != null) {
                textViewProductID.setText(product.mProductID + "");
                textViewProductName.setText(product.mName);
                textViewProductPrice.setText(product.mPrice + ""    );
                textViewProductQuantity.setText(product.mQuantity + "");
            } else {
                textViewProductName.setText("");
                textViewProductPrice.setText("");
                textViewProductQuantity.setText("");
            }

        }
    }

    class AddButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProductModel product = new ProductModel();

            String temp = textViewProductID.getText();

            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "ProductID could not be EMPTY!!!");
                return;
            }
            try {
                product.mProductID = Integer.parseInt(temp);
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "ProductID is INVALID (not a number)!!!");
                return;
            }

            temp = textViewProductName.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Product Name could not be EMPTY!!!");
                return;
            }
            product.mName = temp;

            temp = textViewProductPrice.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Price could not be EMPTY!!!");
                return;
            }

            try {
                product.mPrice = Double.parseDouble(temp);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Price is INVALID (not a number)!!!");
                return;
            }

            temp = textViewProductQuantity.getText();
            if (temp.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Quantity could not be EMPTY!!!");
                return;
            }

            try {
                product.mQuantity = Double.parseDouble(temp);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Quantity is INVALID (not a number)!!!");
                return;
            }
            IDataAccess adapter = StoreManager.getInstance().getDataAccess();

            if (adapter.saveProduct(product))
                if (adapter.getErrorCode() == ResponseModel.PRODUCT_SAVE_OK)
                    JOptionPane.showMessageDialog(null,
                            "Product is saved successfully!\n" + product);
                else if(adapter.getErrorCode() == ResponseModel.PRODUCT_EDIT_OK)
                    JOptionPane.showMessageDialog(null,
                            "Product is edited successfully!\n" + product);
                else
                    JOptionPane.showMessageDialog(null,
                            ResponseModel.getErrorMessage(adapter.getErrorCode()));

        }
    }
}