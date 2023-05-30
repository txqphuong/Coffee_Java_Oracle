package Ultilities.ConvertData;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;


public class ConvertDataORCL {
    public static String ConvertDataToString(Object obj)
    {
        String res = "";
        if (obj instanceof String) {
            String strArray = (String)obj;
            //System.out.print(strArray+", ");
            res = strArray;
        }
        else if(obj instanceof BigDecimal)//convert int/float
        {
            try{
                //System.out.print(Integer.parseInt(obj.toString())+", ");    
                res=String.format("%d", Integer.parseInt(obj.toString()));
            }
            catch(Exception err)
            {
                BigDecimal numberic = new BigDecimal(obj.toString());
                //System.out.print(numberic.floatValue()+", "); 
                res=String.format("%.2f", numberic.floatValue());
            }                               
        }
        else if(obj == null)
        {
            //System.out.print("null, ");
            res ="null";
        }
        else if(obj instanceof Timestamp)
        {
            //Date date1=(Date) new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss").parse(obj.toString());  
            Timestamp ts=new Timestamp(System.currentTimeMillis());  
            Date date=new Date(ts.getTime());  
            //System.out.print(date+", "); 
            res = date.toString();
        }
        else
            res = "null";
        return res;
    }
    
    public static String[][] ConvertObject2DToString2D(Object[][] obj2D)
    {
        int rowCount = obj2D.length;
        if(rowCount != 0)
        {
           int colCount = obj2D[0].length;
           String[][] data = new String[rowCount][colCount];
           for(int i = 0;i<rowCount;i++)
           {
               for(int j =0;j<colCount;j++)
               {
                   data[i][j] = ConvertDataORCL.ConvertDataToString(obj2D[i][j]);
               }
           }
           return data;
        }
        return null;
    }
}
