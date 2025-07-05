package GUI;

import GUI.POSdashboard;
import Home.Sales;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.MySQL;

public class StockProducts extends javax.swing.JFrame {

    private POSdashboard pos;
    private Sales sales;

    public void setPos(POSdashboard posdash) {
        this.pos = posdash;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }

//    private pos pos;
//
//    public void setPOSDash(pos pos) {
//        this.pos = pos;
//    }
    public StockProducts() {
//        super(parent, modal);
        initComponents();
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.setDefaultRenderer(Object.class, render);
        jTextField1.setPreferredSize(new Dimension(jTextField1.getWidth(), jTextField1.getHeight()));
        loadProducts();
        reset();
    }

    private void reset() {
        jTable1.clearSelection();
        loadProducts();
        jTextField1.setText("Search By Product Name/ID AND Stock ID");
        jTextField1.setForeground(java.awt.Color.GRAY);
        jButton1.grabFocus();
        jComboBox1.setPreferredSize(new Dimension(jComboBox1.getWidth(), jComboBox1.getHeight()));
        jComboBox1.setSelectedIndex(0);

    }

    private void loadProducts() {

        String query = "SELECT product.id, product.name, brand.name AS brand_name, stock.id AS stock_id, "
                + "stock.mfd, stock.exp, stock.price, stock.qty, "
                + "offer.amount AS offer_amount "
                + "FROM product "
                + "INNER JOIN brand ON product.brand_id = brand.id "
                + "LEFT JOIN stock ON stock.product_id = product.id "
                + "LEFT JOIN offer ON offer.stock_id = stock.id ";

// Get search text and selected stock status
        String searchText = jTextField1.getText();
        String selectedStockStatus = jComboBox1.getSelectedItem().toString();

// Apply search filter
        if (!searchText.equals("Search By Product Name/ID AND Stock ID") && !searchText.trim().isEmpty()) {
            query += " WHERE (product.id LIKE '%" + searchText + "%' "
                    + "OR product.name LIKE '%" + searchText + "%' "
                    + "OR brand.name LIKE '%" + searchText + "%' "
                    + "OR stock.id LIKE '%" + searchText + "%') "
                    + "AND product.status_id = 1 ";
        } else {
            query += " WHERE product.status_id = 1 ";
        }

// Apply stock filter based on the combo box selection
        if (selectedStockStatus.equals("Stock Available")) {
            query += " AND stock.product_id IS NOT NULL AND stock.qty != 0 ";  // Show products with available stock
        } else if (selectedStockStatus.equals("Stock Not Available")) {
            query += " AND (stock.product_id IS NULL OR stock.qty = 0) ";  // Show products without stock or with qty = 0
        } else if (selectedStockStatus.equals("Stock Discontinued")) {
            query += " AND stock.status_id = 2 ";  // Show products with discontinued stock status
        }

// Order results
        query += " ORDER BY product.id ASC";

        try {
            ResultSet rs = MySQL.executeSearch(query);

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("product.id"));
                v.add(rs.getString("product.name"));
                v.add(rs.getString("brand_name"));
                v.add(rs.getString("stock_id"));
                v.add(rs.getString("stock.mfd"));
                v.add(rs.getString("stock.exp"));
                String offerAmount = rs.getString("offer_amount");
                if (offerAmount != null) {
                    v.add(offerAmount);
                } else {
                    v.add(rs.getString("stock.price"));
                }

                v.add(rs.getString("qty"));
                v.add("0");
                if (offerAmount != null) {
                    v.add("Offer");
                } else {
                    v.add("Normal");
                }

                dtm.addRow(v);
                jTable1.setModel(dtm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jComboBox1 = new javax.swing.JComboBox<>();
        roundedPanel1 = new Components.RoundedPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Product View");
        setAlwaysOnTop(true);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel1.setText("Search");

        jTextField1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Clear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Brand", "Stock ID", "MFD", "EXP", "Price", "Qty", "Discount", "Offer Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(30);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(20);
        }

        jComboBox1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Stock Available", "Stock Not Available", "Stock Discontinued" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1020, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addContainerGap())
        );

        roundedPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 30)); // NOI18N
        jLabel2.setText("Stock Products");

        javax.swing.GroupLayout roundedPanel1Layout = new javax.swing.GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roundedPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        loadProducts();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        if (jTextField1.getText().equals("Search By Product Name/ID AND Stock ID")) {
            jTextField1.setText("");
            jTextField1.setForeground(java.awt.Color.BLACK);
        }
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        if (jTextField1.getText().isEmpty()) {
            jTextField1.setText("Search By Product Name/ID AND Stock ID");
            jTextField1.setForeground(java.awt.Color.GRAY);
        }
    }//GEN-LAST:event_jTextField1FocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        reset();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        loadProducts();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        int row = jTable1.getSelectedRow();

        if (evt.getClickCount() == 2) {

            if (pos != null) {

//                JOptionPane.showMessageDialog(this, "Working", "Warning", JOptionPane.WARNING_MESSAGE);
                pos.getstockId().setText(String.valueOf(jTable1.getValueAt(row, 3)));
                pos.getqty().setText(String.valueOf(jTable1.getValueAt(row, 7)));
                pos.getmfd().setText(String.valueOf(jTable1.getValueAt(row, 4)));
                pos.getexp().setText(String.valueOf(jTable1.getValueAt(row, 5)));
                pos.getbrandName().setText(String.valueOf(jTable1.getValueAt(row, 2)));
                pos.getproductName().setText(String.valueOf(jTable1.getValueAt(row, 1)));
                pos.getunitPrice().setText(String.valueOf(jTable1.getValueAt(row, 6)));
                pos.getunitDiscount().setText(String.valueOf(jTable1.getValueAt(row, 8)));

//                roundedButton1.grabFocus();
                this.dispose();

            } else if (sales != null) {

                String productId = String.valueOf(jTable1.getValueAt(row, 0));
                String stockId = String.valueOf(jTable1.getValueAt(row, 3));
                sales.getJTextfield1().setText(productId);
                sales.getJTextfield6().setText(stockId);

                this.dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Not Working", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }

//        if (evt.getClickCount() == 2) {
//
//            if (posdashboard != null) {
//                
//                JOptionPane.showMessageDialog(this, "Working", "Warning", JOptionPane.WARNING_MESSAGE);
//
//                posdashboard.getstockId().setText(String.valueOf(jTable1.getValueAt(row, 3)));
//
////                this.dispose();
//
//            }
//            String Pname = String.valueOf(jTable1.getValueAt(row, 1));
//            String brandName = String.valueOf(jTable1.getValueAt(row, 2));
//            String stockId = String.valueOf(jTable1.getValueAt(row, 3));
//            String mfd = String.valueOf(jTable1.getValueAt(row, 4));
//            String exp = String.valueOf(jTable1.getValueAt(row, 5));
//            String price = String.valueOf(jTable1.getValueAt(row, 6));
//            String qty = String.valueOf(jTable1.getValueAt(row, 7));
//        }
    }//GEN-LAST:event_jTable1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private Components.RoundedPanel roundedPanel1;
    // End of variables declaration//GEN-END:variables
}
