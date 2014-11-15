package fr.unice.ic.argumentation.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.jgraph.graph.ParentMap;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
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
	private Preferences preferences= new Preferences();
	private HashMap<String, ArrayList<mxCell>> listAdjacences = new HashMap<String, ArrayList<mxCell>>();

	public MxGraph() {
		this.addListener(mxEvent.CHANGE, new mxIEventListener() {

			@Override
			public void invoke(Object sender, mxEventObject evt) {
				System.out.println("labelChange");

			}
		});
	}

	// 7

	public void addVertex(int x, int y) {
		while (vertexs.containsKey("InsertLabel" + number)) {
			number++;
		}
		mxCell vertex = (mxCell) this.insertVertex(this.getDefaultParent(),
				number+"", "InsertLabel" + number++, x, y, 80, 30);
		vertexs.put(vertex.getValue().toString(), vertex);
		ArrayList<mxCell> adjacences = new ArrayList<mxCell>();
		this.listAdjacences.put(vertex.getId(), adjacences);
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
		listAdjacences.get(src.getId()).add(target);
		edges.put(edge.toString(), edge);
		// addPreferences(src, target, name);
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

	public boolean areLinkedWith(mxCell src, mxCell target, String kind) {
		List<mxCell> edges = new ArrayList<mxCell>();
		for (Object edge : this.getEdges(src)) {
			if (edge instanceof mxCell) {
				mxCell edgeCell = (mxCell) edge;
				if (edgeCell.getValue().equals(kind)) {
					edges.add(edgeCell);
				}
			}
		}
		return edges.size() != 0;
	}

	public boolean areLinkedWithStrictly(mxCell src, mxCell target, String kind) {
		for (String edgeRef : this.edges.keySet()) {
			mxCell edge = this.edges.get(edgeRef);
			if (edge.getSource() == src && edge.getTarget() == target
					&& edge.getValue().toString().equals(kind))
				return true;
		}

		return false;
	}

	public boolean addAttack(mxCell src, mxCell target) {
		if (areLinkedWithStrictly(target, src, "attack")) {
			preferences.addPreference(src, target, true);
		}
		return createEdge(src, target, this.ATTACK_STYLE, "attack");
	}

	private boolean areLinkedWithSupport(mxCell target, mxCell src) {
		return areLinkedWith(src, target, "support");
	}

	private boolean areLinkedWithAttack(mxCell target, mxCell src) {
		return areLinkedWith(src, target, "attack");
	}

	public void deleteSelectedCell() {
		if (this.getSelectionCell() instanceof mxCell) {
			this.getModel().beginUpdate();
			mxCell selected = (mxCell) this.getSelectionCell();

			removePreferences(selected);

			if (edges.containsKey(selected.toString())) {
				edges.remove(selected.toString());
			} else if (vertexs.containsKey(selected.getValue().toString())) {
				vertexs.remove(selected.getValue().toString());

				if (listAdjacences.containsKey(selected.getId())) {
					listAdjacences.remove(selected.getId());
				} else {
					for (String key : listAdjacences.keySet()) {
						ArrayList<mxCell> listAdjacenceNode = listAdjacences
								.get(key);
						if (listAdjacenceNode.contains(selected)) {
							listAdjacenceNode.remove(selected);
						}
					}
				}
			}
			selected.removeFromParent();
			selected.removeFromTerminal(true);
			selected.removeFromTerminal(false);
			this.getModel().endUpdate();
		}
		this.refresh();
		this.setSelectionCell(null);
	}

	private void removePreferences(mxCell selected) {
		for (int index = 0; index < selected.getEdgeCount(); index++) {
			mxICell edge = selected.getEdgeAt(index);
			mxCell source = (mxCell) edge.getTerminal(true);
			// on a un voisin
			if (source.isVertex() && !source.equals(selected)) {
				if (areLinked(source, selected)) {
					preferences.deletePreference(selected, source);
				}
			}
		}
		if (selected.isEdge()) {
			preferences.deletePreference((mxCell) selected.getTarget(),
					(mxCell) selected.getSource());
			if (selected.getValue().equals("support")) {
				for (mxCell cell : listAdjacences.get(selected.getTarget()
						.getId())) {
					if (listAdjacences.get(cell.getId()).contains(
							selected.getSource())) {
						if (areLinkedWithStrictly((mxCell) selected.getTarget(), cell, "attack")&&areLinkedWithStrictly( cell,(mxCell) selected.getSource(), "attack"))
							preferences.deletePreference((mxCell) selected.getSource(), cell);
					}
				}
			}else{
				for (mxCell cell : listAdjacences.get(selected.getTarget()
						.getId())) {
					if (listAdjacences.get(cell.getId()).contains(
							selected.getSource())) {
						if (areLinkedWithStrictly((mxCell) selected.getTarget(), cell, "attack")&&areLinkedWithStrictly( cell,(mxCell) selected.getSource(), "support"))
							preferences.deletePreference((mxCell) selected.getTarget(), cell);
					}
				}
			}
			// remove is implicit
		}

	}

	@Override
	public void cellLabelChanged(Object cell, Object value, boolean autoSize) {
		if (!value.equals(((mxCell) cell).getValue().toString())) {
			if (value.toString().equals("")) {
				JOptionPane.showMessageDialog(null, "the name is malformed!",
						"Error", JOptionPane.ERROR_MESSAGE);
			} else if (this.vertexs.containsKey(value.toString())) {
				JOptionPane.showMessageDialog(null, "name is taken!", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				vertexs.remove(((mxCell) cell).getValue());
				vertexs.put(value.toString(), ((mxCell) cell));
				super.cellLabelChanged(cell, value, autoSize);
			}
		}

	}

	public List<mxCell> getAllEdges(){
		List<mxCell> result = new ArrayList<mxCell>();
		for (mxCell cell : this.edges.values()) {
			result.add(cell);
		}
		return result;
	}
	
	public List<mxCell> getAllVertex() {
		List<mxCell> result = new ArrayList<mxCell>();
		for (mxCell cell : this.vertexs.values()) {
			result.add(cell);
		}
		return result;
	}
	
	public mxCell getNode(String value) {
		List<String> childs = new ArrayList<String>();
		for (mxCell cell : this.vertexs.values()) {
			if ((cell.getValue().toString()).equals(value)) {
				return cell;
			}
		}
		return null;
	}

	public List<String> getVertex() {
		List<String> childs = new ArrayList<String>();
		for (mxCell cell : this.vertexs.values()) {
			childs.add(cell.getValue().toString());
		}
		return childs;
	}

	public void setPreference(Preferences preferences) {
		this.preferences = preferences;

	}

	public void findImpliciteReferences() {
		for (String name : this.vertexs.keySet()) {
			mxCell nodeA = vertexs.get(name);
			for (mxCell neighboorA : listAdjacences.get(nodeA.getId())) {
				for (mxCell neighboorOfNBA : listAdjacences.get(neighboorA
						.getId())) {
					if (neighboorOfNBA != nodeA) {
						checkAndaddPreferences(nodeA, neighboorA,
								neighboorOfNBA);
					}
				}
			}
		}

	}

	private void checkAndaddPreferences(mxCell nodeA, mxCell neighboorA,
			mxCell neighboorAtoo) {
		if (areLinkedWithStrictly(nodeA, neighboorA, "attack")
				&& areLinkedWithStrictly(neighboorA, neighboorAtoo, "attack")
				&& areLinkedWithStrictly(neighboorAtoo, nodeA, "support")) {
			preferences.addPreference(neighboorAtoo, neighboorA, true);
		}
	}
	public HashMap<String, ArrayList<mxCell>> getListAdjacences() {
		return listAdjacences;
	}
	public Preferences getPreferences() {
		return preferences;
	}
}
