/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.sql.ResultSet;
import com.placeholder.PlaceHolder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
//import jdk.internal.net.http.common.Utils;
import model.NhanVien;
import view.BaseFrame;
import java.sql.Statement;

/**
 *
 * @author Phuong Lan, KhaiTruong_18520878
 */
public class insertNhanVienJDialog extends javax.swing.JDialog {

    PlaceHolder p1, p2, p3, p4, p5;
    private String gioiTinh="Nam";
    private String chucDanh="Nhan Vien";

    public int inputNhanVien(){
//        int maNV=Integer.parseInt(txtMaNV.getText());
        try {
            String tenNV    = txtTenNV.getText();
            String diaChi   = txtDiaChi.getText();
            long luong      = Long.parseLong(this.txtLuong.getText());
            String soDT     = txtSDT.getText();
            String maPhong  = txtMaPhong.getText();
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String ngaySinh = dateFormat.format(txtNgaySinh.getDate());
            
            if(tenNV.equals("")==true || ngaySinh.equals("")==true || diaChi.equals("")==true  || soDT.equals("")==true || chucDanh.equals("")==true || maPhong.equals("")==true){
                JOptionPane.showMessageDialog(this, "Nhập thiếu giá trị");
                return 0;
            }
            else{
                home.listNV.add(new NhanVien(0, tenNV, ngaySinh, diaChi, gioiTinh, soDT, chucDanh, maPhong, luong));
            }
        } 
        catch (NumberFormatException e  ) {
           JOptionPane.showMessageDialog(this, "Nhập giá trị \"Luong\" là dạng số, không phải dạng ký tự.\n\n             VUI LÒNG NHẬP LẠI!");
           txtLuong.setText("");
           return 0;
        }
        catch(NullPointerException e){
           JOptionPane.showMessageDialog(this, "Nhập sai định dạng giá trị Ngày sinh");
           return 0;
        }
        return 1;
    }
    
    Date today=new Date();
    public void clearField(){
        //txtMaNV.setText("");
        txtTenNV.setText("");
        txtNgaySinh.setDate(today);
        txtDiaChi.setText("");
        txtLuong.setText("");
        //txtGioiTinh.setText("");
        txtSDT.setText("");
        //txtChucDanh.setText("");
        txtMaPhong.setText("");
    }
    
    private Connection conn;
    public void connect(){
        try {
            String url="jdbc:oracle:thin:@localhost:1521:orcl";
            conn=DriverManager.getConnection(url,"system","user_java123");
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showNV(){
        try {
            //ArrayList<NhanVien> list= home.nhanVienList();
            Male.setSelected(true);
            rbtNhanVien.setSelected(true);
            txtNgaySinh.setDate(today);
            
            int maNV=0;
            connect();
            String query = "SELECT MANV FROM SYS.QLPK_NHANVIEN";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                maNV=rs.getInt(1);
            }
            conn.close();
            
            p1=new PlaceHolder(txtTenNV, "Nguyễn Văn A");
            p2=new PlaceHolder(txtDiaChi, "Khu phố 6, phường Linh Trung, quận Thủ Đức");
            p3=new PlaceHolder(txtSDT, "0903144655");
            p4=new PlaceHolder(txtMaPhong, "1");
            p5=new PlaceHolder(txtLuong, "1000000");
        } catch (SQLException ex) {
            Logger.getLogger(insertNhanVienJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Creates new form insertNhanVienJDialog
     */
    private BaseFrame home;
    public insertNhanVienJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        home=(BaseFrame) parent;
        // txtMaNV.setEditable(false);
        showNV();
    }

 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtTenNV = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtMaPhong = new javax.swing.JTextField();
        txtLuong = new javax.swing.JTextField();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        Male = new javax.swing.JRadioButton();
        Female = new javax.swing.JRadioButton();
        btThemNV = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        rbtNhanVien = new javax.swing.JRadioButton();
        rbtQuanLy = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setForeground(new java.awt.Color(244, 162, 97));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Họ và tên");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Ngày sinh");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Địa chỉ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Giới tính");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Số điện thoại");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Chức danh");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Mã phòng");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Lương");

        txtTenNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTenNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenNVActionPerformed(evt);
            }
        });

        txtDiaChi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtSDT.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtMaPhong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtLuong.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtNgaySinh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        Male.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Male.setText("Nam");
        Male.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MaleActionPerformed(evt);
            }
        });

        Female.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Female.setText("Nữ");
        Female.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FemaleActionPerformed(evt);
            }
        });

        btThemNV.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btThemNV.setText("THÊM");
        btThemNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemNVActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton4.setText("THOÁT");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        rbtNhanVien.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rbtNhanVien.setText("Nhân viên");
        rbtNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtNhanVienActionPerformed(evt);
            }
        });

        rbtQuanLy.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rbtQuanLy.setText("Quản lý");
        rbtQuanLy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtQuanLyActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(69, 123, 179));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NHẬP THÔNG TIN NHÂN VIÊN");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rbtNhanVien)
                                .addGap(18, 18, 18)
                                .addComponent(rbtQuanLy)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(99, 99, 99)
                                .addComponent(btThemNV)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                    .addComponent(txtMaPhong)
                                    .addComponent(txtSDT))
                                .addGap(108, 108, 108))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTenNV)
                                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(Male)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Female))
                                    .addComponent(txtDiaChi, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel10, jLabel11, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(Male)
                    .addComponent(Female))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(rbtNhanVien)
                    .addComponent(rbtQuanLy))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btThemNV, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btThemNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemNVActionPerformed
        // TODO add your handling code here:
        int response=JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn thêm nhân viên này không ?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(response==JOptionPane.YES_OPTION){
            if(inputNhanVien()==1){
            if(home.addNhanVien()!=0)
                this.dispose();
            }
        }
    }//GEN-LAST:event_btThemNVActionPerformed

    private void txtTenNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenNVActionPerformed

    private void MaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MaleActionPerformed
        // TODO add your handling code here:
        if(Male.isSelected()){
            Female.setSelected(false);
            gioiTinh="Nam";
        }
    }//GEN-LAST:event_MaleActionPerformed

    private void FemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FemaleActionPerformed
        // TODO add your handling code here:
        if(Female.isSelected()){
            Male.setSelected(false);
            gioiTinh="Nữ";
        }
    }//GEN-LAST:event_FemaleActionPerformed

    private void rbtNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtNhanVienActionPerformed
        // TODO add your handling code here:
        if(rbtNhanVien.isSelected()){
            rbtQuanLy.setSelected(false);
            chucDanh="Nhan Vien";
        }
    }//GEN-LAST:event_rbtNhanVienActionPerformed

    private void rbtQuanLyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtQuanLyActionPerformed
        // TODO add your handling code here:
        if(rbtQuanLy.isSelected()){
            rbtNhanVien.setSelected(false);
            chucDanh="Quan Ly";
        }
    }//GEN-LAST:event_rbtQuanLyActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(insertNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(insertNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(insertNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(insertNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                insertNhanVienJDialog dialog = new insertNhanVienJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JRadioButton Female;
    private javax.swing.JRadioButton Male;
    private javax.swing.JButton btThemNV;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton rbtNhanVien;
    private javax.swing.JRadioButton rbtQuanLy;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMaPhong;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenNV;
    // End of variables declaration//GEN-END:variables
}
