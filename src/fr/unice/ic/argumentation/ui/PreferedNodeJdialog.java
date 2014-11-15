package fr.unice.ic.argumentation.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.mxgraph.model.mxCell;

public class PreferedNodeJdialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private Preferences preferences;
	private ArrayList<ButtonGroup> listGroup = new ArrayList<ButtonGroup>();

	/**
	 * Create the dialog.
	 * @param graph 
	 */
	public PreferedNodeJdialog(final Preferences preferences, final MxGraph graph) {
		setAlwaysOnTop(true);
		this.preferences = preferences;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
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
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);

			// mapPref our map
			JPanel view = new JPanel();
			view.setLayout(new GridBagLayout());
			JScrollPane scrollPane = new JScrollPane(view);
			// scrollPane.setPreferredSize(new Dimension(100,100));
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			for (ArrayList<mxCell> arrayCells : this.preferences
					.getReferencedPreferences().keySet()) {
				ButtonGroup group = new ButtonGroup();
				boolean prefered = this.preferences.getReferencedPreferences()
						.get(arrayCells)
						.isPrefered(arrayCells.get(0), arrayCells.get(1));
				JRadioButton a = new JRadioButton(arrayCells.get(0).getValue()
						.toString(), prefered);
				JRadioButton b = new JRadioButton(arrayCells.get(1).getValue()
						.toString(), !prefered);
				group.add(a);
				group.add(b);
				view.add(a, gbc);
				gbc.gridx++;
				view.add(b, gbc);
				gbc.gridy++;

				gbc.gridx = 0;
				listGroup.add(group);
			}
			gbc_panel.gridy++;
			contentPanel.add(scrollPane, gbc_panel);
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
					for(ButtonGroup group: listGroup){
						 for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
							 AbstractButton button = buttons.nextElement();
							 mxCell p1,p2;
							 p1=graph.getNode(button.getText());   
							 button= buttons.nextElement();
							 p2=graph.getNode(button.getText()); 
					            if (button.isSelected()) {
					               preferences.getPreference(p1, p2).setPreferences(p1, p2, false);
					            }else{
					            	preferences.getPreference(p1, p2).setPreferences(p1, p2, true);
					            }
					        }
						 exit();
						
					}
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
