package ClientSide.Controller;

import ClientSide.Adapter.IDataAccess;
import ClientSide.Adapter.StoreManager;
import Model.PurchaseModel;
import Model.PurchaseModel;
import Model.UserModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class SummaryReportUI {
    public JFrame view;

    public DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public JLabel labSales = new JLabel("Total sales: ");
    public JTextField txtSales = new JTextField(20);
    public JLabel labProductMinPrice = new JLabel("Start Date: ");
    public JTextField txtPurchaseStartTime = new JTextField(15);
    public JLabel labProductMaxPrice = new JLabel("End Date: ");
    public JTextField txtPurchaseEndTime = new JTextField(15);
    public final JButton btnSearch = new JButton("Search");
    private String[] tableColumnHeader = {"Date", "Product Name", "Price", "Quantity", "Total Sales"};
    private PurchaseTableModel purchaseTableModel = new PurchaseTableModel(new ArrayList<PurchaseModel>());
    JTable tablePurchaseInfo = new JTable(purchaseTableModel);


    public SummaryReportUI() {
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
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date lastMonth = cal.getTime();
        long startTime = lastMonth.getTime() / 1000;
        long endTime = now.getTime() / 1000;
        txtPurchaseStartTime.setText(sdf.format(lastMonth));
        txtPurchaseEndTime.setText(sdf.format(now));
        txtSales.setEnabled(false);
        pane.add(labSales);
        pane.add(txtSales);
        pane.add(labProductMinPrice);
        pane.add(txtPurchaseStartTime);
        pane.add(labProductMaxPrice);
        pane.add(txtPurchaseEndTime);
        pane.add(btnSearch);
        btnSearch.addActionListener(new SearchButtonController());
        tablePurchaseInfo.setColumnSelectionAllowed(false);
        tablePurchaseInfo.setRowSelectionAllowed(false);
        tablePurchaseInfo.setCellSelectionEnabled(false);
        tablePurchaseInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePurchaseInfo.setEnabled(false);
        tablePurchaseInfo.setDragEnabled(false);
        tablePurchaseInfo.getTableHeader().setReorderingAllowed(false);
        TableColumnModel colModel = tablePurchaseInfo.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(10);
        colModel.getColumn(1).setPreferredWidth(100);
        colModel.getColumn(2).setPreferredWidth(30);
        colModel.getColumn(3).setPreferredWidth(30);
        JScrollPane paneTable = new JScrollPane(tablePurchaseInfo);

        StoreManager instance = StoreManager.getInstance();
        IDataAccess adapter = instance.getDataAccess();
        PurchaseModel[] purchaseArray = adapter.searchPurchaseByTimePeriod(startTime, endTime);
        if(purchaseArray != null) {
            purchaseTableModel.setList(purchaseArray);
            ((AbstractTableModel) tablePurchaseInfo.getModel()).fireTableDataChanged();
            double totalSales = 0;
            for(int i = 0; i < purchaseArray.length; i ++)
                totalSales += purchaseArray[i].mTotalCost;
            txtSales.setText(Double.toString(totalSales));
        }
        view.getContentPane().add(pane);
        view.getContentPane().add(paneTable);

    }
    class SearchButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            long startTime = 0;
            long endTime = 0;
            try {
                startTime = sdf.parse(txtPurchaseStartTime.getText()).getTime() / 1000;
                endTime = sdf.parse(txtPurchaseEndTime.getText()).getTime() / 1000;
            } catch (Exception e){

                JOptionPane.showMessageDialog(null,
                        "Wrong date format");
                return;
            }
            StoreManager instance = StoreManager.getInstance();
            IDataAccess adapter = instance.getDataAccess();
            PurchaseModel[] purchaseArray = adapter.searchPurchaseByTimePeriod(startTime, endTime);
            if(purchaseArray != null) {
                purchaseTableModel.setList(purchaseArray);
                ((AbstractTableModel) tablePurchaseInfo.getModel()).fireTableDataChanged();
                double totalSales = 0;
                for(int i = 0; i < purchaseArray.length; i ++)
                    totalSales += purchaseArray[i].mTotalCost;
                txtSales.setText(Double.toString(totalSales));
            }
        }
    }
    public class PurchaseTableModel extends AbstractTableModel {

        private List<PurchaseModel> purchases;

        public PurchaseTableModel(List<PurchaseModel> purchases){
            this.purchases = purchases;
        }

        @Override
        public int getRowCount() {
            return purchases.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        public void setList(PurchaseModel[] productArray) {
            purchases= Arrays.asList(productArray);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            PurchaseModel purchase = purchases.get(rowIndex);
            switch (columnIndex){
                case 0:
                    return sdf.format(new Date(purchase.mDate * 1000));
                case 1:
                    return purchase.mProductName;
                case 2:
                    return purchase.mPrice;
                case 3:
                    return purchase.mQuantity;
                case 4:
                    return purchase.mTotalCost;
            }
            return "";
        }

        @Override
        public String getColumnName(int columnIndex) {
            return tableColumnHeader[columnIndex];
        }
    }
}
