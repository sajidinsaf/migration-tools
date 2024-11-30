package com.attuned.o11ytools.model.nr.dashboard;

public class NRVisualization {
    public String id;

    public NRVisualization() {
    	
    }
	public NRVisualization(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return " [id=" + id + "]";
	}
    
    
}
