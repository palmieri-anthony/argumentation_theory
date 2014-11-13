package fr.unice.ic.argumentation.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

public class MxGraph extends mxGraph {

	private final Map<String, mxCell> vertexs = new HashMap<String, mxCell>();
	private final Map<String, mxCell> edges = new HashMap<String, mxCell>();
	private static int number = 0;
	private final String STYLE_SUPPORT = mxConstants.STYLE_DASHED + "=1;"
			+ mxConstants.STYLE_DASH_PATTERN + ";"
			+ mxConstants.STYLE_RESIZABLE + "=0;" + mxConstants.STYLE_MOVABLE
			+ "=0;" + mxConstants.STYLE_EDITABLE + "=0;";
	private final String ATTACK_STYLE = mxConstants.STYLE_DASH_PATTERN + ";"
			+ mxConstants.STYLE_RESIZABLE + "=0;" + "=0;"
			+ mxConstants.STYLE_MOVABLE + "=0;" + mxConstants.STYLE_EDITABLE
			+ "=0;";
	private Preferences preferences;

	public MxGraph() {
	}

	public void addVertex(int x, int y) {
		mxCell vertex = (mxCell) this.insertVertex(this.getDefaultParent(),
				null, "Insert Label" + number++, x, y, 80, 30);
		vertexs.put(vertex.toString(), vertex);
	}

	public boolean addSupport(mxCell src, mxCell target) {
		return createEdge(src, target, this.STYLE_SUPPORT, "support");
	}

	private boolean createEdge(mxCell src, mxCell target, String style,
			String name) {
		if (areLinked(src, target)) {
			return false;
		}
		mxCell edge = (mxCell) this.insertEdge(this.getDefaultParent(), null,
				name, src, target, style);
		edges.put(edge.toString(), edge);
		return true;
	}

	public boolean areLinked(mxCell src, mxCell target) {
		List<mxCell> edges = new ArrayList<mxCell>();
		for (Object edge : this.getEdges(src)) {
			if (edge instanceof mxCell) {
				mxCell edgeCell = (mxCell) edge;
				if (edgeCell.getTarget() == target) {
					edges.add(edgeCell);
				}
			}
		}
		return edges.size() != 0;
	}

	public boolean areLinkedWithAttack(mxCell src, mxCell target) {
		List<mxCell> edges = new ArrayList<mxCell>();
		for (Object edge : this.getEdges(src)) {
			if (edge instanceof mxCell) {
				mxCell edgeCell = (mxCell) edge;
				if (edgeCell.getValue() == "attack") {

				}
			}
		}
		return edges.size() != 0;
	}

	public boolean addAttack(mxCell src, mxCell target) {
		if (areLinkedWithAttack(target, src)) {
			preferences.addPreference(src, target, true);
		}
		return createEdge(src, target, this.ATTACK_STYLE, "attack");
	}

	public void deleteSelectedCell() {
		if (this.getSelectionCell() instanceof mxCell) {
			this.getModel().beginUpdate();
			mxCell selected = (mxCell) this.getSelectionCell();
			
				removePreferences(selected);
			
			if (edges.containsKey(selected.toString())) {
				edges.remove(selected.toString());
			} else if (vertexs.containsKey(selected.toString())) {
				vertexs.remove(selected.toString());
			}
			selected.removeFromParent();
			this.getModel().endUpdate();
		}
		this.refresh();
		this.setSelectionCell(null);
	}

	private void removePreferences(mxCell selected) {
		for (int index = 0; index < selected.getEdgeCount(); index++) {
			mxICell edge = selected.getEdgeAt(index);
			mxCell source = (mxCell) edge.getTerminal(true);
			//on a un voisin
			if (source.isVertex()&&!source.equals(selected)) {
				if(areLinked(source, selected)){
					preferences.deletePreference(selected, source);
				}
			}
			
		}
		if(selected.isEdge()){
			preferences.deletePreference((mxCell)selected.getTarget(),(mxCell)selected.getSource());
		}

	}

	public mxCell getNode(String value) {
		List<String> childs = new ArrayList<String>();
		for (mxCell cell : this.vertexs.values()) {
			if ((cell.getValue().toString() + cell.getId()).equals(value)) {
				return cell;
			}
		}
		return null;
	}

	public List<String> getVertex() {
		List<String> childs = new ArrayList<String>();
		for (mxCell cell : this.vertexs.values()) {
			childs.add(cell.getValue().toString() + cell.getId());
		}
		return childs;
	}

	public void setPreference(Preferences preferences) {
		this.preferences = preferences;

	}
}
