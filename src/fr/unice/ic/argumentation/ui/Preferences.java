package fr.unice.ic.argumentation.ui;

import java.util.ArrayList;
import java.util.Map;

import com.mxgraph.model.mxCell;

public class Preferences {

	Map<ArrayList<mxCell>,Preference> referencedPreferences;
	private MxGraph graph;

	public Preferences(){

	}
	public void setGraph(MxGraph graph) {
		this.graph = graph;
	}
	
	public void deletePreference(mxCell v1, mxCell v2){
		for(ArrayList<mxCell> pref:referencedPreferences.keySet()){
			if(pref.contains(v2)&&pref.contains(v1)){
				referencedPreferences.remove(pref);
			}
		}
	}
	
	public void addPreference(mxCell v1, mxCell v2,boolean isV1Prefered){
		ArrayList<mxCell> pref = new ArrayList<mxCell>();
		pref.add(v1);
		pref.add(v2);
		referencedPreferences.put(pref, new Preference(v1, v2, graph, isV1Prefered));
	}
	
	public boolean isPrefered(mxCell v1, mxCell v2){
		for(ArrayList<mxCell> pref:referencedPreferences.keySet()){
			if(pref.contains(v2)&&pref.contains(v1)){
				referencedPreferences.get(pref).isPrefered(v1, v2);
			}
		}
		return false;
	}
	
	public boolean existPreferencesBetween(mxCell v1, mxCell v2){
		for(ArrayList<mxCell> pref:referencedPreferences.keySet()){
			if(pref.contains(v2)&&pref.contains(v1)){
				return true;
			}
		}
		return false;
	}
	
	
}
