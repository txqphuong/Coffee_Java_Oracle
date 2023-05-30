/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ultilities.swing;

/**
 *
 * @author phatl
 */
public class CreateAlertBox extends Thread implements Runnable{
    @Override
    public void run(){
        AlertBox t = new AlertBox();
        t.setVisible(true);
        t.run("Test");
    }
}
