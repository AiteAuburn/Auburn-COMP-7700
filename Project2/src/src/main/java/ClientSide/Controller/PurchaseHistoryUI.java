package ClientSide.Controller;

import ClientSide.Adapter.IDataAccess;
import ClientSide.Adapter.StoreManager;
import ClientSide.Adapter.TXTReceiptBuilder;
import Model.PurchaseModel;
import Model.PurchaseModel;
import Model.ResponseModel;
import Model.UserModel;

import javax.swing.*;
import javax.swing.table.*;
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

public class PurchaseHistoryUI {
    public JFrame view;

    private String[] tableColumnHeader = {"Date", "Name", "Price", "Quantity", "Cost", "Tax", "TotalCost"};
    private PurchaseTableModel purchaseTableModel = new PurchaseTableModel(new ArrayList<PurchaseModel>());
    JTable tablePurchaseInfo = new JTable(purchaseTableModel);

    public PurchaseHistoryUI() {
        view = new JFrame();
        view.setTitle("Add Purchase");
        view.setSize(500, 400);
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(11,2, 0,6));
        view.getContentPane().add(pane);
        pane = new JPanel();
        pane.setLayout(new FlowLayout());
        tablePurchaseInfo.setColumnSelectionAllowed(false);
        tablePurchaseInfo.setRowSelectionAllowed(false);
        tablePurchaseInfo.setCellSelectionEnabled(false);
        tablePurchaseInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePurchaseInfo.setEnabled(false);
        tablePurchaseInfo.setDragEnabled(false);
        tablePurchaseInfo.getTableHeader().setReorderingAllowed(false);
        JScrollPane paneTable = new JScrollPane(tablePurchaseInfo);
        TableColumnModel colModel=tablePurchaseInfo.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(150);
        colModel.getColumn(2).setPreferredWidth(30);
        colModel.getColumn(3).setPreferredWidth(10);
        colModel.getColumn(4).setPreferredWidth(40);
        colModel.getColumn(5).setPreferredWidth(40);
        colModel.getColumn(6).setPreferredWidth(40);
        view.getContentPane().add(pane);
        view.getContentPane().add(paneTable);
    }
    public void refreshTable(){
        StoreManager instance = StoreManager.getInstance();
        IDataAccess adapter = instance.getDataAccess();
        PurchaseModel[] purchaseArray = adapter.loadUserPurchaseHistory(instance.getUser().mUserID);
        if(purchaseArray != null) {
            purchaseTableModel.setList(purchaseArray);
            ((AbstractTableModel) tablePurchaseInfo.getModel()).fireTableDataChanged();
        }
    }
    public class PurchaseTableModel extends AbstractTableModel {

        private List<PurchaseModel> purchases;

        public PurchaseTableModel(List<PurchaseModel> Purchases){
            this.purchases = Purchases;
        }

        @Override
        public int getRowCount() {
            return purchases.size();
        }

        @Override
        public int getColumnCount() {
            return 7;
        }

        public void setList(PurchaseModel[] PurchaseArray) {
            purchases = Arrays.asList(PurchaseArray);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            PurchaseModel purchase = purchases.get(rowIndex);
            switch (columnIndex){
                case 0:
                    return purchase.getDate();
                case 1:
                    return purchase.mProductName;
                case 2:
                    return purchase.mPrice;
                case 3:
                    return purchase.mQuantity;
                case 4:
                    return purchase.mCost;
                case 5:
                    return purchase.mTax;
                case 6:
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
