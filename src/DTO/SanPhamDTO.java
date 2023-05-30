/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;
import java.sql.Blob; 
/**
 *
 * @author phatl
 */
public class SanPhamDTO {
    private Blob imageBlob ;
    private int price = 0;
    private String name;
    private String id ;
    private String trangthai;
    private String moTa;
    private int soLuong;

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
    public int getSoLuong() {
        return soLuong;
    }

    public String getTrangthai() {
        return trangthai;
    }
    public String getMota() {
        return trangthai;
    }
    public void settMota(String mota) {
        this.moTa = mota;
    }
    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }
    public SanPhamDTO() {
    }
    private String kc;

    public Blob getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(Blob imageBlob) {
        this.imageBlob = imageBlob;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name ;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKc() {
        return kc;
    }

    public void setKc(String kc) {
        this.kc = kc;
    }

    public SanPhamDTO(Blob imageBlob, String name, String id, String kc) {
        this.imageBlob = imageBlob;
        this.name = name;
        this.id = id;
        this.kc = kc;
    } 
    
    public SanPhamDTO(String mota, String name, String id, String kc) {
        this.moTa = mota;
        this.name = name;
        this.id = id;
        this.kc = kc;
    } 
}
