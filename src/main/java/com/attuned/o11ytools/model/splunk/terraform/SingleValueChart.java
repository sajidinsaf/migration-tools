package com.attuned.o11ytools.model.splunk.terraform;

import java.util.ArrayList;
import java.util.List;

public class SingleValueChart extends Chart {

    private String color_by ="Scale",  secondary_visualization  = "Radial";
    
    private List<ColourScale> colourScale;


    public SingleValueChart(List<String> terraformTemplateLines) {
    	super(terraformTemplateLines);
    }
	public SingleValueChart(String id, String name, String description, String programText, String color_by, String secondary_visualization, List<ColourScale> colourScale, List<String> terraformTemplateLines) {
		super(id, name, description, programText, terraformTemplateLines);
		this.color_by = color_by;
		this.secondary_visualization = secondary_visualization;
		this.colourScale = colourScale;
	}
	
	public String getColor_by() {
		return color_by;
	}
	public void setColor_by(String color_by) {
		this.color_by = color_by;
	}
	public String getSecondary_visualization() {
		return secondary_visualization;
	}
	public void setSecondary_visualization(String secondary_visualization) {
		this.secondary_visualization = secondary_visualization;
	}
	public List<ColourScale> getColourScale() {
		return colourScale;
	}
	public void setColourScale(List<ColourScale> colourScale) {
		this.colourScale = colourScale;
	}
	@Override
	public String toString() {
		return "SingleValueChart [color_by=" + color_by + ", secondary_visualization=" + secondary_visualization
				+ ", colourScale=" + colourScale + ", " + super.toString() + "]";
	}
	

    @Override
	protected String renderSpecifics(String line) {
    	line.replaceAll("@@showDataMarkers@@", "true");
    	
    	return line;
	}

	
    

}