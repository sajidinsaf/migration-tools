package com.attuned.o11ytools.model.nr.dashboard;

import java.util.ArrayList;
import java.util.List;

public class NRPage {

    private String name;
    private String description;
    private List<NRWidget> widgets;
    
    public NRPage() {
    	widgets = new ArrayList<NRWidget>();
    }
	public NRPage(String name, String description, List<NRWidget> widgets) {
		super();
		this.name = name;
		this.description = description;
		this.widgets = widgets;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<NRWidget> getWidgets() {
		return widgets;
	}
	public void setWidgets(List<NRWidget> widgets) {
		this.widgets = widgets;
	}
    

	public void addWidget(NRWidget widget) {
		if (widget == null) {
			return;
		}
		this.widgets.add(widget);
	}
	@Override
	public String toString() {
		return "NRPage [name=" + name + ", description=" + description + ", widgets=" + widgets + "]";
	}
	
	
}
