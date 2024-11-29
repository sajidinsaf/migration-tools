package com.attuned.o11ytools.model.splunk.terraform;

import java.util.ArrayList;
import java.util.List;

import com.attuned.o11ytools.migrate.terraform.Terraformable;

public abstract class Chart implements Terraformable {

	private String id, name, description, programText;
    
    private List<String> terraformTemplateLines;

	public Chart(List<String> terraformTemplateLines) {
		this.terraformTemplateLines = terraformTemplateLines;
	}
	
	public Chart(String id, String name, String description, String programText, List<String> terraformTemplateLines) {
		super();
		this.terraformTemplateLines = terraformTemplateLines;
		this.id = id;
		this.name = name;
		this.description = description;
		this.programText = programText;
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
	public String getProgramText() {
		return programText;
	}
	public void setProgramText(String programText) {
		this.programText = programText;
	}
	
	@Override
	public String toString() {
		return "Chart [name=" + name + ", description=" + description + ", programText=" + programText + "]";
	}
	
	@Override
	public List<String> getAsTerraformText() {
		List<String> renderredLines = new ArrayList<String>();
		
		for (String line : terraformTemplateLines) {
			line = line.replaceAll("@@chartId@@", getId());
			line = line.replace("@@chartName@@", getName());
			line = line.replaceAll("@@programText@@", getProgramText());
			line = line.replaceAll("@@description@@", getDescription());
			line = renderSpecifics(line);
			//line = specifics.isEmpty() ? line : specifics;
			renderredLines.add(line);
		}
		
		return renderredLines;
	}

	protected abstract String renderSpecifics(String line);
	
}
