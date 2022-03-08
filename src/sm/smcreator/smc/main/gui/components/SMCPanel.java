/*
 * @(#) SMCPanel.java
 *
 * Created on 08.02.2018 by Anil Kishan
 * 
 *-----------------------------------------------------------------------
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */
package sm.smcreator.smc.main.gui.components;


import sm.smcreator.smc.main.gui.Filecop;
import sm.smcreator.smc.main.gui.MainForm;
import sm.smcreator.smc.system.Helpers;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

/**
 * @author Anil Kishan
 * @since 08.02.2018
 */
public class SMCPanel extends JPanel
{
	private static final long serialVersionUID = 8634277922087324325L;
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
 
    private void initComponents() {
      
        
        buttonGroup1 = new javax.swing.ButtonGroup();        
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jSlider1 = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jToggleButton1 = new javax.swing.JToggleButton();

        setBackground(new java.awt.Color(204, 204, 204));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setForeground(new java.awt.Color(204, 204, 204));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Code2000", 0, 24)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        

        jSlider1.setValue(0);
        jSlider1.setVisible(false);

        //jLabel1.setText("senstivity");

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("edit");
        jRadioButton4.setSelected(true);
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextArea1.setEditable(true);
                //JOptionPane.showMessageDialog(null,"aaaa");
                remo();
            try{    
                     Highlighter chighlighter = jTextArea1.getHighlighter();
        HighlightPainter cpainter = 
             new DefaultHighlighter.DefaultHighlightPainter(Color.white);               
      chighlighter.addHighlight(0,jTextArea1.getText().length(), cpainter );             
            //JOptionPane.showMessageDialog(null, "comming");
            chighlighter.removeAllHighlights();
            }catch(Exception ee){}
            
                jRadioButton4ActionPerformed(evt);
                
               
            }
        });
        
        jRadioButton4.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
         try{
                //JOptionPane.showMessageDialog(null, "lll");
                jTextArea1.requestFocusInWindow();
         }
         catch(Exception ee)
         {
             
         }
                
            }

            @Override
            public void focusLost(FocusEvent e) {
           try{
           }
         catch(Exception eee)
         {
             
         }     
         //       throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        buttonGroup1.add(jRadioButton5);
       
     //focus
          jRadioButton5.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
         try{
                //JOptionPane.showMessageDialog(null, "lll");
                jTextArea1.requestFocusInWindow();
         }
         catch(Exception ee)
         {
             
         }
                
            }

            @Override
            public void focusLost(FocusEvent e) {
           try{
           }
         catch(Exception eee)
         {
             
         }     
         //       throw new UnsupportedOperationException("Not supported yet.");
            }
        });
           jRadioButton5.setText("sync");
       
      
        
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
              //  JOptionPane.showMessageDialog(null,"bbbb");                
                add();
                                if(tokens==null)
                tokens=indexer(jTextArea1.getText());
                else
                tokens=modiindexer(jTextArea1.getText(), tokens);
                setsync();
            
            
           
       /* try {
            
             FileWriter fileWriter = null;
              File newTextFile = new File("d:/testme.txt","UTF-8");
            fileWriter = new FileWriter(newTextFile);
            fileWriter.write(jTextArea1.getText());
            fileWriter.close();
            
           JFileChooser jch=new JFileChooser();
            if(Helpers.filename==null)
            Helpers.filename=Helpers.doOpenFile2("Not Saved", "New");
            PrintWriter writer = new PrintWriter(Helpers.filename+".txt", "UTF-8");
            writer.print(jTextArea1.getText());            
            writer.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"error"+ex);
        } */ 
                jRadioButton4ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton6);
        jRadioButton6.setText("test");
        jRadioButton6.setVisible(false);// future
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.showMessageDialog(null,"cccc");
                jRadioButton4ActionPerformed(evt);
            }
        });
        
        jToggleButton1.setText("sm");
        
        
        jToggleButton1.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                if(i>=1&&i<=tokens.length)
                if(tokens[i-1][0].compareTo("0")==0)
                    tokens[i-1][0]="1";
                else
                    tokens[i-1][0]="0";
                 jTextArea1.requestFocusInWindow();
                 
                 
                } catch(Exception eo){}
                
                    if(tokens[i-1][0].compareTo("0")==0)
                      jToggleButton1.setSelected(false);
                    else
                jToggleButton1.setSelected(true);
         
            }
        }
                );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jRadioButton4)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jToggleButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jRadioButton4)
                        .addComponent(jRadioButton5)
                        .addComponent(jRadioButton6)
                        .addComponent(jToggleButton1)))
                .addGap(10, 10, 10))
        );
    }// </editor-fold>
  
  
  private String[][] indexer(String str)
  {
      int i=0;
      StringTokenizer st=new StringTokenizer(str);
      String restr[][]=new String[st.countTokens()][4];      
      while(st.hasMoreTokens())
      {          
          restr[i][0]="0";
          restr[i][1]=st.nextToken();
          restr[i][2]="0";
          restr[i][3]="0";          
         // JOptionPane.showMessageDialog(null,restr[i][0]+restr[i][1]+restr[i][2]+restr[i][3]);
          i++;          
      }    
          return restr;    
  }
  public static String[][] tokens;
  private String[][] modiindexer(String str,String old[][])
  {
      
      int i=0,k=0; // k counter to old array
      StringTokenizer st=new StringTokenizer(str);
      String restr[][]=new String[st.countTokens()][4];      
      while(st.hasMoreTokens())
      {
          String tk=st.nextToken();
          if(k<old.length&&tk.equalsIgnoreCase(old[k][1]))
          {
          restr[i][0]=old[k][0];
          restr[i][1]=tk;
          restr[i][2]=old[k][2];
          restr[i][3]=old[k][3];          
          k++;
          }
          else
          {
          restr[i][0]="0";
          restr[i][1]=tk;
          restr[i][2]="0";
          restr[i][3]="0";                        
          }
       //   JOptionPane.showMessageDialog(null,restr[i][0]+restr[i][1]+restr[i][2]+restr[i][3]);
          i++;          
      }    
          return restr;    
      
  }
  private String[][] delitem(int item)
  {  
      String[][] restr ,old;
            try{
    restr =new String[tokens.length-1][4];
    old=tokens;

      for(int k=0,j=0;k<old.length;k++)
      {
         
          if(k!=item)
          {
            //  JOptionPane.showMessageDialog(null, "inserted"+old[k][1]);
          restr[j][0]=old[k][0];
          restr[j][1]=old[k][1];
          restr[j][2]=old[k][2];
          restr[j][3]=old[k][3];   
          j++;
          }
       //   else
        //      JOptionPane.showMessageDialog(null, "deleted"+k);
      }
       
       
                       
      return restr;
            }
      catch(Exception e){
           //JOptionPane.showMessageDialog(null, "No Items to delete");
  }
            return tokens;
  }
  
  public void initSBP( SeekBarPanel sbp)
    {
     s=sbp;
    }
          

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    // Variables declaration - do not modify
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    public  javax.swing.JTextArea jTextArea1;
    private javax.swing.JToggleButton jToggleButton1;
    private SeekBarPanel s;
    private String old="0.0",starting="0.0";//curword="0.0";
    // End of variables declaration
	/* Constructor for LEDScrollPanel
	 */
	public SMCPanel()
	{
		super();                
                initComponents();			
		//startThread();
	}
	public synchronized void setScrollTextTo(String newScrollText)
	{
	 JOptionPane.showMessageDialog(null, "D3cP set scroll");
	}
	public synchronized void addScrollText(String appender)
	{
		
	}
	/**
	 * 
	 * @see de.quippy.javamod.main.gui.components.MeterPanelBase#componentWasResized()
	 */
        KeyAdapter l;
        public int i=-1;        
      private  void add()
    {
        
        if(l==null)
        {
    
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
                boolean randjump=true;
                @Override
                
                public void keyPressed(KeyEvent e) {
                   
                    
                    
                  try{
                     
                     //remo();
                   // JOptionPane.showMessageDialog(null, ""+e.getKeyCode()+" "+tokens.length );
                      if(e.getKeyCode()==32&&i<=tokens.length)
                      {                          
                          if(!randjump)
                          i++;                          
                          if(i==-1&&randjump)
                           i++;                                                     
                                                  
                          starting=s.getTimeTextField().getText();                          
                          if(old==null)
                          {
                              if(i==0)
                                  old=tokens[0][2]=starting;
                              else
                              old=tokens[i-1][2]=starting;
                              tog(i);
                              highLite(i, Color.pink);
                              randjump=false;                              
                              return;
                          }
                         // if(i%2==1)                          
                          try{
                         /*
                           if(i==1&&old!=null)
                          {    String dur=String.format("%.3f",Double.parseDouble(starting)-Double.parseDouble(old));
                               tokens[0][3]=dur;                               
                               old=starting;
                               return;
                          }*/
                          
                          if(i==0||randjump)
                          {
                              if(i==0)
                              old=tokens[0][2]=starting;
                              else
                                  old=tokens[i-1][2]=starting;                                  
                          }
                          else{
                              if(old!=null)
                              {
                               String dur=String.format("%.3f",Double.parseDouble(starting)-Double.parseDouble(old));
                               tokens[i-2][3]=dur;
                               tokens[i-1][2]=starting;
                              }
                               old=starting;
                          }
                          
                          
                          }
                          
                       //   JOptionPane.showMessageDialog(null,dur);
                          
                          catch(Exception ee)                              
                          {
                       //       JOptionPane.showMessageDialog(null,ee);
                          }
                          tog(i-1);
                          highLite(i,Color.pink); 
                          randjump=false;  
                      
                     //      JOptionPane.showMessageDialog(null, "space"+i );
                                                    
                      }
                      if(e.getKeyCode()==37&&i>=1) // <-
                      {                          
                          randjump=true;                          
                          i--;
                          old=null;
                          tog(i-1);
                          highLite(i,Color.yellow);                          
                      }                      
                       if(e.getKeyCode()==39&&i<=tokens.length) //->
                      { 
                          if(!randjump) //just came after space
                          {
                          starting=s.getTimeTextField().getText();
                          String dur=String.format("%.3f",Double.parseDouble(starting)-Double.parseDouble(old));
                          tokens[i-1][3]=dur;
                          starting=old=null;                          
                          }
                          randjump=true;
                          i++;
                          tog(i-1);
                          highLite(i,Color.yellow); 
                      }
                       if(e.getKeyCode()==68) //delete down arrow
                      {
                          //jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
                      old=null;
                      tokens[i][2]="0";
                      tokens[i][3]="0";
                      highLite(i,new Color(231,231,231));                      
                      }
     //                  if(e.getKeyCode()==81) //->q ->z
         //             {
     //                    //  JOptionPane.showMessageDialog(null, e.getKeyCode());
                          jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
      //                 }
                       if(e.getKeyCode()==83)//2b removed   ->s Saving to disc
                       {
                           //JOptionPane.showMessageDialog(null, e.getKeyCode());    
                              String temp="";
                           for(int pp=0;pp<tokens.length;pp++)
                           {
                               try{
                               temp+=tokens[pp][0]+","+tokens[pp][1]+","+tokens[pp][2]+","+tokens[pp][3]+"\n";
                           //JOptionPane.showMessageDialog(null, temp);    
                               }
                               catch(Exception ee)
                               {
                                   JOptionPane.showMessageDialog(null, ee);
                               }
                           }
                       //  Filecop.apptxt(null, temp, null,"d:/cmp.csv"); 
                           JOptionPane.showMessageDialog(null, temp);
                       } 
                       
                          if(e.getKeyCode()==80)// ->p
                          { 
                              int k=JOptionPane.showConfirmDialog(null, "Confirm");
                              if(k==0)
                              {
                               //   JOptionPane.showMessageDialog(null, "deleting"+i);
                                 // jTextArea1.setEditable(true);
                                  tokens=delitem(i-1);                                  
                                  setsync();
                                  highLite(i,Color.yellow);
                                  
                              }
                              
                          }
                          
                       /*
                       else
                       {
                           JOptionPane.showMessageDialog(null, e.getKeyCode());
                       }*/
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
       jTextArea1.addKeyListener(l);
        
    //    JOptionPane.showMessageDialog(null, "Added"); 
        
        }
        /*if(!running)
        {
            SMCPanel.jTextArea1.removeKeyListener(l);
            JOptionPane.showMessageDialog(null, "end");
        }*/
                        
    }  
      private void tog(int j)
      {
           try{                        
                      if(tokens!=null)
                    {                        
                    if(tokens[j][0].compareTo("0")==0)
                      jToggleButton1.setSelected(false);
                    else
                jToggleButton1.setSelected(true);
                        
                    }
                    }
                    catch(Exception ee){}
      }
    private void highLite(int word,Color clr)
    {
        int p0=0,p1=0;
        try{
            Highlighter chighlighter = jTextArea1.getHighlighter();
        HighlightPainter cpainter = 
             new DefaultHighlighter.DefaultHighlightPainter(Color.white);               
      chighlighter.addHighlight(0,jTextArea1.getText().length(), cpainter );             
            //JOptionPane.showMessageDialog(null, "comming");
            chighlighter.removeAllHighlights();
            
        Highlighter highlighter = jTextArea1.getHighlighter();
        HighlightPainter painter = 
             new DefaultHighlighter.DefaultHighlightPainter(clr);
    
        for(int k=1;k<=word;k++) 
        {     
            p0=p1;
            p1 = p0+tokens[k-1][1].length()+1;      
      
        }
        highlighter.addHighlight(p0, p1, painter );
      
        }
        catch(Exception ee)
        {
            
        }
    }
    public void remo()
    {
        jTextArea1.removeKeyListener(l);
        l=null;
       
        //JOptionPane.showMessageDialog(null, "rem");
    }
    private void setsync()
    {
                       

                String cnt="";
                for(int p=0;p<tokens.length;p++)
                    cnt+=tokens[p][1]+" ";
                jTextArea1.setText(cnt);
                jTextArea1.setEditable(false);   
                      
    }
    
}
    