/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ultilities.CMD;

import GUI.Dashboard;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
/**
 *
 * @author phatl
 */
public class ExecuteCMD {
    
    public static JTextArea txtAreas = null;
    
    public static boolean runCommand(String cmd, boolean showResult){
        final ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cmd);
        try {
            Process p = builder.start();
            
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            if(showResult){
                String line;
                System.out.println("Command Result:");
                while (true) {
                    line = r.readLine();
                    if (line == null) { break; }
                    System.out.println(line);
                    txtAreas.append(line+"\n");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally {
            return true;
        }
    }
}
