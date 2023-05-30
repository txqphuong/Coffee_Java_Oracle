package DAL;

import java.sql.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataAccess {
    
    public static Connection conn = DBConfig.Connect;
    private static Statement st = getStatement();
    private ResultSet rs = null;

    //contructor
    public DataAccess() {
        
    }
   
    public DataAccess(String query)
    {
        try{
            rs = st.executeQuery(query);
            
        }
        catch(Exception err){
            System.out.print(err.getMessage());
            System.out.println("\n================\nerr in to DATACCESS\n================");
            System.out.print(err.getMessage ()+query);
        }
    }
    
    public ArrayList QueryTable()
    {
        String[] ColumnNames;
        Object[][] DataRows;
        ColumnNames = QueryHeaderTable();
        DataRows = QueryContentTable();
        
        ArrayList<Object> arr = new ArrayList<>();
        arr.add(ColumnNames);
        arr.add(DataRows);
        return arr;
    }
    
    public Object returnValue()
    {
        try{
            Object[][] DataRows;
            DataRows = QueryContentTable();
            return DataRows[0][0];
        }catch(Exception err){
            System.out.print(err.getMessage() + " return Value");
            return null;
        }
    }
    
    // khởi tạo lấy statement
    private static Statement getStatement()
    {
        try{
            return conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        }
        catch(Exception err){
            System.err.print(err.getMessage());
        }
        return null;
    }
    
    // Lấy kết quả câu lệnh thực thi không truy vấn
    public static boolean ResultOfExecuteSql(String query) 
    {
        try{
            st.executeQuery(query);
            return true;
        }
        catch(SQLException err)
        {
            JOptionPane.showMessageDialog(null, err.getMessage(),"Thông Báo",JOptionPane.ERROR_MESSAGE);
            System.out.print(err.getMessage());
            System.out.println("\n================\nerr in to DATACCESS - ResultExecute\n================");
            return false;
        }
    }
    
    //phương thức dùng để truy vấn tên cột
    public String[] QueryHeaderTable()
    {
        try{
            //rs = st.executeQuery(query);
            //get column name of Table Query
            ResultSetMetaData rsmd = rs.getMetaData();
            //get mount of Column
            int numColumn = rsmd.getColumnCount();//System.out.print(rsmd.getColumnCount());
            String[] ColumnNames= new String[numColumn];
            //assign into ColumnNames
            for(int i =0;i<numColumn;i++)
            {
                ColumnNames[i] = rsmd.getColumnName(i+1);
            }
            return ColumnNames;
        }
        catch(Exception err)
        {
            System.out.print(err.getMessage());
        }
        return null;
    }
    
    //phương thức dùng để truy vấn nội dung
    public Object[][] QueryContentTable()
    {
        try{
            //ResultSet rs = st.executeQuery(query);
            //get column name of Table Query
            ResultSetMetaData rsmd = rs.getMetaData();
            //get mount of Column
            int numColumn = rsmd.getColumnCount();//System.out.print(rsmd.getColumnCount());
            //get mount of record/row
            int numRow = 0;
            rs.last();
            numRow = rs.getRow();
            rs.beforeFirst();
            //show results of query
            Object[][] resultsQuery = new Object[numRow][numColumn];

            int i=0;
            while(rs.next()){
                for(int j =0;j < numColumn;j++)
                {
                    resultsQuery[i][j] = rs.getObject(j+1);
                }
                i++;
            } 
            return resultsQuery;
        }
        catch(Exception err)
        {
            System.out.println("Err DataAccess " + err.getMessage());
        }
        return null; 
    }
    
   public static boolean CallSql(String sql){
        try {
            String command = "{call "+sql+" }";
            CallableStatement cstmt = conn.prepareCall(command);
            cstmt.execute();
            cstmt.close();
            return true;
        } catch (SQLException err) {
            JOptionPane.showMessageDialog(null, err.getMessage(),"Thông Báo",JOptionPane.ERROR_MESSAGE);
            return  false;
        }
    }
    
}
