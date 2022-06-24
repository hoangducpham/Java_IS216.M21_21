/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import com.placeholder.PlaceHolder;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.AttributedCharacterIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.BenhNhan;
import model.NhanVien;
import model.PhieuKham;
import model.Thuoc;
import model.User;

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
 * @author An Phan
 */
public class BaseFrame extends javax.swing.JFrame {
    User us = new User();
    int quanLy = 0;
    int location = -1;
    private Connection conn;
    private PlaceHolder p1, p2, p3;

    // SET METHOD
    public void setUser(User us){
        this.us = us;
        quanLy = us.getPhanQuyen();
    }
    
    
    // GET METHOD
    public int getlocation(){
        return location;
    }
    
    public void resetLocation(){
        location = -1;
    }
    
    // Hàm khởi tạo mặc định
    public BaseFrame() {
        initComponents();
        setTitle("Quản lí phòng mạch tư");
        editImageFrame(); // Chèn hình
        show_nhanVien();
        panelNhanVien.setVisible(false);
        show_BenhNhan();
        show_BN();
        show_Thuoc();
        show_KhamBenh();
        showThongKe();
        panelThongKe.setVisible(false);
        this.setSize(1006, 520);
        setColor(pnBttTiepNhan);
    }
    
    // Hàm connect database
    public void connect(){
        try {
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            conn = DriverManager.getConnection(url, "system", "user_java123");
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    /*---------- NHANVIEN ----------*/
    ArrayList<NhanVien> listNV = getListNVDatabase();
    private int maNhanVien = 0;
    private DefaultTableModel tblModelNhanVien;
    
    // Lấy các nhân viên từ database vào list
    public ArrayList<NhanVien> getListNVDatabase(){
        ArrayList<NhanVien> nvList = new ArrayList<>();
        String url="jdbc:oracle:thin:@localhost:1521:orcl";
        
        try{
            Connection conn = DriverManager.getConnection(url,"system","user_java123");
            String query1 = "SELECT MaNV, HoTen, to_char(NgaySinh, 'yyyy/mm/dd') as NgaySinh, DiaChi, GioiTinh, SDT, ChucDanh, MaPhong, Luong "
                    + "FROM SYS.QLPK_NHANVIEN";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query1);
            NhanVien nv;
            while(rs.next()){
//                Date ngaySinh = rs.getDate("NGAYSINH");
//                Date date=(Date) new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(ngaySinh);
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
//                String strDate = dateFormat.format(ngaySinh);
                
                nv = new NhanVien(
                            rs.getInt("MANV"),
                            rs.getString("HOTEN"),
                            rs.getString("NgaySinh"),
                            rs.getString("DIACHI"),
                            rs.getString("GIOITINH"),
                            rs.getString("SDT"),
                            rs.getString("CHUCDANH"),
                            rs.getString("MAPHONG"),
                            rs.getLong("LUONG")
                );
                nvList.add(nv);
            }
            conn.close();
        }
        catch(SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không connect được database");
        }
        
        return nvList;
    }
    
    // Lấy danh sách nhân viên
    public ArrayList<NhanVien> getListNV(){
        return listNV;
    }
    
    // Sau khi nhập database và list, ta fetch data từ list vào table để hiển thị ra màn hình
    public void show_nhanVien(){
        tblModelNhanVien = (DefaultTableModel)tblNhanVien.getModel();
        tblModelNhanVien.setRowCount(0);
        int j = 1;
        for(int i = 0; i < listNV.size(); i++){
            Object[] row={
                j, // STT
                listNV.get(i).getMaNV(),
                listNV.get(i).getTenNV(),
                listNV.get(i).getNgSinh(),
                listNV.get(i).getDiaChi(),
                listNV.get(i).getGioiTinh(),
                listNV.get(i).getSoDT(),
                listNV.get(i).getChucDanh(),
                listNV.get(i).getMaPhong(),
                listNV.get(i).getLuong()
            };
            tblModelNhanVien.addRow(row);
            j++;
        }
        setVisible(true);
    }
    
    public int getMaNV(){
        return maNhanVien;
    }
    
    // Lấy nhân viên với location
    public NhanVien getNV(){
        return listNV.get(location);
    }
    
    public void findNhanVien(int maNV){
        for(int i = 0; i < listNV.size(); i++){
            if(listNV.get(i).getMaNV() == maNV){
                location = i;
            }
        }
    }
    
    // Tìm kiếm vị trí nhân viên
    public int searchNhanVien(ArrayList<NhanVien> arr, long target) {
        for(int i=0; i < arr.size(); i++){
            int x = arr.get(i).getMaNV();
            if (x == target) {
                return i;
            }
        }
        return -1;
    }
    
    public int addNhanVien(){
        try {
            //DefaultTableModel tblModelNhanVien =(DefaultTableModel)tblNhanVien.getModel();
            tblModelNhanVien = (DefaultTableModel)tblNhanVien.getModel();
            int i           = listNV.size()- 1;
            // int MaNV        = listNV.get(i).getMaNV();
            String TenNV    = listNV.get(i).getTenNV();
            String DiaChi   = listNV.get(i).getDiaChi();
            String GioiTinh = listNV.get(i).getGioiTinh();
            String SoDT     = listNV.get(i).getSoDT();
            String ChucDanh = listNV.get(i).getChucDanh();
            int MaPhong     = Integer.parseInt(listNV.get(i).getMaPhong());
            long Luong      = listNV.get(i).getLuong();
            
            String date = listNV.get(i).getNgSinh();
            java.util.Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
            
            int phanQuyen = 2;
            if(ChucDanh.equalsIgnoreCase("nhan vien"))
                phanQuyen = 2;
            else if (ChucDanh.equalsIgnoreCase("quan ly"))
                phanQuyen = 1;
            else{
                JOptionPane.showMessageDialog(panelKhamBenh, "Nhập chức danh là nhan vien hoặc quan ly");
                return 0;
            }
            
            // Thêm vô csdl
            connect();
            String insert = "insert into SYS.QLPK_NHANVIEN(HOTEN,NGAYSINH,GIOITINH,SDT,DIACHI,CHUCDANH,PHANQUYEN,MAPHONG,LUONG) values(?,?,?,?,?,?,?,?,?)";
            PreparedStatement pre = conn.prepareStatement(insert);
            
            pre.setString(1, TenNV);
            pre.setDate(2, sqlDate);
            pre.setString(3, GioiTinh);
            pre.setString(4, SoDT);
            pre.setString(5, DiaChi);
            pre.setString(6, ChucDanh);
            pre.setInt(7, phanQuyen);
            pre.setInt(8, MaPhong);
            pre.setLong(9, Luong);
            int x = pre.executeUpdate();
            JOptionPane.showMessageDialog(panelKhamBenh, x + " dòng đã được thêm vào csdl");
            this.listNV = getListNVDatabase();
            show_nhanVien();
            conn.close();
            return 1;
        }
        catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        }
        catch (ParseException ex){
            //Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi convert dữ liệu Ngày sinh");
            return 0;
        }
    }
    
    public int updateNhanVien(){
        try {
            //Gán thông tin cho biến
            int i = location;
            int MaNV        = listNV.get(i).getMaNV();
            String TenNV    = listNV.get(i).getTenNV();
            String DiaChi   = listNV.get(i).getDiaChi();
            String GioiTinh = listNV.get(i).getGioiTinh();
            String SoDT     = listNV.get(i).getSoDT();
            String ChucDanh = listNV.get(i).getChucDanh();
            int MaPhong     = Integer.parseInt(listNV.get(i).getMaPhong());
            long Luong      = listNV.get(i).getLuong();
            
            String date = listNV.get(i).getNgSinh();
            java.util.Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
            
            int phanQuyen = 2;
            if(ChucDanh.equalsIgnoreCase("nhan vien"))
                phanQuyen = 1;
            else if (ChucDanh.equalsIgnoreCase("quan ly"))
                phanQuyen = 2;
            else{
                JOptionPane.showMessageDialog(panelKhamBenh, "Nhập chức danh là nhan vien hoặc quan ly");
                return 0;
            }
            
            connect();
            String update="update SYS.QLPK_NHANVIEN set HOTEN=?, NGAYSINH=?, GIOITINH=?, DIACHI=?,"
                    + "SDT=?, PHANQUYEN=?, CHUCDANH=?, MAPHONG=?,"
                    + "LUONG=? WHERE MANV="+MaNV;
            PreparedStatement pre=conn.prepareStatement(update);

            pre.setString(1, TenNV);
            pre.setDate(2, sqlDate);
            pre.setString(3, GioiTinh);
            pre.setString(4, DiaChi);
            pre.setString(5, SoDT);
            pre.setInt(6, phanQuyen);
            pre.setString(7, ChucDanh);
            pre.setInt(8, MaPhong);
            pre.setLong(9, Luong);
            int x = pre.executeUpdate();
            JOptionPane.showMessageDialog(panelKhamBenh, x + " dòng đã được cập nhật");
            conn.close();
            
            updateNhanVienJtable(location);
            location = -1;
            return 1;
        }
        catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        }
        catch (ParseException ex){
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi convert dữ liệu Ngày sinh");
            return 0;
        }
    }
    
    public int deleteNhanVien(int manv){
        try {
            tblModelNhanVien.removeRow(location);
            listNV.remove(location);
            location = -1;

            connect();
            String delete = "delete from SYS.QLPK_NHANVIEN where MANV="+manv;
            Statement stm = conn.createStatement();
            int x = stm.executeUpdate(delete);
            txtTimMaNV.setText(""); 
            conn.close();
            return x;
        } 
        catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi kết nối csdl");
            return 0;
        }  
    }
    
    public int deleteNhanVienMouseClick(int manv){
        try {
            tblModelNhanVien.removeRow(location);
            listNV.remove(location);
            location = -1;
            
            connect();
            String delete="delete from SYS.QLPK_NHANVIEN where MANV="+manv;
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            conn.close();
            return x;
        } 
        catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;   
        }  
    }
    
    public void updateNhanVienJtable(int loca){
        tblModelNhanVien.setValueAt(listNV.get(loca).getTenNV(), loca, 2);
        tblModelNhanVien.setValueAt(listNV.get(loca).getNgSinh(), loca, 3);
        tblModelNhanVien.setValueAt(listNV.get(loca).getDiaChi(), loca, 4);
        tblModelNhanVien.setValueAt(listNV.get(loca).getGioiTinh(), loca, 5);
        tblModelNhanVien.setValueAt(listNV.get(loca).getSoDT(), loca, 6);
        tblModelNhanVien.setValueAt(listNV.get(loca).getChucDanh(), loca, 7);
        tblModelNhanVien.setValueAt(listNV.get(loca).getMaPhong(), loca, 8);
        tblModelNhanVien.setValueAt(listNV.get(loca).getLuong(), loca, 9);
    }
    
    
    
    
    
    public Thuoc getThuoc(){
        return listThuoc.get(location);
    }
    
    public void findThuoc(int mathuoc){
        for(int i=0; i<listThuoc.size(); i++){
            if(listThuoc.get(i).getMaThuoc() == mathuoc){
                location = i;
            }
        }
    }
    
    public BenhNhan getBenhNhan(){
        return listBN.get(location);
    }
 
    public void findBenhNhan(int maBN){
        for(int i=0; i<listBN.size(); i++){
            if(listBN.get(i).getMaBN() == maBN){
                location = i;
            }
        }
    }
    
    
    public int deleteBenhNhan(int mabn){
        try {
            tblModelBN.removeRow(location);
            listBN.remove(location);
            location=-1;
            
            connect();
            
            String sql = "SELECT MaHD FROM SYS.QLPK_HOADON WHERE MaBN=" + mabn;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            
            String delete = "";
            while(rs.next()) {
                int maHD = rs.getInt(1);
                
                delete = "DELETE FROM SYS.QLPK_TOATHUOC WHERE MaHD=" + maHD;
                Statement stm1 = conn.createStatement();
                stm1.executeUpdate(delete);
                
                delete = "DELETE FROM SYS.QLPK_HOADON WHERE MaHD=" + maHD;
                Statement stm2 = conn.createStatement();
                stm2.executeUpdate(delete);
            }
            
            delete = "DELETE FROM SYS.QLPK_BENHNHAN WHERE MABN=" + mabn;
            Statement stm3 = conn.createStatement();
            int x = stm3.executeUpdate(delete);

            conn.close();
            return x;
        } 
        catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi kết nối csdl");
            return 0;
        }  
    }
    
    public int deleteThuoc(int maThuoc){
        try {
            //tblModelThuoc.removeRow(location);
            //listThuoc.remove(location);
            //location=-1;
            connect();
            String sql = "UPDATE SYS.QLPK_THUOC SET XOA=1 WHERE MATHUOC=" +maThuoc;
            Statement stm = conn.createStatement();
            int x = stm.executeUpdate(sql);
            
//            String delete = "delete from SYS.QLPK_THUOC where MATHUOC="+maThuoc;
//            Statement stm=conn.createStatement();
//            int x=stm.executeUpdate(delete);
            
            String select="SELECT * FROM SYS.QLPK_THUOC WHERE XOA=0";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(select);
            
            tblModelThuoc.setRowCount(0);
            while(rs.next()) {
                Object[] objs ={rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5)};
                tblModelThuoc.addRow(objs);
            }
            
            conn.close();
            return x;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi kết nối csdl");
            return 0;
        }  
    }
    
    public int getMaHD(){
        int x=Integer.parseInt(txtTimMaHD.getText());
        return x;
    }
    
    
 //-------------------------------------------------------------------------Bệnh nhân--------------------------------------   
    private DefaultTableModel tblModelBN;
    public int deleteBenhNhanMouseClick(int mabn){
        try {
            tblModelBN=(DefaultTableModel)tblBenhNhan.getModel();
            tblModelBN.removeRow(location);
            tblModel.removeRow(location);
            listBN.remove(location);
            location=-1;
            connect();
            String delete="delete from SYS.QLPK_BENHNHAN where MABN="+mabn;
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            
            conn.close();
            return x;
        } 
        catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        }
    }
       
    public void updateBenhNhanJtable(int loca){
        tblModelBN.setValueAt(listBN.get(loca).getHoTen(), loca, 1);
        tblModelBN.setValueAt(listBN.get(loca).getNgaySinh(), loca, 2);
        tblModelBN.setValueAt(listBN.get(loca).getGioiTinh(), loca, 3);
        tblModelBN.setValueAt(listBN.get(loca).getDiaChi(), loca, 4);
        tblModelBN.setValueAt(listBN.get(loca).getSDT(), loca, 5);
    }
    
    public void updateBnTiepNhanJtable(int loca){
        tblModel.setValueAt(listBN.get(loca).getHoTen(), loca, 2);
        tblModel.setValueAt(listBN.get(loca).getNgaySinh(), loca, 3);
        tblModel.setValueAt(listBN.get(loca).getDiaChi(), loca, 5);
        tblModel.setValueAt(listBN.get(loca).getGioiTinh(), loca, 4);
        tblModel.setValueAt(listBN.get(loca).getSDT(), loca, 6);
    }
    
    public void show_BenhNhan(){
        int n = tblBenhNhan.getRowCount();
        tblModelBN =(DefaultTableModel)tblBenhNhan.getModel();
        if(n > 0) tblModelBN.setRowCount(0);
  
        for(int i=0; i < listBN.size(); i++){
            Object[] row = {
                listBN.get(i).getMaBN(),
                listBN.get(i).getHoTen(),
                listBN.get(i).getNgaySinh(), 
                listBN.get(i).getGioiTinh(),
                listBN.get(i).getDiaChi(),
                listBN.get(i).getSDT()
            };
            tblModelBN.addRow(row);
        }
    }
    
    public int updateBenhNhan(){
        try {
            //Gán thông tin cho biến
            int i = location;
            int MaBN        = listBN.get(i).getMaBN();
            String TenBN    = listBN.get(i).getHoTen();
            String DiaChi   = listBN.get(i).getDiaChi();
            String GioiTinh = listBN.get(i).getGioiTinh();
            String SoDT     = listBN.get(i).getSDT();
            
            String date = listBN.get(i).getNgaySinh();
            java.util.Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
            
            connect();
            String update="update SYS.QLPK_BENHNHAN set HOTEN=?, NGAYSINH=?, GIOITINH=?, DIACHI=?,"
                    + "SDT=? WHERE MABN="+MaBN;
            PreparedStatement pre = conn.prepareStatement(update);
            
            pre.setString(1, TenBN);
            pre.setDate(2, sqlDate);
            pre.setString(3, GioiTinh);
            pre.setString(4, DiaChi);
            pre.setString(5, SoDT);
            int x = pre.executeUpdate();
            JOptionPane.showMessageDialog(this, x + " dòng đã được cập nhật");
            conn.close();
            
            // Update Jtable
            updateBenhNhanJtable(location);
            updateBnTiepNhanJtable(location);
            
            location = -1;
            return 1;
        } 
        catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        } 
        catch (ParseException ex){
            //Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi convert dữ liệu Ngày sinh");
            return 0;
        }
    }
    
    //--------------------------------------------------------------------------Tiếp nhận-----------------------------------------
    // Private Connection conn;
    private int maBN;
    private DefaultTableModel tblModel;
    ArrayList<BenhNhan> listBN = getBenhNhanList(); 
        
    public ArrayList<BenhNhan> getBenhNhanList(){
        ArrayList<BenhNhan> BenhNhansList = new ArrayList<>();
        try{
            connect();
            String query2 = "SELECT * FROM SYS.QLPK_BENHNHAN";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            BenhNhan bn;
            while(rs.next()){          
                Date ngaySinh = rs.getDate("NGAYSINH");
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  
                String NgaySinh = dateFormat.format(ngaySinh);
                bn = new BenhNhan(rs.getInt("MABN"),rs.getString("HOTEN"),NgaySinh,rs.getString("GIOITINH"),rs.getString("DIACHI"),rs.getString("SDT"));
                BenhNhansList.add(bn);
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return BenhNhansList;
    }
    
    public int getMaBN(){
        return maBN;
    }
   
    public void show_BN(){
        try {
            tblModel =(DefaultTableModel)tblBN.getModel();
            tblModel.setRowCount(0);
            connect();
            String query = "SELECT * FROM SYS.QLPK_BENHNHAN";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            int i = 1;
            while(rs.next()){
                Date ngaySinh = rs.getDate("NGAYSINH");
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  
                String NgaySinh = dateFormat.format(ngaySinh); 
                tblModel.addRow(new Object[]{
                    i,
                    rs.getInt("MaBN"),
                    rs.getString("HOTEN"), 
                    NgaySinh,
                    rs.getString("GIOITINH"),
                    rs.getString("DIACHI"),
                    rs.getString("SDT")                    
                });
                i++;
            }
            
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        updateCBBchuanDoan();
        updateCBBbacSi();
    }
    
    public void updateCBBchuanDoan(){
        try{
            connect();
            String query2 = "SELECT LOAIBENH FROM SYS.QLPK_LOAIBENH";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            while(rs.next()){          
                cbbChuanDoan.addItem(rs.getString(1));
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void updateCBBbacSi(){
        try{
            connect();
            String query2 = "SELECT * FROM SYS.QLPK_NHANVIEN WHERE MaPhong=1"; // Phong kham
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            while(rs.next()){          
                cbbBacSi.addItem(rs.getString(2));
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    //----------------------------------------------------------------------THUỐC---------------------------------------------------
    ArrayList<Thuoc> listThuoc = getThuocList();
    public ArrayList<Thuoc> getThuocList(){
        ArrayList<Thuoc> ThuocsList = new ArrayList<>();
        try{
            connect();
            String query2 = "SELECT * FROM SYS.QLPK_THUOC WHERE XOA=0";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            Thuoc thuoc;
            while(rs.next()){
                thuoc = new Thuoc(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getInt(4) , rs.getString(5));
                ThuocsList.add(thuoc);
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ThuocsList;
    }
    private int maHoaDon=0;
    public int getMaHoaDon(){
        return maHoaDon;
    }
    
    private int idThuocAuto;
    
    private DefaultTableModel tblModelThuoc;
    public void show_Thuoc(){
        tblModelThuoc =(DefaultTableModel)tblThuoc.getModel();
        for(int i=0; i<listThuoc.size(); i++){
            Object[] row={listThuoc.get(i).getMaThuoc(), listThuoc.get(i).getLoaiThuoc(), listThuoc.get(i).getDonVi(), 
                         listThuoc.get(i).getDonGia(), listThuoc.get(i).getNoiSX()};
            tblModelThuoc.addRow(row);
        }
        
        int id=1;
        boolean flag;
        while(true){
            flag=false;
            for(Thuoc t:listThuoc){
                if(id==t.getMaThuoc()){
                    flag=true;
                    break;
                }
            }
            if(!flag) break;
            ++id;
        }
        idThuocAuto=id;
    }
    
    public int addThuoc(){
        try {
            int i=listThuoc.size()-1;
            //int STT=listThuoc.size();
            //int MaThuoc=listThuoc.get(i).getMaThuoc();
            String TenThuoc=listThuoc.get(i).getLoaiThuoc();
            String DonVi=listThuoc.get(i).getDonVi();
            int DonGia=listThuoc.get(i).getDonGia();
            String NoiSX=listThuoc.get(i).getNoiSX();
            //Object[] objs={ STT, TenThuoc, DonVi, DonGia, NoiSX};
            //thêm vô csdl
            connect();
            String insert="insert into SYS.QLPK_THUOC(LOAITHUOC,DONVI,DONGIA,NOISX) values(?,?,?,?)";
            PreparedStatement pre=conn.prepareStatement(insert);
            
           
            //Nhập dữ liệu vô csdl
            //pre.setInt(1, MaThuoc);
            pre.setString(1, TenThuoc);
            pre.setString(2, DonVi);
            pre.setInt(3, DonGia);
            pre.setString(4, NoiSX);
            int x = pre.executeUpdate();
            JOptionPane.showMessageDialog(this, x+" dòng đã được thêm vào csdl");
            
            String select="SELECT * FROM SYS.QLPK_THUOC WHERE XOA=0";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(select);
            
            tblModelThuoc.setRowCount(0);
            while(rs.next()) {
                Object[] objs ={rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5)};
                tblModelThuoc.addRow(objs);
            }
            
            
            
            
            conn.close();
            return 1;
        }catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi kết nối csdl");
            return 0;
        }
    }
    
    public int returnIdThuoc(){
        return idThuocAuto;
    }
    
    public void updateThuocJtable(int loca){
        tblModelThuoc.setValueAt(listThuoc.get(loca).getLoaiThuoc(), loca, 1);
        tblModelThuoc.setValueAt(listThuoc.get(loca).getDonVi(), loca, 2);
        tblModelThuoc.setValueAt(listThuoc.get(loca).getDonGia(), loca, 3);
        tblModelThuoc.setValueAt(listThuoc.get(loca).getNoiSX(), loca, 4);
    }
    
    public int updateThuoc(){
        try {
            //Gán thông tin cho biến
            int i=location;
            //int STT=listNV.size();
            int MaThuoc=listThuoc.get(i).getMaThuoc();
            String TenThuoc=listThuoc.get(i).getLoaiThuoc();
            String DonVi=listThuoc.get(i).getDonVi();
            int DonGia=listThuoc.get(i).getDonGia();
            String NoiSX=listThuoc.get(i).getNoiSX();
            connect();
            //update dtb
            String update="update SYS.QLPK_THUOC set LOAITHUOC=?, DONVI=?, DONGIA=?, NOISX=? WHERE MATHUOC="+MaThuoc;
            PreparedStatement pre=conn.prepareStatement(update);
            //Nhập dữ liệu vô csdl
            pre.setString(1, TenThuoc);
            pre.setString(2, DonVi);
            pre.setInt(3, DonGia);
            pre.setString(4, NoiSX);
            int x=pre.executeUpdate();
            //Transaction đã kết thúc
            JOptionPane.showMessageDialog(this, x+" dòng đã được cập nhật");
            conn.close();
            //update Jtable
            updateThuocJtable(location);
            location=-1;
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi kết nối csdl");
            return 0;
        } 
    }
//--------------------------------------------------------------------------Khám bệnh-------------------------------------
    private DefaultTableModel tblModelToaThuoc;
    
    public void updateCBBloaiThuoc(){
        for(int i = cbbLoaiThuoc.getItemCount() - 1; i >= 0; i--){
            cbbLoaiThuoc.removeItemAt(i);
        }
        try{
            connect();
            String query2 = "SELECT LOAITHUOC FROM SYS.QLPK_THUOC WHERE XOA=0";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            
            while(rs.next()){          
                cbbLoaiThuoc.addItem(rs.getString(1));
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void updateCBBTienSu(){
        for(int i = cbbTienSu.getItemCount() - 1; i >= 0; i--){
            cbbTienSu.removeItemAt(i);
        }
        try{
            connect();
            String query2 = "SELECT DISTINCT CHUANDOAN FROM SYS.QLPK_HOADON WHERE MABN=" + txtMaBNKham.getText();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            
            while(rs.next()){          
                cbbTienSu.addItem(rs.getString(1));
            }
            
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void inputThuocToToa(){
        try{
            int maHD = Integer.parseInt(txtMaHD.getText());
            int maThuoc = 1;
            String loaiThuoc = cbbLoaiThuoc.getSelectedItem().toString();
            int soLuong=(Integer)this.soLuong.getValue();
            String cachDung=txtCachDung.getText();
            if(cachDung.equals("")==true){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập cách dùng");
        }
        else{
            try{
                connect();
                String getMaThuoc="select distinct MATHUOC FROM SYS.QLPK_THUOC WHERE XOA=0 AND LOAITHUOC='"+loaiThuoc+"'";
                Statement stm=conn.createStatement();
                ResultSet rs=stm.executeQuery(getMaThuoc);
                
                while(rs.next()){
                    maThuoc=rs.getInt(1);
                }
                
                String insert="insert into SYS.QLPK_TOATHUOC (MAHD,MATHUOC,SOLUONG,THANHTIEN,CACHDUNG,TINHTRANG) values(?,?,?,?,?,?)";
                PreparedStatement pre=conn.prepareStatement(insert);
                //Nhập dữ liệu vô csdl
                pre.setInt(1, maHD);
                pre.setInt(2, maThuoc);
                pre.setInt(3, soLuong);
                pre.setInt(4, 0);
                pre.setString(5, cachDung);
                pre.setInt(6, 0);
                int x=pre.executeUpdate();

                Object[] objs={loaiThuoc, soLuong, cachDung};
                tblModelToaThuoc.addRow(objs);
                conn.close();
            }catch (SQLException ex) {
                Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            }
        }
        } catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(panelKhamBenh, "Vui lòng tìm phiếu khám trước khi thêm hóa đơn");
        }
    }
    
  
    
    public void show_KhamBenh(){
        txtMaBNKham.setEditable(false);
        txtHoTen.setEditable(false);
        txtLiDoKham.setEditable(false);
        updateCBBloaiThuoc();
        txtDonVi.setEditable(false);
        txtMaHD.setEditable(false);
        tblModelToaThuoc = (DefaultTableModel)tblToaThuoc.getModel();
    }
    
//------------------------------------------------------------------------Thống kê-----------------------------------------------------------------------------------
    DefaultTableModel tblModelHoaDon;
    DefaultTableModel tblModelDoanhSoThuoc;
    DefaultTableModel tblModelTimKiemHD;
    public void updateTblHoaDon(){
        try {
            tblModelHoaDon=(DefaultTableModel)tblHoaDon.getModel();
            connect();
            String query="select MAHD, MABN, MANV, THANHTIEN FROM SYS.QLPK_HOADON";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                Object[] obj={rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4)};
                tblModelHoaDon.addRow(obj);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateTblDoanhSoThuoc(){
        try {
            tblModelDoanhSoThuoc=(DefaultTableModel)tblDoanhSoThuoc.getModel();
            connect();
            String query="select MATHUOC, DOANHTHU FROM SYS.QLPK_THUOC";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                Object[] obj={rs.getInt(1), rs.getInt(2)};
                tblModelDoanhSoThuoc.addRow(obj);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public long tinhTongDoanhThu(){
        long money=0;
        for(int i=0;i<tblHoaDon.getRowCount();i++){
            money=Long.parseLong(tblHoaDon.getValueAt(i, 3).toString())+money;
        } 
        return money;
    }
    
    public String rutGonDoanhThu(long money){
        ArrayList<String> arr=new ArrayList<>();
        while(money>999){
            long temp=money%1000;
            money/=1000;
            if(temp==0){
                arr.add(String.valueOf(temp)+"00");
            }else if(temp<=9){
                arr.add("00"+String.valueOf(temp));
            }else if(temp<=99){
                arr.add("0"+String.valueOf(temp));
            }else 
                arr.add(String.valueOf(temp));
        }
        arr.add(String.valueOf(money));
        String tien=" ";
        for(int j=arr.size()-1; j>0; j--){
            tien=tien+arr.get(j)+".";
        }
        tien=tien+arr.get(0);
        return tien;
    }
    
    
    public void showThongKe(){
        updateTblHoaDon();
        updateTblDoanhSoThuoc();
        long money=tinhTongDoanhThu();
        //System.out.println(money);
        tblModelTimKiemHD=(DefaultTableModel)tblTimKiemHD.getModel();
        txtTongDoanhThu.setText(rutGonDoanhThu(money)+" VND");
        txtTongDoanhThu.setEditable(false);
        txtDoanhThuThang.setEditable(false);
        updateCBBNam();
    }
    
    public void updateCBBNam(){
        try{
            connect();
            String query2 = "SELECT DISTINCT EXTRACT(YEAR FROM NGAYKHAM) FROM SYS.QLPK_HOADON";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            while(rs.next()){          
                txtYear.addItem(rs.getString(1));
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void refreshKhamBenh(){
        txtMaBNKham.setText("");
        txtMaHD.setText("");
        txtTimMaPK.setText("");
        txtHoTen.setText("");
        txtLiDoKham.setText("");
        for(int i=cbbTienSu.getItemCount()-1;i>=0;i--){
            cbbTienSu.removeItemAt(i);
        }
        txtTrieuChung.setText("");
        txtCachDung.setText("");
        tblModelToaThuoc.setRowCount(0);
    }
//--------------------------------------------------------------------hàm khởi tạo mặc định------------------------------
    //-------------------set color khi click button
    public void setColor(JPanel panel){
        panel.setBackground(new Color(240, 240, 240));
    }
    public void resetColor(JPanel panel){
        panel.setBackground(new Color(204, 204, 255));
    }
    //lấy hình từ folder src/images để chèn hình vào frame
    public void editImageFrame(){
        ImageIcon myimage = new ImageIcon("src/images/da1.png");
        //Image img1 = myimage.getImage();
        Image img2=myimage.getImage().getScaledInstance(jLabel1.getWidth(), jLabel1.getHeight(), Image.SCALE_SMOOTH);
        jLabel1.setIcon(new ImageIcon(img2));
        
        ImageIcon myimage3 = new ImageIcon("src/images/drugs.png");
        //Image img1 = myimage.getImage();
        Image img3=myimage3.getImage().getScaledInstance(jLabel1.getWidth(), jLabel1.getHeight(), Image.SCALE_SMOOTH);
        label_drugs.setIcon(new ImageIcon(img3));
        
        
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PopUpMenu = new javax.swing.JPopupMenu();
        deleteNV = new javax.swing.JMenuItem();
        updateNV = new javax.swing.JMenuItem();
        PopUpMenu1 = new javax.swing.JPopupMenu();
        deleteBN = new javax.swing.JMenuItem();
        updateBN = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jPanel_title = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        label_drugs = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        pnBttBenhNhan = new javax.swing.JPanel();
        jButtonBenhNhan1 = new javax.swing.JButton();
        pnBttKhamBenh = new javax.swing.JPanel();
        jButtonKhamBenh = new javax.swing.JButton();
        pnBttNhanVien = new javax.swing.JPanel();
        jButtonNhanvien = new javax.swing.JButton();
        pnBttThongKe = new javax.swing.JPanel();
        jButtonThongke = new javax.swing.JButton();
        pnBttTiepNhan = new javax.swing.JPanel();
        jButtonTiepNhan = new javax.swing.JButton();
        pnBttThuoc = new javax.swing.JPanel();
        jButtonThuoc = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        panelTiepNhan = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBN = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtTenBN = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        jMale = new javax.swing.JRadioButton();
        jFemale = new javax.swing.JRadioButton();
        btThemBN = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtTimMaBN = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        panelBenhNhan = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblBenhNhan = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtTimBenhNhan = new javax.swing.JTextField();
        btTimMaBN = new javax.swing.JButton();
        panelKhamBenh = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        txtLiDoKham = new javax.swing.JTextField();
        txtTrieuChung = new javax.swing.JTextField();
        cbbChuanDoan = new javax.swing.JComboBox<>();
        cbbBacSi = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        txtMaBNKham = new javax.swing.JTextField();
        btThemBenhAn = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        cbbLoaiThuoc = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        soLuong = new javax.swing.JSpinner();
        jLabel40 = new javax.swing.JLabel();
        txtCachDung = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblToaThuoc = new javax.swing.JTable();
        jLabel41 = new javax.swing.JLabel();
        btThemThuocToToa = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        txtTimMaPK = new javax.swing.JTextField();
        btTimPK = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtDonVi = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbbTienSu = new javax.swing.JComboBox<>();
        panelThuoc = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtTimMaThuoc = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        btThemThuoc = new javax.swing.JButton();
        btTimMatoa = new javax.swing.JButton();
        txtTimMaHD = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblThuoc = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        panelNhanVien = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        btThemNV = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtTimMaNV = new javax.swing.JTextField();
        btTimNV = new javax.swing.JButton();
        panelThongKe = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        txtTongDoanhThu = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblDoanhSoThuoc = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        btRefreshTK = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblTimKiemHD = new javax.swing.JTable();
        jLabel27 = new javax.swing.JLabel();
        cbbMonth = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        btTimHD = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        txtDoanhThuThang = new javax.swing.JTextField();
        btInBaoCaoThang = new javax.swing.JButton();
        btInBaoCaoNam = new javax.swing.JButton();
        txtYear = new javax.swing.JComboBox<>();

        deleteNV.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deleteNV.setText("Xóa");
        deleteNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteNVActionPerformed(evt);
            }
        });
        PopUpMenu.add(deleteNV);

        updateNV.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        updateNV.setText("Sửa ");
        updateNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateNVActionPerformed(evt);
            }
        });
        PopUpMenu.add(updateNV);

        deleteBN.setText("Xóa");
        deleteBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBNActionPerformed(evt);
            }
        });
        PopUpMenu1.add(deleteBN);

        updateBN.setText("Sửa");
        updateBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBNActionPerformed(evt);
            }
        });
        PopUpMenu1.add(updateBN);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel_title.setBackground(new java.awt.Color(69, 123, 179));
        jPanel_title.setPreferredSize(new java.awt.Dimension(1001, 65));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("QUẢN LÍ PHÒNG MẠCH TƯ");

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel_titleLayout = new javax.swing.GroupLayout(jPanel_title);
        jPanel_title.setLayout(jPanel_titleLayout);
        jPanel_titleLayout.setHorizontalGroup(
            jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_titleLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(269, 269, 269)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label_drugs, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel_titleLayout.setVerticalGroup(
            jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel_titleLayout.createSequentialGroup()
                .addGroup(jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                    .addComponent(label_drugs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1001, 53));

        pnBttBenhNhan.setBackground(new java.awt.Color(204, 204, 255));

        jButtonBenhNhan1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonBenhNhan1.setText("BỆNH NHÂN");
        jButtonBenhNhan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBenhNhan1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttBenhNhanLayout = new javax.swing.GroupLayout(pnBttBenhNhan);
        pnBttBenhNhan.setLayout(pnBttBenhNhanLayout);
        pnBttBenhNhanLayout.setHorizontalGroup(
            pnBttBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttBenhNhanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonBenhNhan1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnBttBenhNhanLayout.setVerticalGroup(
            pnBttBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttBenhNhanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonBenhNhan1, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnBttKhamBenh.setBackground(new java.awt.Color(204, 204, 255));

        jButtonKhamBenh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonKhamBenh.setText("KHÁM BỆNH");
        jButtonKhamBenh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKhamBenhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttKhamBenhLayout = new javax.swing.GroupLayout(pnBttKhamBenh);
        pnBttKhamBenh.setLayout(pnBttKhamBenhLayout);
        pnBttKhamBenhLayout.setHorizontalGroup(
            pnBttKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnBttKhamBenhLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(jButtonKhamBenh)
                .addContainerGap())
        );
        pnBttKhamBenhLayout.setVerticalGroup(
            pnBttKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttKhamBenhLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonKhamBenh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnBttNhanVien.setBackground(new java.awt.Color(204, 204, 255));

        jButtonNhanvien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonNhanvien.setText("NHÂN VIÊN");
        jButtonNhanvien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNhanvienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttNhanVienLayout = new javax.swing.GroupLayout(pnBttNhanVien);
        pnBttNhanVien.setLayout(pnBttNhanVienLayout);
        pnBttNhanVienLayout.setHorizontalGroup(
            pnBttNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonNhanvien)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnBttNhanVienLayout.setVerticalGroup(
            pnBttNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonNhanvien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnBttThongKe.setBackground(new java.awt.Color(204, 204, 255));

        jButtonThongke.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonThongke.setText("THỐNG KÊ");
        jButtonThongke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThongkeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttThongKeLayout = new javax.swing.GroupLayout(pnBttThongKe);
        pnBttThongKe.setLayout(pnBttThongKeLayout);
        pnBttThongKeLayout.setHorizontalGroup(
            pnBttThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttThongKeLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jButtonThongke)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        pnBttThongKeLayout.setVerticalGroup(
            pnBttThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnBttThongKeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonThongke, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnBttTiepNhan.setBackground(new java.awt.Color(204, 204, 255));

        jButtonTiepNhan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonTiepNhan.setText("TIẾP NHẬN");
        jButtonTiepNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTiepNhanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttTiepNhanLayout = new javax.swing.GroupLayout(pnBttTiepNhan);
        pnBttTiepNhan.setLayout(pnBttTiepNhanLayout);
        pnBttTiepNhanLayout.setHorizontalGroup(
            pnBttTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttTiepNhanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonTiepNhan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnBttTiepNhanLayout.setVerticalGroup(
            pnBttTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttTiepNhanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonTiepNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnBttThuoc.setBackground(new java.awt.Color(204, 204, 255));

        jButtonThuoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonThuoc.setText("THUỐC");
        jButtonThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThuocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttThuocLayout = new javax.swing.GroupLayout(pnBttThuoc);
        pnBttThuoc.setLayout(pnBttThuocLayout);
        pnBttThuocLayout.setHorizontalGroup(
            pnBttThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttThuocLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jButtonThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        pnBttThuocLayout.setVerticalGroup(
            pnBttThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttThuocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(pnBttTiepNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(pnBttBenhNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnBttKhamBenh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnBttThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnBttNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnBttThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnBttBenhNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(pnBttTiepNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(pnBttNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnBttThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnBttThuoc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnBttKhamBenh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setPreferredSize(new java.awt.Dimension(1001, 367));

        jLayeredPane1.setPreferredSize(new java.awt.Dimension(1001, 332));
        jLayeredPane1.setLayout(new java.awt.CardLayout());

        panelTiepNhan.setPreferredSize(new java.awt.Dimension(1001, 311));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel13.setText("TÌM KIẾM BỆNH NHÂN");

        tblBN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã bệnh nhân", "Tên bệnh nhân", "Ngày Sinh", "Giới tính", "Địa chỉ", "SĐT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblBNMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblBN);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel14.setText("THÊM BỆNH NHÂN");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Tên bệnh nhân");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setText("Ngày Sinh");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("Giới tính");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setText("Địa chỉ");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("SĐT");

        jMale.setText("Nam");
        jMale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMaleActionPerformed(evt);
            }
        });

        jFemale.setText("Nữ");
        jFemale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFemaleActionPerformed(evt);
            }
        });

        btThemBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btThemBN.setText("Thêm bệnh nhân");
        btThemBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemBNActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Tìm mã bệnh nhân");

        txtTimMaBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton1.setText("Tìm kiếm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTiepNhanLayout = new javax.swing.GroupLayout(panelTiepNhan);
        panelTiepNhan.setLayout(panelTiepNhanLayout);
        panelTiepNhanLayout.setHorizontalGroup(
            panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel18))
                                .addGap(53, 53, 53)
                                .addComponent(jMale)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jFemale))
                            .addComponent(jLabel14)
                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btThemBN)
                                    .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtDiaChi)
                                        .addComponent(txtNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                        .addComponent(txtTenBN))))))
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jLabel13))
                .addGap(35, 58, Short.MAX_VALUE))
        );
        panelTiepNhanLayout.setVerticalGroup(
            panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtTimMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel14))
                .addGap(15, 15, 15)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtTenBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17)
                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jMale)
                            .addComponent(jFemale))
                        .addGap(18, 18, 18)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btThemBN))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLayeredPane1.add(panelTiepNhan, "card6");

        panelBenhNhan.setPreferredSize(new java.awt.Dimension(1006, 332));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel32.setText("THÔNG TIN BỆNH NHÂN");

        tblBenhNhan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã BN", "Họ và tên", "Ngày sinh", "Giới tính", "Địa chỉ", "Số điện thoại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBenhNhan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblBenhNhanMouseReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tblBenhNhan);
        if (tblBenhNhan.getColumnModel().getColumnCount() > 0) {
            tblBenhNhan.getColumnModel().getColumn(0).setPreferredWidth(60);
            tblBenhNhan.getColumnModel().getColumn(0).setMaxWidth(70);
            tblBenhNhan.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblBenhNhan.getColumnModel().getColumn(1).setMaxWidth(160);
            tblBenhNhan.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblBenhNhan.getColumnModel().getColumn(2).setMaxWidth(120);
            tblBenhNhan.getColumnModel().getColumn(3).setPreferredWidth(60);
            tblBenhNhan.getColumnModel().getColumn(3).setMaxWidth(70);
            tblBenhNhan.getColumnModel().getColumn(4).setPreferredWidth(170);
            tblBenhNhan.getColumnModel().getColumn(4).setMaxWidth(200);
            tblBenhNhan.getColumnModel().getColumn(5).setPreferredWidth(130);
            tblBenhNhan.getColumnModel().getColumn(5).setMaxWidth(140);
        }

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Tìm mã bệnh nhân");

        txtTimBenhNhan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTimBenhNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimBenhNhanActionPerformed(evt);
            }
        });
        txtTimBenhNhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimBenhNhanKeyReleased(evt);
            }
        });

        btTimMaBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btTimMaBN.setText("Tìm");
        btTimMaBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTimMaBNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBenhNhanLayout = new javax.swing.GroupLayout(panelBenhNhan);
        panelBenhNhan.setLayout(panelBenhNhanLayout);
        panelBenhNhanLayout.setHorizontalGroup(
            panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBenhNhanLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBenhNhanLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimBenhNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btTimMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(164, Short.MAX_VALUE))
        );
        panelBenhNhanLayout.setVerticalGroup(
            panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBenhNhanLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel9)
                    .addComponent(txtTimBenhNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btTimMaBN))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayeredPane1.add(panelBenhNhan, "card3");

        panelKhamBenh.setPreferredSize(new java.awt.Dimension(1006, 332));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("THÔNG TIN BỆNH ÁN");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setText("Họ tên");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel23.setText("Lí do khám");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setText("Triệu chứng");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel25.setText("Chuẩn đoán");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel26.setText("Bác sĩ khám");

        txtHoTen.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtLiDoKham.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtTrieuChung.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cbbChuanDoan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbbChuanDoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbChuanDoanActionPerformed(evt);
            }
        });

        cbbBacSi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbbBacSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbBacSiActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel28.setText("Mã BN");

        txtMaBNKham.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btThemBenhAn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btThemBenhAn.setText("THÊM BỆNH ÁN");
        btThemBenhAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemBenhAnActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel35.setText("Loại thuốc");

        cbbLoaiThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbbLoaiThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbLoaiThuocActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel39.setText("Số lượng");

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel40.setText("Cách dùng");

        txtCachDung.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        tblToaThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Loại thuốc", "Số lượng", "Cách dùng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblToaThuoc);
        if (tblToaThuoc.getColumnModel().getColumnCount() > 0) {
            tblToaThuoc.getColumnModel().getColumn(0).setPreferredWidth(140);
            tblToaThuoc.getColumnModel().getColumn(0).setMaxWidth(150);
            tblToaThuoc.getColumnModel().getColumn(1).setPreferredWidth(60);
            tblToaThuoc.getColumnModel().getColumn(1).setMaxWidth(80);
        }

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel41.setText("TOA THUỐC");

        btThemThuocToToa.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btThemThuocToToa.setText("THÊM THUỐC");
        btThemThuocToToa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemThuocToToaActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel42.setText("Mã phiếu khám");

        txtTimMaPK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btTimPK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btTimPK.setText("TÌM");
        btTimPK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTimPKActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Đơn vị");

        txtDonVi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Mã bệnh án");

        txtMaHD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Tiền sử bệnh án");

        javax.swing.GroupLayout panelKhamBenhLayout = new javax.swing.GroupLayout(panelKhamBenh);
        panelKhamBenh.setLayout(panelKhamBenhLayout);
        panelKhamBenhLayout.setHorizontalGroup(
            panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGap(15, 15, 15))
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                        .addComponent(jLabel40)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCachDung))
                                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                                .addComponent(jLabel41)
                                                .addGap(80, 80, 80))
                                            .addComponent(cbbLoaiThuoc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(22, 22, 22)
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                        .addComponent(jLabel39)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(soLuong)
                                        .addGap(18, 18, 18))
                                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(15, 15, 15))))))
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(btThemThuocToToa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTrieuChung, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbChuanDoan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbbBacSi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMaBNKham, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLiDoKham))
                        .addGap(37, 37, 37)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(27, 27, 27)
                                .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(193, 193, 193))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhamBenhLayout.createSequentialGroup()
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btThemBenhAn, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(cbbTienSu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                            .addComponent(jLabel42)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtTimMaPK, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btTimPK))))
                                .addGap(128, 128, 128))))
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGap(192, 192, 192)
                        .addComponent(jLabel7)
                        .addContainerGap())))
        );
        panelKhamBenhLayout.setVerticalGroup(
            panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel41))
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel28)
                                .addComponent(txtMaBNKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(13, 13, 13)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42)
                            .addComponent(txtTimMaPK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btTimPK))
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbLoaiThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35)
                            .addComponent(jLabel3)
                            .addComponent(txtDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCachDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40)
                            .addComponent(jLabel39)
                            .addComponent(soLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)))
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLiDoKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23)
                            .addComponent(jLabel5)
                            .addComponent(cbbTienSu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtTrieuChung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(cbbChuanDoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbBacSi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btThemThuocToToa)
                    .addComponent(btThemBenhAn))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLayeredPane1.add(panelKhamBenh, "card7");

        panelThuoc.setPreferredSize(new java.awt.Dimension(1006, 332));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel36.setText("DANH SÁCH THUỐC");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel37.setText("Mã thuốc");

        txtTimMaThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTimMaThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimMaThuocActionPerformed(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel38.setText("Mã hóa đơn");

        btThemThuoc.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btThemThuoc.setText("Thêm thuốc mới");
        btThemThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemThuocActionPerformed(evt);
            }
        });

        btTimMatoa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btTimMatoa.setText("Tìm");
        btTimMatoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTimMatoaActionPerformed(evt);
            }
        });

        txtTimMaHD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTimMaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimMaHDActionPerformed(evt);
            }
        });

        tblThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã thuốc", "Loại thuốc", "Đơn vị", "Dơn giá", "Nơi sản xuất"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblThuoc);
        if (tblThuoc.getColumnModel().getColumnCount() > 0) {
            tblThuoc.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblThuoc.getColumnModel().getColumn(0).setMaxWidth(60);
            tblThuoc.getColumnModel().getColumn(2).setMinWidth(60);
            tblThuoc.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblThuoc.getColumnModel().getColumn(2).setMaxWidth(120);
        }

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel10.setText("TÌM KIẾM THUỐC");

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setText("Tìm");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel33.setText("TÌM TOA THUỐC");

        javax.swing.GroupLayout panelThuocLayout = new javax.swing.GroupLayout(panelThuoc);
        panelThuoc.setLayout(panelThuocLayout);
        panelThuocLayout.setHorizontalGroup(
            panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThuocLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThuocLayout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThuocLayout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(95, 95, 95))
                            .addGroup(panelThuocLayout.createSequentialGroup()
                                .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelThuocLayout.createSequentialGroup()
                                        .addComponent(jLabel38)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(panelThuocLayout.createSequentialGroup()
                                                .addComponent(txtTimMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btTimMatoa))
                                            .addGroup(panelThuocLayout.createSequentialGroup()
                                                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(17, 17, 17))))
                                    .addComponent(btThemThuoc)
                                    .addGroup(panelThuocLayout.createSequentialGroup()
                                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(txtTimMaThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton3)))
                                .addGap(78, 78, 78))))
                    .addGroup(panelThuocLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelThuocLayout.setVerticalGroup(
            panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThuocLayout.createSequentialGroup()
                .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThuocLayout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThuocLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimMaThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTimMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btTimMatoa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(60, 60, 60)
                        .addComponent(btThemThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jLayeredPane1.add(panelThuoc, "card4");

        panelNhanVien.setPreferredSize(new java.awt.Dimension(1006, 332));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel8.setText("DANH SÁCH NHÂN VIÊN");

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã NV", "Họ và tên", "Ngày sinh", "Địa chỉ", "Giới tính", "SĐT", "Chức danh", "Mã phòng", "Lương"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblNhanVien);
        if (tblNhanVien.getColumnModel().getColumnCount() > 0) {
            tblNhanVien.getColumnModel().getColumn(0).setMinWidth(20);
            tblNhanVien.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblNhanVien.getColumnModel().getColumn(1).setPreferredWidth(30);
            tblNhanVien.getColumnModel().getColumn(2).setPreferredWidth(135);
            tblNhanVien.getColumnModel().getColumn(2).setMaxWidth(140);
            tblNhanVien.getColumnModel().getColumn(3).setPreferredWidth(40);
            tblNhanVien.getColumnModel().getColumn(4).setPreferredWidth(160);
            tblNhanVien.getColumnModel().getColumn(4).setMaxWidth(200);
            tblNhanVien.getColumnModel().getColumn(5).setPreferredWidth(30);
            tblNhanVien.getColumnModel().getColumn(6).setPreferredWidth(40);
            tblNhanVien.getColumnModel().getColumn(7).setPreferredWidth(40);
            tblNhanVien.getColumnModel().getColumn(8).setPreferredWidth(30);
            tblNhanVien.getColumnModel().getColumn(9).setPreferredWidth(45);
        }

        btThemNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btThemNV.setText("Thêm nhân viên");
        btThemNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemNVActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Tìm mã nhân viên");

        txtTimMaNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btTimNV.setText("TÌM");
        btTimNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTimNVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelNhanVienLayout = new javax.swing.GroupLayout(panelNhanVien);
        panelNhanVien.setLayout(panelNhanVienLayout);
        panelNhanVienLayout.setHorizontalGroup(
            panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btTimNV)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btThemNV)
                        .addGap(23, 23, 23))))
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelNhanVienLayout.setVerticalGroup(
            panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btThemNV)
                    .addComponent(jLabel6)
                    .addComponent(txtTimMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btTimNV))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayeredPane1.add(panelNhanVien, "card2");

        panelThongKe.setPreferredSize(new java.awt.Dimension(1006, 332));

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel34.setText("TỔNG DOANH THU");

        txtTongDoanhThu.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel46.setText("THỐNG KÊ DOANH THU");

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel43.setText("HÓA ĐƠN BỆNH ÁN");

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã hóa đơn", "Mã bệnh nhân", "Mã nhân viên", "Tổng tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(tblHoaDon);
        if (tblHoaDon.getColumnModel().getColumnCount() > 0) {
            tblHoaDon.getColumnModel().getColumn(3).setMinWidth(120);
            tblHoaDon.getColumnModel().getColumn(3).setPreferredWidth(130);
            tblHoaDon.getColumnModel().getColumn(3).setMaxWidth(150);
        }

        tblDoanhSoThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã thuốc", "Doanh số"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(tblDoanhSoThuoc);
        if (tblDoanhSoThuoc.getColumnModel().getColumnCount() > 0) {
            tblDoanhSoThuoc.getColumnModel().getColumn(0).setMinWidth(40);
            tblDoanhSoThuoc.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblDoanhSoThuoc.getColumnModel().getColumn(0).setMaxWidth(100);
            tblDoanhSoThuoc.getColumnModel().getColumn(1).setMinWidth(120);
            tblDoanhSoThuoc.getColumnModel().getColumn(1).setPreferredWidth(140);
            tblDoanhSoThuoc.getColumnModel().getColumn(1).setMaxWidth(200);
        }

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setText("DOANH SỐ THUỐC");

        btRefreshTK.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btRefreshTK.setText("REFRESH");
        btRefreshTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRefreshTKActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel21.setText("TÌM KIẾM HÓA ĐƠN");

        tblTimKiemHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã hóa đơn", "Ngày hóa đơn", "Tổng tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(tblTimKiemHD);
        if (tblTimKiemHD.getColumnModel().getColumnCount() > 0) {
            tblTimKiemHD.getColumnModel().getColumn(0).setMinWidth(40);
            tblTimKiemHD.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblTimKiemHD.getColumnModel().getColumn(0).setMaxWidth(100);
            tblTimKiemHD.getColumnModel().getColumn(1).setMinWidth(60);
            tblTimKiemHD.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblTimKiemHD.getColumnModel().getColumn(1).setMaxWidth(100);
            tblTimKiemHD.getColumnModel().getColumn(2).setMinWidth(80);
            tblTimKiemHD.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblTimKiemHD.getColumnModel().getColumn(2).setMaxWidth(150);
        }

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel27.setText("Tháng");

        cbbMonth.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbbMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel29.setText("Năm");

        btTimHD.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btTimHD.setText("TÌM");
        btTimHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTimHDActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel30.setText("DOANH THU");

        txtDoanhThuThang.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btInBaoCaoThang.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btInBaoCaoThang.setText("IN BÁO CÁO THÁNG");
        btInBaoCaoThang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btInBaoCaoThangActionPerformed(evt);
            }
        });

        btInBaoCaoNam.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btInBaoCaoNam.setText("IN BÁO CÁO NĂM");
        btInBaoCaoNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btInBaoCaoNamActionPerformed(evt);
            }
        });

        txtYear.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout panelThongKeLayout = new javax.swing.GroupLayout(panelThongKe);
        panelThongKe.setLayout(panelThongKeLayout);
        panelThongKeLayout.setHorizontalGroup(
            panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongKeLayout.createSequentialGroup()
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTongDoanhThu))
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel12))
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btRefreshTK)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btTimHD))
                    .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(panelThongKeLayout.createSequentialGroup()
                            .addComponent(btInBaoCaoNam)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btInBaoCaoThang))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelThongKeLayout.createSequentialGroup()
                            .addComponent(jLabel30)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtDoanhThuThang))
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(46, Short.MAX_VALUE))
            .addGroup(panelThongKeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel46)
                .addGap(156, 156, 156)
                .addComponent(jLabel21)
                .addGap(91, 91, 91))
        );
        panelThongKeLayout.setVerticalGroup(
            panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongKeLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jLabel12)
                    .addComponent(jLabel27)
                    .addComponent(cbbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(btTimHD)
                    .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btRefreshTK, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10))
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDoanhThuThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btInBaoCaoNam)
                            .addComponent(btInBaoCaoThang))
                        .addGap(7, 7, 7))))
        );

        jLayeredPane1.add(panelThongKe, "card8");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1034, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 1034, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1034, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThuocActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(true);
        panelThongKe.setVisible(false);
        
        resetColor(pnBttBenhNhan);
        resetColor(pnBttNhanVien);
        resetColor(pnBttKhamBenh);
        resetColor(pnBttTiepNhan);
        setColor(pnBttThuoc);
        resetColor(pnBttThongKe);
    }//GEN-LAST:event_jButtonThuocActionPerformed

    private void jButtonNhanvienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNhanvienActionPerformed
        // TODO add your handling code here:
        if(quanLy!=1){
            JOptionPane.showMessageDialog(panelKhamBenh, "Chỉ có quản lý mới được xem mục này");
        }
        else{
            panelBenhNhan.setVisible(false);
            panelNhanVien.setVisible(true);
            panelKhamBenh.setVisible(false);
            panelTiepNhan.setVisible(false);
            panelThuoc.setVisible(false);
            panelThongKe.setVisible(false);

            resetColor(pnBttBenhNhan);
            setColor(pnBttNhanVien);
            resetColor(pnBttKhamBenh);
            resetColor(pnBttTiepNhan);
            resetColor(pnBttThuoc);
            resetColor(pnBttThongKe);
        }
    }//GEN-LAST:event_jButtonNhanvienActionPerformed

    private void jButtonTiepNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTiepNhanActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(true);
        show_BenhNhan();
        
        panelThuoc.setVisible(false);
        panelThongKe.setVisible(false);
        
        resetColor(pnBttBenhNhan);
        resetColor(pnBttNhanVien);
        resetColor(pnBttKhamBenh);
        setColor(pnBttTiepNhan);
        resetColor(pnBttThuoc);
        resetColor(pnBttThongKe);
        
        
        
    }//GEN-LAST:event_jButtonTiepNhanActionPerformed

    private void jButtonKhamBenhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKhamBenhActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(true);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
        panelThongKe.setVisible(false);
        
        resetColor(pnBttBenhNhan);
        resetColor(pnBttNhanVien);
        setColor(pnBttKhamBenh);
        resetColor(pnBttTiepNhan);
        resetColor(pnBttThuoc);
        resetColor(pnBttThongKe);
    }//GEN-LAST:event_jButtonKhamBenhActionPerformed

    private void btThemNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemNVActionPerformed
        // TODO add your handling code here:
        insertNhanVienJDialog insertNV= new insertNhanVienJDialog(this, rootPaneCheckingEnabled);
        insertNV.setVisible(true);
    }//GEN-LAST:event_btThemNVActionPerformed

    private void tblNhanVienMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseReleased
        // TODO add your handling code here:
        if(evt.getButton()==MouseEvent.BUTTON3){
            if(evt.isPopupTrigger()&&tblNhanVien.getSelectedRowCount()!=0){
                PopUpMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tblNhanVienMouseReleased

    private void deleteNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteNVActionPerformed
        // TODO add your handling code here:
        int row=tblNhanVien.getSelectedRow();
        
        if(row!=-1){
            location=row;
            int manv=Integer.parseInt(tblNhanVien.getValueAt(row, 1).toString());
            String name=tblNhanVien.getValueAt(row, 2).toString();
            int response=JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn xóa "+name+" không ?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(response==JOptionPane.YES_OPTION){
                if(deleteNhanVienMouseClick(manv)==1){
                        JOptionPane.showMessageDialog(rootPane, "Nhân viên "+name+" đã bị xóa");
                }
                else{
                    JOptionPane.showMessageDialog(panelKhamBenh, "Xóa không thành công!");
                }
            }
        }
    }//GEN-LAST:event_deleteNVActionPerformed

    private void updateNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateNVActionPerformed
        // TODO add your handling code here:
        int row = tblNhanVien.getSelectedRow();
        location = row;
        showNhanVienDialog show = new showNhanVienDialog(this, rootPaneCheckingEnabled);
        show.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_updateNVActionPerformed

    private void btThemBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemBNActionPerformed
        boolean flag = true;
        if(txtTenBN.getText().equals("") || txtNgaySinh.getDate().equals("") || txtDiaChi.getText().equals("") || txtSDT.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin");
        }
        else{
            try{
                String tenBN    = txtTenBN.getText();
                String diaChi   = txtDiaChi.getText();
                String soDT     = txtSDT.getText();
                
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String ngaySinh = dateFormat.format(txtNgaySinh.getDate());
                java.util.Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(ngaySinh);
                java.sql.Date sqlDate = new java.sql.Date(date2.getTime());

                String gioiTinh = "";
                if(jMale.isSelected()){
                    gioiTinh += "Nam";
                }
                if(jFemale.isSelected()){
                    gioiTinh += "Nữ";
                }

                if(flag == true){
                    java.util.Date today = new java.util.Date();
                    txtTenBN.setText("");
                    txtNgaySinh.setDate(today);
                    txtDiaChi.setText("");
                    txtSDT.setText("");
                    
                    connect();
                    try {
                        String insert = "insert into SYS.QLPK_BENHNHAN(HOTEN,NGAYSINH,GIOITINH,SDT,DIACHI) values(?,?,?,?,?)";
                        PreparedStatement st = conn.prepareStatement(insert);
                        st.setString(1, tenBN);
                        st.setDate(2, sqlDate);
                        st.setString(3, gioiTinh);
                        st.setString(4, soDT);
                        st.setString(5, diaChi);
                        int n = st.executeUpdate();
                        
                        if(n > 0) {
                            JOptionPane.showMessageDialog(this, "Thêm thành công");
                             
                            String getmabn = "select MAX(MABN) from SYS.QLPK_BENHNHAN";
                            Statement stm = conn.createStatement();
                            ResultSet rs = stm.executeQuery(getmabn);
                            while(rs.next()){
                                this.maBN = rs.getInt(1);
                            }
                            
                            this.listBN = getBenhNhanList();
                            show_BN();
                            
                            InserpkJdialog show = new InserpkJdialog(this, true);
                            show.setVisible(true);
                        }
                        else JOptionPane.showMessageDialog(this, "Thêm thất bại");
                    } 
                    catch (SQLException ex) {
                        Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this, "Lỗi csdl");
                    }
                } 
            }
            catch (ParseException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi convert dữ liệu Ngày sinh");
            }
        }
    }//GEN-LAST:event_btThemBNActionPerformed

    private void tblBenhNhanMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBenhNhanMouseReleased
        // TODO add your handling code here:
        if(evt.getButton()==MouseEvent.BUTTON3){
            if(evt.isPopupTrigger()&&tblBenhNhan.getSelectedRowCount()!=0){
                PopUpMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tblBenhNhanMouseReleased

    private void deleteBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBNActionPerformed
        int row=tblBenhNhan.getSelectedRow();
        
        if(row!=-1){
            location=row;
            int mabn=Integer.parseInt(tblBenhNhan.getValueAt(row, 0).toString());
            String name=tblBenhNhan.getValueAt(row, 1).toString();
            int response=JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn xóa "+name+" không ?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(response==JOptionPane.YES_OPTION){
                if(deleteBenhNhanMouseClick(mabn)==1){
                        JOptionPane.showMessageDialog(rootPane, "Bệnh nhân "+name+" đã bị xóa");
                }
                else{
                    JOptionPane.showMessageDialog(panelKhamBenh, "Xóa không thành công!");
                }
            }
        }
    }//GEN-LAST:event_deleteBNActionPerformed

    private void btThemThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemThuocActionPerformed
        // TODO add your handling code here:
        insertThuocJDialog insertThuoc= new insertThuocJDialog(this, rootPaneCheckingEnabled);
        insertThuoc.setVisible(true);
    }//GEN-LAST:event_btThemThuocActionPerformed

    private void txtTimMaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimMaHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimMaHDActionPerformed

    private void cbbChuanDoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbChuanDoanActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cbbChuanDoanActionPerformed

    private void cbbBacSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbBacSiActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cbbBacSiActionPerformed

    private void btTimMatoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimMatoaActionPerformed
        try {
            // TODO add your handling code here:
            //String maHD= txtTimMaHD.getText();
            int mahd=Integer.parseInt(txtTimMaHD.getText());
            String query="select MAHD from SYS.QLPK_TOATHUOC WHERE MAHD="+mahd;
            
            connect();
            Statement stt=conn.createStatement();
            ResultSet rs=stt.executeQuery(query);
            
            int temp=0;
            while(rs.next()){
                temp=rs.getInt(1);
            }
            
            if(temp<1){
                JOptionPane.showMessageDialog(panelKhamBenh, "Không tồn tại mã hóa đơn");
                txtTimMaHD.setText("");
            }
            else{
                //tạo biến maHoaDon để dialog truy vấn và xuất csdl ra màn hình dialog
                maHoaDon=Integer.parseInt(txtTimMaHD.getText());
                //chạy dialog xuất thông tin
                showToaThuocJDialog showTT=new showToaThuocJDialog(this, rootPaneCheckingEnabled);
                showTT.setVisible(true);
                txtTimMaHD.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//GEN-LAST:event_btTimMatoaActionPerformed

    private void btThemBenhAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemBenhAnActionPerformed
        try {                                             
            try{
                if(txtTrieuChung.getText().equals("")){
                    JOptionPane.showMessageDialog(panelKhamBenh, "Vui lòng điền đầy đủ thông tin");
                }
            }
            catch(Exception e){
                System.out.println("Không được để trống vùng dữ liệu");
            }
            
            String trieuChung   = txtTrieuChung.getText();
            String chuanDoan    = cbbChuanDoan.getSelectedItem().toString();
            String bacSiKham    = cbbBacSi.getSelectedItem().toString();
            int mahd            = Integer.parseInt(txtMaHD.getText());
            
            connect();
            int manv = 0;
            String getMaNV = "select MANV FROM SYS.QLPK_NHANVIEN WHERE HOTEN='"+bacSiKham+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(getMaNV);
            while(rs.next()){
                manv = rs.getInt(1);
            }
            conn.close();
            
            connect();
            String query = "UPDATE SYS.QLPK_HOADON SET MANV=?, TRIEUCHUNG=?, CHUANDOAN=?  WHERE MAHD="+mahd;
            PreparedStatement pre=conn.prepareStatement(query);
            pre.setInt(1, manv);
            pre.setString(2, trieuChung);
            pre.setString(3, chuanDoan);
            
            int a = pre.executeUpdate();
            JOptionPane.showMessageDialog(panelKhamBenh, "Thêm "+a+" bệnh án thành công");
            
            String diaChi = "";
            String gioiTinh = "";
            String query1 = "select GIOITINH, DIACHI FROM SYS.QLPK_BENHNHAN WHERE MABN="+txtMaBNKham.getText();
            Statement stm = conn.createStatement();
            ResultSet rss = stm.executeQuery(query1);
            while(rss.next()){
                gioiTinh = rss.getString(1);
                diaChi = rss.getString(2);
            }
            
            
            try {    
                Map<String, Object> parameters = new HashMap<String, Object>();
                JasperDesign jasperDesign = JRXmlLoader.load("C:\\Users\\T470\\Documents\\Java\\DoAn\\Java_IS216.M21_21\\src\\report\\report1.jrxml");
                parameters.put("maBN", txtMaBNKham.getText().toString());
                parameters.put("maHD", txtMaHD.getText().toString());
                parameters.put("hoTen", txtHoTen.getText().toString());
                parameters.put("gioiTinh", gioiTinh);
                parameters.put("diaChi", diaChi);
                parameters.put("chuanDoan", cbbChuanDoan.getSelectedItem().toString());
                parameters.put("bacSi", cbbBacSi.getSelectedItem().toString());

                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport , parameters, conn);

                JasperViewer.viewReport(jasperPrint);
                conn.close();
                refreshKhamBenh();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi tạo hóa đơn");
                e.printStackTrace();
            }
        } 
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(panelKhamBenh, "lỗi truy vấn csdl");
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null,ex);
        }
    }//GEN-LAST:event_btThemBenhAnActionPerformed

    private void txtTimMaThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimMaThuocActionPerformed
        DefaultTableModel table= (DefaultTableModel)tblThuoc.getModel();
        String search= txtTimMaThuoc.getText().toUpperCase();
        TableRowSorter<DefaultTableModel> tr=new TableRowSorter<DefaultTableModel>(table);
        tblThuoc.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(search));
    }//GEN-LAST:event_txtTimMaThuocActionPerformed

    private void btThemThuocToToaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemThuocToToaActionPerformed
        // TODO add your handling code here:
        inputThuocToToa();
        txtCachDung.setText("");
        soLuong.setValue(0);
    }//GEN-LAST:event_btThemThuocToToaActionPerformed

    private void btTimPKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimPKActionPerformed
        try {
            connect();
            String query = "SELECT H.MABN, B.HOTEN, H.LIDOKHAM FROM SYS.QLPK_HOADON H, SYS.QLPK_BENHNHAN B WHERE H.MABN=B.MABN AND H.MAHD="+txtTimMaPK.getText();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            if(!rs.isBeforeFirst()){
                JOptionPane.showMessageDialog(panelKhamBenh, "Không tồn tại mã phiếu khám");
            }
            else{
                while(rs.next()){
                    String maBN = String.valueOf(rs.getInt(1));
                    txtMaBNKham.setText(maBN);
                    txtHoTen.setText(rs.getString(2));
                    txtLiDoKham.setText(rs.getString(3));
                }
                txtTrieuChung.setText("");
                txtCachDung.setText("");
                tblModelToaThuoc.setRowCount(0);
                txtMaHD.setText(txtTimMaPK.getText());
                updateCBBTienSu();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btTimPKActionPerformed

    private void updateBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBNActionPerformed
        int row=tblBenhNhan.getSelectedRow();
        location=row;
        showBenhNhanDialog show=new showBenhNhanDialog(this, true);
        show.setVisible(true);
    }//GEN-LAST:event_updateBNActionPerformed

    private void txtTimBenhNhanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimBenhNhanKeyReleased
        // TODO add your handling code here:
//        DefaultTableModel table= (DefaultTableModel)tblBenhNhan.getModel();
//        String search= txtTimBenhNhan.getText().toUpperCase();
//        TableRowSorter<DefaultTableModel> tr=new TableRowSorter<DefaultTableModel>(table);
//        tblBenhNhan.setRowSorter(tr);
//        tr.setRowFilter(RowFilter.regexFilter(search));
    }//GEN-LAST:event_txtTimBenhNhanKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            int maThuoc = Integer.parseInt(txtTimMaThuoc.getText());
            String sql="SELECT MATHUOC FROM SYS.QLPK_THUOC WHERE XOA=0 AND MATHUOC="+maThuoc;
            connect();
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(sql);
            //tạo biến tạm thời chứa mã nv là temp
            int temp=0;
            while(rs.next()){
                temp=rs.getInt(1);
            }
            if(temp<1){
                JOptionPane.showMessageDialog(this, "Không tồn tại thuốc");
                txtTimMaThuoc.setText("");
            }
            else{
                findThuoc(temp);
                showThuocDialog show=new showThuocDialog(this, true);
                show.setVisible(true);
                txtTimMaThuoc.setText("");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelKhamBenh, "Mã thuốc là số");
            txtTimMaThuoc.setText("");
        }  
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btTimNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimNVActionPerformed
        try {
            int maNV=Integer.parseInt(txtTimMaNV.getText());

            String sql="SELECT MANV FROM SYS.QLPK_NHANVIEN WHERE MANV="+maNV;
            connect();
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(sql);
            //tạo biến tạm thời chứa mã nv là temp
            int temp=0;
            while(rs.next()){
                temp=rs.getInt(1);
            }
            if(temp<1){
                JOptionPane.showMessageDialog(panelKhamBenh, "Không tồn tại nhân viên");
            }
            else{
                //chạy hàm findNhanVien để set location của nhân viên cần show
                findNhanVien(temp);
                showNhanVienDialog show=new showNhanVienDialog(this, true);
                show.setVisible(true);
                txtTimMaNV.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Mã phiếu khám phải là số và không chứa kí tự khác");
            txtTimMaNV.setText("");
        }
    }//GEN-LAST:event_btTimNVActionPerformed

    private void btTimMaBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimMaBNActionPerformed
        //boolean flag = true;
        try {
                String maBN = String.valueOf(txtTimBenhNhan.getText());
                String sql="SELECT MABN FROM SYS.QLPK_BENHNHAN WHERE MABN="+maBN;

                connect();
                Statement stm=conn.createStatement();
                ResultSet rs=stm.executeQuery(sql);
                //tạo biến tạm thời chứa mã nv là temp
                int temp=0;
                while(rs.next()){
                    temp=rs.getInt(1);
                }
                if(temp<1){
                    JOptionPane.showMessageDialog(panelKhamBenh, "Không tồn tại mã bệnh nhân");
                }
                else{
                    findBenhNhan(temp);
                    showBenhNhanDialog show=new showBenhNhanDialog(this, true);
                    show.setVisible(true);
                    txtTimBenhNhan.setText("");
                }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelKhamBenh, "Mã bệnh nhân là số");
        }  
    }//GEN-LAST:event_btTimMaBNActionPerformed

    private void txtTimBenhNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimBenhNhanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimBenhNhanActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        boolean flag = true;
        try {
            if(flag == true){
                int maBN = Integer.parseInt(txtTimMaBN.getText());
                String sql="SELECT MABN FROM SYS.QLPK_BENHNHAN WHERE MABN="+maBN;
                
                connect();
                Statement stm=conn.createStatement();
                ResultSet rs=stm.executeQuery(sql);
                //tạo biến tạm thời chứa mã nv là temp
                int temp=0;
                while(rs.next()){
                    temp=rs.getInt(1);
                }
                if(temp<1){
                    JOptionPane.showMessageDialog(this, "Không tồn tại mã bệnh nhân");
                }
                else{
                    int response=JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn thêm phiếu khám cho bệnh nhân này không ?", "Thêm phiếu khám", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(response==JOptionPane.YES_OPTION){
                        this.maBN=temp;
                        InserpkJdialog show=new InserpkJdialog(this, true);
                        show.setVisible(true);
                    }
                    txtTimMaBN.setText("");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelKhamBenh, "Mã bê?nh nhân là số");
            flag = false;
        }  
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbbLoaiThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLoaiThuocActionPerformed
        try {
            String loaiThuoc = cbbLoaiThuoc.getSelectedItem().toString();
            connect();
            String donvi="";
            String getMaThuoc="select DONVI FROM SYS.QLPK_THUOC WHERE LOAITHUOC='"+loaiThuoc+"'";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(getMaThuoc);
            while(rs.next()){
                donvi=rs.getString(1);
            }
            txtDonVi.setText(donvi);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cbbLoaiThuocActionPerformed

    private void jButtonThongkeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThongkeActionPerformed
        // TODO add your handling code here:
        if(quanLy!=1){
            JOptionPane.showMessageDialog(panelKhamBenh, "Chỉ có quản lý mới được xem mục này");
        }
        else{
            panelBenhNhan.setVisible(false);
            panelNhanVien.setVisible(false);
            panelKhamBenh.setVisible(false);
            panelTiepNhan.setVisible(false);
            panelThuoc.setVisible(false);
            panelThongKe.setVisible(true);

            resetColor(pnBttBenhNhan);
            resetColor(pnBttNhanVien);
            resetColor(pnBttKhamBenh);
            resetColor(pnBttTiepNhan);
            resetColor(pnBttThuoc);
            setColor(pnBttThongKe);
        }
    }//GEN-LAST:event_jButtonThongkeActionPerformed

    private void jButtonBenhNhan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBenhNhan1ActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(true);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
        panelThongKe.setVisible(false);
        
        setColor(pnBttBenhNhan);
        resetColor(pnBttNhanVien);
        resetColor(pnBttKhamBenh);
        resetColor(pnBttTiepNhan);
        resetColor(pnBttThuoc);
        resetColor(pnBttThongKe);
    }//GEN-LAST:event_jButtonBenhNhan1ActionPerformed

    private void btRefreshTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshTKActionPerformed
        // TODO add your handling code here:
        tblModelHoaDon.setRowCount(0);
        tblModelDoanhSoThuoc.setRowCount(0);
        tblModelTimKiemHD.setRowCount(0);
        
        
        tinhTongDoanhThu();
        updateTblHoaDon();
        updateTblDoanhSoThuoc();
        long money=tinhTongDoanhThu();
        txtTongDoanhThu.setText(rutGonDoanhThu(money)+" VND");
        //txtYear.setText("");
    }//GEN-LAST:event_btRefreshTKActionPerformed

    private void btTimHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimHDActionPerformed
        tblModelTimKiemHD.setRowCount(0);
        txtDoanhThuThang.setText("");
        try {
            // TODO add your handling code here:
            connect();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
            String query="SELECT MAHD, NGAYKHAM, THANHTIEN " +
                    "FROM SYS.QLPK_HOADON " +
                    "WHERE EXTRACT(YEAR FROM NGAYKHAM)=" + txtYear.getSelectedItem().toString()+
                    " AND EXTRACT(MONTH FROM NGAYKHAM)=" + cbbMonth.getSelectedItem().toString();
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String ngaySinh = dateFormat.format(rs.getDate(2));
                Object[] obj={rs.getInt(1),ngaySinh , rs.getInt(3)};
                tblModelTimKiemHD.addRow(obj);
                //txtDoanhThuThang.setText(rutGonDoanhThu(rs.getInt(3))+" VNĐ");
            }
            
            String query2="SELECT SUM(THANHTIEN) " +
                    "FROM SYS.QLPK_HOADON " +
                    "WHERE EXTRACT(YEAR FROM NGAYKHAM)=" + txtYear.getSelectedItem().toString() +
                    " AND EXTRACT(MONTH FROM NGAYKHAM)=" + cbbMonth.getSelectedItem().toString();
            //Statement stm1=conn.createStatement();
            ResultSet rs2=stm.executeQuery(query2);
            while(rs2.next()){
                txtDoanhThuThang.setText(rutGonDoanhThu(rs2.getInt(1))+" VNĐ");
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btTimHDActionPerformed

    private void btInBaoCaoThangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInBaoCaoThangActionPerformed
        try {
                connect();
                try {
                    Map<String, Object> parameters = new HashMap<String, Object>();
                    JasperDesign jdesign = JRXmlLoader.load("C:\\Users\\T470\\Documents\\Java\\DoAn\\Java_IS216.M21_21\\src\\report\\reportThongKe.jrxml");
                    //parameters.put("TongDoanhThu", rutGonDoanhThu(thanhtien)+" VNĐ");
                    parameters.put("Thang", cbbMonth.getSelectedItem().toString());
                    parameters.put("Nam", txtYear.getSelectedItem().toString());

                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, parameters, conn);
                    JasperViewer.viewReport(jprint);

                } catch (Exception ex) {
                    Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_btInBaoCaoThangActionPerformed

    private void btInBaoCaoNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInBaoCaoNamActionPerformed
        try {
            connect();
            try {
                Map<String, Object> parameters = new HashMap<String, Object>();
                JasperDesign jdesign = JRXmlLoader.load("C:\\Users\\T470\\Documents\\Java\\DoAn\\Java_IS216.M21_21\\src\\report\\newReport2.jrxml");
                //parameters.put("TongDoanhThu", rutGonDoanhThu(thanhtien)+" VNĐ");
                parameters.put("Nam", txtYear.getSelectedItem().toString());

                JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                JasperPrint jprint = JasperFillManager.fillReport(jreport, parameters, conn);
                JasperViewer.viewReport(jprint);

            } catch (Exception ex) {
                Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btInBaoCaoNamActionPerformed

    private void jMaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMaleActionPerformed
        jFemale.setSelected(false);
    }//GEN-LAST:event_jMaleActionPerformed

    private void jFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFemaleActionPerformed
        // TODO add your handling code here:
        jMale.setSelected(false);
    }//GEN-LAST:event_jFemaleActionPerformed

    private void tblBNMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBNMouseReleased
        // TODO add your handling code here:
        //        if(evt.getButton()==MouseEvent.BUTTON3){
            //            if(evt.isPopupTrigger()&&tblBN.getSelectedRowCount()!=0){
                //                PopUpMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                //            }
            //        }
    }//GEN-LAST:event_tblBNMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws ClassNotFoundException, SQLException{
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(() -> {
//            new JFrame().setVisible(true);
//        });
        
//        Connection con=DriverManager.getConnection(
//                "jdbc:oracle:thin:@localhost:1521:orcl","system","user_java123");
//        //Connection con=DriverManger
//        Statement stmt=con.createStatement();
//        //thực hiện câu truy vấn
//        ResultSet rs=stmt.executeQuery("select * from NHANVIEN");
//        while(rs.next())
//            System.out.print(rs.getInt(1)+"\t"+ rs.getString(2)+"\t"+ rs.getString(3)+"\t"+ rs.getString(4)+"\t"+ 
//                        rs.getString(5)+"\t"+ rs.getString(6)+"\t"+ rs.getString(7)+"\t"+ rs.getString(8)+"\t"+ rs.getLong(9));
//        con.close();
        BaseFrame frame=new BaseFrame();
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu PopUpMenu;
    private javax.swing.JPopupMenu PopUpMenu1;
    private javax.swing.JButton btInBaoCaoNam;
    private javax.swing.JButton btInBaoCaoThang;
    private javax.swing.JButton btRefreshTK;
    private javax.swing.JButton btThemBN;
    private javax.swing.JButton btThemBenhAn;
    private javax.swing.JButton btThemNV;
    private javax.swing.JButton btThemThuoc;
    private javax.swing.JButton btThemThuocToToa;
    private javax.swing.JButton btTimHD;
    private javax.swing.JButton btTimMaBN;
    private javax.swing.JButton btTimMatoa;
    private javax.swing.JButton btTimNV;
    private javax.swing.JButton btTimPK;
    private javax.swing.JComboBox<String> cbbBacSi;
    private javax.swing.JComboBox<String> cbbChuanDoan;
    private javax.swing.JComboBox<String> cbbLoaiThuoc;
    private javax.swing.JComboBox<String> cbbMonth;
    private javax.swing.JComboBox<String> cbbTienSu;
    private javax.swing.JMenuItem deleteBN;
    private javax.swing.JMenuItem deleteNV;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonBenhNhan1;
    private javax.swing.JButton jButtonKhamBenh;
    private javax.swing.JButton jButtonNhanvien;
    private javax.swing.JButton jButtonThongke;
    private javax.swing.JButton jButtonThuoc;
    private javax.swing.JButton jButtonTiepNhan;
    private javax.swing.JRadioButton jFemale;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JRadioButton jMale;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel_title;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel label_drugs;
    private javax.swing.JPanel panelBenhNhan;
    private javax.swing.JPanel panelKhamBenh;
    private javax.swing.JPanel panelNhanVien;
    private javax.swing.JPanel panelThongKe;
    private javax.swing.JPanel panelThuoc;
    private javax.swing.JPanel panelTiepNhan;
    private javax.swing.JPanel pnBttBenhNhan;
    private javax.swing.JPanel pnBttKhamBenh;
    private javax.swing.JPanel pnBttNhanVien;
    private javax.swing.JPanel pnBttThongKe;
    private javax.swing.JPanel pnBttThuoc;
    private javax.swing.JPanel pnBttTiepNhan;
    private javax.swing.JSpinner soLuong;
    private javax.swing.JTable tblBN;
    private javax.swing.JTable tblBenhNhan;
    private javax.swing.JTable tblDoanhSoThuoc;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTable tblThuoc;
    private javax.swing.JTable tblTimKiemHD;
    private javax.swing.JTable tblToaThuoc;
    private javax.swing.JTextField txtCachDung;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDoanhThuThang;
    private javax.swing.JTextField txtDonVi;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtLiDoKham;
    private javax.swing.JTextField txtMaBNKham;
    private javax.swing.JTextField txtMaHD;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenBN;
    private javax.swing.JTextField txtTimBenhNhan;
    private javax.swing.JTextField txtTimMaBN;
    private javax.swing.JTextField txtTimMaHD;
    private javax.swing.JTextField txtTimMaNV;
    private javax.swing.JTextField txtTimMaPK;
    private javax.swing.JTextField txtTimMaThuoc;
    private javax.swing.JTextField txtTongDoanhThu;
    private javax.swing.JTextField txtTrieuChung;
    private javax.swing.JComboBox<String> txtYear;
    private javax.swing.JMenuItem updateBN;
    private javax.swing.JMenuItem updateNV;
    // End of variables declaration//GEN-END:variables
}
