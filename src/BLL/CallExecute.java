/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BLL;
import DAL.DataAccess;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;
/**
 *
 * @author phatl
 */
public class CallExecute {
    public static void callDeleteKH(String id){       
        DataAccess.CallSql(String.format("xoaKhachHang('%s')", id));
    }
    
    public static void callDeleteKH_PKH(String id){
        DataAccess.CallSql(String.format("KH_PKG.xoa('%s')", id));
    }
    
//    public static void callTimKiemSanPham(String tenSP){
//        DataAccess.CallSql(String.format("traCuuMonAn('%s')", tenSP));
//    }
    
     // <editor-fold defaultstate="collapsed" desc="Khách hàng Hóa đơn">
        public static boolean deleteHD(String mahd)
    {
       String chuoi =String.format("XoaHoaDon('%s')",mahd);
       return DataAccess.CallSql(chuoi);                    
    }
        public static boolean insertHD(String httt,String makh,String makm,String manv)
    {
        String chuoi =String.format("ThemHoaDon(N'%s','%s','%s','%s')",httt,makh,makm,manv);
        return DataAccess.CallSql(chuoi);
    }
        public static boolean updateHD(String ngaylap,String httt,String makh,String makm,String manv,String mahd)
    {
        String chuoi =String.format("CapNhatHoaDon(To_Date('%s','dd/mm/yyyy'),N'%s','%s','%s','%s','%s')",ngaylap,httt,makh,makm,manv,mahd);
        return DataAccess.CallSql(chuoi);
    }
         public static boolean insertCTHD(String soluong,String mahd,String matd)
    {
        String chuoi =String.format("ThemChiTietHoaDon(%s,'%s','%s')",soluong,mahd,matd);
        return DataAccess.CallSql(chuoi);
    }
    // </editor-fold>  
    // <editor-fold defaultstate="collapsed" desc="Nghĩa Khách hàng">
     public static boolean insertKH(String tenkh,String gt,String sdt,String diemtichluy,String hsd)
    {
        String chuoi =String.format("ThemKhachHang(N'%s', N'%s', '%s',%s,To_Date('%s','dd/mm/yyyy'))",tenkh,gt,sdt,diemtichluy,hsd);
        return DataAccess.CallSql(chuoi);
    }
              public static boolean updateKH(String makh,String tenkh,String gt,String sdt,String diemtichluy,String hsd)
    {
        String chuoi =String.format("CapNhatKhachHang('%s', N'%s', N'%s', '%s', %s, To_Date('%s','dd/mm/yyyy'))",makh,tenkh,gt,sdt,diemtichluy,hsd);
        return DataAccess.CallSql(chuoi);
    }
               public static boolean deleteKH(String makh)
    {
        String chuoi =String.format("XoaKhachHang('%s')",makh);
        return DataAccess.CallSql(chuoi);
    }
               // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Phương tìm kiếm cursor">        
    public static Vector<Vector> callTimKiemSanPham(String str){
        try {
            Vector<Vector> v = new Vector<Vector>();
            String command = "{call datacaphe.traCuuMonAn(?,?)}";
            CallableStatement cstmt = DataAccess.conn.prepareCall(command);
            cstmt.setString(1, str);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.execute();
            ResultSet rs = (ResultSet) cstmt.getObject(2);
            while (rs.next()) {
                Vector t = new Vector();
                t.add(rs.getString(1));
                t.add(rs.getString(2));
                t.add(Integer.parseInt(rs.getString(3)));
                t.add(rs.getString(4));
                if(Integer.parseInt(rs.getString(5)) == 1)
                    t.add("Còn");
                else
                    t.add("Hết");
                v.add(t);
            }
            return v;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Vector<Vector> callTimKiemSanPhamTheoLoai(String str){
        try {
            Vector<Vector> v = new Vector<Vector>();
            String command = "{call datacaphe.traCuuMonTheoLoai(?,?)}";
            CallableStatement cstmt = DataAccess.conn.prepareCall(command);
            cstmt.setString(1, str);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.execute();
            ResultSet rs = (ResultSet) cstmt.getObject(2);
            while (rs.next()) {
                Vector t = new Vector();
                t.add(rs.getString(1));
                t.add(rs.getString(2));
                t.add(Integer.parseInt(rs.getString(3)));
                t.add(rs.getString(4));
                if(Integer.parseInt(rs.getString(5)) == 1)
                    t.add("Còn");
                else
                    t.add("Hết");
                v.add(t);
            }
            return v;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    // </editor-fold>
}
