/*
 * @(#) PitchShiftGUI.java
 *
 * Created on 21.01.2013 by Anil Kishan
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
package sm.smcreator.smc.mixer.dsp.pitchshift;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sm.smcreator.smc.system.Helpers;

/**
 * @author Anil Kishan
 * @since 21.01.2013
 */
public class PitchShiftGUI extends JPanel
{
	private static final long serialVersionUID = 4300230957744752568L;
	
	private static final int SHIFT = 100;
	private static final int SLIDER_MAX = +1 * SHIFT;
	private static final int SLIDER_MIN = -1 * SHIFT;
	private static final double factor = Math.log10(2d);

	private PitchShift thePitcher;
	
	private JCheckBox pitchShiftActive = null;
	private JPanel pitchShiftPanel = null;
	private JPanel scaleShiftPanel = null;
	private JSlider pitchSlider = null;
	private JLabel pitchMinLable = null;
	private JLabel pitchCenterLable = null;
	private JLabel pitchMaxLable = null;
	private JSlider sampleScaleSlider = null;
	private JLabel scaleMinLable = null;
	private JLabel scaleCenterLable = null;
	private JLabel scaleMaxLable = null;
	private JLabel presetOversamplingLabel = null;
	private JComboBox presetOversampling = null;
	private JLabel presetFrameSizeLabel = null;
	private JComboBox presetFrameSize = null;

	private static final Integer OVERSAMPLINGS[] =
	{
	 	Integer.valueOf(1),
	 	Integer.valueOf(5),
	 	Integer.valueOf(10),
	 	Integer.valueOf(15),
	 	Integer.valueOf(20)
	};
	private static final Integer FRAMESIZE[] =
	{
	 	Integer.valueOf(512),
	 	Integer.valueOf(1024),
	 	Integer.valueOf(2048),
	 	Integer.valueOf(4096),
	 	Integer.valueOf(8192)
	};
	/**
	 * Constructor for PitchShiftGUI
	 */
	public PitchShiftGUI(PitchShift pitchShift)
	{
		super();
		thePitcher = pitchShift;
		initialize();
	}
	private void initialize()
	{
		setName("PitchShift");
		setLayout(new java.awt.GridBagLayout());
		add(getPitchShiftActive(),			Helpers.getGridBagConstraint(0, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
		add(getPresetOversamplingLabel(),	Helpers.getGridBagConstraint(1, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		add(getPresetOversampling(),		Helpers.getGridBagConstraint(2, 0, 1, 1, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
		add(getPresetFramesizeLabel(),		Helpers.getGridBagConstraint(3, 0, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 0.0, 0.0));
		add(getPresetFramesize(),			Helpers.getGridBagConstraint(4, 0, 1, 0, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
		add(getPitchShiftPanel(),			Helpers.getGridBagConstraint(0, 1, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.WEST, 1.0, 1.0));
		add(getScaleShiftPanel(),			Helpers.getGridBagConstraint(0, 2, 1, 0, java.awt.GridBagConstraints.BOTH, java.awt.GridBagConstraints.WEST, 1.0, 1.0));
	}
	private JLabel getPresetOversamplingLabel()
	{
		if (presetOversamplingLabel==null)
		{
			presetOversamplingLabel = new JLabel("Oversampling:");
			presetOversamplingLabel.setFont(Helpers.DIALOG_FONT);
		}
		return presetOversamplingLabel;
	}
	private JComboBox getPresetOversampling()
	{
		if (presetOversampling == null)
		{
			presetOversampling = new JComboBox();
			presetOversampling.setName("presetOversampling");

			DefaultComboBoxModel theModel = new DefaultComboBoxModel(OVERSAMPLINGS);
			presetOversampling.setModel(theModel);
			presetOversampling.setFont(Helpers.DIALOG_FONT);
			presetOversampling.setEditable(true);
			if (thePitcher!=null) presetOversampling.setSelectedItem(Integer.valueOf(thePitcher.getFFTOversampling()));
			presetOversampling.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED)
					{
						int oversampling = ((Integer)getPresetOversampling().getSelectedItem()).intValue();
						if (thePitcher!=null) thePitcher.setFFTOversampling(oversampling);
					}
				}
			});
		}
		return presetOversampling;
	}
	private JLabel getPresetFramesizeLabel()
	{
		if (presetFrameSizeLabel==null)
		{
			presetFrameSizeLabel = new JLabel("Frame size:");
			presetFrameSizeLabel.setFont(Helpers.DIALOG_FONT);
		}
		return presetFrameSizeLabel;
	}
	private JComboBox getPresetFramesize()
	{
		if (presetFrameSize == null)
		{
			presetFrameSize = new JComboBox();
			presetFrameSize.setName("presetFrameSize");

			DefaultComboBoxModel theModel = new DefaultComboBoxModel(FRAMESIZE);
			presetFrameSize.setModel(theModel);
			presetFrameSize.setFont(Helpers.DIALOG_FONT);
			presetFrameSize.setEditable(true);
			if (thePitcher!=null) presetFrameSize.setSelectedItem(Integer.valueOf(thePitcher.getFftFrameSize()));
			presetFrameSize.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED)
					{
						int framesize = ((Integer)getPresetFramesize().getSelectedItem()).intValue();
						if (thePitcher!=null) thePitcher.setFFTFrameSize(framesize);
					}
				}
			});
		}
		return presetFrameSize;
	}
	private JPanel getPitchShiftPanel()
	{
		if (pitchShiftPanel == null)
		{
			pitchShiftPanel = new JPanel();
			pitchShiftPanel.setName("pitchShiftPanel");
			pitchShiftPanel.setLayout(new java.awt.GridBagLayout());
			pitchShiftPanel.setBorder(new TitledBorder(null, "Pitch", TitledBorder.LEADING, TitledBorder.TOP, Helpers.DIALOG_FONT, null));
			pitchShiftPanel.add(getPitchSlider(),		Helpers.getGridBagConstraint(0, 0, 1, 3, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.NORTH, 1.0, 0.0));
			pitchShiftPanel.add(getPitchMinLabel(),		Helpers.getGridBagConstraint(0, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
			pitchShiftPanel.add(getPitchCenterLabel(),	Helpers.getGridBagConstraint(1, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 1.0, 0.0));
			pitchShiftPanel.add(getPitchMaxLabel(),		Helpers.getGridBagConstraint(2, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.EAST, 1.0, 0.0));
		}
		return pitchShiftPanel;
	}
	private JPanel getScaleShiftPanel()
	{
		if (scaleShiftPanel == null)
		{
			scaleShiftPanel = new JPanel();
			scaleShiftPanel.setName("scaleShiftPanel");
			scaleShiftPanel.setLayout(new java.awt.GridBagLayout());
			scaleShiftPanel.setBorder(new TitledBorder(null, "Tempo", TitledBorder.LEADING, TitledBorder.TOP, Helpers.DIALOG_FONT, null));
			scaleShiftPanel.add(getScaleSlider(),		Helpers.getGridBagConstraint(0, 0, 1, 3, java.awt.GridBagConstraints.HORIZONTAL, java.awt.GridBagConstraints.NORTH, 1.0, 0.0));
			scaleShiftPanel.add(getScaleMinLabel(),		Helpers.getGridBagConstraint(0, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.WEST, 1.0, 0.0));
			scaleShiftPanel.add(getScaleCenterLabel(),	Helpers.getGridBagConstraint(1, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.CENTER, 1.0, 0.0));
			scaleShiftPanel.add(getScaleMaxLabel(),		Helpers.getGridBagConstraint(2, 1, 1, 1, java.awt.GridBagConstraints.NONE, java.awt.GridBagConstraints.EAST, 1.0, 0.0));
		}
		return scaleShiftPanel;
	}
	private JCheckBox getPitchShiftActive()
	{
		if (pitchShiftActive == null)
		{
			pitchShiftActive = new javax.swing.JCheckBox();
			pitchShiftActive.setName("pitchShiftActive");
			pitchShiftActive.setText("activate pitch shift");
			pitchShiftActive.setFont(Helpers.DIALOG_FONT);
			if (thePitcher!=null) pitchShiftActive.setSelected(thePitcher.isActive());
			pitchShiftActive.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange()==ItemEvent.SELECTED || e.getStateChange()==ItemEvent.DESELECTED)
					{
						if (thePitcher!=null) thePitcher.setIsActive(getPitchShiftActive().isSelected());
					}
				}
			});
		}
		return pitchShiftActive;
	}
	private JSlider getPitchSlider()
	{
		if (pitchSlider==null)
		{
			int value = (int)(Math.log10((double)thePitcher.getPitchScale()) / factor * (double)SHIFT);
			if (value>SLIDER_MAX) value = SLIDER_MAX;
			else 
			if (value<SLIDER_MIN) value = SLIDER_MIN;
			pitchSlider = new JSlider(JSlider.HORIZONTAL, SLIDER_MIN, SLIDER_MAX, value);
			pitchSlider.setFont(Helpers.DIALOG_FONT);
			pitchSlider.setMinorTickSpacing((int)(0.1f*SHIFT));
			pitchSlider.setMajorTickSpacing((int)(0.5f*SHIFT));
			pitchSlider.setPaintTicks(true);
			pitchSlider.setSnapToTicks(false);
			pitchSlider.setPaintLabels(false);
			pitchSlider.setPaintTrack(true);
			pitchSlider.setToolTipText(Float.toString(Math.round(value*10f)/(SHIFT*10f)));
			pitchSlider.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount()>1)
					{
						getPitchSlider().setValue(0);
					}
				}
			});
			pitchSlider.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e)
				{
					double value = (double)getPitchSlider().getValue() / (double)SHIFT;
					thePitcher.setPitchScale((float)Math.pow(10d, value*factor));
					pitchSlider.setToolTipText(Float.toString(Math.round(value*10f)/10f));
				}
			});
		}
		return pitchSlider;
	}
	public javax.swing.JLabel getPitchMinLabel()
	{
		if (pitchMinLable==null)
		{
			pitchMinLable = new JLabel("one octave down");
			pitchMinLable.setFont(Helpers.DIALOG_FONT);
		}
		return pitchMinLable;
	}
	public javax.swing.JLabel getPitchCenterLabel()
	{
		if (pitchCenterLable==null)
		{
			pitchCenterLable = new JLabel("center");
			pitchCenterLable.setFont(Helpers.DIALOG_FONT);
		}
		return pitchCenterLable;
	}
	public javax.swing.JLabel getPitchMaxLabel()
	{
		if (pitchMaxLable==null)
		{
			pitchMaxLable = new JLabel("one octave up");
			pitchMaxLable.setFont(Helpers.DIALOG_FONT);
		}
		return pitchMaxLable;
	}
	private JSlider getScaleSlider()
	{
		if (sampleScaleSlider==null)
		{
			int value = (int)(Math.log10((double)thePitcher.getSampleScale()) / factor * (double)SHIFT);
			if (value>SLIDER_MAX) value = SLIDER_MAX;
			else 
			if (value<SLIDER_MIN) value = SLIDER_MIN;
			sampleScaleSlider = new JSlider(JSlider.HORIZONTAL, SLIDER_MIN, SLIDER_MAX, value);
			sampleScaleSlider.setFont(Helpers.DIALOG_FONT);
			sampleScaleSlider.setMinorTickSpacing((int)(0.1f*SHIFT));
			sampleScaleSlider.setMajorTickSpacing((int)(0.5f*SHIFT));
			sampleScaleSlider.setPaintTicks(true);
			sampleScaleSlider.setSnapToTicks(false);
			sampleScaleSlider.setPaintLabels(false);
			sampleScaleSlider.setPaintTrack(true);
			sampleScaleSlider.setToolTipText(Float.toString(Math.round(value*10f)/(SHIFT*10f)));
			sampleScaleSlider.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount()>1)
					{
						getScaleSlider().setValue(0);
					}
				}
			});
			sampleScaleSlider.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e)
				{
					double value = (double)getScaleSlider().getValue() / (double)SHIFT;
					thePitcher.setPitchAndSampleScale((float)Math.pow(10d, -value*factor), (float)Math.pow(10d, value*factor));
					sampleScaleSlider.setToolTipText(Float.toString(Math.round(value*10f)/10f));
					getPitchSlider().setValue((int)((-value) * (double)SHIFT));
				}
			});
		}
		return sampleScaleSlider;
	}
	public javax.swing.JLabel getScaleMinLabel()
	{
		if (scaleMinLable==null)
		{
			scaleMinLable = new JLabel("half speed");
			scaleMinLable.setFont(Helpers.DIALOG_FONT);
		}
		return scaleMinLable;
	}
	public javax.swing.JLabel getScaleCenterLabel()
	{
		if (scaleCenterLable==null)
		{
			scaleCenterLable = new JLabel("normal");
			scaleCenterLable.setFont(Helpers.DIALOG_FONT);
		}
		return scaleCenterLable;
	}
	public javax.swing.JLabel getScaleMaxLabel()
	{
		if (scaleMaxLable==null)
		{
			scaleMaxLable = new JLabel("double speed");
			scaleMaxLable.setFont(Helpers.DIALOG_FONT);
		}
		return scaleMaxLable;
	}
}
