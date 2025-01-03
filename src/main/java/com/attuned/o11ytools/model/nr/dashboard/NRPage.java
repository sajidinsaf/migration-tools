package com.attuned.o11ytools.model.nr.dashboard;

import java.util.ArrayList;
import java.util.List;

public class NRPage {

  private String id;
  private String name;
  private String description;
  private List<NRWidget> widgets;
    
  public NRPage() {
  	widgets = new ArrayList<NRWidget>();
  }

	
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
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
    return "NRPage [id=" + id + ", name=" + name + ", description=" + description + ", widgets=" + widgets + "]";
  }
	
	
}
