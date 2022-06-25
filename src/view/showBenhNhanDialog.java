/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.BenhNhan;

/**
 *
 * @author Phuong Lan, An Phan
 */
public class showBenhNhanDialog extends javax.swing.JDialog {
    private String gioiTinh;
    /**
     * Creates new form insertBNDialog
     */
    public void showBenhNhanInfo(BenhNhan bn){
        try{
            txtMaBN.setText(bn.getMaBN()+"");
            txtHoTen.setText(bn.getHoTen());
            //txtNgaySinh.setText(nv.getNgSinh());
            String date=bn.getNgaySinh();
            Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            txtNgaySinh.setDate(date2);
            if(bn.getGioiTinh().equalsIgnoreCase("Nam")){
                Male.setSelected(true);
                Female.setSelected(false);
                gioiTinh="Nam";
            }
            else{
                Male.setSelected(false);
                Female.setSelected(true);
                gioiTinh="Nữ";
            }
            txtDiaChi.setText(bn.getDiaChi());
            
            txtSDT.setText(bn.getSDT());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(rootPane, "Lỗi hiện thông tin");
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public int updateBenhNhan(int location){
        // Kiểm tra thông tin có đúng chuẩn ko
        
        try {
            String tenBN    = txtHoTen.getText();
            String diaChi   = txtDiaChi.getText();
            String soDT     = txtSDT.getText();
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String ngaySinh = dateFormat.format(txtNgaySinh.getDate());
            
            if(tenBN.equals("")==true || ngaySinh.equals("")==true || diaChi.equals("")==true  || soDT.equals("")==true){
                JOptionPane.showMessageDialog(this, "Nhập thiếu giá trị");
                return 0;
            }
            else{
                //home.listNV.add(new NhanVien(maNV, tenNV, ngaySinh, diaChi, gioiTinh, soDT, chucDanh, maPhong, luong));
                home.listBN.get(location).setHoTen(tenBN);
                home.listBN.get(location).setNgaySinh(ngaySinh);
                home.listBN.get(location).setGioiTinh(gioiTinh);
                home.listBN.get(location).setDiaChi(diaChi);
                home.listBN.get(location).setSDT(soDT);
            }
        }
        catch(NullPointerException e){
           JOptionPane.showMessageDialog(this, "Nhập sai định dạng giá trị Ngày sinh");
           return 0;
       }
       return 1;
    }
    
    private BaseFrame home;
    public showBenhNhanDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        home=(BaseFrame) parent;
        txtMaBN.setEditable(false);
        this.setTitle("Thông tin bệnh nhân");
        BenhNhan bn=home.getBenhNhan();
        showBenhNhanInfo(bn);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtMaBN = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        btThem = new javax.swing.JButton();
        btThoat = new javax.swing.JButton();
        btXoa = new javax.swing.JButton();
        Male = new javax.swing.JRadioButton();
        Female = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(69, 123, 179));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("THÔNG TIN BỆNH NHÂN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel1)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Mã bệnh nhân");

        txtMaBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Họ và tên");

        txtHoTen.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Giới tính");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Địa chỉ");

        txtDiaChi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel32.setText("Số ĐT");

        txtSDT.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Ngày sinh");

        txtNgaySinh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btThem.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btThem.setText("CẬP NHẬT");
        btThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemActionPerformed(evt);
            }
        });

        btThoat.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btThoat.setText("THOÁT");
        btThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThoatActionPerformed(evt);
            }
        });

        btXoa.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btXoa.setText("XÓA");
        btXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btXoaActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btThem)
                        .addGap(18, 18, 18)
                        .addComponent(btXoa)
                        .addGap(18, 18, 18)
                        .addComponent(btThoat))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtDiaChi)
                                .addComponent(txtMaBN)
                                .addComponent(txtSDT)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(Male)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(Female))
                                        .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(txtHoTen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Male)
                        .addComponent(Female))
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btXoa)
                    .addComponent(btThoat)
                    .addComponent(btThem, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btXoaActionPerformed
        // TODO add your handling code here:
        int mabn=Integer.parseInt(txtMaBN.getText());
        if(home.deleteBenhNhan(mabn)==1){
            JOptionPane.showMessageDialog(rootPane, "Một dòng đã bị xóa");
            this.dispose();
        }
        
    }//GEN-LAST:event_btXoaActionPerformed

    private void btThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemActionPerformed
        // TODO add your handling code here:
        if(updateBenhNhan(home.getlocation()) == 1){//update trong arraylist
            if(home.updateBenhNhan() == 1){//update trong database
                this.dispose();
            }
            else{
                JOptionPane.showMessageDialog(rootPane, "Lỗi update trong baseframe");
            }
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Không có vị trí");
        }
    }//GEN-LAST:event_btThemActionPerformed

    private void btThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThoatActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btThoatActionPerformed

    private void MaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MaleActionPerformed
        // TODO add your handling code here:
        Male.setSelected(true);
        Female.setSelected(false);
        gioiTinh="Nam";
    }//GEN-LAST:event_MaleActionPerformed

    private void FemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FemaleActionPerformed
        // TODO add your handling code here:
        Male.setSelected(false);
        Female.setSelected(true);
        gioiTinh="Nữ";
    }//GEN-LAST:event_FemaleActionPerformed

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
            java.util.logging.Logger.getLogger(showBenhNhanDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(showBenhNhanDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(showBenhNhanDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(showBenhNhanDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                showBenhNhanDialog dialog = new showBenhNhanDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btThem;
    private javax.swing.JButton btThoat;
    private javax.swing.JButton btXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaBN;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JTextField txtSDT;
    // End of variables declaration//GEN-END:variables
}
