package fr.unice.ic.argumentation.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mxgraph.model.mxCell;

public class Preferences {

	private Map<ArrayList<mxCell>, Preference> referencedPreferences = new HashMap<ArrayList<mxCell>, Preference>();
	private MxGraph graph;

	public Preferences() {

	}

	public void setGraph(MxGraph graph) {
		this.graph = graph;
		graph.setPreference(this);
	}

	public Map<ArrayList<mxCell>, Preference> getReferencedPreferences() {
		return referencedPreferences;
	}

	public void deletePreference(mxCell v1, mxCell v2) {
		List<ArrayList<mxCell>> toRemove = new ArrayList<ArrayList<mxCell>>();
		for (ArrayList<mxCell> pref : referencedPreferences.keySet()) {
			if (pref.contains(v2) && pref.contains(v1)) {
				toRemove.add(pref);
			}
		}
		for (ArrayList<mxCell> prefRM : toRemove) {
			referencedPreferences.remove(prefRM);
		}
	}

	public void addPreference(mxCell v1, mxCell v2, boolean isV1Prefered) {
		if (!existPreferencesBetween(v1, v2)) {
			ArrayList<mxCell> pref = new ArrayList<mxCell>();
			pref.add(v1);
			pref.add(v2);
			referencedPreferences.put(pref, new Preference(v1, v2, graph,
					isV1Prefered));
		}
	}

	public boolean isPrefered(mxCell v1, mxCell v2) {
		for (ArrayList<mxCell> pref : referencedPreferences.keySet()) {
			if (pref.contains(v2) && pref.contains(v1)) {
				referencedPreferences.get(pref).isPrefered(v1, v2);
			}
		}
		return false;
	}

	public boolean existPreferencesBetween(mxCell v1, mxCell v2) {
		for (ArrayList<mxCell> pref : referencedPreferences.keySet()) {
			if (pref.contains(v2) && pref.contains(v1)) {
				return true;
			}
		}
		return false;
	}
	
	public Preference getPreference(mxCell v1, mxCell v2){
		for (ArrayList<mxCell> pref : referencedPreferences.keySet()) {
			if (pref.contains(v2) && pref.contains(v1)) {
				return referencedPreferences.get(pref);
			}
		}
		return null;
	}

}
