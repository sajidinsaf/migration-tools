package com.attuned.o11ytools.model.splunk.terraform;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.attuned.o11ytools.migrate.terraform.Terraformable;

public abstract class Chart implements Terraformable {

  private final Logger LOGGER;
	private String id, name, description, programText;
    
  private List<String> terraformTemplateLines;

	
	public Chart(String id, String name, String description, String programText, List<String> terraformTemplateLines) {
		LOGGER = LogManager.getLogger(getClass());
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
		  try {
			line = line.replaceAll("@@chartId@@", getId());
			line = line.replace("@@chartName@@", getName());
			line = line.replaceAll("@@programText@@", getProgramText(line));
			line = line.replaceAll("@@description@@", getDescription());
			line = renderSpecifics(line);
			//line = specifics.isEmpty() ? line : specifics;
			renderredLines.add(line);
		  } catch (Exception e) {
		    LOGGER.error("Warning!!! Error passing line: ["+line+"] "+this, e);
		  }
		}
		
		return renderredLines;
	}

	private String getProgramText(String line) {
	  String lineSep = System.getProperty("line.separator");
	  if (programText.indexOf(lineSep) == -1) {
	    return programText;
	  }
	  String indent = line.split("@")[0];
	  String[] dataPoints = programText.split(lineSep);
	  StringBuilder sb = new StringBuilder(dataPoints[0]);
	  
	  for (int i=1; i<dataPoints.length; i++) {
	    sb.append(lineSep).append(indent).append(dataPoints[i]);
	  }
    return sb.toString();
  }

  protected abstract String renderSpecifics(String line);
  
  public abstract String getSignalFxTerraformChartType();
  
}
