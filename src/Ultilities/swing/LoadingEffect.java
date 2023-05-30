/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ultilities.swing;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author phatl
 */
public class LoadingEffect extends Thread implements Runnable{
    
    public String mess;
    public JLabel control;
    public LoadingEffect(String mess){
        this.mess=mess;
    }
    
    @Override
    public synchronized void run() {
        int n = 0;
        String newMess = mess;
        while(true){
            System.out.println(newMess);
            control.setText(newMess);
            newMess+=".";
            n++;
            if(n==4){
                newMess=mess;
                n = 0;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoadingEffect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
