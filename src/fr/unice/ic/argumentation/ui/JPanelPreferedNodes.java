package fr.unice.ic.argumentation.ui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class JPanelPreferedNodes extends JPanel {

	private mxGraph graph;
	private mxGraphComponent graphComponent;

	
	public JPanelPreferedNodes() {
		// TODO Auto-generated constructor stub
	}



	private List<ArrayList<String>> getNodeToMakeChoice() {
		List<ArrayList<String>> nodesChoices= new ArrayList<ArrayList<String>>();
		Object[] childs = graph.getChildCells(graph.getDefaultParent());
		for(Object child: childs){
			if(child instanceof mxCell && !((mxCell) child).isEdge() ){
				((mxCell) child).isEdge();
				System.out.println("ok");
			}
		}
		return nodesChoices;
	}
	
	private boolean isConnectedTo(mxCell src,mxCell target) {
		
		return false;
	}



	public void init(mxGraph graph, mxGraphComponent graphComponent) {
		this.graph=graph;
		this.graphComponent=graphComponent;
		List<ArrayList<String>> preferedNode= getNodeToMakeChoice();
		int size= 1;
		if(preferedNode.size()>0){
			size=preferedNode.size()+1;
		}
		setLayout(new GridLayout(size, 2, 0, 0));
		JLabel lblChooseYourPreferences = new JLabel("Choose your preferences");
		lblChooseYourPreferences.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblChooseYourPreferences);
		
	}




}
