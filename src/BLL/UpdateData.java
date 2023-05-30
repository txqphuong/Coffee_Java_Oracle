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
public class UpdateData {
     private DataAccess da;
     private String user;
    public UpdateData() {
        try{
            da = new DataAccess ("select distinct owner from DBA_TABLES where owner like (select owner from DBA_TABLES where table_name ='HOADON')");
            user = da.returnValue ().toString ();      
        }catch(Exception err){}
    }
    // <editor-fold defaultstate="collapsed" desc="Tablespace">  
    //Tạo tablespace
    public boolean createTablespace(String name, String path, String size)
    {
        try
        {
            da = new DataAccess(String.format ("CREATE TABLESPACE %s datafile '%s' size %s M" ,name, path, size));
            return true;
        }
        catch(Exception ex)
        {
            System.out.println (ex.getMessage ());
            return false;
        }
    }
    //Thay đổi tên tablespace
    public boolean changeNameTablespace(String oldname, String newname)
    {
        try
        {
            da = new DataAccess(String.format ("Alter tablespace %s rename to %s" ,oldname, newname));
            return true;
        }
        catch(Exception ex)
        {
            System.out.println (ex.getMessage ());
            return false;
        }
                
        
    }
    //Thêm datafile vào tablespace
    public boolean addDataFileTablespace(String name, String path, String size)
    {
        try
        {
            da = new DataAccess(String.format ("Alter tablespace %s add datafile '%s' size %s"+ "M" ,name,path, size));
            return true;
        }
        catch(Exception ex)
        {
            System.out.println (ex.getMessage ());
            return false;
        }
                
        
    }
    //Thêm datafile đã tồn tại vào tablespace
    public boolean addDataFileExistTablespace(String name, String path)
    {
        try
        {
            da = new DataAccess(String.format ("Alter tablespace %s add  datafile '%s'" ,name,path));
            return true;
        }
        catch(Exception ex)
        {
            System.out.println (ex.getMessage ());
            return false;
        }
                
        
    }
    //Xóa datafile ra khỏi tablespace
    public boolean dropDataFileTablespace(String path, String name)
    {
        try
        {
            da = new DataAccess(String.format ("ALTER TABLESPACE %s DROP DATAFILE '%s'" ,name,path));
            return true;
        }
        catch(Exception ex)
        {
            System.out.println (ex.getMessage ());
            return false;
        }
                
        
    }
    //Xóa tablespace
    public boolean dropTablespace(String name)
    {
        try
        {
            da = new DataAccess(String.format ("drop Tablespace %s including contents" ,name));
            return true;
        }
        catch(Exception ex)
        {
            System.out.println (ex.getMessage ());
            return false;
        }
                
        
    }
    //Xóa tablespace and datafile
    public boolean dropAllTablespace(String name)
    {
        try
        {
            da = new DataAccess(String.format ("drop Tablespace %s including contents and datafiles" ,name));
            return true;
        }
        catch(Exception ex)
        {
            System.out.println (ex.getMessage ());
            return false;
        }
                
        
    }
    public boolean resizeDatafile(String path, String size)
    {
        try
        {
            da = new DataAccess(String.format ("Alter database datafile '%s' resize %s"+ "M" ,path, size));
            return true;
        }
        catch(Exception ex)
        {
            System.out.println (ex.getMessage ());
            return false;
        }
                
        
    }
    //</editor-fold>                        
    // <editor-fold defaultstate="collapsed" desc="Role">  
   
    public boolean createRoleWithPass(String name, String pass)
    {
         da = new DataAccess(String.format ("CREATE ROLE %s identified by %s " ,name, pass));
         return true;
    }
    public boolean createRoleWithNotPass(String name)
    {
         da = new DataAccess(String.format ("CREATE ROLE %s " ,name));
         return true;
    }
    public boolean dropRole(String name)
    {
         da = new DataAccess(String.format ("DROP ROLE %s " ,name));
         return true;
    }
    public boolean grantPrivToRole(String priv, String name, String object)
    {
         da = new DataAccess(String.format ("grant %s on %s.%s to %s" ,priv,user,object, name));
         return true;
    }
   
    public boolean grantAllToRole( String name, String object)
    {
         da = new DataAccess(String.format ("grant insert, update, select, delete on %s.%s to %s" ,user,object, name));
         return true;
    }
    
    public boolean revokeAllToRole( String name, String object)
    {
         da = new DataAccess(String.format ("revoke all  on %s.%s from %s" ,user,object, name));
         return true;
    }
    public boolean revokeRole(String priv, String name, String object)
    {
         da = new DataAccess(String.format ("revoke %s on %s.%s from %s" ,priv,user,object, name));
         return true;
    }
    public boolean revokeRoleToUser(String role, String usename)
    {
         da = new DataAccess(String.format ("revoke %s from %s" ,role,usename));
         return true;
    }
    public boolean alterRoleWithPass( String name, String pass)
    {
         da = new DataAccess(String.format (" alter role %s IDENTIFIED by %s" ,name, pass));
         return true;
    }
    public boolean alterRoleWithNotPass( String name)
    {
         da = new DataAccess(String.format (" alter role %s not IDENTIFIED" ,name));
         return true;
    }
    
    // </editor-fold> 
    // <editor-fold defaultstate="collapsed" desc="Privileges">  
   
    public boolean revokePrivToUser(String user, String obj ,String priv)
    {
         da = new DataAccess(String.format ("Revoke %s on datacaphe.%s from %s" ,priv, obj , user));
         return true;
    }
     
    public boolean grantPrivToUser(String user,String Object, String priv)
    {
         da = new DataAccess(String.format ("grant %s on datacaphe.%s to %s" ,priv,Object, user));
         return true;
    }
    public boolean grantPrivToUserGrantOption(String user,String Object, String priv)
    {
         da = new DataAccess(String.format ("grant %s on datacaphe.%s to %s with grant option" ,priv,Object, user));
         return true;
    }
    public boolean grantAllPrivToUser(String user,String Object)
    {
         da = new DataAccess(String.format ("grant select, update, delete on datacaphe.%s to %s" ,Object, user));
         return true;
    }
    public boolean grantAllPrivToUserGrantOption(String user,String Object)
    {
         da = new DataAccess(String.format ("grant select, update, delete on datacaphe.%s to %s with grant option" ,Object, user));
         return true;
    }
    public boolean revokeAllPrivToUser(String user,String Object)
    {
         da = new DataAccess(String.format ("revoke select, update, delete on datacaphe.%s from %s" ,Object, user));
         return true;
    }
    public boolean defaultRole(String user,String role)
    {
         da = new DataAccess(String.format ("ALTER USER %s DEFAULT  ROLE %s" , user, role));
         return true;
    }
    public boolean defaultAllRole(String user)
    {
         da = new DataAccess(String.format ("ALTER USER %s DEFAULT  ROLE ALL" , user));
         return true;
    }
    // </editor-fold>
}
