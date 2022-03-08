/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.smcreator.smc.main.gui;


import sm.smcreator.smc.main.SMCreator;
import sm.smcreator.smc.main.gui.components.SMCPanel;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
/**
 *
 * @author lincon
 */
public class Sync {
    String time="";
    static boolean running=true;
     static KeyAdapter l;
     
    Sync()
    {
    }    
    static public  void add()
    {                      
        running=true;
           l = new KeyAdapter() {

                @Override
                public void keyTyped(KeyEvent e) {
                    try{
                  //  JOptionPane.showMessageDialog(null, running+""+e.getKeyCode());
                    }
                    catch (Exception er)
                    {                        
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {                    
                  try{
                      if(!running)
                     remo();
                    if(e.getKeyCode()==32)
                    {
                        
                    }
                    
                    }
                    catch (Exception er)
                    {                        
                    }    
                }

                @Override
                public void keyReleased(KeyEvent e) {
                  try{
                    //JOptionPane.showMessageDialog(null, e.getKeyChar());
                    }
                    catch (Exception er)
                    {
                        
                    }
                }
        };
    
            
       //SMCPanel.jTextArea1.setText(SMCPanel.jTextArea1.getText()+i);
      //SMCPanel.jTextArea1.addKeyListener(l);
        
        JOptionPane.showMessageDialog(null, "Added"+running); 
        
        
        /*if(!running)
        {
            SMCPanel.jTextArea1.removeKeyListener(l);
            JOptionPane.showMessageDialog(null, "end");
        }*/
                        
    }    
    
    public static void remo()
    {
  //      SMCPanel.jTextArea1.removeKeyListener(l);
        l=null;
        running=false;
        JOptionPane.showMessageDialog(null, running+"");
    }
    
        
}