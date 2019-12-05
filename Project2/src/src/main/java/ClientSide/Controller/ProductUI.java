package ClientSide.Controller;

import ClientSide.Adapter.IDataAccess;
import ClientSide.Adapter.StoreManager;
import ClientSide.Adapter.TXTReceiptBuilder;
import Model.ProductModel;
import Model.PurchaseModel;
import Model.ResponseModel;
import Model.UserModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ProductUI {
    public JFrame view;

    public JLabel labProductName = new JLabel("Product Name: ");
    public JTextField txtProductName = new JTextField(20);
    public JLabel labProductMinPrice = new JLabel("Product Min Price: ");
    public JTextField txtProductMinPrice = new JTextField(10);
    public JLabel labProductMaxPrice = new JLabel("Product Max Price: ");
    public JTextField txtProductMaxPrice = new JTextField(10);
    public final JButton btnSearch = new JButton("Search");
    private String[] tableColumnHeader = {"ID", "Name", "Price", "Availability"};
    private ProductTableModel productTableModel = new ProductTableModel(new ArrayList<ProductModel>());
    JTable tableProductInfo = new JTable(productTableModel);


    ProductModel product;
    UserModel customer;
    PurchaseModel purchase;

    public ProductUI() {
        view = new JFrame();
        view.setTitle("Add Product");
        view.setSize(500, 400);
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(11,2, 0,6));
        view.getContentPane().add(pane);
        pane = new JPanel();
        pane.setLayout(new FlowLayout());
        txtProductMinPrice.setText("0");
        txtProductMaxPrice.setText("100000");
        pane.add(labProductName);
        pane.add(txtProductName);
        pane.add(labProductMinPrice);
        pane.add(txtProductMinPrice);
        pane.add(labProductMaxPrice);
        pane.add(txtProductMaxPrice);
        pane.add(btnSearch);
        btnSearch.addActionListener(new SearchButtonController());
        tableProductInfo.setColumnSelectionAllowed(false);
        tableProductInfo.setRowSelectionAllowed(false);
        tableProductInfo.setCellSelectionEnabled(false);
        tableProductInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableProductInfo.setEnabled(false);
        tableProductInfo.setDragEnabled(false);
        tableProductInfo.getTableHeader().setReorderingAllowed(false);
        TableColumnModel colModel=tableProductInfo.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(10);
        colModel.getColumn(1).setPreferredWidth(100);
        colModel.getColumn(2).setPreferredWidth(30);
        colModel.getColumn(3).setPreferredWidth(30);
        JScrollPane paneTable = new JScrollPane(tableProductInfo);
        IDataAccess adapter = StoreManager.getInstance().getDataAccess();
        ProductModel[] productArray = adapter.loadProductAll();
        if(productArray != null) {
            productTableModel.setList(productArray);
            ((AbstractTableModel) tableProductInfo.getModel()).fireTableDataChanged();
        }
        view.getContentPane().add(pane);
        view.getContentPane().add(paneTable);

    }
    class SearchButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String productName = txtProductName.getText();
            double minPrice = 0;
            double maxPrice = 0;
            try {
                minPrice = Double.parseDouble(txtProductMinPrice.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "minPrice is not a number.");
                return;
            }
            try {
                maxPrice = Double.parseDouble(txtProductMaxPrice.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "maxPrice is not a number.");
                return;
            }
            IDataAccess adapter = StoreManager.getInstance().getDataAccess();
            ProductModel[] productArray = adapter.searchProductByNameAndPrice(productName, minPrice, maxPrice);
            if(productArray != null) {
                productTableModel.setList(productArray);
                ((AbstractTableModel) tableProductInfo.getModel()).fireTableDataChanged();
            }
        }
    }
    public class ProductTableModel extends AbstractTableModel {

        private List<ProductModel> products;

        public ProductTableModel(List<ProductModel> products){
            this.products = products;
        }

        @Override
        public int getRowCount() {
            return products.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        public void setList(ProductModel[] productArray) {
            products = Arrays.asList(productArray);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ProductModel product = products.get(rowIndex);
            switch (columnIndex){
                case 0:
                    return product.mProductID;
                case 1:
                    return product.mName;
                case 2:
                    return product.mPrice;
                case 3:
                    return (product.mQuantity > 0)? "In stock" : "Out of stock";
            }
            return "";
        }

        @Override
        public String getColumnName(int columnIndex) {
            return tableColumnHeader[columnIndex];
        }
    }
}
