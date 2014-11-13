package fr.unice.ic.argumentation.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

public class SupportJDialogue extends JDialog {
	final Map<String, mxCell> compo = new HashMap<String, mxCell>();
	private final JPanel contentPanel = new JPanel();
	final List<String> vertexs = new ArrayList<String>();
	SupportJDialogue jdial = this;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			// SupportJDialogue dialog = new SupportJDialogue();
			// dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			// dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SupportJDialogue(final MxGraph graph, final boolean isSupport) {
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		fillChildrenNode(graph);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0,
				0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel;
			if (isSupport) {
				lblNewLabel = new JLabel("Support a node");
			} else {
				lblNewLabel = new JLabel("Attack a node");
			}
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.gridwidth = 7;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			JLabel lblNodeWhichSupport = null;
			if (isSupport) {
				lblNodeWhichSupport = new JLabel("node which support");
			} else {
				lblNodeWhichSupport = new JLabel("node which attack");
			}
			GridBagConstraints gbc_lblNodeWhichSupport = new GridBagConstraints();
			gbc_lblNodeWhichSupport.gridwidth = 3;
			gbc_lblNodeWhichSupport.insets = new Insets(0, 0, 5, 5);
			gbc_lblNodeWhichSupport.gridx = 0;
			gbc_lblNodeWhichSupport.gridy = 2;
			contentPanel.add(lblNodeWhichSupport, gbc_lblNodeWhichSupport);
		}
		{
			JLabel lblNodeSupported;
			if (isSupport) {
				lblNodeSupported = new JLabel("node supported");
			} else {
				lblNodeSupported = new JLabel("node attacked");
			}
			GridBagConstraints gbc_lblNodeSupported = new GridBagConstraints();
			gbc_lblNodeSupported.gridwidth = 3;
			gbc_lblNodeSupported.insets = new Insets(0, 0, 5, 5);
			gbc_lblNodeSupported.gridx = 4;
			gbc_lblNodeSupported.gridy = 2;
			contentPanel.add(lblNodeSupported, gbc_lblNodeSupported);
		}
		{

			final JList listBegin = new JList(graph.getVertex().toArray());
			listBegin.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			GridBagConstraints gbc_list = new GridBagConstraints();
			gbc_list.gridwidth = 3;
			gbc_list.insets = new Insets(0, 0, 0, 5);
			gbc_list.fill = GridBagConstraints.BOTH;
			gbc_list.gridx = 0;
			gbc_list.gridy = 3;
			contentPanel.add(listBegin, gbc_list);

			final JList listTarget = new JList(graph.getVertex().toArray());
			listTarget.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			GridBagConstraints gbc_list2 = new GridBagConstraints();
			gbc_list2.gridwidth = 3;
			gbc_list2.fill = GridBagConstraints.BOTH;
			gbc_list2.gridx = 4;
			gbc_list2.gridy = 3;
			contentPanel.add(listTarget, gbc_list2);

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						mxCell src = graph.getNode(listBegin.getSelectedValue()
								.toString());
						mxCell target = graph.getNode(listTarget
								.getSelectedValue().toString());
						if (src!=target&& !graph.areLinked(src, target)) {
							if (isSupport) {
								graph.addSupport(src, target);
							} else {
								graph.addAttack(src, target);
							}
							exit();
						} else {
							JOptionPane
									.showMessageDialog(
											jdial,
											"Impossible to create this edge, check that not already exist or the source node is different from target!",
											"Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				okButton.setActionCommand("OK");
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
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	private void fillChildrenNode(mxGraph graph) {
		Object[] list = graph.getChildVertices(graph.getDefaultParent());
		for (Object cell : list) {
			if (cell instanceof mxCell) {
				mxCell cel = (mxCell) cell;
				compo.put(cel.getValue().toString() + "-ID-" + cel.getId(), cel);
				vertexs.add(cel.getValue().toString() + "-ID-" + cel.getId());
			}
		}
	}

	private void exit() {
		this.dispose();
	}

}
