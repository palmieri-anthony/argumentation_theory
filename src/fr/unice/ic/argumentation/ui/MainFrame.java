package fr.unice.ic.argumentation.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JPanel;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -8123406571694511514L;
	private MxGraph graph;
	private mxGraphComponent graphComponent;
	private Object parent;
	private Action action = null;
	private final Map<String, mxCell> compo = new HashMap<String, mxCell>();

	public MainFrame() {
		super("Extra Application");
		graph = new MxGraph();
		parent = graph.getDefaultParent();
		final Preferences preferences = new Preferences();
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
		graphComponent.setColumnHeaderView(toolBar);

		final JToggleButton addVertex = new JToggleButton("add vertex");
		addVertex.addMouseListener(new MouseAdapter() {
			private boolean enable = false;

			@Override
			public void mouseClicked(MouseEvent e) {
				if (enable) {
					setAction(null);
				} else {
					setAction(new ActionCreateVertex(graph, parent));
				}
				enable = !enable;
			}
		});

		addVertex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// graph.getAllEdges(cells)
				// transformer les support en attaque.
				// preprocessing gerer les preferences
				// TODO transformation en dynpar...
				// enregistrer ca dans un fichier temporaire
				// passer la reference au service.
				// update couleur sur graph
			}
		});
		toolBar.add(addVertex);

		JButton createSupport = new JButton("create support");
		createSupport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new SupportJDialogue(graph, true).setVisible(true);
			}
		});
		toolBar.add(createSupport);

		JButton createAttaque = new JButton("create Attack");
		createAttaque.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new SupportJDialogue(graph, false).setVisible(true);
			}
		});
		toolBar.add(createAttaque);

		JButton btnCompute = new JButton("compute accepted arguments");
		btnCompute.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		toolBar.add(btnCompute);
		
		JButton btnSetPreferedNode = new JButton("Set Prefered Node");
		btnSetPreferedNode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new PreferedNodeJdialog(preferences).setVisible(true);
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

	boolean isPrefered(mxCell node1, mxCell node2) {
		return false;
	}
	
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);
		frame.setVisible(true);
	}

	

}
