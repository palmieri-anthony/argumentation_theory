package fr.unice.ic.argumentation.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -8123406571694511514L;
	private MxGraph graph;
	private mxGraphComponent graphComponent;
	private Object parent;
	private Action action = null;
	private final Preferences preferences = new Preferences();;

	public MainFrame() {
		super("Extra Application");
		graph = new MxGraph();
		parent = graph.getDefaultParent();
		init();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 444, 0 };
		gridBagLayout.rowHeights = new int[] { 273, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		graphComponent.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					graph.deleteSelectedCell();
				}
				graphComponent.updateComponents();
				graphComponent.updateUI();
				graphComponent.repaint();

			}
		});
		GridBagConstraints gbc_graphComponent = new GridBagConstraints();
		gbc_graphComponent.fill = GridBagConstraints.BOTH;
		gbc_graphComponent.gridx = 0;
		gbc_graphComponent.gridy = 0;
		getContentPane().add(graphComponent, gbc_graphComponent);

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		graphComponent.setColumnHeaderView(toolBar);

		final JToggleButton addVertex = new JToggleButton("add argument");
		addVertex.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setAction(new ActionCreateVertex(graph, parent));
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					setAction(null);

				}

			}
		});
		toolBar.add(addVertex);

		JButton createSupport = new JButton("create support");
		createSupport.addMouseListener(new MouseAdapter() {
			private SupportJDialogue window;

			@Override
			public void mouseClicked(MouseEvent e) {
				if (window != null) {
					this.window.dispose();
				}
				this.window = new SupportJDialogue(graph, true);
				this.window.setVisible(true);
			}
		});
		toolBar.add(createSupport);

		JButton createAttaque = new JButton("create attack");
		createAttaque.addMouseListener(new MouseAdapter() {
			private SupportJDialogue window;

			@Override
			public void mouseClicked(MouseEvent e) {
				if (window != null) {
					this.window.dispose();
				}
				this.window = new SupportJDialogue(graph, false);
				this.window.setVisible(true);
			}
		});
		toolBar.add(createAttaque);

		JButton btnCompute = new JButton("compute accepted arguments");
		btnCompute.addMouseListener(new MouseAdapter() {
			JList<String> listGraf = new JList<String>();
			JScrollPane listScroller = new JScrollPane(listGraf);
			int j = 0;
			private String[] choice;

			@Override
			public void mouseClicked(MouseEvent e) {

				GraphTransformator gt = new GraphTransformator(graph,
						preferences);
				final List<List<String>> solList = gt.launchDynPARTIX();
				// Arrays.toString(solList.toArray());
				listGraf.removeAll();
				// listModel.clear();;
				listGraf.repaint();
				if (solList != null) {
					choice = new String[solList.size()];
					for (int i = 0; i < solList.size(); ++i) {
						choice[i] = "Solution " + (i + 1);
					}

					listGraf.setListData(choice);
					listGraf.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					listGraf.setLayoutOrientation(JList.VERTICAL);

					graphComponent.setRowHeaderView(listScroller);
					// int ind = listGraf.getSelectedIndex();
					listGraf.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							graph.updateVertexToDefaultLayout();
							if (e.getClickCount() == 2) {

								String selectedItem = (String) listGraf
										.getSelectedValue();
								// add selectedItem to your second list.
								if (selectedItem != null) {
									graph.updateNode(solList.get(listGraf
											.getSelectedIndex()));
								}
							}
						}
					});
				} else {
					listGraf.setModel(new DefaultListModel());
					graph.updateVertexToDefaultLayout();
					listGraf.removeAll();
					listGraf.repaint();
				}

			}
		});
		toolBar.add(btnCompute);

		JButton btnSetPreferedNode = new JButton("set prefered arguments");
		btnSetPreferedNode.addMouseListener(new MouseAdapter() {
			private PreferedNodeJdialog window;

			@Override
			public void mouseClicked(MouseEvent e) {
				graph.findImpliciteReferences();
				if (preferences.getReferencedPreferences().size() == 0) {
					JOptionPane.showMessageDialog(null, "there is no choice!",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					if (this.window != null) {
						this.window.dispose();
					}
					this.window = new PreferedNodeJdialog(preferences, graph);
					this.window.setVisible(true);
				}
			}
		});
		toolBar.add(btnSetPreferedNode);
		JPanel panelPrefredNodes = new JPanel();
		graphComponent.setRowHeaderView(panelPrefredNodes);
		preferences.setGraph(graph);
	}

	private void init() {
		graph.setCellsResizable(true);
		graph.setCellsDeletable(true);
		graph.setCellsSelectable(true);
		graph.setResetEdgesOnConnect(false);
		graph.setAllowLoops(false);
		graph.setAllowDanglingEdges(false);
		graphComponent = new mxGraphComponent(graph);
		graphComponent.setConnectable(false);
		graphComponent.setDragEnabled(false);
		graph.isCellDeletable(true);
		graphComponent.getConnectionHandler().setSelect(false);
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (action != null) {
					action.perform(e);
				}
			}
		});
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(760, 320);
		frame.setVisible(true);
	}

}
