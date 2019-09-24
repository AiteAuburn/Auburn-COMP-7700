package View;
import com.sun.deploy.panel.JavaPanel;

import javax.swing.*;
import java.awt.*;

public class AddProductView extends JFrame
{
    public JTextField textViewProductID = null;
    public JTextField textViewProductName = null;
    public JTextField textViewProductPrice = null;
    public JTextField textViewProductQuantity = null;
    public JButton btnConfirm = null;
    public JButton btnCancel = null;

    public AddProductView(){
        this.setTitle("Product View");
        // operation to do when the window is closed.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 400);
        Container pane = this.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        textViewProductID = new JTextField(20);
        textViewProductName = new JTextField(20);
        textViewProductPrice = new JTextField(20);
        textViewProductQuantity = new JTextField(20);
        btnConfirm = new JButton("Confirm");
        btnCancel = new JButton("Cancel");

        JPanel panelProductName = new JPanel(new FlowLayout());
        JPanel panelProductID = new JPanel(new FlowLayout());
        JPanel panelProductPrice = new JPanel(new FlowLayout());
        JPanel panelProductQuantity = new JPanel(new FlowLayout());
        JPanel panelConfirmAndCancel = new JPanel(new FlowLayout());

        panelProductID.add(new JLabel("ProductID"));
        panelProductID.add(textViewProductID);
        panelProductName.add(new JLabel("ProductName"));
        panelProductName.add(textViewProductName);
        panelProductPrice.add(new JLabel("ProductPrice"));
        panelProductPrice.add(textViewProductPrice);
        panelProductQuantity.add(new JLabel("ProductQuantity"));
        panelProductQuantity.add(textViewProductQuantity);
        panelConfirmAndCancel.add(btnConfirm);
        panelConfirmAndCancel.add(btnCancel);

        pane.add(panelProductID);
        pane.add(panelProductName);
        pane.add(panelProductPrice);
        pane.add(panelProductQuantity);
        pane.add(panelConfirmAndCancel);
    }
}
