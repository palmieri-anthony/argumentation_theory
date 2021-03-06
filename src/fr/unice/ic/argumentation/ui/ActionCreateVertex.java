package fr.unice.ic.argumentation.ui;
import java.awt.event.MouseEvent;

import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;


public class ActionCreateVertex implements Action {
	private MxGraph graph;
	private Object parent;
	private static int number=0;
	
	public ActionCreateVertex(MxGraph graph, Object parent) {
		this.graph=graph;
		this.parent=parent;
		
	}
	@Override
	public void perform(MouseEvent e) {
		graph.addVertex(e.getX(),e.getY());
	}
	
	public void createNode(int x, int y) {
		graph.getModel().beginUpdate();
		mxICell v1 = (mxICell) graph.insertVertex(parent, null, "Insert Label"+number++,
				x, y, 80, 30);
		v1.setVisible(true);
		graph.getModel().endUpdate();
	}

}
