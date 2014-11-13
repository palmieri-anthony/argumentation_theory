package fr.unice.ic.argumentation.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

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
	private Preferences preferences;

	public MxGraph() {
		this.addListener(mxEvent.CHANGE, new mxIEventListener() {

			@Override
			public void invoke(Object sender, mxEventObject evt) {
				System.out.println("labelChange");

			}
		});
	}

	public void addVertex(int x, int y) {
		Set<String> keys = vertexs.keySet();
		while (vertexs.containsKey("InsertLabel" + number)) {
			number++;
		}
		mxCell vertex = (mxCell) this.insertVertex(this.getDefaultParent(),
				null, "InsertLabel" + number++, x, y, 80, 30);
		vertexs.put(vertex.getValue().toString(), vertex);
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
		addPreferences(src, target, name);
		return true;
	}

	private void addPreferences(mxCell src, mxCell target, String name) {
		// si on ajoute un support entre les deux, alors il faut verifier qu'il
		// n'existe pas de noeud voisin
		// attaque par target et qui attaquent src.
		if (name.equals("support")) {
			checkAndAddPreferenceIfSupportInducedBySupport(src, target);
		}else{
			//pour chaque voisin de src
			for (int i = 0; i < src.getEdgeCount(); i++) {
				mxCell supportSrc = (mxCell) src.getEdgeAt(i).getTerminal(false);

				if (supportSrc != src
						&& src.getEdgeAt(i).getValue().toString()
								.equals("support") && areLinkedWithStrictly(target, supportSrc,"attack")){
							preferences.addPreference(target, supportSrc, true);
							System.out.println("ajout pref cas 1");
				}
			}
			
			
			for (int i = 0; i < target.getEdgeCount(); i++) {
				mxCell supportTarget = (mxCell) target.getEdgeAt(i).getTerminal(true);

				if (supportTarget != target
						 && areLinkedWithStrictly(supportTarget, src,"attack")&&areLinkedWithStrictly(target, supportTarget, "support")){
							preferences.addPreference(src, target, true);
							System.out.println("ajout pref cas 2");
				}
			}

		}

		// si on ajoute une attaque verifier que 1: le noeud que l'on attaque
		// attaque un noeud qui nous supporte

		// 2:si il existe un noeud qui nous attaque qui est supporter par un des
		// noeud que l'on attaque
	}

	private void checkAndAddPreferenceIfSupportInducedBySupport(mxCell src,
			mxCell target) {
		for (int i = 0; i < src.getEdgeCount(); i++) {
			mxCell otherCell = (mxCell) src.getEdgeAt(i).getTerminal(false);

			if (otherCell != src
					&& src.getEdgeAt(i).getValue().toString()
							.equals("attack")) {
				for (int j = 0; j < target.getEdgeCount(); j++) {
					if (target.getEdgeAt(j).getTerminal(false) == otherCell
							&& areLinkedWithAttack(target,otherCell)) {
						preferences.addPreference(src, otherCell, true);
					}
				}
			}
		}
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
		List<mxCell> edges = new ArrayList<mxCell>();
		for (int j = 0; j < src.getEdgeCount(); j++) {
			if (src.getEdgeAt(j).getTerminal(true) != src && areLinkedWith(src, target, kind)){
				edges.add((mxCell) src.getEdgeAt(j).getTerminal(true));
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
		}

	}

	@Override
	public void cellLabelChanged(Object cell, Object value, boolean autoSize) {
		if (!value.equals(((mxCell) cell).getValue().toString())) {
			Pattern pattern = Pattern.compile("\\s");
			Matcher matcher = pattern.matcher(value.toString());
			if (matcher.find() || value.toString().equals("")
					|| !value.toString().matches("[A-Za-z0-9]+")) {
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
}
