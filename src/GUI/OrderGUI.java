/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import BLL.GetData;
import DTO.SanPhamDTO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.FontMetrics;

/**
 *
 * @author phatl
 */
public class OrderGUI extends javax.swing.JPanel {

    /**
     * Creates new form OrderGUI
     */
    Vector<Vector> sp = new Vector<Vector>();
    GetData x = new GetData();
    public OrderGUI() {
        initComponents();
        loadDataMon(x.getDataThucDon());
        initTableOrder();
    }
    Vector header = new Vector();
    void initTableOrder(){
        DefaultTableModel model = (DefaultTableModel)tbOrder.getModel();
        model.setNumRows(0);
        header.add("Mã Món"); header.add("Tên Món"); header.add("Giá"); header.add("Số Lượng"); header.add("Kích Cỡ");
        model.setColumnIdentifiers(header);
    }
    
    void loadDataMon(ArrayList info) {
        SanPhamDTO td = new SanPhamDTO();
        // Set up the table with column names
        String[] columnNames = {"Mã món", "Tên món", "Kích cỡ", "Giá", "Hình ảnh"};
        tbThucDon.setModel(new DefaultTableModel(new Object[][]{}, columnNames) {
            Class[] columTypes = new Class[]{
                Object.class, Object.class, Object.class, Object.class, ImageIcon.class
            };

            public Class getColumnClass(int columnIndex) {
                return columTypes[columnIndex];
            }
        });
        DefaultTableModel model = (DefaultTableModel) tbThucDon.getModel();
        String trangthai = null;
        // Add the data rows to the table
        Object[][] info1 = (Object[][]) info.get(1);
        int daudexoa = 0;
        for (Object[] innerList : info1) {

            if (innerList[0] instanceof String) {
                td.setId( (String) innerList[0]);
            }
            if (innerList[5] instanceof Blob) {
                td.setImageBlob((Blob) innerList[5]);
            }
            if (innerList[3] instanceof BigDecimal) {
                td.setPrice(((BigDecimal) innerList[3]).intValue());
            }
            if (innerList[1] instanceof String) {
                td.setName((String) innerList[1]);
            }
            if (innerList[2] instanceof String) {
                 td.setKc((String)  innerList[2]);
            }
            if (innerList[4] instanceof byte[]) {
                byte[] byteArr = (byte[]) innerList[4];
                trangthai = Byte.toString(byteArr[0]);
                if (trangthai.compareTo("1") == 0) {
                    td.setTrangthai("Còn");
                } else {
                     td.setTrangthai("Đã hết");
                    continue;
                }
            }

            BufferedImage image;
            try {
                try {
                    image = ImageIO.read(td.getImageBlob().getBinaryStream());
                    Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
                    // Add the row data to the table model
                    if(td.getTrangthai().equals("Còn"))
                    {
                         Object[]  rowData = {td.getId(), td.getName(), td.getKc(), td.getPrice() , scaledImageIcon};
                         model.addRow(rowData);
                    }
                   
                } catch (IOException ex) {
                    //Logger.getLogger(SanPham.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                //Logger.getLogger(SanPham.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        tbThucDon.setRowHeight(100);
    }
       
    public PageFormat getPageFormat(PrinterJob pj)
{
    
    PageFormat pf = pj.defaultPage();
    Paper paper = pf.getPaper();    

    double middleHeight =8.0;  
    double headerHeight = 2.0;                  
    double footerHeight = 2.0;                  
    double width = convert_CM_To_PPI(10);      //printer know only point per inch.default value is 72ppi
    double height = convert_CM_To_PPI(headerHeight+middleHeight+footerHeight); 
    paper.setSize(width, height);
    paper.setImageableArea(                    
        0,
        10,
        width,            
        height - convert_CM_To_PPI(1)
    );   //define boarder size    after that print area width is about 180 points
            
    pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
    pf.setPaper(paper);    

    return pf;
}
    
    protected static double convert_CM_To_PPI(double cm) {            
	        return toPPI(cm * 0.393600787);            
}
 
protected static double toPPI(double inch) {            
	        return inch * 72d;            
}

public class BillPrintable implements Printable {
    
  public int print(Graphics graphics, PageFormat pageFormat,int pageIndex) 
  throws PrinterException 
  {    
        int result = NO_SUCH_PAGE;    
        if (pageIndex == 0) {                    
        
            Graphics2D g2d = (Graphics2D) graphics;                    

            double width = pageFormat.getImageableWidth();                    
           
            g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY()); 

            FontMetrics metrics=g2d.getFontMetrics(new Font("Arial",Font.BOLD,7));
            int idLength=metrics.stringWidth("000");
            int amtLength=metrics.stringWidth("000000");
            int qtyLength=metrics.stringWidth("00000");
            int priceLength=metrics.stringWidth("000000");
            int prodLength=(int)width - idLength - amtLength - qtyLength - priceLength-17;

            
            int productPosition = 0;
            int discountPosition= prodLength+5;
            int pricePosition = discountPosition +idLength+10;
            int qtyPosition=pricePosition + priceLength + 4;
            int amtPosition=qtyPosition + qtyLength;
            
            DefaultTableModel model = (DefaultTableModel)tbOrder.getModel();
            Vector data = model.getDataVector();
              
        try{
            /*Draw Header*/
            int y=20;
            int yShift = 10;
            int headerRectHeight=15;
            int headerRectHeighta=40;
            int sum = 0;
                
             g2d.setFont(new Font("Monospaced",Font.PLAIN,9));
            g2d.drawString("-----------------------------------------------------",12,y);y+=yShift;
            g2d.drawString("               ShopCoffee Bill Receipt               ",12,y);y+=yShift;
            g2d.drawString("-----------------------------------------------------",12,y);y+=headerRectHeight;
      
            g2d.drawString("-----------------------------------------------------",10,y);y+=yShift;
            g2d.drawString(" ID Food    Name Food                 SL   T.Price   ",10,y);y+=yShift;
            g2d.drawString("-----------------------------------------------------",10,y);y+=headerRectHeight;
            for(int i = 0;i<data.size();i++){
                String[] temp = data.get(i).toString().split(",");
                String id = temp[0].replace("[","");
                g2d.drawString(id +","+ temp[1] +","+ temp[3] +","+ temp[2],10,y);y+=yShift;
                sum+=Integer.parseInt(temp[2].trim());
            }
            g2d.drawString("-----------------------------------------------------",10,y);y+=yShift;
            g2d.drawString(" Total amount: "+sum+"                               ",10,y);y+=yShift;
            g2d.drawString("-----------------------------------------------------",10,y);y+=yShift;
            g2d.drawString("                    Free Home Delivery               ",10,y);y+=yShift;
            g2d.drawString("                        03111111111                  ",10,y);y+=yShift;
            g2d.drawString("*****************************************************",10,y);y+=yShift;
            g2d.drawString("             THANKS TO VISIT OUR SHOPCOFFEE          ",10,y);y+=yShift;
            g2d.drawString("*****************************************************",10,y);y+=yShift;

    }
    catch(Exception r){
    r.printStackTrace();
    }
              result = PAGE_EXISTS;    
          }    
          return result;    
      }
   }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbThucDon = new Ultilities.swing.Controls.Table();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbOrder = new Ultilities.swing.Controls.Table();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnChonMon = new Ultilities.swing.Controls.ButtonGradient();
        btnXoa = new Ultilities.swing.Controls.ButtonGradient();
        btnLuu = new Ultilities.swing.Controls.ButtonGradient();
        btnXoaHet = new Ultilities.swing.Controls.ButtonGradient();
        btnXuatHoaDon = new Ultilities.swing.Controls.ButtonGradient();
        jLabel3 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1090, 713));
        setPreferredSize(new java.awt.Dimension(1090, 713));

        tbThucDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbThucDon);

        tbOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tbOrder);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("DANH SÁCH MÓN CHỌN");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("DANH SÁCH THỰC ĐƠN");

        btnChonMon.setForeground(new java.awt.Color(0, 0, 0));
        btnChonMon.setText("Chọn Món");
        btnChonMon.setColor1(new java.awt.Color(255, 204, 102));
        btnChonMon.setColor2(new java.awt.Color(255, 153, 153));
        btnChonMon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnChonMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonMonActionPerformed(evt);
            }
        });

        btnXoa.setForeground(new java.awt.Color(0, 0, 0));
        btnXoa.setText("Xóa");
        btnXoa.setColor1(new java.awt.Color(255, 204, 102));
        btnXoa.setColor2(new java.awt.Color(255, 153, 153));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLuu.setText("Lưu");
        btnLuu.setColor2(new java.awt.Color(0, 0, 255));
        btnLuu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btnXoaHet.setText("Xóa Hết");
        btnXoaHet.setColor1(new java.awt.Color(255, 153, 153));
        btnXoaHet.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnXoaHet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHetActionPerformed(evt);
            }
        });

        btnXuatHoaDon.setForeground(new java.awt.Color(0, 0, 0));
        btnXuatHoaDon.setText("Xuất Hóa Đơn");
        btnXuatHoaDon.setColor1(new java.awt.Color(51, 255, 51));
        btnXuatHoaDon.setColor2(new java.awt.Color(0, 255, 153));
        btnXuatHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        btnXuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHoaDonActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("CHỌN MÓN");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1))
                        .addGap(18, 18, 18)
                        .addComponent(btnChonMon, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(700, 700, 700))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 921, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnXoaHet, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(367, 367, 367)
                .addComponent(btnXuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnChonMon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addComponent(btnXoaHet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnXuatHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = tbOrder.getSelectedRow();
        if(row < 0){
            JOptionPane.showMessageDialog(null, "vui lòng chọn dòng cần xóa !");
            return;
        }

        if(JOptionPane.showConfirmDialog(null, "Xác nhận xóa món khỏi danh sách chọn ?")==JOptionPane.YES_OPTION){
            DefaultTableModel model = (DefaultTableModel)tbOrder.getModel();
            model.removeRow(row);
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnChonMonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonMonActionPerformed
        try{
            DefaultTableModel model = (DefaultTableModel)tbOrder.getModel();
            int indexR = tbThucDon.getSelectedRow();
            SanPhamDTO temp = new SanPhamDTO();
            temp.setId(tbThucDon.getValueAt(indexR, 0).toString());
            temp.setName(tbThucDon.getValueAt(indexR, 1).toString());
            temp.setPrice(Integer.parseInt(tbThucDon.getValueAt(indexR, 3).toString()));
            temp.setSoLuong(1);
            temp.setKc(tbThucDon.getValueAt(indexR, 2).toString());
            Vector t = new Vector();
            t.add(temp.getId());
            t.add(temp.getName());
            t.add(temp.getPrice());
            t.add(temp.getSoLuong());
            t.add(temp.getPrice());
            //sp.add(t);
            System.out.print(sp.size());
            model.addRow(t);
            //model.setDataVector(header, sp);
        }catch(Exception err){
            
        }
    }//GEN-LAST:event_btnChonMonActionPerformed

    private void btnXoaHetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaHetActionPerformed
        DefaultTableModel model = (DefaultTableModel)tbOrder.getModel();
        model.setNumRows(0);
    }//GEN-LAST:event_btnXoaHetActionPerformed

    private void btnXuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHoaDonActionPerformed
        PrinterJob pj = PrinterJob.getPrinterJob();        
        pj.setPrintable(new BillPrintable(),getPageFormat(pj));
        try {
             pj.print();
          
        }
         catch (PrinterException ex) {
                 ex.printStackTrace();
        }
    }//GEN-LAST:event_btnXuatHoaDonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Ultilities.swing.Controls.ButtonGradient btnChonMon;
    private Ultilities.swing.Controls.ButtonGradient btnLuu;
    private Ultilities.swing.Controls.ButtonGradient btnXoa;
    private Ultilities.swing.Controls.ButtonGradient btnXoaHet;
    private Ultilities.swing.Controls.ButtonGradient btnXuatHoaDon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private Ultilities.swing.Controls.Table tbOrder;
    private Ultilities.swing.Controls.Table tbThucDon;
    // End of variables declaration//GEN-END:variables
}
