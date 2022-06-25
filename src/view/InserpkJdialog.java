package view;

import com.fasterxml.jackson.databind.type.MapLikeType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.BenhNhan;
import model.PhieuKham;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Phuong Lan, An Phan
 */
public class InserpkJdialog extends javax.swing.JDialog {
    private BaseFrame home;
    private int maPK = 0;
    private Connection conn;
    
    public InserpkJdialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        home = (BaseFrame) parent;
        this.setTitle("Nhập thông tin phiếu khám");
        this.setLocationRelativeTo(null);
        txtMaBN.setEditable(false);
        txtMaBN.setText(home.getMaBN()+"");
    }

    public void connect(){
        try {
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            conn = DriverManager.getConnection(url, "system", "user_java123");
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtMaBN = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtLiDoKham = new javax.swing.JTextField();
        btThem = new javax.swing.JButton();
        btThoat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        jPanel1.setBackground(new java.awt.Color(69, 123, 179));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("THÔNG TIN PHIẾU KHÁM");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Mã bệnh nhân");

        txtMaBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Lí do khám");

        txtLiDoKham.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btThem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btThem.setText("Thêm");
        btThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemActionPerformed(evt);
            }
        });

        btThoat.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btThoat.setText("Thoát");
        btThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThoatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLiDoKham))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btThem)
                        .addGap(18, 18, 18)
                        .addComponent(btThoat)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtLiDoKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btThem)
                    .addComponent(btThoat))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThoatActionPerformed
        this.dispose();
    }//GEN-LAST:event_btThoatActionPerformed

    private void btThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemActionPerformed
        String liDoKham = txtLiDoKham.getText();
        if(txtMaBN.getText().equals("") || txtLiDoKham.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Bạn hãy điền đầy đủ thông tin");
        }
        else{
            try {
                java.util.Date today = new java.util.Date();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String ngayKham = dateFormat.format(today);
                java.util.Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(ngayKham);
                java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
                
                connect();
                try {
                    conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                } catch (SQLException ex) {
                    Logger.getLogger(InserpkJdialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    String insert = "insert into SYS.QLPK_HOADON(MaBN, MaNV, LiDoKham, NgayKham) values(?,?,?,?)";
                    PreparedStatement st = conn.prepareStatement(insert);
                    st.setInt(1, home.getMaBN());
                    st.setInt(2, 1);
                    st.setString(3, liDoKham);
                    st.setDate(4, sqlDate);
                    
                    int x = st.executeUpdate();
                    if(x > 0){
                        JOptionPane.showMessageDialog(this, "Thêm phiếu khám thành công ");
                    }else{
                        JOptionPane.showMessageDialog(this, "Không thêm được vào dữ liệu");
                    }
                    
                String tenBN = "";
                String sql = "Select HOTEN From SYS.QLPK_BENHNHAN Where MABN="+txtMaBN.getText();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                while(rs.next()){
                    tenBN = rs.getString(1);
                }
                
                String maPK = "";
                sql = "SELECT MAX(MaHD) FROM SYS.QLPK_HOADON";
                Statement stm2 = conn.createStatement();
                ResultSet rs2 = stm2.executeQuery(sql);
                while(rs2.next()){
                    maPK = rs2.getString(1);
                }
                
                try{
                    Map<String, Object> parameters = new HashMap<String, Object>();
                    JasperDesign jdesign = JRXmlLoader.load("C:\\Users\\T470\\Documents\\Java\\DoAn\\Java_IS216.M21_21\\src\\report\\report2.jrxml");
                    parameters.put("maPK", maPK);
                    parameters.put("maBN", txtMaBN.getText());
                    parameters.put("LiDo", txtLiDoKham.getText());
                    parameters.put("tenBN", tenBN);

                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, parameters, conn);
                    JasperViewer.viewReport(jprint, false);
                    conn.close();
                } catch (Exception ex) {
                    Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                }catch (SQLException ex) {
                    Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }catch (ParseException ex) {
                Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.dispose();
        }  
    }//GEN-LAST:event_btThemActionPerformed

    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InserpkJdialog dialog = new InserpkJdialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btThem;
    private javax.swing.JButton btThoat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtLiDoKham;
    private javax.swing.JTextField txtMaBN;
    // End of variables declaration//GEN-END:variables
}
