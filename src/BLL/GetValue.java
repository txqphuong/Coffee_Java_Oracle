/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BLL;

import DAL.DataAccess;

/**
 *
 * @author PC THAO
 */
public class GetValue {
    private DataAccess da;

    public GetValue() {
       
    }
    public String sumTablespaces()
    {
        da = new DataAccess("Select sum(bytes)/1024/1024 used_space_mb from dba_segments");
        return da.returnValue().toString () + " MB";
    }
    public String sumDataFile()
    {
        da = new DataAccess("Select sum(bytes)/1024/1024 total_space_mb from dba_data_files");
        return da.returnValue().toString () + " MB";
    }
    
    public String getMaLoaiMon(String tenLoai){
        da = new DataAccess(String.format("select MALOAI from datacaphe.phanloai Where TENLOAI like N'%s'",tenLoai));
        return da.returnValue().toString();
    }
}
