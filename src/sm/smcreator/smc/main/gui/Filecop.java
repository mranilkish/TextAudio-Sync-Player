/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.smcreator.smc.main.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author lincon
 */
public class Filecop {
     static boolean copy(String resource1, String resource2,String destination) {        
         try{
        File f1 = new File(destination);
        OutputStream resStreamOut = new FileOutputStream(f1);
         
        
        InputStream res1 = MainForm.class.getClassLoader().getResourceAsStream(resource1);
        try {
            
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = res1.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            res1.close();
            //resStreamOut.close();
            } catch (Exception ex) { JOptionPane.showMessageDialog(null, ex +"1 prob");}
            
        InputStream res2 = MainForm.class.getClassLoader().getResourceAsStream(resource2);
        try {
            
            
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = res2.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            res1.close();                        
            resStreamOut.close();
            

        } catch (Exception exx) { JOptionPane.showMessageDialog(null,exx+"2 prob" );}
        
        } catch (Exception eyx) { JOptionPane.showMessageDialog(null,eyx+"3 prob" );}
                
return true;
    }
     
     

     public static boolean apptxt(String txt1,String between, String txt2,String destination) {        
     if(txt1!=null)    
         txt1="d3/d3creator/d3c/main/gui/ressources/"+txt1;
     if(txt2!=null)    
         txt2="d3/d3creator/d3c/main/gui/ressources/"+txt2;
         
         if(between==null)         
             between="";
                      
         try{
             PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(destination, true)));
             try{
        BufferedReader br = new BufferedReader(new InputStreamReader(MainForm.class.getClassLoader().getResourceAsStream(txt1)));
   //     StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            out.println(line);
       //     sb.append(line);
        //    sb.append(System.lineSeparator());
            line = br.readLine();
            
        }
    //    String everything = sb.toString();
        //JOptionPane.showMessageDialog(null, everything);
    } catch(Exception hh){}
             
             out.println(between);
                    
             try{
        BufferedReader br = new BufferedReader(new InputStreamReader(MainForm.class.getClassLoader().getResourceAsStream(txt2)));
      //  StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            out.println(line);
          //  sb.append(line);
          //  sb.append(System.lineSeparator());
            line = br.readLine();
     
        }
        //String everything = sb.toString();
        //JOptionPane.showMessageDialog(null, everything);
     } catch(Exception hh){}
    
     out.close();
 
     } catch (Exception eyx) { 
     //    JOptionPane.showMessageDialog(null,eyx+"3 prob" );
     }
                
     return true;
     }
     
        
        
        public static void main(String[] aa)
        {
            String s1="ki1.txt";
            String s2="ki2.txt";
            String s3="d:/ki3.txt";
            JOptionPane.showMessageDialog(null,HtmlEscape.escape("<br>fff"));
            
        }
    
}
