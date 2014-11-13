package fr.unice.ic.argumentation.ui;

import com.mxgraph.model.mxCell;

public class Preference {
	private boolean v1IsPrefered;
	private mxCell v1;
	private mxCell v2;
	private MxGraph graph;

	public Preference(mxCell v1, mxCell v2, MxGraph graph, boolean v1prefered) {
		v1IsPrefered = v1prefered;
		this.v1 = v1;
		this.v2 = v2;
	}

	public boolean isPrefered(mxCell v1, mxCell v2) {
		if (this.v1 == v1 && this.v2 == v2) {
			return v1IsPrefered;
		}
		return !v1IsPrefered;
	}
	
	public void setV1IsPrefered(boolean v1IsPrefered) {
		this.v1IsPrefered = v1IsPrefered;
	}
	

}
