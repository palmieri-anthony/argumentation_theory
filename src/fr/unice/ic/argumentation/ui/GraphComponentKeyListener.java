package fr.unice.ic.argumentation.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class GraphComponentKeyListener implements KeyListener {

	private mxGraph graph;
	private mxGraphComponent graphComponent;

	public GraphComponentKeyListener(mxGraph graph,
			mxGraphComponent graphComponent) {
		this.graph=graph;
		this.graphComponent=graphComponent;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {

			Object selection = graph.getSelectionCell();
			if (selection instanceof mxCell) {
				graph.getModel().beginUpdate();
				mxCell selected = (mxCell) selection;
				selected.removeFromParent();
				graph.getModel().endUpdate();
			}
		}
		graph.refresh();
		graph.setSelectionCell(null);
		graphComponent.updateComponents();
		graphComponent.updateUI();
		graphComponent.repaint();

	}


}
