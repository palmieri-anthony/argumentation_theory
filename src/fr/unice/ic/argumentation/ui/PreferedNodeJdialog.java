package fr.unice.ic.argumentation.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.EmptyBorder;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import com.mxgraph.model.mxCell;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PreferedNodeJdialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Preferences preferences;
	private ArrayList<ButtonGroup> listGroup = new ArrayList<ButtonGroup>();


	/**
	 * Create the dialog.
	 */
	public PreferedNodeJdialog(Preferences preferences) {
		setAlwaysOnTop(true);
		this.preferences= preferences;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblSetYourPreferences = new JLabel("set your preferences");
			GridBagConstraints gbc_lblSetYourPreferences = new GridBagConstraints();
			gbc_lblSetYourPreferences.insets = new Insets(0, 0, 5, 0);
			gbc_lblSetYourPreferences.gridx = 0;
			gbc_lblSetYourPreferences.gridy = 0;
			contentPanel.add(lblSetYourPreferences, gbc_lblSetYourPreferences);
		}
		{
			JPanelPreferedNodes panel = new JPanelPreferedNodes();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			
//		 	//preferences.
//			// test init
//			Map<ArrayList<mxCell>,Preference> mapPref = new HashMap<ArrayList<mxCell>,Preference>();
//			ArrayList<mxCell> arrayTest = new ArrayList<mxCell>();
//			mxCell aCell = new mxCell("A");
//			aCell.setId("A");
//			mxCell bCell = new mxCell("B");
//			bCell.setId("B");
//			arrayTest.add(aCell);
//			arrayTest.add(bCell);
//			Preference p = new Preference(aCell, bCell, new MxGraph(), true);
//			mapPref.put(arrayTest, p);
			//
			//mapPref our map
			JPanel view = new JPanel();
			view.setLayout(new GridBagLayout());
			JScrollPane scrollPane = new JScrollPane(view);
			//scrollPane.setPreferredSize(new Dimension(100,100));
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			ArrayList<mxCell> arrayCell;
			for (ArrayList<mxCell> arrayCells : this.preferences.getReferencedPreferences().keySet()) {
			    ButtonGroup group =  new ButtonGroup();
			    if(this.preferences.getReferencedPreferences().get(arrayCells).isPrefered(arrayCells.get(0), arrayCells.get(1)) == true){
			    	JRadioButton a = new JRadioButton(arrayCells.get(0).getValue().toString(),true);
			    	JRadioButton b = new JRadioButton(arrayCells.get(1).getValue().toString(),false);
			    	group.add(a);
			    	group.add(b);
			    	view.add(a,gbc);
			    	gbc.gridx++;
			    	view.add(b,gbc);
			    	gbc.gridy++;
			    } else {
			    	JRadioButton a = new JRadioButton(arrayCells.get(0).getValue().toString(),false);
			    	JRadioButton b = new JRadioButton(arrayCells.get(1).getValue().toString(),true);
			    	group.add(a);
			    	group.add(b);
			    	view.add(a,gbc);
			    	gbc.gridx++;
			    	view.add(b,gbc);
			    	gbc.gridy++;
			    }
			    gbc.gridx = 0;
			    listGroup.add(group);
			}
			gbc_panel.gridy++;
			contentPanel.add(scrollPane,gbc_panel);
			this.repaint();
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						
					}
				});
				okButton.setActionCommand("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						//TODO persistance
						/*
						int r = 0;
						ArrayList<mxCell> arrayCell;
						for (ArrayList<mxCell> arrayCells : this.preferences.getReferencedPreferences().keySet()) {
							    if(arrayCells.get(0).getValue().toString().equals(listGroup.get(r).getSelection().getActionCommand())){
							    	this.preferences.getReferencedPreferences().get(arrayCells).setV1IsPrefered(true);
							    } else {
							    	this.preferences.getReferencedPreferences().get(arrayCells).setV1IsPrefered(false);
							    }
							    r++;
						}
						*/
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						exit();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	private void exit() {
		this.dispose();
	}
}
