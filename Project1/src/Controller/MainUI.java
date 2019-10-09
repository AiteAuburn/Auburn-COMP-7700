package Controller;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI {
    public JFrame view;
    public JButton btnAddProduct = new JButton("Add Product");
    public JButton btnAddCustomer = new JButton("Add Customer");
    public JButton btnAddPurchase = new JButton("Add Purchase");

    public MainUI() {
        view = new JFrame();
        view.setTitle("Store Management System");
        view.setSize(1000, 600);
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container pane = view.getContentPane();
        pane.setLayout(new BorderLayout());

        JLabel title = new JLabel("Store Management System");
        title.setFont(new Font("Serif", Font.BOLD, 32));

        JPanel titlePane = new JPanel(new FlowLayout());

        titlePane.add(title);
        pane.add(titlePane, BorderLayout.PAGE_START);
        JTabbedPane tabbedPane = new JTabbedPane();
        AddProductUI productFrame = new AddProductUI();
        AddCustomerUI customerFrame = new AddCustomerUI();
        AddPurchaseUI purchaseFrame = new AddPurchaseUI();

        tabbedPane.addTab("Product", null, productFrame.view.getContentPane());
        tabbedPane.addTab("Customer", null, customerFrame.view.getContentPane());
        tabbedPane.addTab("Purchase", null, purchaseFrame.view.getContentPane());

        pane.add(tabbedPane);

    }
}