package GUI;

import Components.RoundedTextfield;
import Home.CustomerRegistration;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import jakarta.activation.CommandMap;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.MailcapCommandMap;
import jakarta.activation.MimeType;
import jakarta.activation.MimeTypeParseException;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
//import gui.StockProducts;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import model.InvoiceItems;
import model.MySQL;
import java.sql.ResultSet;
import java.util.Stack;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import raven.toast.Notifications;

public class POSdashboard extends javax.swing.JFrame {

    HashMap<Object, InvoiceItems> InvoiceItemMap = new HashMap<>();
    HashMap<String, String> paymentMethodMap = new HashMap<>();


    public RoundedTextfield getcusMobile() {
        return roundedTextfield1;
    }

    public JLabel getCusName() {
        return jLabel5;
    }

    public JLabel getCusPoints() {
        return jLabel30;
    }

    public RoundedTextfield getstockId() {
        return roundedTextfield2;
    }

    public JLabel getqty() {
        return jLabel16;
    }

    public JLabel getmfd() {
        return jLabel20;
    }

    public JLabel getexp() {
        return jLabel19;
    }

    public JLabel getbrandName() {
        return jLabel18;
    }

    public JLabel getproductName() {
        return jLabel9;
    }

    public RoundedTextfield getunitPrice() {
        return roundedTextfield3;
    }

    public RoundedTextfield getunitDiscount() {
        return roundedTextfield5;
    }

    public JFormattedTextField getRefundAmt() {
        return jFormattedTextField1;
    }

    private POSdashboard pos;
    private String loggerName;

    public void openPOSDash() {
        this.pos = pos;
    }

    public POSdashboard() {
        initComponents();
        FullScreenFrame();
        theader();
        time();
        date();
        generateInvoiceId();
        init();
        jLabel5.grabFocus();
        loadPaymentMethod();

        roundedTextfield6.grabFocus();
//        
//        jComboBox1.setEnabled(false);
//        jFormattedTextField1.setEnabled(false);
//        jFormattedTextField2.setEnabled(false);
//        jCheckBox1.setEnabled(false);

        String empMail = SignIn.getEmployeeEmail();
        jLabel21.setText(empMail);

    }

    private void generateInvoiceId() {
        long id = System.currentTimeMillis();
        jLabel8.setText(String.valueOf(id));
    }

    private void init() {
        roundedTextfield6.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search Product Name");
    }

    private void theader() {
        JTableHeader thead = jTable1.getTableHeader();
        thead.setForeground((new Color(255, 255, 255)));
        thead.setBackground(new Color(0, 0, 0));
        thead.setFont(new Font("Poppins", Font.BOLD, 12));
        TableColumn coll = jTable1.getColumnModel().getColumn(0);

        coll.setPreferredWidth(100);
    }

    public void date() {

        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMMM dd, yyyy");

        String dd = sdf.format(dt);
        date.setText(dd);

    }

    public void time() {

        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Date tm = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");

                String tim = sdf.format(tm);
                tim = tim.toUpperCase();
                jLabel3.setText(tim);
            }
        }).start();
    }

    public void FullScreenFrame() {
        setTitle("SuperERP POS System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Maximizes the window to full screen
    }

    private void loadCustomer(String mobile) {
        try {

            ResultSet rs = MySQL.executeSearch("SELECT * FROM `customer` WHERE `mobile` = '" + mobile + "' ");

            if (rs.next()) {
                jLabel5.setText(rs.getString("first_name"));
                jLabel30.setText(rs.getString("points"));
            } else {
                jLabel5.setText("Add");
                jLabel30.setText("(......)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInvoice() {

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        double total = 0;
//        double discount = 0;

        for (InvoiceItems invoiceItem : InvoiceItemMap.values()) {

            Vector<String> vector = new Vector<>();
            vector.add(invoiceItem.getStockId());
            vector.add(invoiceItem.getBrandName());
            vector.add(invoiceItem.getProName());
            vector.add(invoiceItem.getMfd());
            vector.add(invoiceItem.getExp());
            vector.add(invoiceItem.getUnitPrice());
            vector.add(invoiceItem.getQty());
            vector.add(invoiceItem.getUnitDiscount());

            double itemTotal = (Double.parseDouble(invoiceItem.getQty()) * Double.parseDouble(invoiceItem.getUnitPrice())) - Double.parseDouble(invoiceItem.getUnitDiscount());
            total += itemTotal;

            vector.add(String.valueOf(itemTotal));

            model.addRow(vector);

        }
//        jLabel9.setText(String.valueOf(total));
        jFormattedTextField4.setText(String.valueOf(total));
    }

    private void loadPaymentMethod() {

        try {

            ResultSet rs = MySQL.executeSearch("SELECT * FROM `payment_method`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (rs.next()) {

                paymentMethodMap.put(rs.getString("name"), rs.getString("id"));

                vector.add(rs.getString("name"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private double total = 0;
    private double discount = 0;
    private double payment = 0;
    private double balance = 0;
    private String paymentMethod = "Select";
    private boolean withdrawPoints = false;
//    private boolean withdrawPointsgoOff = false;
    private double points = 0;

    private void calculate() {

        total = Double.parseDouble(jFormattedTextField4.getText());

        if (jFormattedTextField1.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Double.parseDouble(jFormattedTextField1.getText());

            if (discount < 0) {
                JOptionPane.showMessageDialog(this, "Discount cannot be in minus", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }

        if (jFormattedTextField2.getText().isEmpty()) {
            payment = 0;
        } else {
            payment = Double.parseDouble(jFormattedTextField2.getText());

            if (payment < 0) {
                JOptionPane.showMessageDialog(this, "Payment cannot be in minus", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }

        if (jCheckBox1.isSelected()) {
            withdrawPoints = true;
        }
//        else {
//            withdrawPointsgoOff = true;
//        }
//        if (!jCheckBox1.checked) {
//            
//        }

        total -= discount;

        paymentMethod = String.valueOf(jComboBox1.getSelectedItem());

        if (paymentMethod.equals("Select")) {

            JOptionPane.showMessageDialog(this, "Select a Payment Method", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            if (withdrawPoints) {

                String point = jLabel30.getText();

                if (jLabel30.getText().equals("(......)")) {
                    JOptionPane.showMessageDialog(this, "Select a Customer", "Warning", JOptionPane.WARNING_MESSAGE);

                } else if (Double.parseDouble(point) == total) {
                    points = 0;
                    total = 0;
                    // no payment req

                } else if (Double.parseDouble(point) < total) {
                    points = 0;
                    total -= Double.parseDouble(point);
                    // no payment req

                } else {
                    points = Double.parseDouble(point) - total;
                    total = 0;
                }
            }


            if (paymentMethod.equals("Cash")) {
//            //cash
                jFormattedTextField2.setEditable(true);
                balance = payment - total;
//
                if (balance < 0) {
                    roundedButton4.setEnabled(false);
                    roundedButton7.setEnabled(true);
                } else {
                    roundedButton4.setEnabled(true);
                    roundedButton7.setEnabled(false);
                }
//
            } else {
//            //card
                payment = total;
                balance = 0;
                jFormattedTextField2.setText(String.valueOf(payment));
                jFormattedTextField2.setEditable(false);
                jButton3.setEnabled(true);

            }
        }

//        balance -= balance ;
        jFormattedTextField3.setText(String.valueOf(balance));

    }

    private JasperPrint print;
    File tempFile;

    private void viewReport() {

        String total = jFormattedTextField4.getText();
        String discount = String.valueOf(jFormattedTextField1.getText());

        String cusmobile = roundedTextfield1.getText();
        String invoiceId = jLabel8.getText();
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String payment = String.valueOf(jFormattedTextField2.getText());
        String balance = String.valueOf(jFormattedTextField3.getText());
        String empemail = jLabel21.getText();

//            String empid = "4752034190";
//        String payment = jFormattedTextField2.getText();
        paymentMethod = String.valueOf(jComboBox1.getSelectedItem());

        String emailToSend = "";
        String customerQuery = "SELECT * FROM `customer` WHERE `mobile` = '" + cusmobile + "' AND `ebill_id` ='" + 3 + "'";

        String path = "src//reports//SuperERPreceipt.jasper";

        HashMap<String, Object> params = new HashMap<>();

        try {

//                    String empemail = jLabel21.getText();
            params.put("Parameter1", jLabel8.getText());   //invoiceId
            params.put("Parameter2", empemail);
            params.put("Parameter3", total);
            params.put("Parameter4", discount);

            if (paymentMethod.equals("Cash")) {
                params.put("Parameter5", payment);
            } else {
                params.put("Parameter5", "0.00");
            }

            if (paymentMethod.equals("Card")) {
                params.put("Parameter6", payment);
            } else {
                params.put("Parameter6", "0.00");
            }

            params.put("Parameter7", balance);

            try {

                ResultSet rs = MySQL.executeSearch(customerQuery);

                if (rs.next()) {
                    emailToSend = rs.getString("email");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());

            JasperPrint jasperPrint = JasperFillManager.fillReport(path, params, dataSource);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            e.printStackTrace();

        }

        JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());

        try {

            print = JasperFillManager.fillReport(path, params, dataSource);

            if (!emailToSend.isEmpty()) {
                sendEmailWithReport(emailToSend, print);
            } else {
                // Export to PDF as a byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                JasperExportManager.exportReportToPdfStream(print, baos);

                byte[] pdfData = baos.toByteArray();

                try {
                    tempFile = File.createTempFile("Invoice_", ".pdf");
                    tempFile.deleteOnExit(); // Ensure it gets deleted on exit
                } catch (IOException ex) {
//                    Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Write the bytes to the temporary file
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    fos.write(pdfData);
                    fos.flush(); // Ensure all data is written
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Open the PDF with the default PDF viewer
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(tempFile);
                    } catch (IOException ex) {
//                        Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, "Failed to open PDF: " + ex.getMessage(), ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Desktop is not supported. Please open the PDF manually.");
                }
            }

        } catch (Exception e) {
        }

    }

    static {
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        try {
            MimeType mt = new MimeType("multipart/mixed");
            mc.addMailcap("multipart/mixed;; x-java-content-handler=com.sun.activation.registries.MimeTypeRegistry");
        } catch (MimeTypeParseException e) {
            e.printStackTrace();
        }
        CommandMap.setDefaultCommandMap(mc);
    }

    private static final String FROM = "nahji1101@gmail.com";
    private static final String PASSWORD = "rniuyliafnkxsirk";
    private static final Properties PROPERTIES = new Properties();

    static {
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
        PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
        PROPERTIES.put("mail.smtp.port", "587");
    }

    private static final Session SESSION = Session.getInstance(PROPERTIES, new jakarta.mail.Authenticator() {

        @Override
        protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(FROM, PASSWORD);
        }
    });

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void sendEmailWithReport(String email, JasperPrint print) {
        executor.submit(() -> {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                JasperExportManager.exportReportToPdfStream(print, baos);
                byte[] pdfData = baos.toByteArray();

                // Create email message
                Message message = new MimeMessage(SESSION);
                message.setFrom(new InternetAddress(FROM));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject("SuperERP Invoice");

                // Create the message body part
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText("Your Invoice is attached below");

                // Create the attachment part
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource dataSource = new ByteArrayDataSource(pdfData, "application/pdf");
                attachmentPart.setDataHandler(new DataHandler(dataSource));
                attachmentPart.setFileName("Invoice.pdf");

                // Create a multipart message for combining body and attachment
                Multipart multipart = new MimeMultipart("mixed"); // Explicitly specify the MIME type
                multipart.addBodyPart(messageBodyPart);  // Add the text message part
                multipart.addBodyPart(attachmentPart);   // Add the attachment part

                // Set the complete message parts
                message.setContent(multipart);

                // Send the email
                Transport.send(message);
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        Notifications.Location.TOP_RIGHT, 3000L, "Email Sent Successfully");

            } catch (Exception e) {
                e.printStackTrace();
            }
            executor.shutdown();
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundedPanel1 = new Components.RoundedPanel();
        jLabel4 = new javax.swing.JLabel();
        roundedPanel2 = new Components.RoundedPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        roundedPanel3 = new Components.RoundedPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        roundedTextfield1 = new Components.RoundedTextfield();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        roundedPanel4 = new Components.RoundedPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        roundedTextfield2 = new Components.RoundedTextfield();
        roundedTextfield3 = new Components.RoundedTextfield();
        roundedTextfield4 = new Components.RoundedTextfield();
        roundedButton1 = new Components.RoundedButton();
        roundedButton2 = new Components.RoundedButton();
        roundedButton3 = new Components.RoundedButton();
        roundedButton6 = new Components.RoundedButton();
        jLabel32 = new javax.swing.JLabel();
        roundedTextfield5 = new Components.RoundedTextfield();
        jLabel9 = new javax.swing.JLabel();
        roundedTextfield6 = new Components.RoundedTextfield();
        jLabel16 = new javax.swing.JLabel();
        roundedPanel5 = new Components.RoundedPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jLabel29 = new javax.swing.JLabel();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        roundedButton4 = new Components.RoundedButton();
        roundedButton5 = new Components.RoundedButton();
        roundedButton7 = new Components.RoundedButton();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel26 = new javax.swing.JLabel();
        roundedPanel6 = new Components.RoundedPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Super ERP");

        roundedPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/SE.jpg"))); // NOI18N

        javax.swing.GroupLayout roundedPanel1Layout = new javax.swing.GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        roundedPanel2.setBackground(new java.awt.Color(0, 51, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 255, 255));
        jLabel1.setText("Point of Sales");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Time");

        date.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        date.setForeground(new java.awt.Color(255, 255, 255));
        date.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        date.setText("Date");

        jLabel17.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Cashier Email :");

        jLabel21.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("a@gmail.com");

        javax.swing.GroupLayout roundedPanel2Layout = new javax.swing.GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(123, 123, 123)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        roundedPanel2Layout.setVerticalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        roundedPanel3.setBackground(new java.awt.Color(204, 255, 255));

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel2.setText("Customer Name");

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Add");
        jLabel5.setToolTipText("");
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel6.setText("Customer Mobile");

        roundedTextfield1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        roundedTextfield1.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        roundedTextfield1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedTextfield1ActionPerformed(evt);
            }
        });
        roundedTextfield1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                roundedTextfield1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                roundedTextfield1KeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 255));
        jLabel7.setText("Invoice No :");

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel33.setFont(new java.awt.Font("Poppins", 0, 30)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(51, 51, 255));
        jLabel33.setText("Total");

        javax.swing.GroupLayout roundedPanel3Layout = new javax.swing.GroupLayout(roundedPanel3);
        roundedPanel3.setLayout(roundedPanel3Layout);
        roundedPanel3Layout.setHorizontalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(roundedTextfield1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel33)
                .addGap(118, 118, 118))
        );
        roundedPanel3Layout.setVerticalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roundedTextfield1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        roundedPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Id", "Brand", "Name", "MFD", "EXP", "Unit Price", "Quantity", "Discount", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(30);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel10.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel10.setText("Item Code");

        jLabel11.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel11.setText("Unit Price");

        jLabel12.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel12.setText("Quantity");

        jLabel13.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel13.setText("MFD");

        jLabel14.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel14.setText("EXP");

        jLabel15.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel15.setText("Brand");

        jLabel18.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel19.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N

        roundedTextfield2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        roundedTextfield2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        roundedTextfield2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedTextfield2ActionPerformed(evt);
            }
        });
        roundedTextfield2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                roundedTextfield2KeyReleased(evt);
            }
        });

        roundedTextfield3.setEditable(false);
        roundedTextfield3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        roundedTextfield3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        roundedTextfield4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        roundedTextfield4.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        roundedTextfield4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                roundedTextfield4KeyPressed(evt);
            }
        });

        roundedButton1.setBackground(new java.awt.Color(204, 255, 255));
        roundedButton1.setText("Select Product");
        roundedButton1.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        roundedButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton1ActionPerformed(evt);
            }
        });
        roundedButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                roundedButton1KeyReleased(evt);
            }
        });

        roundedButton2.setBackground(new java.awt.Color(255, 51, 51));
        roundedButton2.setForeground(new java.awt.Color(255, 255, 255));
        roundedButton2.setText("Reset");
        roundedButton2.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        roundedButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton2ActionPerformed(evt);
            }
        });

        roundedButton3.setBackground(new java.awt.Color(51, 51, 255));
        roundedButton3.setForeground(new java.awt.Color(255, 255, 255));
        roundedButton3.setText("Add to Invoice");
        roundedButton3.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        roundedButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton3ActionPerformed(evt);
            }
        });

        roundedButton6.setForeground(new java.awt.Color(255, 255, 255));
        roundedButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-garbage-bin-20.png"))); // NOI18N
        roundedButton6.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        roundedButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton6ActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel32.setText("Discount");

        roundedTextfield5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        roundedTextfield5.setText("0");
        roundedTextfield5.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Product Name Here");
        jLabel9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(153, 255, 255), new java.awt.Color(0, 0, 255)));
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        roundedTextfield6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        roundedTextfield6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                roundedTextfield6KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                roundedTextfield6KeyReleased(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("0");
        jLabel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(153, 255, 255), new java.awt.Color(0, 0, 255)));

        javax.swing.GroupLayout roundedPanel4Layout = new javax.swing.GroupLayout(roundedPanel4);
        roundedPanel4.setLayout(roundedPanel4Layout);
        roundedPanel4Layout.setHorizontalGroup(
            roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel4Layout.createSequentialGroup()
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel14))
                                    .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33)
                                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel15)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel32)
                                        .addGap(18, 18, 18)
                                        .addComponent(roundedTextfield5, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addGap(162, 162, 162)
                                        .addComponent(roundedButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(roundedButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(roundedButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11))
                                .addGap(18, 18, 18)
                                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addComponent(roundedTextfield3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(68, 68, 68)
                                        .addComponent(jLabel12)
                                        .addGap(18, 18, 18)
                                        .addComponent(roundedTextfield4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(roundedTextfield6, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(roundedTextfield2, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1046, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        roundedPanel4Layout.setVerticalGroup(
            roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(roundedTextfield6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(roundedPanel4Layout.createSequentialGroup()
                            .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(roundedPanel4Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(roundedTextfield2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(roundedTextfield4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(roundedTextfield3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(15, 15, 15))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)))
                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(roundedTextfield5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                        .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addComponent(roundedButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(roundedButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                                    .addComponent(roundedButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(15, 15, 15)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                        .addComponent(roundedButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        roundedPanel5.setBackground(new java.awt.Color(255, 255, 255));
        roundedPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel24.setFont(new java.awt.Font("Poppins", 0, 30)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 255));
        jLabel24.setText("Total");

        jLabel27.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel27.setText("Payment Method");
        jLabel27.setToolTipText("");

        jComboBox1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel28.setText("Payment");
        jLabel28.setToolTipText("");

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField2.setFont(new java.awt.Font("Yu Gothic UI", 0, 15)); // NOI18N
        jFormattedTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField2KeyReleased(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 0, 0));
        jLabel29.setText("Balance");
        jLabel29.setToolTipText("");

        jFormattedTextField3.setEditable(false);
        jFormattedTextField3.setForeground(new java.awt.Color(255, 0, 0));
        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField3.setFont(new java.awt.Font("Yu Gothic UI", 0, 15)); // NOI18N

        jLabel30.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 0, 0));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("(......)");

        jLabel31.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel31.setText("Points Available");

        jCheckBox1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jCheckBox1.setText("Withdraw Points");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        roundedButton4.setBackground(new java.awt.Color(51, 255, 51));
        roundedButton4.setText("Pay");
        roundedButton4.setFont(new java.awt.Font("Poppins", 0, 20)); // NOI18N
        roundedButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton4ActionPerformed(evt);
            }
        });

        roundedButton5.setBackground(new java.awt.Color(255, 0, 51));
        roundedButton5.setForeground(new java.awt.Color(255, 255, 255));
        roundedButton5.setText("Cancel");
        roundedButton5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        roundedButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton5ActionPerformed(evt);
            }
        });

        roundedButton7.setText("Add to Credit");
        roundedButton7.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        roundedButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton7ActionPerformed(evt);
            }
        });

        jFormattedTextField4.setEditable(false);
        jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField4.setFont(new java.awt.Font("Yu Gothic UI", 0, 15)); // NOI18N
        jFormattedTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField4KeyReleased(evt);
            }
        });

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jFormattedTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField1ActionPerformed(evt);
            }
        });
        jFormattedTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField1KeyReleased(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel26.setText("Discount");
        jLabel26.setToolTipText("");

        javax.swing.GroupLayout roundedPanel5Layout = new javax.swing.GroupLayout(roundedPanel5);
        roundedPanel5.setLayout(roundedPanel5Layout);
        roundedPanel5Layout.setHorizontalGroup(
            roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(roundedPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                        .addGap(123, 123, 123)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, roundedPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(roundedPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                        .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(roundedPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(roundedPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                                .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(roundedButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(roundedButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(roundedButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel31)
                                .addGap(91, 91, 91)
                                .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(30, 30, 30))
        );
        roundedPanel5Layout.setVerticalGroup(
            roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31)
                    .addComponent(jCheckBox1))
                .addGap(18, 18, 18)
                .addGroup(roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addComponent(roundedButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundedButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundedButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        roundedPanel6.setBackground(new java.awt.Color(204, 255, 255));

        jButton3.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jButton3.setText("Credit");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jButton4.setText("Return");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jButton6.setText("Sales History");
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jButton7.setText("Lock Screen");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jButton10.setText("Close Day");
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jButton11.setText("New Sale");
        jButton11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jButton12.setText("Drawer");
        jButton12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Poppins", 0, 15)); // NOI18N
        jButton13.setText("Cash IN / OUT");
        jButton13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundedPanel6Layout = new javax.swing.GroupLayout(roundedPanel6);
        roundedPanel6.setLayout(roundedPanel6Layout);
        roundedPanel6Layout.setHorizontalGroup(
            roundedPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(187, 187, 187)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        roundedPanel6Layout.setVerticalGroup(
            roundedPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel6Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(roundedPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roundedPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roundedPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roundedPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(roundedPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roundedPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(roundedPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roundedPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundedPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roundedPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roundedPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundedPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void roundedButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton2ActionPerformed

        reset();
        roundedTextfield2.setEnabled(true);

    }//GEN-LAST:event_roundedButton2ActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked

        CustomerRegistration Cr = new CustomerRegistration();
        Cr.setVisible(true);
        Cr.setPOSDash(this);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void roundedButton1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roundedButton1KeyReleased


    }//GEN-LAST:event_roundedButton1KeyReleased

    private void roundedButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton6ActionPerformed

        int row = jTable1.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please Select a Product to Delete", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            String stockId = String.valueOf(jTable1.getValueAt(row, 0));
            String brand = String.valueOf(jTable1.getValueAt(row, 1));
            String proName = String.valueOf(jTable1.getValueAt(row, 2));
            String mfd = String.valueOf(jTable1.getValueAt(row, 3));
            String exp = String.valueOf(jTable1.getValueAt(row, 4));
            String price = String.valueOf(jTable1.getValueAt(row, 5));
            String qty = String.valueOf(jTable1.getValueAt(row, 6));
            String dis = String.valueOf(jTable1.getValueAt(row, 7));
//            String total = String.valueOf(jTable1.getValueAt(row, 8));

            InvoiceItems invoiceItem = new InvoiceItems();
            invoiceItem.setStockId(stockId);
            invoiceItem.setBrandName(brand);
            invoiceItem.setProName(proName);
            invoiceItem.setMfd(mfd);
            invoiceItem.setExp(exp);
            invoiceItem.setUnitPrice(price);
            invoiceItem.setQty(qty);
            invoiceItem.setUnitDiscount(dis);

            if (InvoiceItemMap.containsKey(stockId)) {
//                JOptionPane.showMessageDialog(this, "Can be deleted", "Warning", JOptionPane.WARNING_MESSAGE);

                if (InvoiceItemMap.get(stockId) != null) {

                    int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this Product :" + proName, "Message",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    if (option == JOptionPane.YES_OPTION) {

                        InvoiceItemMap.remove(stockId);

                    }

                    loadInvoice();
                    reset();

                }
            } else {
                JOptionPane.showMessageDialog(this, "this product is not in the list", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }

    }//GEN-LAST:event_roundedButton6ActionPerformed

    private void roundedButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton3ActionPerformed

        String stockId = roundedTextfield2.getText();
        String proName = jLabel9.getText();
        String unitPrice = roundedTextfield3.getText();
        String qty = roundedTextfield4.getText();
        String mfd = jLabel20.getText();
        String exp = jLabel19.getText();
        String brandName = jLabel18.getText();
        String unitDiscount = roundedTextfield5.getText();

        String totalqty = jLabel16.getText();

        if (stockId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a Product", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (qty.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter Quantity", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!qty.matches("\\d+")) {

            JOptionPane.showMessageDialog(this, "Invalid Qty Amount", "Warning", JOptionPane.WARNING_MESSAGE);
            roundedTextfield4.setText("");
        } else if (Double.parseDouble(qty) < 0) {

            JOptionPane.showMessageDialog(this, "Quantity can't be Minus", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (qty.equals("0")) {

            JOptionPane.showMessageDialog(this, "Quantity can't be 0", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (Double.parseDouble(qty) > Double.parseDouble(totalqty)) {
            JOptionPane.showMessageDialog(this, "Quantity Exceeds the Stock Quantity", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (Double.parseDouble(qty) > Double.parseDouble(totalqty)) {

            JOptionPane.showMessageDialog(this, "Quantity Exceeds the Stock Quantity", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!unitDiscount.matches("\\d+")) {

            JOptionPane.showMessageDialog(this, "Invalid Discount Amount", "Warning", JOptionPane.WARNING_MESSAGE);
            roundedTextfield5.setText("0");
        } else {

//            JOptionPane.showMessageDialog(this, "Success", "Warning", JOptionPane.WARNING_MESSAGE);
            InvoiceItems invoiceItem = new InvoiceItems();
            invoiceItem.setExp(exp);
            invoiceItem.setMfd(mfd);
            invoiceItem.setProName(proName);
            invoiceItem.setBrandName(brandName);
            invoiceItem.setQty(qty);
            invoiceItem.setUnitPrice(unitPrice);
            invoiceItem.setStockId(stockId);
            invoiceItem.setUnitDiscount(unitDiscount);

            if (InvoiceItemMap.get(stockId) == null) {
                InvoiceItemMap.put(stockId, invoiceItem);
//                                loadInvoiceItem();

            } else {

                InvoiceItems found = InvoiceItemMap.get(stockId);
//
                Double newqty = Double.parseDouble(found.getQty()) + Double.parseDouble(qty);

//                        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update the Quantity of Product :" + pName, "Message",
//                                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
//                    JOptionPane.showMessageDialog(this, qty, "Warning", JOptionPane.WARNING_MESSAGE);
//
                if (newqty > Double.parseDouble(totalqty)) {
                    JOptionPane.showMessageDialog(this, "Quantity exceeds Stock Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
//                        JOptionPane.showMessageDialog(this, found.getQty(), "Warning", JOptionPane.WARNING_MESSAGE);
//                    found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(pqty)));
//                    found.setUnitDiscount(String.valueOf(Double.parseDouble(found.getUnitDiscount()) + Double.parseDouble(dis)));

//                    found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));
//                        JOptionPane.showMessageDialog(this, found.setQty(qty), "Warning", JOptionPane.WARNING_MESSAGE);
                } else {

                    int option = JOptionPane.showConfirmDialog(this, "Do you want to Update the Quantity of Product :" + proName, "Message",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

//                    JOptionPane.showMessageDialog(this, qty, "Warning", JOptionPane.WARNING_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {

//                        JOptionPane.showMessageDialog(this, found.getQty(), "Warning", JOptionPane.WARNING_MESSAGE);
                        found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));

                        found.setUnitDiscount(String.valueOf(Double.parseDouble(found.getUnitDiscount()) + Double.parseDouble(unitDiscount)));

//                    found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));
//                        JOptionPane.showMessageDialog(this, found.setQty(qty), "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
//

            }
            loadInvoice();
            roundedTextfield2.setEnabled(true);
//                invoiceItemMap.put(stockId, invoiceItem);
//            roundedTextfields1.setText("");
            reset();
//            int rowcount = jTable1.getRowCount();

//            if (!jFormattedTextField4.getText().isEmpty()) {
////                jComboBox1.setEnabled(true);
////                jFormattedTextField1.setEnabled(true);
////                jFormattedTextField2.setEnabled(true);
////                jCheckBox1.setEnabled(true);
////                jFormattedTextField1.setText("0");
//            }
        }


    }//GEN-LAST:event_roundedButton3ActionPerformed

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked

        String cashemail = jLabel21.getText();

        SalesHistory sales = new SalesHistory(cashemail);
        sales.setVisible(true);

    }//GEN-LAST:event_jButton6MouseClicked

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

        String total = jFormattedTextField4.getText();

        if (total.isEmpty()) {

        } else {
            calculate();
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jFormattedTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyReleased

        String total = jFormattedTextField4.getText();
        String discount = jFormattedTextField1.getText();

        if (total.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a Product", "Warning", JOptionPane.WARNING_MESSAGE);
            jFormattedTextField1.setText("0");
        } else if (!discount.matches("-?\\d*\\.?\\d+")) {
            JOptionPane.showMessageDialog(this, "Cannot use letter or symbols", "Warning", JOptionPane.WARNING_MESSAGE);
            jFormattedTextField1.setText("0");
        } else if (discount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Dicount Cannot be Empty", "Warning", JOptionPane.WARNING_MESSAGE);
            jFormattedTextField1.setText("0");
        } else if (Double.parseDouble(discount) < 0) {
            JOptionPane.showMessageDialog(this, "Dicount Cannot be in Minus", "Warning", JOptionPane.WARNING_MESSAGE);
            jFormattedTextField1.setText("0");
        } else {
            calculate();
        }
//        if (Double.parseDouble(discount) < 0 ) {
//            JOptionPane.showMessageDialog(this, "Dicount Cannot be in Minus", "Warning", JOptionPane.WARNING_MESSAGE);
//        }else

    }//GEN-LAST:event_jFormattedTextField1KeyReleased

    private void jFormattedTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField2KeyReleased

        String total = jFormattedTextField4.getText();
        String payment = jFormattedTextField2.getText();

        if (total.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a Product", "Warning", JOptionPane.WARNING_MESSAGE);
            jFormattedTextField2.setText("");
        } else if (!payment.matches("-?\\d*\\.?\\d+")) {
            JOptionPane.showMessageDialog(this, "Cannot use letter or symbols", "Warning", JOptionPane.WARNING_MESSAGE);
            jFormattedTextField2.setText("");
        } else if (payment.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Payment Cannot be Empty", "Warning", JOptionPane.WARNING_MESSAGE);
            jFormattedTextField2.setText("");
        } else if (payment.equals("-")) {
            JOptionPane.showMessageDialog(this, "Payment Cannot be in Minus", "Warning", JOptionPane.WARNING_MESSAGE);
            jFormattedTextField2.setText("");
        } else if (Double.parseDouble(payment) < 0) {
            JOptionPane.showMessageDialog(this, "Payment Cannot be in Minus", "Warning", JOptionPane.WARNING_MESSAGE);
            jFormattedTextField1.setText("");
        } else if (payment.equals("+")) {
            JOptionPane.showMessageDialog(this, "Cannot use symbol or letters", "Warning", JOptionPane.WARNING_MESSAGE);
            jFormattedTextField1.setText("0");
        } else if (payment.equals("/")) {
            JOptionPane.showMessageDialog(this, "Cannot use symbol or letters", "Warning", JOptionPane.WARNING_MESSAGE);
            jFormattedTextField1.setText("0");
        } else {
            calculate();
        }

    }//GEN-LAST:event_jFormattedTextField2KeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        String cashemail = jLabel21.getText();

        PendingDue dueamount = new PendingDue(cashemail);
        dueamount.setVisible(true);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void roundedButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton7ActionPerformed

        try {

            if (jFormattedTextField1.getText().isEmpty()) {
                jFormattedTextField1.setText("0");
            }

            if (jFormattedTextField2.getText().isEmpty()) {
                jFormattedTextField2.setText("0");
            }

            String cusmobile = roundedTextfield1.getText();
            String invoiceId = jLabel8.getText();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String total = jFormattedTextField4.getText();
            String discount = String.valueOf(jFormattedTextField1.getText());
            String payment = String.valueOf(jFormattedTextField2.getText());
            String balance = String.valueOf(jFormattedTextField3.getText());
            String empemail = jLabel21.getText();

//            String empid = "4752034190";
//        String payment = jFormattedTextField2.getText();
            paymentMethod = String.valueOf(jComboBox1.getSelectedItem());

            String paymentMethodID = paymentMethodMap.get(String.valueOf(jComboBox1.getSelectedItem()));

            if (cusmobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a Registered Customer", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (jTable1.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "There is no Product added", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (total.isEmpty()) {
                JOptionPane.showMessageDialog(this, "There is no Product added", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (paymentMethod.equals("Select")) {

                JOptionPane.showMessageDialog(this, "Please Select CASH as Payment Method", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (paymentMethod.equals("Cash")) {

//                JOptionPane.showMessageDialog(this, "Please Select CASH as Payment Method", "Warning", JOptionPane.WARNING_MESSAGE);
                ResultSet rs = MySQL.executeSearch("SELECT * FROM `employee` WHERE `email`='" + empemail + "'");

                String empId = null;

                if (rs.next()) {

//                    String empid = null;
                    empId = rs.getString("id");

//                    int = int(empid);
//                    JOptionPane.showMessageDialog(this, empId, "Warning", JOptionPane.WARNING_MESSAGE);
                }
//                int empid = 0;
//                try {
//                    empid = Integer.parseInt(empId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                JOptionPane.showMessageDialog(this, empId, "Warning", JOptionPane.WARNING_MESSAGE);

                //insert into invoice table as a due payment
                MySQL.executeIUD("INSERT INTO `invoice` (`id`,`date`,`paid_amount`,`payment_method_id`,`discount`,`employee_id`,`customer_mobile`,`due_amount`) VALUES('" + invoiceId + "','" + dateTime + "','" + payment + "','" + paymentMethodID + "',"
                        + "'" + discount + "','" + empId + "','" + cusmobile + "','" + balance + "')");

                for (InvoiceItems invoiceItem : InvoiceItemMap.values()) {

                    //insert to invoiceItem
                    MySQL.executeIUD("INSERT INTO `invoice_item`(`qty`,`invoice_id`,`stock_id`)"
                            + "VALUES('" + invoiceItem.getQty() + "','" + invoiceId + "','" + invoiceItem.getStockId() + "')");
                    //insert to invoiceItem

                    //stock update
                    MySQL.executeIUD("UPDATE `stock` SET `qty`=`qty`-'" + invoiceItem.getQty() + "' WHERE `id`='" + invoiceItem.getStockId() + "'");
                    //stock update
                }

                double updatePoints = Double.parseDouble(jFormattedTextField2.getText()) / 100;

                Math.round(updatePoints);

                //withdraw points
                if (withdrawPoints) {
                    points += updatePoints;
                    MySQL.executeIUD("UPDATE `customer` SET `points` = '" + updatePoints + "' WHERE `mobile` = '" + cusmobile + "'");
                } else {
                    MySQL.executeIUD("UPDATE `customer` SET `points` = `points` + '" + updatePoints + "' WHERE `mobile` = '" + cusmobile + "'");
                }

                JOptionPane.showMessageDialog(this, "Success", "Warning", JOptionPane.WARNING_MESSAGE);

                viewReport();
                refreshPOS();

            } else {
                JOptionPane.showMessageDialog(this, "Credit Cannot be accepted through card Payment", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_roundedButton7ActionPerformed

    private void roundedButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton4ActionPerformed

        try {

            if (jFormattedTextField1.getText().isEmpty()) {
                jFormattedTextField1.setText("0");
            }

            String total = jFormattedTextField4.getText();
            String discount = String.valueOf(jFormattedTextField1.getText());

            String cusmobile = roundedTextfield1.getText();
            String invoiceId = jLabel8.getText();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String payment = String.valueOf(jFormattedTextField2.getText());
            String balance = String.valueOf(jFormattedTextField3.getText());
            String empemail = jLabel21.getText();

//            String empid = "4752034190";
//        String payment = jFormattedTextField2.getText();
            paymentMethod = String.valueOf(jComboBox1.getSelectedItem());

            ResultSet rs = MySQL.executeSearch("SELECT * FROM `employee` WHERE `email`='" + empemail + "'");

            String paymentMethodID = paymentMethodMap.get(String.valueOf(jComboBox1.getSelectedItem()));

            if (cusmobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a Registered Customer", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (jTable1.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "There is no Product added", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (total.isEmpty()) {
                JOptionPane.showMessageDialog(this, "There is no Product added", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (paymentMethod.equals("Select")) {

                JOptionPane.showMessageDialog(this, "Please Select a Payment Method", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (payment.isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please Select CASH as Payment Method", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (Double.parseDouble(payment) < 0) {

                JOptionPane.showMessageDialog(this, "Payment cannot be in Minus", "Warning", JOptionPane.WARNING_MESSAGE);

            } else {

                if (Double.parseDouble(discount) >= Double.parseDouble(total)) {
                    jFormattedTextField2.setText("0");
                }
//                JOptionPane.showMessageDialog(this, "Please Select CASH as Payment Method", "Warning", JOptionPane.WARNING_MESSAGE);
//                String empName = null;
                String empId = null;

                if (rs.next()) {
//                    String empid = null;
                    empId = rs.getString("id");
//                    empName = rs.getString("fname" + "lname");
                }

                String query = "SELECT * FROM `invoice` WHERE `customer_mobile` = '" + cusmobile + "' AND `due_amount` != 0";

                ResultSet rs2 = MySQL.executeSearch(query);

                if (rs2.next()) {

                    if (rs2 != null) {
                        JOptionPane.showMessageDialog(this, "This customer has a due payment", "Warning", JOptionPane.WARNING_MESSAGE);
                    }

                }

//                JOptionPane.showMessageDialog(this, empId, "Warning", JOptionPane.WARNING_MESSAGE);
//                insert into invoice table as a due payment
                MySQL.executeIUD("INSERT INTO `invoice` (`id`,`date`,`paid_amount`,`payment_method_id`,`discount`,`employee_id`,`customer_mobile`,`due_amount`) "
                        + "VALUES('" + invoiceId + "','" + dateTime + "','" + payment + "','" + paymentMethodID + "',"
                        + "'" + discount + "','" + empId + "','" + cusmobile + "','0')");

                for (InvoiceItems invoiceItem : InvoiceItemMap.values()) {

                    //insert to invoiceItem
                    MySQL.executeIUD("INSERT INTO `invoice_item`(`qty`,`invoice_id`,`stock_id`)"
                            + "VALUES('" + invoiceItem.getQty() + "','" + invoiceId + "','" + invoiceItem.getStockId() + "')");
                    //insert to invoiceItem

                    //stock update
                    MySQL.executeIUD("UPDATE `stock` SET `qty`=`qty`-'" + invoiceItem.getQty() + "' WHERE `id`='" + invoiceItem.getStockId() + "'");
                    //stock update
                }

                double updatePoints = Double.parseDouble(jFormattedTextField2.getText()) / 100;

                Math.round(updatePoints);

                //withdraw points
                if (withdrawPoints) {
                    points += updatePoints;
                    MySQL.executeIUD("UPDATE `customer` SET `points` = '" + updatePoints + "' WHERE `mobile` = '" + cusmobile + "'");
                } else {
                    MySQL.executeIUD("UPDATE `customer` SET `points` = `points` + '" + updatePoints + "' WHERE `mobile` = '" + cusmobile + "'");
                }

                JOptionPane.showMessageDialog(this, "Success", "Warning", JOptionPane.WARNING_MESSAGE);

//                jComboBox1.setSelectedIndex(0);
//                String empName = null;
//
//                if (rs.next()) {
//                    empName = rs.getString("fname");
//                }
//                String emailToSend = "";
//                String customerQuery = "SELECT * FROM `customer` WHERE `mobile` = '" + cusmobile + "' AND `ebill_id` ='" + 2 + "'";
//                try {
//
//                    String path = "src//reports//SuperERPreceipt.jasper";
//
//                    HashMap<String, Object> params = new HashMap<>();
//                    params.put("Parameter1", jLabel8.getText());   //invoiceId
//                    params.put("Parameter2", empemail);
//                    params.put("Parameter3", total);
//                    params.put("Parameter4", discount);
//
//                    if (paymentMethod.equals("Cash")) {
//                        params.put("Parameter5", payment);
//                    } else {
//                        params.put("Parameter5", "0.00");
//                    }
//
//                    if (paymentMethod.equals("Card")) {
//                        params.put("Parameter6", payment);
//                    } else {
//                        params.put("Parameter6", "0.00");
//                    }
//
//                    params.put("Parameter7", balance);
//
//                    JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());
//
//                    JasperPrint jasperPrint = JasperFillManager.fillReport(path, params, dataSource);
//                    JasperViewer.viewReport(jasperPrint, false);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
                viewReport();
                refreshPOS();

            }

//             else {
//                JOptionPane.showMessageDialog(this, "Failed", "Warning", JOptionPane.WARNING_MESSAGE);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_roundedButton4ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged

        if (jLabel30.getText().equals("(......)")) {
            JOptionPane.showMessageDialog(this, "Select a Customer", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            if (jCheckBox1.isSelected()) {
                calculate();
            }
        }


    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void roundedTextfield2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roundedTextfield2KeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            String barcode = roundedTextfield2.getText();
//            String pqty = roundedTextfield4.getText();

            String pqty = "1";

            String dis = roundedTextfield5.getText();

            try {

                ResultSet rs = MySQL.executeSearch("SELECT * FROM `product` INNER JOIN `brand` ON product.brand_id = brand.id "
                        + "LEFT JOIN `stock` ON stock.product_id = product.id WHERE `stock`.`id`='" + barcode + "'");

                if (rs.next()) {
//                    String pid = rs.getString("product.id");
                    String pName = rs.getString("product.name");
//                    String mCat = rs.getString("main_category.cname");
//                    String sCat = rs.getString("sub_category.Cname");rs.ne
                    String brand = rs.getString("brand.name");
                    String mfd = rs.getString("stock.mfd");
                    String exp = rs.getString("stock.exp");
                    String sPrice = rs.getString("stock.price");
                    String totalqty = rs.getString("stock.qty");

//                    jLabel13.setText(pid);
                    jLabel9.setText(pName);
//                    jLabel22.setText(mCat);
//                    jLabel25.setText(sCat);
                    jLabel18.setText(brand);
                    jLabel20.setText(mfd);
                    jLabel19.setText(exp);
                    roundedTextfield3.setText(sPrice);
                    jLabel16.setText(totalqty);

                    InvoiceItems invoiceItem = new InvoiceItems();
                    invoiceItem.setExp(exp);
                    invoiceItem.setMfd(mfd);
                    invoiceItem.setProName(pName);
                    invoiceItem.setBrandName(brand);
                    invoiceItem.setQty(pqty);
                    invoiceItem.setUnitPrice(sPrice);
                    invoiceItem.setStockId(barcode);
                    invoiceItem.setUnitDiscount(dis);

                    if (InvoiceItemMap.get(barcode) == null) {
                        InvoiceItemMap.put(barcode, invoiceItem);
//                                loadInvoiceItem();

                    } else {

                        InvoiceItems found = InvoiceItemMap.get(barcode);
//
//                        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update the Quantity of Product :" + pName, "Message",
//                                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

//                    JOptionPane.showMessageDialog(this, qty, "Warning", JOptionPane.WARNING_MESSAGE);
//
                        if (Double.parseDouble(found.getQty()) < Double.parseDouble(totalqty)) {

//                        JOptionPane.showMessageDialog(this, found.getQty(), "Warning", JOptionPane.WARNING_MESSAGE);
                            found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(pqty)));

                            found.setUnitDiscount(String.valueOf(Double.parseDouble(found.getUnitDiscount()) + Double.parseDouble(dis)));

//                    found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));
//                        JOptionPane.showMessageDialog(this, found.setQty(qty), "Warning", JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Quantity exceeds Stock Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
                        }

                    }
                    loadInvoice();
//                invoiceItemMap.put(stockId, invoiceItem);
//            roundedTextfields1.setText("");
                    reset();
                    roundedTextfield2.grabFocus();

                    if (!jFormattedTextField4.getText().isEmpty()) {
                        jComboBox1.setEnabled(true);
                        jFormattedTextField1.setEnabled(true);
                        jFormattedTextField2.setEnabled(true);
                        jCheckBox1.setEnabled(true);
                        jFormattedTextField1.setText("0");
                    }
//            int rowcount = jTable1.getRowCount();

                } else {
//                    jLabel13.setText("0");
//                    jLabel23.setText("No Product Name Available");
                    JOptionPane.showMessageDialog(this, "No Product Available", "Warning", JOptionPane.WARNING_MESSAGE);
//                    jLabel22.setText("No Main Category");
//                    jLabel25.setText("No Subcategory");
//                    jLabel16.setText("No Brand");
//                    jLabel19.setText("No Color");
//                    jLabel20.setText("No Size");
//                    jFormattedTextField1.setText("0.00"); // Default to 0.00 for a price
//                    jLabel28.setText("Not Available");
                    roundedTextfield2.grabFocus();
                    roundedTextfield2.setText("");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }//GEN-LAST:event_roundedTextfield2KeyReleased

    private void roundedTextfield2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedTextfield2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roundedTextfield2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        String empId = jLabel21.getText();

        Returns returns = new Returns(empId);
        returns.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void roundedTextfield1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedTextfield1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roundedTextfield1ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed

        try {

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String dayOrders;

            //load Orders and Transactions
            ResultSet rs = MySQL.executeSearch("SELECT SUM(paid_amount) AS orderTotal, COUNT(id) AS orderCount FROM `invoice` WHERE `date`='" + date + "'");
            if (rs.next()) {

                dayOrders = rs.getString("orderCount");

                if (Integer.parseInt(dayOrders) == 0) {
                    JOptionPane.showMessageDialog(this, "No Orders have been processed today", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    CloseDay cd = new CloseDay();
                    cd.setVisible(true);
                }
//                jLabel4.setText(rs.getString("orderCount"));
//                jLabel5.setText(rs.getString("orderTotal"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed

        int option = JOptionPane.showConfirmDialog(this, "Opening the Drawer ?", "Message",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Drawer Opened", "Warning", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Drawer cannot be opened", "Warning", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_jButton12ActionPerformed

    private void roundedButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton1ActionPerformed

        StockProducts stock = new StockProducts();
        stock.setVisible(true);
        stock.setPos(this);

//        String qty = jLabel16.getText();
//        String barcode = roundedTextfield2.getText();
//
//        
//        
////        InvoiceItems invoiceItem = new InvoiceItems();
//
//        InvoiceItems found = InvoiceItemMap.get(barcode);
//
//        if (found != null) {
//            String newqty = String.valueOf(Double.parseDouble(qty) - Double.parseDouble(found.getQty()));
//
//            jLabel16.setText(newqty);
//        }
        roundedTextfield4.grabFocus();
        roundedTextfield2.setEnabled(false);

    }//GEN-LAST:event_roundedButton1ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        String cashEmail = jLabel21.getText();

        CashInOut cashinout = new CashInOut(this, true, cashEmail);
        cashinout.setVisible(true);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        CashierLockScreen cls = new CashierLockScreen();
        cls.setVisible(true);

        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        POSdashboard pos = new POSdashboard();
        pos.setVisible(true);

    }//GEN-LAST:event_jButton11ActionPerformed

    private void jFormattedTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField4KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField4KeyReleased

    private void roundedTextfield1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roundedTextfield1KeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            loadCustomer(roundedTextfield1.getText());
        }
    }//GEN-LAST:event_roundedTextfield1KeyPressed

    private void roundedTextfield1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roundedTextfield1KeyReleased
        loadCustomer(roundedTextfield1.getText());
    }//GEN-LAST:event_roundedTextfield1KeyReleased

    private void jFormattedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void roundedTextfield6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roundedTextfield6KeyReleased

        String name = roundedTextfield6.getText();

        try {

            if (name != null && name.trim().isEmpty()) {

                //                JOptionPane.showMessageDialog(this, "Not Empty", "Warning", JOptionPane.WARNING_MESSAGE);
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                loadInvoice();

            } else {

                String query = "SELECT product.id, product.name, brand.name AS brand_name, stock.id AS stock_id, "
                        + "stock.mfd, stock.exp, stock.price, stock.qty, "
                        + "offer.amount AS offer_amount "
                        + "FROM product "
                        + "INNER JOIN brand ON product.brand_id = brand.id "
                        + "LEFT JOIN stock ON stock.product_id = product.id "
                        + "LEFT JOIN offer ON offer.stock_id = stock.id ";

                if (name != null) {
                    query += " WHERE `product`.`name` LIKE '%" + name + "%'";
                }

                ResultSet resultSet = MySQL.executeSearch(query);

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);
                //jTable1.grabFocus();
                while (resultSet.next()) {

                    Vector<String> vector = new Vector<>();
                    vector.add(resultSet.getString("stock_id"));
                    vector.add(resultSet.getString("brand_name"));
                    vector.add(resultSet.getString("product.name"));
                    vector.add(resultSet.getString("stock.mfd"));
                    vector.add(resultSet.getString("stock.exp"));
                    String offerAmount = resultSet.getString("offer_amount");
                    if (offerAmount != null) {
                        vector.add(offerAmount);
                    } else {
                        vector.add(resultSet.getString("stock.price"));
                    }
                    vector.add(resultSet.getString("qty"));
                    vector.add("0");
                    if (offerAmount != null) {
                        vector.add("Offer");
                    } else {
                        vector.add("Normal");
                    }

//                    v.add(rs.getString("product.id"));
//                    v.add(rs.getString("product.name"));
//                    v.add(rs.getString("brand_name"));
//                    v.add(rs.getString("stock_id"));
//                    v.add(rs.getString("stock.mfd"));
//                    v.add(rs.getString("stock.exp"));

                    model.addRow(vector);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_roundedTextfield6KeyReleased

    private void roundedButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton5ActionPerformed

        jComboBox1.setSelectedIndex(0);
        jFormattedTextField1.setText("");
        jFormattedTextField2.setText("");
        jFormattedTextField3.setText("");
        jCheckBox1.setSelected(false);


    }//GEN-LAST:event_roundedButton5ActionPerformed

    private void roundedTextfield6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roundedTextfield6KeyPressed

        String searchBar = roundedTextfield6.getText();

        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {

            if (searchBar.isEmpty()) {

                JOptionPane.showMessageDialog(this, "Enter Product Name", "Warning", JOptionPane.WARNING_MESSAGE);

            } else {

                if (jTable1.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "No Product with this name", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    jTable1.grabFocus();
                    jTable1.setRowSelectionInterval(0, 0);
                }

            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {

            roundedTextfield6.grabFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String invoiceId = jLabel8.getText();
            String cusMobile = roundedTextfield1.getText();
            int totalProducts = jTable1.getRowCount();
            String totalAmount = jFormattedTextField4.getText();
            String empmail = jLabel21.getText();

            if (cusMobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Select a Customer", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (totalProducts == 0) {
                JOptionPane.showMessageDialog(this, "No Products Has been Added", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (totalAmount.equals("SubTotal")) {
                JOptionPane.showMessageDialog(this, "No Products Has been Added", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

//                refreshPOS();
            }
        }

    }//GEN-LAST:event_roundedTextfield6KeyPressed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            int row = jTable1.getSelectedRow();

            this.jLabel18.setText(String.valueOf(jTable1.getValueAt(row, 1)));
            this.roundedTextfield2.setText(String.valueOf(jTable1.getValueAt(row, 0)));
            this.jLabel9.setText(String.valueOf(jTable1.getValueAt(row, 2)));
            this.jLabel20.setText(String.valueOf(jTable1.getValueAt(row, 3)));
            this.jLabel19.setText(String.valueOf(jTable1.getValueAt(row, 4)));
            this.roundedTextfield3.setText(String.valueOf(jTable1.getValueAt(row, 5)));
            this.jLabel16.setText(String.valueOf(jTable1.getValueAt(row, 6)));

            roundedTextfield6.setText("");
            roundedTextfield4.grabFocus();

        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void roundedTextfield4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roundedTextfield4KeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            String stockId = roundedTextfield2.getText();
            String proName = jLabel9.getText();
            String unitPrice = roundedTextfield3.getText();
            String qty = roundedTextfield4.getText();
            String mfd = jLabel20.getText();
            String exp = jLabel19.getText();
            String brandName = jLabel18.getText();
            String unitDiscount = roundedTextfield5.getText();

            String totalqty = jLabel16.getText();

            if (stockId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a Product", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (qty.isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please Enter Quantity", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (!qty.matches("\\d+")) {

                JOptionPane.showMessageDialog(this, "Invalid Qty Amount", "Warning", JOptionPane.WARNING_MESSAGE);
                roundedTextfield4.setText("");
            } else if (Double.parseDouble(qty) < 0) {

                JOptionPane.showMessageDialog(this, "Quantity can't be Minus", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (qty.equals("0")) {

                JOptionPane.showMessageDialog(this, "Quantity can't be 0", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (Double.parseDouble(qty) > Double.parseDouble(totalqty)) {
                JOptionPane.showMessageDialog(this, "Quantity Exceeds the Stock Quantity", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (Double.parseDouble(qty) > Double.parseDouble(totalqty)) {

                JOptionPane.showMessageDialog(this, "Quantity Exceeds the Stock Quantity", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (!unitDiscount.matches("\\d+")) {

                JOptionPane.showMessageDialog(this, "Invalid Discount Amount", "Warning", JOptionPane.WARNING_MESSAGE);
                roundedTextfield5.setText("0");
            } else {

//            JOptionPane.showMessageDialog(this, "Success", "Warning", JOptionPane.WARNING_MESSAGE);
                InvoiceItems invoiceItem = new InvoiceItems();
                invoiceItem.setExp(exp);
                invoiceItem.setMfd(mfd);
                invoiceItem.setProName(proName);
                invoiceItem.setBrandName(brandName);
                invoiceItem.setQty(qty);
                invoiceItem.setUnitPrice(unitPrice);
                invoiceItem.setStockId(stockId);
                invoiceItem.setUnitDiscount(unitDiscount);

                if (InvoiceItemMap.get(stockId) == null) {
                    InvoiceItemMap.put(stockId, invoiceItem);
//                                loadInvoiceItem();

                } else {

                    InvoiceItems found = InvoiceItemMap.get(stockId);
//
                    Double newqty = Double.parseDouble(found.getQty()) + Double.parseDouble(qty);

//                        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update the Quantity of Product :" + pName, "Message",
//                                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
//                    JOptionPane.showMessageDialog(this, qty, "Warning", JOptionPane.WARNING_MESSAGE);
//
                    if (newqty > Double.parseDouble(totalqty)) {
                        JOptionPane.showMessageDialog(this, "Quantity exceeds Stock Quantity", "Warning", JOptionPane.WARNING_MESSAGE);
//                        JOptionPane.showMessageDialog(this, found.getQty(), "Warning", JOptionPane.WARNING_MESSAGE);
//                    found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(pqty)));
//                    found.setUnitDiscount(String.valueOf(Double.parseDouble(found.getUnitDiscount()) + Double.parseDouble(dis)));

//                    found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));
//                        JOptionPane.showMessageDialog(this, found.setQty(qty), "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {

                        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update the Quantity of Product :" + proName, "Message",
                                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

//                    JOptionPane.showMessageDialog(this, qty, "Warning", JOptionPane.WARNING_MESSAGE);
                        if (option == JOptionPane.YES_OPTION) {

//                        JOptionPane.showMessageDialog(this, found.getQty(), "Warning", JOptionPane.WARNING_MESSAGE);
                            found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));

                            found.setUnitDiscount(String.valueOf(Double.parseDouble(found.getUnitDiscount()) + Double.parseDouble(unitDiscount)));

//                    found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));
//                        JOptionPane.showMessageDialog(this, found.setQty(qty), "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }
//

                }
                loadInvoice();
                roundedTextfield2.setEnabled(true);
//                invoiceItemMap.put(stockId, invoiceItem);
//            roundedTextfields1.setText("");
                reset();
//            int rowcount = jTable1.getRowCount();

//            if (!jFormattedTextField4.getText().isEmpty()) {
////                jComboBox1.setEnabled(true);
////                jFormattedTextField1.setEnabled(true);
////                jFormattedTextField2.setEnabled(true);
////                jCheckBox1.setEnabled(true);
////                jFormattedTextField1.setText("0");
//            }
            }

        }

    }//GEN-LAST:event_roundedTextfield4KeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacLightLaf.setup();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new POSdashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel date;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private Components.RoundedButton roundedButton1;
    private Components.RoundedButton roundedButton2;
    private Components.RoundedButton roundedButton3;
    private Components.RoundedButton roundedButton4;
    private Components.RoundedButton roundedButton5;
    private Components.RoundedButton roundedButton6;
    private Components.RoundedButton roundedButton7;
    private Components.RoundedPanel roundedPanel1;
    private Components.RoundedPanel roundedPanel2;
    private Components.RoundedPanel roundedPanel3;
    private Components.RoundedPanel roundedPanel4;
    private Components.RoundedPanel roundedPanel5;
    private Components.RoundedPanel roundedPanel6;
    private Components.RoundedTextfield roundedTextfield1;
    private Components.RoundedTextfield roundedTextfield2;
    private Components.RoundedTextfield roundedTextfield3;
    private Components.RoundedTextfield roundedTextfield4;
    private Components.RoundedTextfield roundedTextfield5;
    private Components.RoundedTextfield roundedTextfield6;
    // End of variables declaration//GEN-END:variables

    private void reset() {

        jLabel9.setText("Product Name Here");
        roundedTextfield6.setText("");
        roundedTextfield2.setText("");
        roundedTextfield3.setText("");
        roundedTextfield4.setText("");
        jLabel16.setText("0");
        jLabel20.setText("");
        jLabel19.setText("");
        jLabel18.setText("");
        roundedTextfield5.setText("0");
        roundedButton1.grabFocus();

    }

    private void refreshPOS() {

        generateInvoiceId();
        jLabel5.setText("Add");
        roundedTextfield1.setText("");

        InvoiceItemMap.clear();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        //Checkout UI
        jFormattedTextField4.setText("");
        jComboBox1.setSelectedIndex(0);
        jFormattedTextField1.setText("0");
        jFormattedTextField2.setText("");
        jCheckBox1.setSelected(false);
        jLabel30.setText("(......)");
        jFormattedTextField3.setText("");
        reset();
        roundedButton4.setEnabled(true);
        roundedButton7.setEnabled(true);

//        jComboBox1.setEnabled(false);
//        jFormattedTextField1.setEnabled(false);
//        jFormattedTextField2.setEnabled(false);
//        jCheckBox1.setEnabled(false);
    }

//    private void holdreset() {
//        
//        InvoiceItemMap.clear();
//        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
//        model.setRowCount(0);
//        
//        generateInvoiceId();
//    }
//    private void posholdOrder(){
//        int row = jTable1.getSelectedRow();
//
//        if (row == -1) {
//            JOptionPane.showMessageDialog(this, "Please Select a Row", "Warning", JOptionPane.WARNING_MESSAGE);
//        } else {
//            String cusName = jLabel5.getText();
//            String cusMobile = roundedTextfield1.getText();
//
//            String stockId = String.valueOf(jTable1.getValueAt(row, 0));
//            String brandName = String.valueOf(jTable1.getValueAt(row, 0));
//            String proName = String.valueOf(jTable1.getValueAt(row, 0));
//            String mfd = String.valueOf(jTable1.getValueAt(row, 0));
//            String exp = String.valueOf(jTable1.getValueAt(row, 0));
//            String unitPrice = String.valueOf(jTable1.getValueAt(row, 0));
//            String qty = String.valueOf(jTable1.getValueAt(row, 0));
//            String unitDiscount = String.valueOf(jTable1.getValueAt(row, 0));
//            String holdtotal = String.valueOf(jTable1.getValueAt(row, 0));
//
//            if (cusName.equals("Add")) {
//                JOptionPane.showMessageDialog(this, "Add a Customer", "Warning", JOptionPane.WARNING_MESSAGE);
//            } else if (cusName.equals("Walking")) {
//                JOptionPane.showMessageDialog(this, "Not Available for Walking Customers", "Warning", JOptionPane.WARNING_MESSAGE);
//            } else if (cusMobile.equals("0000000000")) {
//                JOptionPane.showMessageDialog(this, "Not Available for Walking Customers", "Warning", JOptionPane.WARNING_MESSAGE);
//            } else if (jTable1.getRowCount() == 0) {
//                JOptionPane.showMessageDialog(this, "No Products Has been Added to the list", "Warning", JOptionPane.WARNING_MESSAGE);
//            } else {
//
//                HoldItems holdItems = new HoldItems();
//                holdItems.setCusMobile(cusMobile);
//                holdItems.setCusMobile(cusMobile);
//                holdItems.setStockId(stockId);
//                holdItems.setBrandName(brandName);
//                holdItems.setProName(proName);
//                holdItems.setMfd(mfd);
//                holdItems.setExp(exp);
//                holdItems.setUnitPrice(unitPrice);
//                holdItems.setQty(qty);
//                holdItems.setUnitDiscount(unitDiscount);
//                holdItems.setTotal(holdtotal);
//
////            InvoiceItemMap
//                if (HoldItemMap.get(cusMobile) == null) {
//                    HoldItemMap.put(cusMobile, holdItems);
//
//                    JOptionPane.showMessageDialog(this, "Success", "Warning", JOptionPane.WARNING_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(this, "Failed", "Warning", JOptionPane.WARNING_MESSAGE);
//
//                }
//
////                refreshPOS();
//            }
//        }
//    }
}
