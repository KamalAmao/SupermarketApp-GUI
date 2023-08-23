package supermarket;


import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.sql.DriverManager;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */

/**
 *
 * @author Thatblackbwoy
 */
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SalesPage extends javax.swing.JInternalFrame {
    PreparedStatement pst;
    Connection con;
    ResultSet rs;
    Statement stm;
    
    ImageIcon imgIcon = null;
    private JTextArea textArea;

    /**
     * Creates new form SalesPage
     */
    public SalesPage() {
        initComponents();
        loadCustomer();
        loadProduct();
        //loadPrice();
    }
    
    static class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable jTable1, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int colomn) {
            if(value instanceof String) {
                String imageUrl = (String) value;
                ImageIcon imageIcon = loadImageIcon(imageUrl);
                setIcon(imageIcon);
                setText("");
            } else {
                setIcon(null);
                setText("");
            }
            return this;
        }

        private ImageIcon loadImageIcon(String imagePath) {
            try {
               File file = new File(imagePath);
                Image image = new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
   
     public void loadDrive(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public void connect(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void loadCustomer(){
        //String customer = cus_cmb.getSelectedItem().toString();
        try{
            connect();
            stm = con.createStatement();
           rs= stm.executeQuery("SELECT * FROM CUSTOMERS");
           //Vector vector = new Vector();
           while(rs.next()){
               //vector.add(rs.getString("customer_name"));
               //DefaultComboBoxModel combox =  new DefaultComboBoxModel(vector);
               //cus_cmb.setModel(combox);
               DefaultComboBoxModel combox =  (DefaultComboBoxModel) cus_cmb.getModel();
               cus_cmb.addItem(rs.getString("customer_name"));
           }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     public void loadProduct(){
        //String customer = cus_cmb.getSelectedItem().toString();
        try{
            connect();
            stm = con.createStatement();
           rs= stm.executeQuery("SELECT * FROM PRODUCTS");
           Vector vector = new Vector();
           while(rs.next()){
//               List<Customers> customers = new ArrayList<>();
//               vector.add(rs.getString("p_name"));
//               DefaultComboBoxModel combox = new DefaultComboBoxModel(vector);
//               prod_cmb.setModel(combox);
               DefaultComboBoxModel combox =  (DefaultComboBoxModel) prod_cmb.getModel();
               prod_cmb.addItem(rs.getString("p_name"));
           }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void calculateTotal(){
           Double qty =Double.valueOf(qtyTxt.getText());
           Double price =Double.valueOf(unitPrice.getText());
           
           Double total = qty * price;
           totalPrice.setText(String.valueOf(total));
    }
    
    public void calSubTotal(){
        //int row = jTable1.getRowCount();
        double sum = 0;
        for(int i = 0; i< jTable1.getRowCount(); i++){
            sum = sum + Double.valueOf(jTable1.getValueAt(i, 3).toString());
        }
         totalAmount.setText(Double.toString(sum));
    }
    
    public void addToSales(){
        String cus_name = cus_cmb.getSelectedItem().toString();
        //String subTotal = totalAmount.getText();
        try{
             connect();
            for(int i = 0; i < jTable1.getRowCount(); i++){
                String item = String.valueOf(jTable1.getValueAt(i, 0));
                String qty = String.valueOf(jTable1.getValueAt(i, 1));
                String unit_price = String.valueOf(jTable1.getValueAt(i, 2));
                String total_price = String.valueOf(jTable1.getValueAt(i, 3));
                
                pst = con.prepareStatement("INSERT INTO SALES VALUES (?,?,?,?,?,?)");
                pst.setInt(1, 0);
                pst.setString(2, cus_name);
                pst.setString(3, item);
                pst.setString(4, qty);
                pst.setString(5, unit_price);
                pst.setString(6, total_price);
                //pst.setString(7, subTotal);
                int ok = pst.executeUpdate();
                if(ok > 0){
                JOptionPane.showMessageDialog(this, "record added");
                }else{
                }
//                con.close();
            }
            
           
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void receipt(){
        String cus = cus_cmb.getItemAt(WIDTH).toString();
        String subTotal = totalAmount.getText();
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        textArea.setText(textArea.getText() + "\t" + "INVOICE \n");
        textArea.setText(textArea.getText() + "============================== \n");
        textArea.setText(textArea.getText() + "\n");
        textArea.setText(textArea.getText() + "Name "+ "\t" + "cus" + "\n");
        textArea.setText(textArea.getText() + "\n");
        Date date = new Date();
        String thisDate = date.toString();
        textArea.setText(textArea.getText() + "\n" +thisDate+ "\n");
        textArea.setText(textArea.getText() + "\n");
        textArea.setText(textArea.getText() + "item" + "\t" + "qty" + " unit price" + "/t" + "total price" + "\n");
        textArea.setText(textArea.getText() + "\n");
        for(int i = 0; i < jTable1.getRowCount(); i++){
            
            String item = jTable1.getValueAt(i,0).toString();
            String qty = jTable1.getValueAt(i,1).toString();
            String unitPrice = jTable1.getValueAt(i,2).toString();
            String totalPrice = jTable1.getValueAt(i,3).toString();
            textArea.setText(textArea.getText() + item + "\t" + qty + "\t" + unitPrice + "\t" + totalPrice + "\t");
        }
        textArea.setText(textArea.getText() + "\n");
        textArea.setText(textArea.getText() + "\t" + subTotal + "/n");
        textArea.setText(textArea.getText() + "\n");
        textArea.setText(textArea.getText() + "============================== \n");
        textArea.setText(textArea.getText() + "\t" + "THANKS COME AGAIN" + "\n");
    }
    
//    public void loadImage(){
//        try{
//            byte[] imageData = rs.getBytes("imagefile");
//            imgIcon = new ImageIcon(imageData);
//            Image img = imgIcon.getImage();
//            img = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
//            prod_image.setIcon(new ImageIcon(img));
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
    
//     public void loadPrice(){
//        String prodName = prod_cmb.getSelectedItem().toString();
//        try{
//            connect();
//            stm = con.createStatement();
//           rs= stm.executeQuery("SELECT (price) FROM PRODUCTS WHERE p_name = '"+prodName+"'");
//           Vector vector = new Vector();
//           while(rs.next()){
//               unitPrice.setText(rs.getString("price"));
//           }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//      public void totalPrice(){
//        String prodName = prod_cmb.getSelectedItem().toString();
//        try{
//            connect();
//            stm = con.createStatement();
//           rs= stm.executeQuery("SELECT (price) FROM PRODUCTS WHERE p_name = '"+prodName+"'");
//           Vector vector = new Vector();
//           while(rs.next()){
//               unitPrice.setText(rs.getString("price"));
//           }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cus_cmb = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        prod_cmb = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        qtyTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        unitPrice = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        totalPrice = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        totalAmount = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        prod_image = new javax.swing.JLabel();

        jLabel7.setText("jLabel7");

        setPreferredSize(new java.awt.Dimension(812, 421));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Remove");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTable1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Quantity", "Unit Price", "Total", "Image"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jScrollPane2.setViewportView(jScrollPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Customer");

        cus_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        cus_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cus_cmbActionPerformed(evt);
            }
        });

        jLabel2.setText("Product");

        prod_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        prod_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prod_cmbActionPerformed(evt);
            }
        });

        jLabel3.setText("Qty");

        qtyTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtyTxtActionPerformed(evt);
            }
        });
        qtyTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                qtyTxtKeyReleased(evt);
            }
        });

        jLabel4.setText("Unit Price");

        unitPrice.setEditable(false);
        unitPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitPriceActionPerformed(evt);
            }
        });

        jLabel5.setText("Total Price");

        totalPrice.setEditable(false);
        totalPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalPriceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cus_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prod_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(qtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(unitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(totalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cus_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(prod_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(qtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(unitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(totalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setText("Total Amount");

        totalAmount.setEditable(false);

        jButton4.setText("Print");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        prod_image.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addComponent(prod_image, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(230, 230, 230)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addGap(131, 131, 131))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(totalAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(prod_image, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        try{
            int selectedRow = jTable1.getSelectedRow();
            tableModel.removeRow(selectedRow);
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void qtyTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtyTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qtyTxtActionPerformed

    private void totalPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalPriceActionPerformed

    private void unitPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitPriceActionPerformed

    private void cus_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cus_cmbActionPerformed
   
        //String customer = cus_cmb.getSelectedItem().toString();
//        try{
//            connect();
//            stm = con.createStatement();
//           rs= stm.executeQuery("SELECT * FROM CUSTOMERS");
//           Vector vector = new Vector();
//           while(rs.next()){
//               vector.add(rs.getString("customer_name"));
//               DefaultComboBoxModel combox = new DefaultComboBoxModel(vector);
//               cus_cmb.setModel(combox);
//           }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    
    }//GEN-LAST:event_cus_cmbActionPerformed

    private void prod_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prod_cmbActionPerformed
        //loadPrice();
        String prodName = prod_cmb.getSelectedItem().toString();
        try{
            connect();
            stm = con.createStatement();
           //rs= stm.executeQuery("SELECT barcode, price, image FROM PRODUCTS WHERE p_name = '"+prodName+"'");
           rs= stm.executeQuery("SELECT price, imagefile FROM PRODUCTS WHERE p_name = '"+prodName+"'");
           
           Vector vector = new Vector();
           while(rs.next()){
               unitPrice.setText(rs.getString("price"));
               //loadImage();
           }
           
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }//GEN-LAST:event_prod_cmbActionPerformed

    private void qtyTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtyTxtKeyReleased
        calculateTotal();
        //totalPrice.setText("");
    }//GEN-LAST:event_qtyTxtKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       addToSales();
       
//        InvoicePage invoice = new InvoicePage();
//        invoice.setVisible(true);
//        jDesktopPane.removeAll();
//        Invoice invoice = new Invoice();
//        jDesktopPane.add(invoive).setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      String itemName = prod_cmb.getSelectedItem().toString();
      String qty = qtyTxt.getText();
      String unitprice = unitPrice.getText();
      String total = totalPrice.getText();
        try{
          connect(); 
          stm = con.createStatement();
          rs = stm.executeQuery("SELECT image FROM PRODUCTS WHERE p_name = '"+itemName+"' "); 
          while(rs.next()){
              String image = rs.getString("image");
              DefaultTableModel tableModel = (DefaultTableModel)jTable1.getModel();
              tableModel.addRow(new Object [] {itemName, qty, unitprice, total, image});
              jTable1.getColumnModel().getColumn(4).setCellRenderer(new ImageRenderer());
          }
//          String itemName = prod_cmb.getSelectedItem().toString();
//          String qty = qtyTxt.getText();
//          String unitprice = unitPrice.getText();
//          String total = totalPrice.getText();

      }catch(Exception e){
          e.printStackTrace();
      }
//       Vector vector = new Vector();
//       vector.add(itemName);
//       vector.add(qty);
//       vector.add(unitprice);
//       vector.add(total);
//       tableModel.addRow(vector);
       calSubTotal();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        try{
            tableModel.setRowCount(0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cus_cmb;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> prod_cmb;
    private javax.swing.JLabel prod_image;
    private javax.swing.JTextField qtyTxt;
    private javax.swing.JTextField totalAmount;
    private javax.swing.JTextField totalPrice;
    private javax.swing.JTextField unitPrice;
    // End of variables declaration//GEN-END:variables
}
