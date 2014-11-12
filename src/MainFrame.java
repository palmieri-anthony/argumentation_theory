import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -8123406571694511514L;
	private mxGraph graph;
	private mxGraphComponent graphComponent;
	private Object parent;
	private Action action = null;
	private final Map<String,mxCell> compo= new HashMap<String,mxCell>();

	public MainFrame() {
		super("Extra Application");
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();
		Object v1 = graph
				.insertVertex(parent, null, "Hell222o", 20, 20, 80, 30);

		graph.getModel().add(parent, v1, 0);
		graph.getModel().endUpdate();
		graph.setCellsResizable(true);
		graph.setCellsDeletable(true);
		graph.setCellsSelectable(true);
		graph.setResetEdgesOnConnect(false);
		graph.setAllowLoops(false);
		graph.setAllowDanglingEdges(true);
		graphComponent = new mxGraphComponent(graph);
		graphComponent.setConnectable(false);
		graphComponent.setDragEnabled(true);
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (action != null) {
					action.perform(e);
				}
			}
		});
		getContentPane().add(graphComponent);

		graphComponent.addListener(mxEvent.LABEL_CHANGED,
				new mxIEventListener() {

					@Override
					public void invoke(Object sender, mxEventObject evt) {
						compo.clear();
						fillChildrenNode(graph);
						System.out.println(sender.getClass());
//						if(compo.containsKey())
					}
				});
		JTextPane textPane = new JTextPane();
		graphComponent.setRowHeaderView(textPane);

		JToolBar toolBar = new JToolBar();
		graphComponent.setColumnHeaderView(toolBar);

		final JButton addVertex = new JButton("add vertex");
		addVertex.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!addVertex.isEnabled()) {
					setAction(null);
					addVertex.setEnabled(true);
				} else {
					setAction(new ActionCreateVertex(graph, parent));
					addVertex.setEnabled(false);
				}

			}
		});
		
		addVertex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				graph.getAllEdges(cells)
				//transformer les support en attaque.
				//preprocessing gerer les preferences
				//TODO transformation en dynpar...
				// enregistrer ca dans un fichier temporaire
				//passer la reference au service.
				//update couleur sur graph
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
		
		JButton btnCompute = new JButton("compute");
		btnCompute.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		toolBar.add(btnCompute);
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public void createNode() {
		graph.getModel().beginUpdate();
		mxICell v1 = (mxICell) graph.insertVertex(parent, null, "Insert Label",
				200, 200, 80, 30);
		v1.setVisible(true);
		graph.getModel().endUpdate();
	}

	public void addSupport() {
		String edgeStyle = mxConstants.STYLE_DASHED + "=1;"
				+ mxConstants.STYLE_DASH_PATTERN + "=10";

		// Object edge11 = graph.insertEdge(level1, null, "lien11_12", level1_1,
		// level1_2, edgeStyle);
	}
	
	private void fillChildrenNode(mxGraph graph) {
		Object[] list =  graph.getChildVertices(graph.getDefaultParent());
		for(Object cell: list){
			if(cell instanceof mxCell){
				mxCell cel = (mxCell) cell;
			compo.put(cel.getValue().toString()+cel.getId(), cel);
			}
		}	
	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);
		frame.setVisible(true);
		frame.createNode();
	}
	
	boolean isPrefered(mxCell node1,mxCell node2){
		return false;
	}

}
