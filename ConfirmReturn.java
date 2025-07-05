package GUI;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import model.MySQL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

public class ConfirmReturn extends javax.swing.JDialog {

    private POSdashboard exchangePos;

//    private POSdashboard pos;
//    public void setPos(POSdashboard posdash){
//        this.pos = posdash;
//    }
    private String pName;
    private String stockId;
    private String pQty;
    private String price;
    private String empEmail;
    private String invoiceId;
    private String cusMobile;
    private String cusName;

    private boolean withdrawPoints = false;

    HashMap<String, String> ReturnReasonMap = new HashMap<>();

    public ConfirmReturn(java.awt.Frame parent, boolean modal, String pName, String stockId, String pQty, String price, String empEmail, String invoiceId, String cusMobile, String cusName) {
        super(parent, modal);
        initComponents();

        this.pName = pName;
        this.stockId = stockId;
        this.pQty = pQty;
        this.price = price;
        this.empEmail = empEmail;
        this.invoiceId = invoiceId;
        this.cusMobile = cusMobile;
        this.cusName = cusName;
        this.exchangePos = exchangePos;

        loadData();
        loadReturnReason();
        generateReturnId();
    }

    private void loadData() {
        jLabel5.setText(pName);
        jLabel6.setText(stockId);
        jLabel10.setText(pQty);
        jLabel12.setText(price);
    }

    private void generateReturnId() {
        long id = System.currentTimeMillis();
        jLabel14.setText(String.valueOf(id));
    }

    private void loadReturnReason() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `cashier_return_condition`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {

                ReturnReasonMap.put(resultSet.getString("reason"), resultSet.getString("id"));

                vector.add(resultSet.getString("reason"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        roundedPanel1 = new Components.RoundedPanel();
        jLabel2 = new javax.swing.JLabel();
        roundedPanel2 = new Components.RoundedPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Confirm Return");

        roundedPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 30)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Confirm Return");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout roundedPanel1Layout = new javax.swing.GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        roundedPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        jLabel3.setText("Product Name   :");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel5.setText("Product Name Here");

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        jLabel4.setText("Stock Id :");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel6.setText("Stock Id Here");

        jLabel7.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        jLabel7.setText("Returning Quantity :");

        jTextField1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        jLabel8.setText("Return Reason :");

        jComboBox1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        jLabel9.setText("Refund Amount  :");

        jFormattedTextField1.setEditable(false);
        jFormattedTextField1.setForeground(new java.awt.Color(255, 0, 51));
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jFormattedTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField1KeyReleased(evt);
            }
        });

        buttonGroup1.add(jCheckBox1);
        jCheckBox1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jCheckBox1.setText("Add to Points");
        jCheckBox1.setActionCommand("5");

        buttonGroup1.add(jCheckBox2);
        jCheckBox2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jCheckBox2.setText("Exchange a Product");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jCheckBox2.setActionCommand("4");

        jLabel10.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        jLabel11.setText("Unit Price :");

        jLabel12.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel12.setText("Price Here");

        jLabel13.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        jLabel13.setText("Return Id :");

        jLabel14.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel14.setText("Return Id Here");

        jButton1.setBackground(new java.awt.Color(51, 153, 255));
        jButton1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Accept Return");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/cancel.png"))); // NOI18N

        javax.swing.GroupLayout roundedPanel2Layout = new javax.swing.GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(181, 181, 181))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel2Layout.createSequentialGroup()
                        .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(roundedPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(234, 234, 234))
                            .addGroup(roundedPanel2Layout.createSequentialGroup()
                                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(101, 101, 101)))
                        .addGap(60, 60, 60))
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jCheckBox1)
                        .addGap(42, 42, 42)
                        .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(roundedPanel2Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(roundedPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(16, 16, 16))
                                .addGroup(roundedPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(22, 22, 22)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(roundedPanel2Layout.createSequentialGroup()
                                    .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addGroup(roundedPanel2Layout.createSequentialGroup()
                                            .addGap(184, 184, 184)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(roundedPanel2Layout.createSequentialGroup()
                                    .addGap(184, 184, 184)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        roundedPanel2Layout.setVerticalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addGap(32, 32, 32)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roundedPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roundedPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundedPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jFormattedTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyReleased


    }//GEN-LAST:event_jFormattedTextField1KeyReleased

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        String qty = String.valueOf(jTextField1.getText());
        String price = String.valueOf(jLabel12.getText());

        if (!qty.matches("-?\\d*\\.?\\d+")) {
            JOptionPane.showMessageDialog(this, "Cannot use letter or symbols", "Warning", JOptionPane.WARNING_MESSAGE);
            jTextField1.setText("");
        } else {
            Double refundAmt = Double.parseDouble(qty) * Double.parseDouble(price);
            jFormattedTextField1.setText(String.valueOf(refundAmt));
        }


    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {

            String empmail = empEmail;
            String invoiceid = invoiceId;
            String qty = jTextField1.getText();
            String sqty = jLabel10.getText();
            String stockId = jLabel6.getText();
            String returnId = jLabel14.getText();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            String returnReasonId = ReturnReasonMap.get(String.valueOf(jComboBox1.getSelectedItem()));
            String refundAmt = jFormattedTextField1.getText();
            String returnReason = String.valueOf(jComboBox1.getSelectedItem());

            ButtonModel returnType = buttonGroup1.getSelection();

            if (!qty.matches("-?\\d*\\.?\\d+")) {
                JOptionPane.showMessageDialog(this, "Cannot use letter or symbols", "Warning", JOptionPane.WARNING_MESSAGE);
                jTextField1.setText("");
            } else if (qty.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter Returning Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (Double.parseDouble(qty) > Double.parseDouble(sqty)) {
                JOptionPane.showMessageDialog(this, "Please enter the correct Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (returnReason.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please Select a Returning Reason", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (returnType == null) {
                JOptionPane.showMessageDialog(this, "Please Select a Method", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                ResultSet rs = MySQL.executeSearch("SELECT * FROM `employee` WHERE `email`='" + empmail + "'");
                String empId = null;
                if (rs.next()) {
//                    String empid = null;
                    empId = rs.getString("id");
                }

                int rvId = Integer.parseInt(returnType.getActionCommand());


                MySQL.executeIUD("INSERT INTO `cashier_returns` (`id`,`date`,`return_amount`,`cashier_return_condition_id`,`employee_id`,`invoice_id`,`return_value_id`) "
                        + "VALUES ('" + returnId + "','" + dateTime + "','" + refundAmt + "','" + returnReasonId + "','" + empId + "','" + invoiceid + "','" + rvId + "') ");

//                MySQL.executeIUD("INSERT INTO `cashier_returns` (`id`,`date`,`return_amount`,`cashier_return_condition_id`,`employee_id`,`invoice_id`,`return_value_id`) "
//                        + "VALUES ('" + returnId + "','" + dateTime + "','" + refundAmt + "','" + returnReasonId + "','" + empId + "','" + invoiceId + "','" + rvId + "')");
                MySQL.executeIUD("INSERT INTO `cashier_return_items` (`qty`,`cashier_returns_id`,`stock_id`) "
                        + " VALUES ('" + qty + "','" + returnId + "','" + stockId + "')");

                ResultSet rs1 = MySQL.executeSearch("SELECT * FROM `invoice_item` WHERE `invoice_id` = '" + invoiceid + "' AND `stock_id`='" + stockId + "'");

                Double soldqty = null;
                if (rs1.next()) {
                    soldqty = Double.parseDouble(rs1.getString("qty"));
                }

                if (soldqty.equals(qty)) {
                    MySQL.executeIUD("DELETE FROM `invoice_item` WHERE `invoice_id` = '" + invoiceId + "' AND `stock_id`='" + stockId + "'");
                } else {
                    MySQL.executeIUD("UPDATE `invoice_item` SET `qty` = `qty` - '" + qty + "' WHERE  `invoice_id` = '" + invoiceId + "' AND `stock_id`='" + stockId + "'");
                }

                if (rvId == 5) {

                    double updatePoints = Double.parseDouble(jFormattedTextField1.getText()) / 5;

                    ResultSet resultset = MySQL.executeSearch("SELECT * FROM `customer`");
                    Double points = null;
                    while (resultset.next()) {
                        points = Double.parseDouble(resultset.getString("points"));
                    }

                    //update points
                    if (withdrawPoints) {
                        points += updatePoints;
                        MySQL.executeIUD("UPDATE `customer` SET `points` = '" + updatePoints + "' WHERE `mobile` = '" + cusMobile + "'");
                    } else {
                        MySQL.executeIUD("UPDATE `customer` SET `points` = `points` + '" + updatePoints + "' WHERE `mobile` = '" + cusMobile + "'");
                    }

                    JOptionPane.showMessageDialog(this, "Success", "Warning", JOptionPane.INFORMATION_MESSAGE);

                } else if (rvId == 4) {

                    POSdashboard pos = new POSdashboard();
                    pos.setVisible(true);

                    if (pos != null) {

                        pos.getRefundAmt().setText(String.valueOf(refundAmt));
                        pos.getcusMobile().setText(cusMobile);
                        pos.getCusName().setText(cusName);
                        pos.openPOSDash();

//                        JOptionPane.showMessageDialog(this, "Success", "Warning", JOptionPane.INFORMATION_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "No Options have been Selected", "Warning", JOptionPane.INFORMATION_MESSAGE);
                }

//                this.enableEvents(EXIT_ON_CLOSE);
                this.dispose();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatMacLightLaf.setup();

        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ConfirmReturn dialog = new ConfirmReturn(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField1;
    private Components.RoundedPanel roundedPanel1;
    private Components.RoundedPanel roundedPanel2;
    // End of variables declaration//GEN-END:variables
}
