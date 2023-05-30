/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

import BLL.GetData;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
 
import javax.swing.JPanel;

public class Chart_PieData extends JPanel {
   
    private String title;
    
    public Chart_PieData( String title) {
        this.title = title;
    }

    private static PieDataset Dataset_1( ) {
        try{
            GetData x = new GetData();
            Object[][] data = x.getDataSellable();
            DefaultPieDataset dataset = new DefaultPieDataset( );
            System.out.print(data.length);
            for(int i =0 ;i<data.length;i++){
                dataset.setValue( data[i][1].toString() , new Double( data[i][0].toString() ) );  
            }
            return dataset;         
        }
        catch(Exception err){}
        return null;
    }
    
    private JFreeChart createPieChart() {
        JFreeChart chart = ChartFactory.createPieChart(
          title,   // chart title 
          Dataset_1(),          // data    
          true,             // include legend   
          true, 
          false);
        return chart;
    }
       
    public JPanel createPieChartPanel() {
       return new ChartPanel(createPieChart()); 
    }   
}