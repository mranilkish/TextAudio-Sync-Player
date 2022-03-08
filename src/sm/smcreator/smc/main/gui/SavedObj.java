/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.smcreator.smc.main.gui;

import java.io.Serializable;

                                       class SavedObj implements Serializable
                                        {
                                            public String tkns[][];
                                            public void set(String s[][])
                                            {
                                                tkns=s;
                                            }
                                            public String[][] get()
                                            {
                                                return tkns;
                                            }                                            
                                        };