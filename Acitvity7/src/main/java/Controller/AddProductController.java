package Controller;

import Adapter.SQLiteDataAdapter;
import Model.ProductModel;
import View.AddProductView;

import java.awt.event.ActionListener;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProductController {
    public AddProductView view;
    public SQLiteDataAdapter dataAdapter;

    public AddProductController(AddProductView view, SQLiteDataAdapter adapter) {
        this.view = view;
        this.dataAdapter = adapter;
        view.btnConfirm.addActionListener(new AddButtonController());
        view.btnCancel.addActionListener(new CancelButtonController());
    }


    class AddButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProductModel product = new ProductModel();
            product.mProductID = Integer.parseInt(view.textViewProductID.getText());
            product.mName = view.textViewProductName.getText();
            product.mPrice = Double.parseDouble(view.textViewProductPrice.getText());
            product.mQuantity = Double.parseDouble(view.textViewProductQuantity.getText());
            if(dataAdapter.saveProduct(product))
                JOptionPane.showMessageDialog(null, product);
            else
                JOptionPane.showMessageDialog(null, "Error");
        }
    }

    class CancelButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            view.setVisible(false);
        }
    }

}