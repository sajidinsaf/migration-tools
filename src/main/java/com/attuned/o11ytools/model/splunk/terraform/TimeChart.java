package com.attuned.o11ytools.model.splunk.terraform;

import java.util.List;

public class TimeChart extends Chart {
    private String plotType;
    private boolean showDataMarkers = true;
    private LegendOptionsFields legendOptionsFields;

	
	public TimeChart(String id, String name, String description, String programText, String plotType, boolean showDataMarkers, List<String> terraformTemplateLines) {
		super(id, name, description, programText, terraformTemplateLines);
		this.plotType = plotType;
		this.showDataMarkers = showDataMarkers;
	}


	public String getPlot_type() {
		return plotType;
	}

	public void setPlot_type(String plotType) {
		this.plotType = plotType;
	}

	public boolean isShowDataMarkers() {
		return showDataMarkers;
	}

	public void setShowDataMarkers(boolean showDataMarkers) {
		this.showDataMarkers = showDataMarkers;
	}

	public LegendOptionsFields getLegendOptionsFields() {
		return legendOptionsFields;
	}

	public void setLegendOptionsFields(LegendOptionsFields legendOptionsFields) {
		this.legendOptionsFields = legendOptionsFields;
	}

    @Override
	protected String renderSpecifics(String line) {
    	line = line.replaceAll("@@showDataMarkers@@", showDataMarkers+"");
    	if (line.contains("@@legend_options_fields@@")) {
          if (legendOptionsFields == null || legendOptionsFields.getFields() == null || legendOptionsFields.getFields().isEmpty()) {
        		return "";
          }
       	  List<String> legendOptionsFieldsList = legendOptionsFields.getAsTerraformText();
    	  String lof = getAsString(legendOptionsFieldsList);
    	  line = line.replaceAll("@@legend_options_fields@@", lof);
    	}
    	return line;
	}

	private String getAsString(List<String> legendOptionsFieldsList) {
		StringBuilder sb = new StringBuilder();
		for (String s : legendOptionsFieldsList) {
			sb.append(s).append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
  @Override
  public String getSignalFxTerraformChartType() {
    return "signalfx_time_chart";
  }

    
}
