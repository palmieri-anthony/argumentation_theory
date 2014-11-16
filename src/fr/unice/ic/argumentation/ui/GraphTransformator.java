package fr.unice.ic.argumentation.ui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.mxgraph.model.mxCell;

import fr.unice.ic.argumentation.core.DynPARTIXService;
import fr.unice.ic.argumentation.core.IOStreamASPARTIX;

public class GraphTransformator {

	private MxGraph graph;
	private IOStreamASPARTIX iostream = new IOStreamASPARTIX();
	private DynPARTIXService dyn = new DynPARTIXService();
	private Preferences preferences;

	public GraphTransformator(MxGraph graph, Preferences preferences) {
		this.graph = graph;
		this.preferences = preferences;
	}

	public String getASPARTIXRepresentation() {
		StringBuilder sb = new StringBuilder();
		for (mxCell vertex : this.graph.getAllVertexs()) {
			sb.append("arg(" + vertex.getId() + "). ");
		}

		List<mxCell> edges = this.graph.getAllEdges();
		for (mxCell edge : edges) {
			if (edge.getValue().toString().equals("attack")) {
				sb.append(transformAttacks(edge));
			} else if (edge.getValue().toString().equals("support")) {
				sb.append(transformSupport(edge));
			}
		}
		return sb.toString();
	}

	private String transformSupport(mxCell edge) {
		StringBuilder sb = new StringBuilder();
		// pour chaque voisins du noeud final d'un support
		for (mxCell neigboorOfTarget : graph.getListAdjacences().get(
				edge.getTarget().getId())) {
			//si ce noeud attaque un autre
			if (graph.areLinkedWithStrictly((mxCell) edge.getTarget(),
					neigboorOfTarget, "attack")) {
				checkPreferences(sb, (mxCell) edge.getSource(), neigboorOfTarget);

			}
		}
		return sb.toString();
	}

	private String checkPreferences(StringBuilder sb, mxCell src,
			mxCell neigboorOfTarget) {
		if ((preferences.existPreferencesBetween(src,
				neigboorOfTarget))) {
			if (preferences.isPrefered(src,
					neigboorOfTarget)) {
				sb.append("att(" + src.getId() + ","
						+ neigboorOfTarget.getId() + ").");
			}else{
				sb.append("att(" + neigboorOfTarget.getId()+ ","
						+src.getId()   + ").");
			}
		} else {

			sb.append("att(" + src.getId() + ","
					+ neigboorOfTarget.getId() + ").");
		}
		return sb.toString();
	}

	private String transformAttacks(mxCell edge) {
		if ((preferences.existPreferencesBetween((mxCell) edge.getSource(),
				(mxCell) edge.getTarget()))) {
			if (preferences.isPrefered((mxCell) edge.getSource(),
					(mxCell) edge.getTarget())) {
				return "att(" + edge.getSource().getId() + ","
						+ edge.getTarget().getId() + ").";
			}
		} else {
			return "att(" + edge.getSource().getId() + ","
					+ edge.getTarget().getId() + ").";
		}
		return "";
	}

	static private int nb=0;
	public List<List<String>> launchDynPARTIX() {
		List<List<String>> result = null;
		// change directory and name of the file
		File f;
		try {
			f = File.createTempFile("temp", "new"+nb+++".txt");
			System.out.println(getASPARTIXRepresentation());
			iostream.writer(f, getASPARTIXRepresentation());
			// change the option of the console application
			result = dyn.output(f, "-s preferred");
			iostream.delete(f);
			System.gc();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
