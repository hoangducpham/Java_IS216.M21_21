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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.BenhNhan;
import model.NhanVien;
import model.PhieuKham;
import model.Thuoc;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
//import com.sun.imageio.plugins.png.RowFilter;
//import java.lang.Object;
//import javax.swing.RowFilter;
//import javax.swing.table.TableRowSorter;


/**
 *
 * @author MyPC
 */
public class BaseFrame extends javax.swing.JFrame {
    int location=-1;
    private PlaceHolder p1, p2, p3;
    public int getlocation(){
        return location;
    }
    
    public void resetLocation(){
        location=-1;
    }
//--------------------------------------------------------------------------Nhân viên--------------------------------------------
    ArrayList<NhanVien> listNV= nhanVienList();
    private int maNhanVien=0;
    public int getMaNhanVien(){
        return maNhanVien;
    }
    //nhập các nhân viên từ database vào list
    public ArrayList<NhanVien> nhanVienList(){
        ArrayList<NhanVien> nhanViensList=new ArrayList<>();
        String url="jdbc:oracle:thin:@localhost:1521:orcl";
        try{
            Connection conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            String query1="SELECT * FROM NHANVIEN";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query1);
            NhanVien nv;
            while(rs.next()){
                Date ngaySinh=rs.getDate("NGAYSINH");
                //Date date=(Date) new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(ngaySinh);
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");  
                String strDate = dateFormat.format(ngaySinh);  
                nv=new NhanVien(rs.getInt("MANV"), rs.getString("HOTEN"),strDate,rs.getString("DIACHI"),rs.getString("GIOITINH"),
                    rs.getString("SDT"), rs.getString("CHUCDANH"), rs.getString("MAPHONG"), rs.getLong("LUONG"));
                nhanViensList.add(nv);
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không connect được database");
        }
//        catch (ParseException ex) {
//            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return nhanViensList;
        //conn.close();
    }
    
    private DefaultTableModel tblModelNhanVien;
    public ArrayList<NhanVien> getList(){
        return listNV;
    }
    
    //lấy nhân viên sau khi tìm kiếm
    public NhanVien getNhanVien(){
        return listNV.get(location);
    }
    
    public void findNhanVien(int maNV){
        for(int i=0; i<listNV.size(); i++){
            if(listNV.get(i).getMaNV()==maNV){
                location=i;
                //JOptionPane.showMessageDialog(panelKhamBenh, i);
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
    
    private Connection conn;
    public void connect(){
        try {
            String url="jdbc:oracle:thin:@localhost:1521:orcl";
            conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    
    //tìm kiếm vị trí nhân viên
    public int searchNhanVien(ArrayList<NhanVien> arr, long target) {
        for(int i=0; i<arr.size(); i++){
            int x=arr.get(i).getMaNV();
            if (x==target) {
                return i;
            }
        }
        return -1;
    }
    
    
    
    //sau khi nhập database và list, ta fetch data vào table để hiển thị ra màn hình
    public void show_nhanVien(){
        tblModelNhanVien=(DefaultTableModel)tblNhanVien.getModel();
        //tblNhanVien.setModel(tblModelNhanVien);
        int j=1;
        for(int i=0; i<listNV.size();i++){
            Object[] row={j, listNV.get(i).getMaNV(),listNV.get(i).getTenNV(),listNV.get(i).getNgSinh(),listNV.get(i).getDiaChi(),
                listNV.get(i).getGioiTinh(),listNV.get(i).getSoDT(),listNV.get(i).getChucDanh(),listNV.get(i).getMaPhong(),listNV.get(i).getLuong()};
            tblModelNhanVien.addRow(row);
            j++;
        }
        setVisible(true);
    }
    
    //sau khi thêm thông tin nhân viên ở insertNhanVienDialog, ta insert dữ liệu đó vào list và database, sau đó add nhân viên vào table
    public int addNhanVien(){
        try {
            DefaultTableModel tblModelNhanVien=(DefaultTableModel)tblNhanVien.getModel();
            int i=listNV.size()-1;
            int STT=listNV.size();
            int MaNV=listNV.get(i).getMaNV();
            String TenNV=listNV.get(i).getTenNV();
            //String NgSinh=listNV.get(i).getNgSinh();
            String NgSinh=listNV.get(i).getNgSinh();
            
            String date=listNV.get(i).getNgSinh();
            
            java.util.Date date2 = new SimpleDateFormat("MMMM d, yyyy").parse(date);
            java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
            
            String DiaChi=listNV.get(i).getDiaChi();
            String GioiTinh=listNV.get(i).getGioiTinh();
            String SoDT=listNV.get(i).getSoDT();
            String ChucDanh=listNV.get(i).getChucDanh();
            int MaPhong=Integer.parseInt(listNV.get(i).getMaPhong());
            long Luong=listNV.get(i).getLuong();
            //thêm vô csdl
            connect();
            String insert="insert into NHANVIEN values(?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pre=conn.prepareStatement(insert);
            int phanQuyen=1;
            if(ChucDanh.equalsIgnoreCase("nhan vien"))
                phanQuyen=1;
            else if (ChucDanh.equalsIgnoreCase("quan ly"))
                phanQuyen=2;
            else{
                JOptionPane.showMessageDialog(panelKhamBenh, "Nhập chức danh là nhan vien hoặc quan ly");
                return 0;
            }
            
            //Nhập dữ liệu vô csdl
            pre.setInt(1, MaNV);
            pre.setString(2, TenNV);
            pre.setDate(3, sqlDate);
            pre.setString(4, GioiTinh);
            pre.setString(5, DiaChi);
            pre.setString(6, SoDT);
            pre.setString(7, "");
            pre.setString(8, "");
            pre.setInt(9, phanQuyen);
            pre.setString(10, ChucDanh);
            pre.setInt(11, MaPhong);
            pre.setLong(12, Luong);
            int x=pre.executeUpdate();
            JOptionPane.showMessageDialog(panelKhamBenh, x+" dòng đã được thêm vào csdl");
            
            Object[] objs={STT, MaNV, TenNV, NgSinh, DiaChi, GioiTinh ,SoDT, ChucDanh, MaPhong,Luong};
            tblModelNhanVien.addRow(objs);
            conn.close();
            return 1;
        }catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        }catch (ParseException ex){
            //Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi convert dữ liệu Ngày sinh");
            return 0;
        }
    }
    
    public int deleteNhanVien(){
        try {
            tblModelNhanVien.removeRow(location);
            listNV.remove(location);
            location=-1;
//            String url="jdbc:oracle:thin:@localhost:1521:orcl";
//            Connection conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            connect();
            String delete="delete from NHANVIEN where MANV="+txtTimMaNV.getText().toString();
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            txtTimMaNV.setText(""); 
            conn.close();
            return x;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        }  
    }
    
    public int deleteNhanVienMouseClick(int manv){
        try {
            tblModelNhanVien.removeRow(location);
            listNV.remove(location);
            location=-1;
            connect();
            String delete="delete from NHANVIEN where MANV="+manv;
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            conn.close();
            return x;
        } catch (SQLException ex) {
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
    
    public int updateNhanVien(){
        try {
            DefaultTableModel tblModelNhanVien=(DefaultTableModel)tblNhanVien.getModel();
            //Gán thông tin cho biến
            int i=location;
            int STT=listNV.size();
            int MaNV=listNV.get(i).getMaNV();
            String TenNV=listNV.get(i).getTenNV();
            //String NgSinh=listNV.get(i).getNgSinh();
            String NgSinh=listNV.get(i).getNgSinh();
            
            String date=listNV.get(i).getNgSinh();
            java.util.Date date2 = new SimpleDateFormat("MMMM d, yyyy").parse(date);
            java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
            
            String DiaChi=listNV.get(i).getDiaChi();
            String GioiTinh=listNV.get(i).getGioiTinh();
            
            String SoDT=listNV.get(i).getSoDT();
            String ChucDanh=listNV.get(i).getChucDanh();
            int MaPhong=Integer.parseInt(listNV.get(i).getMaPhong());
            long Luong=listNV.get(i).getLuong();
            
            //kết nối csdl
//            String url="jdbc:oracle:thin:@localhost:1521:orcl";
//            Connection conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            connect();
            //update dtb
            String update="update NHANVIEN set HOTEN=?, NGAYSINH=?, GIOITINH=?, DIACHI=?,"
                    + "SDT=?, USERNAME=?, PASSWORD=?, PHANQUYEN=?, CHUCDANH=?, MAPHONG=?,"
                    + "LUONG=? WHERE MANV="+MaNV;
            PreparedStatement pre=conn.prepareStatement(update);
            int phanQuyen=1;
            if(ChucDanh.equalsIgnoreCase("nhan vien"))
                phanQuyen=1;
            else if (ChucDanh.equalsIgnoreCase("quan ly"))
                phanQuyen=2;
            else{
                JOptionPane.showMessageDialog(panelKhamBenh, "Nhập chức danh là nhan vien hoặc quan ly");
                return 0;
            }
            
            //Nhập dữ liệu vô csdl
            
            pre.setString(1, TenNV);
            pre.setDate(2, sqlDate);
            pre.setString(3, GioiTinh);
            pre.setString(4, DiaChi);
            pre.setString(5, SoDT);
            pre.setString(6, "");
            pre.setString(7, "");
            pre.setInt(8, phanQuyen);
            pre.setString(9, ChucDanh);
            pre.setInt(10, MaPhong);
            pre.setLong(11, Luong);
            //pre.setInt(12, MaNV);
            int x=pre.executeUpdate();
            JOptionPane.showMessageDialog(panelKhamBenh, x+" dòng đã được cập nhật");
            
            //tblModelNhanVien.addRow(objs);
            conn.close();
            //update Jtable
            updateNhanVienJtable(location);
            location=-1;
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        } catch (ParseException ex){
            //Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi convert dữ liệu Ngày sinh");
            return 0;
        }
    }
    
 //---------------------------------------------------------------------------Thuốc--------------------------------------------------------------
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
            listBN.remove(location);
            location=-1;
            connect();
            String delete="delete from BENHNHAN where MABN="+mabn;
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            
            conn.close();
            return x;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        }  //catch (IndexOutOfBoundsException e){
//            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi out of bound index");
//            return 0;
//        }
    }
       

    
    public void show_BenhNhan(){
        tblModel =(DefaultTableModel)tblBenhNhan.getModel();
        int j = 1;
        for(int i=0; i<listBN.size(); i++){
            Object[] row={listBN.get(i).getMaBN(), listBN.get(i).getHoTen(), listBN.get(i).getNgaySinh(), 
                         listBN.get(i).getGioiTinh(), listBN.get(i).getDiaChi(), listBN.get(i).getSDT()};
            tblModel.addRow(row);
        }
    }
    
//--------------------------------------------------------------------------Tiếp nhận-----------------------------------------
    //private Connection conn;
    private DefaultTableModel tblModel;
    ArrayList<BenhNhan> listBN = getBenhNhanList(); 
        
    public ArrayList<BenhNhan> getBenhNhanList(){
        ArrayList<BenhNhan> BenhNhansList = new ArrayList<>();
        try{
            connect();
            String query2 = "SELECT * FROM BENHNHAN";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            BenhNhan bn;
            while(rs.next()){          
                Date ngaySinh = rs.getDate("NGAYSINH");
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");  
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
   
    
    public void addBenhNhan(BenhNhan s){
        listBN.add(s);
        tblModel = (DefaultTableModel)tblBN.getModel();
        tblModel.setRowCount(0);
        int i = 1;
        for(BenhNhan bn: listBN){
            tblModel.addRow(new Object[]{i,bn.getMaBN(),bn.getHoTen(),bn.getNgaySinh(),bn.getGioiTinh(),bn.getDiaChi(),bn.getSDT()});
            i++;
        }
    }
    
    public void setMaBN(){
        try {
            connect();
            int count=0;
            String query="SELECT MABN FROM BENHNHAN ORDER BY MABN DESC FETCH FIRST 1 ROW ONLY";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                count=rs.getInt(1)+1;
            }
            String x=String.valueOf(count);
            txtMaBN.setText(x);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void show_BN(){
        //ArrayList<BenhNhan> list = getBenhNhanList();
        tblModel =(DefaultTableModel)tblBN.getModel();
        int j = 1;
        for(int i=0; i<listBN.size(); i++){
            Object[] row={j, listBN.get(i).getMaBN(), listBN.get(i).getHoTen(), listBN.get(i).getNgaySinh(), 
                         listBN.get(i).getGioiTinh(), listBN.get(i).getDiaChi(), listBN.get(i).getSDT()};
            tblModel.addRow(row);
            j++;
        }
//        int id=1;
//        boolean flag;
//        while(true){
//            flag=false;
//            for(BenhNhan nv:listBN){
//                if(id==nv.getMaBN()){
//                    flag=true;
//                    break;
//                }
//            }
//            if(!flag) break;
//            ++id;
//        }
        //txtMaBN.setText(id+"");
        setMaBN();
        txtMaBN.setEditable(false);
        updateCBBchuanDoan();
        updateCBBbacSi();
    }
    
    public void updateCBBchuanDoan(){
        try{
            connect();
            String query2 = "SELECT LOAIBENH FROM LOAIBENH";
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
            String query2 = "SELECT * FROM NHANVIEN";
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
            String query2 = "SELECT * FROM THUOC";
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
    public void show_Thuoc(){
        tblModel =(DefaultTableModel)tblThuoc.getModel();
        for(int i=0; i<listThuoc.size(); i++){
            Object[] row={listThuoc.get(i).getMaThuoc(), listThuoc.get(i).getLoaiThuoc(), listThuoc.get(i).getDonVi(), 
                         listThuoc.get(i).getDonGia(), listThuoc.get(i).getNoiSX()};
            tblModel.addRow(row);
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
            DefaultTableModel tblModelThuoc=(DefaultTableModel)tblThuoc.getModel();
            int i=listThuoc.size()-1;
            int STT=listThuoc.size();
            int MaThuoc=listThuoc.get(i).getMaThuoc();
            String TenThuoc=listThuoc.get(i).getLoaiThuoc();
            String DonVi=listThuoc.get(i).getDonVi();
            int DonGia=listThuoc.get(i).getDonGia();
            String NoiSX=listThuoc.get(i).getNoiSX();
            Object[] objs={STT, MaThuoc, TenThuoc, DonVi, DonGia,NoiSX};
            
            //thêm vô csdl
            connect();
            String insert="insert into THUOC values(?,?,?,?,?)";
            PreparedStatement pre=conn.prepareStatement(insert);
           
            //Nhập dữ liệu vô csdl
            pre.setInt(1, MaThuoc);
            pre.setString(2, TenThuoc);
            pre.setString(3, DonVi);
            pre.setInt(4, DonGia);
            pre.setString(5, NoiSX);
            int x=pre.executeUpdate();
            JOptionPane.showMessageDialog(this, x+" dòng đã được thêm vào csdl");
            
            tblModelThuoc.addRow(objs);
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
//--------------------------------------------------------------------------Khám bệnh-------------------------------------
    private DefaultTableModel tblModelToaThuoc;
    ArrayList<PhieuKham> listPK = getPhieuKhamList(); 
    
    public void updateCBBloaiThuoc(){
        try{
            connect();
            String query2 = "SELECT LOAITHUOC FROM THUOC";
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
    
    public void setMaHD(){
        try {
            connect();
            int count=0;
            String query="SELECT COUNT(*) FROM HOADON";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                count=rs.getInt(1)+1;
            }
            String x=String.valueOf(count);
            txtMaHD.setText(x);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void inputThuocToToa(){
        tblModelToaThuoc=(DefaultTableModel)tblToaThuoc.getModel();
        int maHD=Integer.parseInt(txtMaBNKham.getText());
        int maThuoc=1;
        String loaiThuoc=cbbLoaiThuoc.getSelectedItem().toString();
        int soLuong=(Integer)this.soLuong.getValue();
        String cachDung=txtCachDung.getText();
        if(cachDung.equals("")==true){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập cách dùng");
        }
        else{
            try{
                connect();
                String getMaThuoc="select MATHUOC FROM THUOC WHERE TENTHUOC="+loaiThuoc;
                Statement stm=conn.createStatement();
                ResultSet rs=stm.executeQuery(getMaThuoc);
                while(rs.next()){
                    maThuoc=rs.getInt(1);
                }
                
                String insert="insert into TOATHUOC values(?,?,?,?,?,?)";
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
    }
    
    public ArrayList<PhieuKham> getPhieuKhamList(){
        ArrayList<PhieuKham> PhieuKhamsList = new ArrayList<>();
        try{
            connect();
            //String query2 = "SELECT MAPK, MABN, LIDOKHAM FROM PHIEUKHAM WHERE NGAYKHAM="+sqlDate.toString();
            String query2 = "SELECT * FROM PHIEUKHAM";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            
            while(rs.next()){ 
                PhieuKham pk;
                pk = new PhieuKham(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getDate(4).toString());
                PhieuKhamsList.add(pk);
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return PhieuKhamsList;
    }
    
    public void show_KhamBenh(){
        txtTienKham.setText("30.000Đ");
        txtTienKham.setEditable(false);
        txtTienThuoc.setEditable(false);
        txtMaHD.setEditable(false);
        setMaHD();
        updateCBBloaiThuoc();
    }
    
    
    
    //---------------------hàm khởi tạo mặc định------------------------------
    //lấy hình từ folder src/images để chèn hình vào frame
    public void editImageFrame(){
        ImageIcon myimage = new ImageIcon("src/images/da1.png");
        Image img1 = myimage.getImage();
        Image img2=myimage.getImage().getScaledInstance(jLabel1.getWidth(), jLabel1.getHeight(), Image.SCALE_SMOOTH);
        jLabel1.setIcon(new ImageIcon(img2));
        
        ImageIcon myimage1 = new ImageIcon("src/images/da2.png");
        Image img3 = myimage1.getImage().getScaledInstance(jLabel3.getWidth(), jLabel3.getHeight(), Image.SCALE_SMOOTH);
        //ImageIcon i1 = new ImageIcon(img4);
        jLabel3.setIcon(new ImageIcon(img3));
        
        ImageIcon myimage2 = new ImageIcon("src/images/da3.png");
        Image img5 = myimage2.getImage().getScaledInstance(jLabel4.getWidth(), jLabel4.getHeight(), Image.SCALE_SMOOTH);
        //ImageIcon i2 = new ImageIcon(img6);
        jLabel4.setIcon(new ImageIcon(img5));
        
        ImageIcon imgExit= new ImageIcon("src/images/exit.png");
        Image imgExit1=imgExit.getImage().getScaledInstance(btExit.getWidth(), btExit.getHeight(), Image.SCALE_SMOOTH);
    }
    //hàm khởi tạo mặc định
    public BaseFrame() {
        initComponents();
              
        Border jpanel_title_border = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black);
        jPanel_title.setBorder(jpanel_title_border);
        //chèn hình
        editImageFrame();
        setTitle("Quản lí phòng mạch tư");
        show_nhanVien();
        show_BenhNhan();
        show_BN();
        show_Thuoc();
        show_KhamBenh();
        setVisible(true);
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
        jPanel_title = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButtonBenhNhan = new javax.swing.JButton();
        jButtonThuoc = new javax.swing.JButton();
        jButtonTiepNhan = new javax.swing.JButton();
        jButtonKhamBenh = new javax.swing.JButton();
        jButtonNhanvien = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        panelNhanVien = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        btThemNV = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtTimNV = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTimMaNV = new javax.swing.JTextField();
        btTimNV = new javax.swing.JButton();
        panelBenhNhan = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblBenhNhan = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtTimBenhNhan = new javax.swing.JTextField();
        btTimMaBN = new javax.swing.JButton();
        panelKhamBenh = new javax.swing.JPanel();
        btToaThuoc = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        txtLiDoKham = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        cbbChuanDoan = new javax.swing.JComboBox<>();
        cbbBacSi = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        txtMaBNKham = new javax.swing.JTextField();
        btThemBenhAn = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtTienKham = new javax.swing.JTextField();
        txtTienThuoc = new javax.swing.JTextField();
        txtThanhTien = new javax.swing.JTextField();
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
        panelTiepNhan = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtTim = new javax.swing.JTextField();
        btThemPK = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBN = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtMaBN = new javax.swing.JTextField();
        txtTenBN = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        jMale = new javax.swing.JRadioButton();
        jFemale = new javax.swing.JRadioButton();
        btThemBN = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
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
        jLabel34 = new javax.swing.JLabel();
        txtTimKeyThuoc = new javax.swing.JTextField();
        btExit = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel_title.setBackground(new java.awt.Color(69, 123, 179));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("QUẢN LÍ PHÒNG MẠCH TƯ");

        jLabel3.setText("jLabel3");

        jLabel4.setText("jLabel4");

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel_titleLayout = new javax.swing.GroupLayout(jPanel_title);
        jPanel_title.setLayout(jPanel_titleLayout);
        jPanel_titleLayout.setHorizontalGroup(
            jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_titleLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel_titleLayout.setVerticalGroup(
            jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
            .addGroup(jPanel_titleLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel_titleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jButtonBenhNhan.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\patients-icon.png")); // NOI18N
        jButtonBenhNhan.setText("BỆNH NHÂN");
        jButtonBenhNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBenhNhanActionPerformed(evt);
            }
        });

        jButtonThuoc.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\drugs-icon.png")); // NOI18N
        jButtonThuoc.setText("THUỐC");
        jButtonThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThuocActionPerformed(evt);
            }
        });

        jButtonTiepNhan.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\register-icon.png")); // NOI18N
        jButtonTiepNhan.setText("TIẾP NHẬN");
        jButtonTiepNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTiepNhanActionPerformed(evt);
            }
        });

        jButtonKhamBenh.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\doctor-icon.png")); // NOI18N
        jButtonKhamBenh.setText("KHÁM BỆNH");
        jButtonKhamBenh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKhamBenhActionPerformed(evt);
            }
        });

        jButtonNhanvien.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\employee-icon.png")); // NOI18N
        jButtonNhanvien.setText("NHÂN VIÊN");
        jButtonNhanvien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNhanvienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jButtonNhanvien)
                .addGap(33, 33, 33)
                .addComponent(jButtonBenhNhan)
                .addGap(84, 84, 84)
                .addComponent(jButtonThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jButtonTiepNhan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonKhamBenh, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonNhanvien)
                    .addComponent(jButtonBenhNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonThuoc)
                    .addComponent(jButtonTiepNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonKhamBenh))
                .addContainerGap())
        );

        jLayeredPane1.setLayout(new java.awt.CardLayout());

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

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Tìm nhân viên");

        txtTimNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTimNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimNVActionPerformed(evt);
            }
        });
        txtTimNV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimNVKeyReleased(evt);
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 952, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimNV, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                    .addComponent(jLabel5)
                    .addComponent(btThemNV)
                    .addComponent(txtTimNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtTimMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btTimNV))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayeredPane1.add(panelNhanVien, "card2");

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
                .addGap(86, 86, 86)
                .addGroup(panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 787, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBenhNhanLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(273, 273, 273)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimBenhNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btTimMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(99, Short.MAX_VALUE))
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

        btToaThuoc.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btToaThuoc.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\drugsList-icon.png")); // NOI18N
        btToaThuoc.setText("THÊM TOA THUỐC");
        btToaThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btToaThuocActionPerformed(evt);
            }
        });

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

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setText("Mã hóa đơn");

        txtMaHD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

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
        btThemBenhAn.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\save-icon.png")); // NOI18N
        btThemBenhAn.setText("THÊM BỆNH ÁN");
        btThemBenhAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemBenhAnActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel29.setText("Tiền khám");

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel30.setText("Tiền thuốc");

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel31.setText("Thành tiền");

        txtTienKham.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtTienThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtThanhTien.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel35.setText("Loại thuốc");

        cbbLoaiThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

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
        btThemThuocToToa.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\add-icon.png")); // NOI18N
        btThemThuocToToa.setText("THÊM THUỐC");
        btThemThuocToToa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemThuocToToaActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel42.setText("Mã PK");

        txtTimMaPK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btTimPK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btTimPK.setText("TÌM");
        btTimPK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTimPKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKhamBenhLayout = new javax.swing.GroupLayout(panelKhamBenh);
        panelKhamBenh.setLayout(panelKhamBenhLayout);
        panelKhamBenhLayout.setHorizontalGroup(
            panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                .addContainerGap(674, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(194, 194, 194))
            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(btToaThuoc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btThemThuocToToa))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(15, 15, 15))
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCachDung, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                        .addComponent(cbbLoaiThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(36, 36, 36)
                                        .addComponent(jLabel39)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(soLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel41))))
                        .addGap(66, 66, 66)))
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
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
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbChuanDoan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbbBacSi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMaBNKham, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLiDoKham))
                        .addGap(37, 37, 37)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                    .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                        .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtTienThuoc)
                                        .addComponent(txtThanhTien)))
                                .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTienKham, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel42))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtTimMaPK)
                                    .addComponent(txtMaHD, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btTimPK))))
                    .addComponent(btThemBenhAn, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(116, 116, 116))
        );
        panelKhamBenhLayout.setVerticalGroup(
            panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel41))
                .addGap(18, 18, 18)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel28)
                                .addComponent(txtMaBNKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel42)
                                .addComponent(txtTimMaPK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btTimPK)))
                        .addGap(15, 15, 15)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27)
                            .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(soLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39)
                            .addComponent(cbbLoaiThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35))
                        .addGap(18, 18, 18)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCachDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40))
                        .addGap(9, 9, 9)))
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLiDoKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addGap(16, 16, 16)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel24)
                            .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel29)
                                .addComponent(txtTienKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(16, 16, 16)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(cbbChuanDoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30)
                            .addComponent(txtTienThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbBacSi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26)
                            .addComponent(jLabel31)
                            .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btToaThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btThemThuocToToa))
                        .addGap(32, 32, 32))
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addComponent(btThemBenhAn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jLayeredPane1.add(panelKhamBenh, "card7");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel12.setText("DANH SÁCH PHIẾU KHÁM");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("TÌM KIẾM BỆNH NHÂN");

        txtTim.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTim.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKeyReleased(evt);
            }
        });

        btThemPK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btThemPK.setText("Thêm phiếu khám");
        btThemPK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemPKActionPerformed(evt);
            }
        });

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

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("THÔNG TIN BỆNH NHÂN ĐĂNG KÝ KHÁM");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("Mã bệnh nhân");

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

        jMale.setText("MALE");

        jFemale.setText("FEMALE");

        btThemBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btThemBN.setText("Thêm bệnh nhân");
        btThemBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemBNActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel21.setText("Tìm kiếm");
        jLabel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setText("Tim maBN");

        jButton1.setText("tim kiem");
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
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1)
                                .addGap(198, 198, 198)
                                .addComponent(jLabel21)
                                .addGap(18, 18, 18)
                                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTim, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btThemPK, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane2))
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtMaBN))
                                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel17))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtSDT)
                                            .addComponent(txtDiaChi)
                                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtTenBN)))
                                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel20)
                                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel19)
                                                    .addComponent(jLabel18))
                                                .addGap(53, 53, 53)
                                                .addComponent(jMale)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jFemale)))
                                        .addGap(0, 55, Short.MAX_VALUE))))
                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btThemBN))))
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addGap(48, 48, 48)))
                .addGap(35, 35, 35))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTiepNhanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(220, 220, 220))
        );
        panelTiepNhanLayout.setVerticalGroup(
            panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btThemPK)
                    .addComponent(jLabel15)
                    .addComponent(txtMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21)
                        .addComponent(jLabel11)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(txtTenBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jLayeredPane1.add(panelTiepNhan, "card6");

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
        btThemThuoc.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\add-icon.png")); // NOI18N
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

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel34.setText("Key");

        txtTimKeyThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTimKeyThuoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKeyThuocKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelThuocLayout = new javax.swing.GroupLayout(panelThuoc);
        panelThuoc.setLayout(panelThuocLayout);
        panelThuocLayout.setHorizontalGroup(
            panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThuocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
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
                            .addGroup(panelThuocLayout.createSequentialGroup()
                                .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelThuocLayout.createSequentialGroup()
                                        .addComponent(txtTimMaThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton3))
                                    .addComponent(txtTimKeyThuoc)))
                            .addComponent(btThemThuoc))
                        .addGap(79, 79, 79))))
            .addGroup(panelThuocLayout.createSequentialGroup()
                .addGap(192, 192, 192)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelThuocLayout.setVerticalGroup(
            panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThuocLayout.createSequentialGroup()
                .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelThuocLayout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(panelThuocLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimMaThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3))
                        .addGap(18, 18, 18)
                        .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(txtTimKeyThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btExit.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\close.png")); // NOI18N
        btExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExitActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\mini1.png")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btExit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btExit, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jPanel_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    }//GEN-LAST:event_jButtonThuocActionPerformed

    private void jButtonBenhNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBenhNhanActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(true);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
    }//GEN-LAST:event_jButtonBenhNhanActionPerformed

    private void btToaThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btToaThuocActionPerformed
        // TODO add your handling code here:
        insertToaThuocDialog insetTT=new insertToaThuocDialog(this, true);
        insetTT.setVisible(true);
    }//GEN-LAST:event_btToaThuocActionPerformed

    private void jButtonNhanvienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNhanvienActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(true);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
    }//GEN-LAST:event_jButtonNhanvienActionPerformed

    private void jButtonTiepNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTiepNhanActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(true);
        panelThuoc.setVisible(false);
    }//GEN-LAST:event_jButtonTiepNhanActionPerformed

    private void jButtonKhamBenhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKhamBenhActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(true);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
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
        int row=tblNhanVien.getSelectedRow();
        location=row;
        showNhanVienDialog show=new showNhanVienDialog(this, rootPaneCheckingEnabled);
        show.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_updateNVActionPerformed

    private void txtTimKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKeyReleased
        DefaultTableModel table = (DefaultTableModel) tblBN.getModel();
        String search = txtTim.getText().toUpperCase();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(table);
        tblBN.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(search));
    }//GEN-LAST:event_txtTimKeyReleased

    private void btThemPKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemPKActionPerformed
        //InsertPhieuKhamJDialog insertPK = new InsertPhieuKhamJDialog(this, rootPaneCheckingEnabled);
        //insertPK.setVisible(true);
        int getRFow=tblBN.getSelectedRow();
        InserpkJdialog insertPK = new InserpkJdialog(this, rootPaneCheckingEnabled);
        insertPK.setVisible(true);
    }//GEN-LAST:event_btThemPKActionPerformed

    private void btThemBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemBNActionPerformed
        int maBN = 0;
        boolean flag = true;

        if(txtMaBN.getText().equals("")||txtTenBN.getText().equals("")||txtNgaySinh.getDate().equals("")||
            txtDiaChi.getText().equals("")||txtSDT.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Bạn hãy điền đầy đủ thông tin");
        }
        else{
            try{

                try{
                    maBN = Integer.parseInt(txtMaBN.getText());
                }catch(Exception e){
                    JOptionPane.showMessageDialog(rootPane, "Mã nhân viên phải là số và không chứa kí tự khác");
                    flag = false;
                }

                String tenBN = txtTenBN.getText();
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
                String ngaySinh = dateFormat.format(txtNgaySinh.getDate());
                java.util.Date date2 = new SimpleDateFormat("MMM d, yyyy").parse(ngaySinh);
                java.sql.Date sqlDate = new java.sql.Date(date2.getTime());

                String gioiTinh = "";
                if(jMale.isSelected()){
                    gioiTinh += "MALE";
                }
                if(jFemale.isSelected()){
                    gioiTinh += "FEMALE";
                }

                String diaChi = txtDiaChi.getText();
                String soDT = txtSDT.getText();

                if(flag == true){
                    BenhNhan bn = new BenhNhan(maBN,tenBN,ngaySinh,gioiTinh,diaChi,soDT);
                    addBenhNhan(bn);
                    JOptionPane.showMessageDialog(this, "Thêm thành công");

                    java.util.Date today = new java.util.Date();
                    txtMaBN.setText("");
                    txtTenBN.setText("");
                    txtNgaySinh.setDate(today);
                    txtDiaChi.setText("");
                    txtSDT.setText("");
                }
                String url = "jdbc:oracle:thin:@localhost:1521:orcl";
                try {
                    conn = DriverManager.getConnection(url,"DOAN_ORACLE","admin");
                    String insert = "insert into BENHNHAN values(?,?,?,?,?,?)";
                    PreparedStatement st = conn.prepareStatement(insert);
                    st.setInt(1, maBN);
                    st.setString(2, tenBN);
                    st.setDate(3, sqlDate);
                    st.setString(4, gioiTinh);
                    st.setString(5, diaChi);
                    st.setString(6, soDT);
                    int a = st.executeUpdate();

                    JOptionPane.showMessageDialog(this, "THEM THANH CONG "+a+" dòng");
                } catch (SQLException ex) {
                    Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Lỗi csdl");
                }

            }catch (ParseException ex){
                JOptionPane.showMessageDialog(this, "Lỗi convert dữ liệu Ngày sinh");
            }

        }

    }//GEN-LAST:event_btThemBNActionPerformed

    private void btExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExitActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btExitActionPerformed

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
            String query="select MAHD from TOATHUOC WHERE MAHD="+mahd;
            
            connect();
            Statement stt=conn.createStatement();
            ResultSet rs=stt.executeQuery(query);
            
            int temp=0;
            while(rs.next()){
                temp=rs.getInt(1);
            }
            
            if(mahd<1){
                JOptionPane.showMessageDialog(panelKhamBenh, "Không tồn tại mã hóa đơn");
            }
            else{
                //tạo biến maHoaDon để dialog truy vấn và xuất csdl ra màn hình dialog
                maHoaDon=Integer.parseInt(txtTimMaHD.getText());
                //chạy dialog xuất thông tin
                showToaThuocJDialog showTT=new showToaThuocJDialog(this, rootPaneCheckingEnabled);
                showTT.setVisible(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//GEN-LAST:event_btTimMatoaActionPerformed

    private void btThemBenhAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemBenhAnActionPerformed
        // TODO add your handling code here:
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport("D:\\Java\\JavaPhongMT\\src\\report\\report1.jrxml");
            JRDataSource datasource =new JREmptyDataSource();
            
            Map<String, Object> parameters=new HashMap<String, Object>();
            //parameters.put("ngayKham", txtNgayKham.getDate().toString());
            parameters.put("hoTen", txtHoTen.getText());
            parameters.put("maBN", txtMaBNKham.getText());
            parameters.put("gioiTinh", txtMaBNKham.getText());
            parameters.put("diaChi", txtMaBNKham.getText());
            parameters.put("chuanDoan", cbbChuanDoan.getSelectedItem().toString());
            parameters.put("bacSi", cbbBacSi.getSelectedItem().toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi tạo hóa đơn");
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_btThemBenhAnActionPerformed

    private void tblBNMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBNMouseReleased
        // TODO add your handling code here:
//        if(evt.getButton()==MouseEvent.BUTTON3){
//            if(evt.isPopupTrigger()&&tblBN.getSelectedRowCount()!=0){
//                PopUpMenu.show(evt.getComponent(), evt.getX(), evt.getY());
//            }
//        }
    }//GEN-LAST:event_tblBNMouseReleased

    private void txtTimKeyThuocKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKeyThuocKeyReleased
        // TODO add your handling code here:
        DefaultTableModel table= (DefaultTableModel)tblThuoc.getModel();
        String search= txtTimKeyThuoc.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> tr=new TableRowSorter<DefaultTableModel>(table);
        tblThuoc.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(search));
    }//GEN-LAST:event_txtTimKeyThuocKeyReleased

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
    }//GEN-LAST:event_btThemThuocToToaActionPerformed

    private void btTimPKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimPKActionPerformed
        try {
            // TODO add your handling code here:
            connect();
            String query="SELECT P.MABN, B.HOTEN, P.LIDOKHAM FROM PHIEUKHAM P, BENHNHAN B WHERE P.MABN=B.MABN AND MAPK="+txtTimMaPK.getText();
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                String maBN=String.valueOf(rs.getInt(1));
                txtMaBNKham.setText(maBN);
                txtHoTen.setText(rs.getString(2));
                txtLiDoKham.setText(rs.getString(3));
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

    private void txtTimNVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimNVKeyReleased
        // TODO add your handling code here:
        DefaultTableModel table= (DefaultTableModel)tblNhanVien.getModel();
        String search= txtTimNV.getText().toUpperCase();
        TableRowSorter<DefaultTableModel> tr=new TableRowSorter<DefaultTableModel>(table);
        tblNhanVien.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(search));
    }//GEN-LAST:event_txtTimNVKeyReleased

    private void txtTimNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimNVActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btTimNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimNVActionPerformed
        try {
            int maNV=Integer.parseInt(txtTimMaNV.getText());

            String sql="SELECT MANV FROM NHANVIEN WHERE MANV="+maNV;
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
        boolean flag = true;
        try {
            if(flag == true){
                int maBN = Integer.parseInt(txtTimBenhNhan.getText());
                String sql="SELECT MABN FROM BENHNHAN WHERE MABN="+maBN;

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
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelKhamBenh, "Mã nhân viên là số");
            flag = false;
        }  
    }//GEN-LAST:event_btTimMaBNActionPerformed

    private void txtTimBenhNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimBenhNhanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimBenhNhanActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

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
//                "jdbc:oracle:thin:@localhost:1521:orcl","DOAN_ORACLE","minhhy");
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
    private javax.swing.JButton btExit;
    private javax.swing.JButton btThemBN;
    private javax.swing.JButton btThemBenhAn;
    private javax.swing.JButton btThemNV;
    private javax.swing.JButton btThemPK;
    private javax.swing.JButton btThemThuoc;
    private javax.swing.JButton btThemThuocToToa;
    private javax.swing.JButton btTimMaBN;
    private javax.swing.JButton btTimMatoa;
    private javax.swing.JButton btTimNV;
    private javax.swing.JButton btTimPK;
    private javax.swing.JButton btToaThuoc;
    private javax.swing.JComboBox<String> cbbBacSi;
    private javax.swing.JComboBox<String> cbbChuanDoan;
    private javax.swing.JComboBox<String> cbbLoaiThuoc;
    private javax.swing.JMenuItem deleteBN;
    private javax.swing.JMenuItem deleteNV;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonBenhNhan;
    private javax.swing.JButton jButtonKhamBenh;
    private javax.swing.JButton jButtonNhanvien;
    private javax.swing.JButton jButtonThuoc;
    private javax.swing.JButton jButtonTiepNhan;
    private javax.swing.JRadioButton jFemale;
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
    private javax.swing.JLabel jLabel31;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JRadioButton jMale;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel_title;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JPanel panelBenhNhan;
    private javax.swing.JPanel panelKhamBenh;
    private javax.swing.JPanel panelNhanVien;
    private javax.swing.JPanel panelThuoc;
    private javax.swing.JPanel panelTiepNhan;
    private javax.swing.JSpinner soLuong;
    private javax.swing.JTable tblBN;
    private javax.swing.JTable tblBenhNhan;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTable tblThuoc;
    private javax.swing.JTable tblToaThuoc;
    private javax.swing.JTextField txtCachDung;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtLiDoKham;
    private javax.swing.JTextField txtMaBN;
    private javax.swing.JTextField txtMaBNKham;
    private javax.swing.JTextField txtMaHD;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenBN;
    private javax.swing.JTextField txtThanhTien;
    private javax.swing.JTextField txtTienKham;
    private javax.swing.JTextField txtTienThuoc;
    private javax.swing.JTextField txtTim;
    private javax.swing.JTextField txtTimBenhNhan;
    private javax.swing.JTextField txtTimKeyThuoc;
    private javax.swing.JTextField txtTimMaHD;
    private javax.swing.JTextField txtTimMaNV;
    private javax.swing.JTextField txtTimMaPK;
    private javax.swing.JTextField txtTimMaThuoc;
    private javax.swing.JTextField txtTimNV;
    private javax.swing.JMenuItem updateBN;
    private javax.swing.JMenuItem updateNV;
    // End of variables declaration//GEN-END:variables
}
