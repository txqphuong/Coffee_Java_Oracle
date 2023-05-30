/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author phatl
 */
public class KhachHangDTO {
    private String maKH;
    private String tenKH;
    private String gioiTinh;
    private String sdt;
    private int diemTichLuy;
    private String hsd;

    public KhachHangDTO(String maKH, String tenKH, String gioiTinh, String sdt, int diemTichLuy, String hsd) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.diemTichLuy = diemTichLuy;
        this.hsd = hsd;
    }

    public KhachHangDTO() {
    }

    public String getMaKH() {
        return maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public String getSdt() {
        return sdt;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public String getHsd() {
        return hsd;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public void setHsd(String hsd) {
        this.hsd = hsd;
    }
    
    
}
