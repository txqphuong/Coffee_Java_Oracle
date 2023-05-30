/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;
import DAL.DBConfig;
import Ultilities.CMD.*;
import java.io.File;
/**
 *
 * @author phatl
 */
public class ImportExport {
    public static boolean ExportDataPump(String command){
        try{
            ExecuteCMD.runCommand(command, false);
        }
        catch(Exception err){
            return false;
        }finally{
            return true;
        }
    }
    
    public static boolean ExportDataSqlcl(String path, String tableName){
        try{
            File f = new File("src/ResourceSQL/exportSQLCL.sql");
            String scriptPath = f.getAbsolutePath();
           
            RWFile.write(scriptPath, "set SQLFORMAT csv;\n" +
                    "set encoding UTF-8;\n" +
                    "SPOOL '"+path+"'\n" +
                    "select * from "+tableName+";\n" +
                    "SPOOL off\n" + "exit");
            
            ExecuteCMD.runCommand("sql datacaphe/datacaphe@localhost/caphe @"+scriptPath, false);
        }
        catch(Exception err){
            return false;
        }finally{
            return true;
        }
    }
    
    public static boolean ImportDataSqlcl(String path, String tableName){
        try{
            File f = new File("src/ResourceSQL/importSQLCL.sql");
            String scriptPath = f.getAbsolutePath();
           
            RWFile.write(scriptPath, "set SQLFORMAT csv;\n" +
                    "set encoding UTF-8;\n" +
                    "LOAD datacaphe."+tableName+" "+path+"\n " +
                    "exit");
            
            ExecuteCMD.runCommand("sql datacaphe/datacaphe@localhost/caphe @"+scriptPath, false);
        }
        catch(Exception err){
            return false;
        }finally{
            return true;
        }
    }
}
