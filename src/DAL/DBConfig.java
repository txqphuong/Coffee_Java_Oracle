/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;
import java.sql.*;
/**
 *
 * @author phatl
 */
public class DBConfig {
    private static String servername = "@192.168.251.173";
    private static String port = "1521";
    private static String SID = "caphe";
    public static String username;
    public static String password;
    public static Connection Connect = getConnectionString();
    
    public static Connection getConnectionString()
    {
        try{
            //load the driver class  
            Class.forName("oracle.jdbc.driver.OracleDriver");  
            if(username.equals("sys"))
                username += " as sysdba"; 
            return DriverManager.getConnection(String.format("jdbc:oracle:thin:%s:%s:%s", servername, port, SID), username, password);   
        }
        catch(Exception err)
        {
            System.out.println(err.getMessage());
            return null;
        } 
    }
    
    public static void testgetConnect(){
        System.out.println();
        System.out.println(String.format("jdbc:oracle:thin:%s:%s:%s - %s/%s", servername, port, SID, username, password));
    }
}
