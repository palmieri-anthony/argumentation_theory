package fr.unice.ic.argumentation.ui;

import java.io.File;
import java.util.List;

import com.mxgraph.model.mxCell;

import fr.unice.ic.argumentation.core.DynPARTIXService;
import fr.unice.ic.argumentation.core.IOStreamASPARTIX;

public class GraphTransformator {

	private MxGraph graph;
	private IOStreamASPARTIX iostream = new IOStreamASPARTIX();
	private DynPARTIXService dyn = new DynPARTIXService();
	
	public GraphTransformator(MxGraph graph) {
		this.graph=graph;
	}
	
	public String getASPARTIXRepresentation(){
		String s = "";
		List<mxCell> vertex = this.graph.getAllVertex();
		for(int i = 0; i < vertex.size(); ++i){
			s += "arg("+vertex.get(i).getId()+"). ";
		}
		
		List<mxCell> edges = this.graph.getAllEdges();
		for(int i = 0; i < edges.size(); ++i){
			if(edges.get(i).getValue().toString().equals("attack")){
				s += "att("+ edges.get(i).getSource().getId() +
						","+ edges.get(i).getTarget().getId() +"). ";
				
			}
		}
		return s;
	}
	
	public List<List<String>> launchDynPARTIX() {
		// change directory and name of the file
		File f = new File("new.txt");
		iostream.writer(f, getASPARTIXRepresentation());
		// change the option of the console application
		List<List<String>> result = dyn.output(f, "-s admissible");
		iostream.delete(f);
		return result;
	}
	
	/*
	public static void main(String[] args) {
		MxGraph graph = new MxGraph();
		graph.addVertex(0, 1);
		graph.addVertex(0, 1);
		graph.addAttack(graph.getNode("InsertLabel0"), graph.getNode("InsertLabel1"));
		GraphTransformator g = new GraphTransformator(graph);
		System.out.println(g.getASPARTIXRepresentation());
		System.out.println(g.launchDynPARTIX());
	}*/
	
}
