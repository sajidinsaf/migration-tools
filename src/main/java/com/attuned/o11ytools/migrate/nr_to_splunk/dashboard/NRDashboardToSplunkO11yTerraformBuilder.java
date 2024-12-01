package com.attuned.o11ytools.migrate.nr_to_splunk.dashboard;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.attuned.o11ytools.migrate.nr_to_splunk.transform.Transformer;
import com.attuned.o11ytools.model.nr.dashboard.NRDashboard;
import com.attuned.o11ytools.model.nr.dashboard.NRPage;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.splunk.terraform.Chart;
import com.attuned.o11ytools.model.wrapper.NRWidgetAndChartWrapper;
import com.attuned.o11ytools.model.wrapper.Wrapper;
import com.attuned.o11ytools.util.FileUtils;
import com.attuned.o11ytools.util.IdUtils;

public class NRDashboardToSplunkO11yTerraformBuilder {
	
	private FileUtils fileUtils;
	private Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> widgetToChartTransformers;
	private IdUtils idUtils;
	public NRDashboardToSplunkO11yTerraformBuilder(FileUtils fileUtils, IdUtils idUtils, Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> widgetToChartTransformers) {
		this.fileUtils = fileUtils;
		this.idUtils = idUtils;
		this.widgetToChartTransformers = widgetToChartTransformers;
	}

  public void build(List<NRDashboard> nrDashboards, Properties props) throws Exception {
		
		String tfDirNamePrefix = props.getProperty("tf.dir.name.prefix");
		
		File terraformDir = fileUtils.getUniquePathUnderBaseDir(new File(props.getProperty("splunk.o11y.terrform.base.dir")), tfDirNamePrefix);
		
		
		
		for (NRDashboard nrDashboard : nrDashboards) {
//		  if (!nrDashboard.getName().contains("SRE")) {
//		    continue;
//		  }
			File dashboardGroupTFFile = fileUtils.getSplunkDashboardGroupTFFilePath(nrDashboard, terraformDir);
      fileUtils.writeToFile(getDashboardGroupLines(nrDashboard, props), dashboardGroupTFFile, false);
			for (NRPage page : nrDashboard.getPages()) {

		    fileUtils.writeToFile(getDashboardOpeningLines(page, nrDashboard, props), dashboardGroupTFFile, true);
	      File dashboardChartsFile = fileUtils.getChartsFilePathForDashboard(nrDashboard, terraformDir);
	      
	      int row = 0;
	      int column = 0;
				for (NRWidget w : page.getWidgets()) {
					String widgetId = w.getVisualization().getId();
					Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>> transformer = widgetToChartTransformers.get(widgetId);
					if (transformer == null) {
					  System.out.println("No transformer found for NR widget: "+w);
					  continue;
					}
					NRWidgetAndChartWrapper<? extends Chart> wrapper = transformer.transform(w);
		      writeChartsFile(dashboardChartsFile ,Arrays.asList(wrapper));
		      writeAddChartToDashboard(dashboardGroupTFFile, wrapper, column, row);
		      
		      column += 4;
          if (column==12) {
		        ++row;
		        column=0;
		      }
				}
				
				closeJson(dashboardGroupTFFile, "");
				
			}
			
		}
		
		
	}

  private void closeJson(File file, String indent) {
    
    fileUtils.writeToFile(Arrays.asList(indent+"}"), file, true);
   
  }

  private void writeAddChartToDashboard(File dashboardGroupTFFile, NRWidgetAndChartWrapper<? extends Chart> wrapper, int column, int row) {
    List<String> lines = new ArrayList<String>();
    
    lines.add("  chart {");
    lines.add("    chart_id = "+idUtils.getChartIdForTerraformDashboard(wrapper)+".id");
    lines.add("    width = 4");
    lines.add("    height = 1");
    lines.add("    column = "+column);
    lines.add("    row = "+row);
    lines.add("  }");
    fileUtils.writeToFile(lines, dashboardGroupTFFile, true);
    
  }

  private List<String> getDashboardOpeningLines(NRPage page, NRDashboard dashboard, Properties props) {
    List<String> lines = new ArrayList<String>();
  
    lines.add("resource \"signalfx_dashboard\" \""+page.getId()+"\" {");
    lines.add("  name = \""+page.getName()+"\"");
    lines.add("  description = \""+page.getDescription()+"\""); 
    lines.add("  dashboard_group = signalfx_dashboard_group."+dashboard.getId()+".id");
    return lines;
  }

  private List<String> getDashboardGroupLines(NRDashboard nrDashboard, Properties props) {
    List<String> lines = new ArrayList<String>();
    lines.add("resource \"signalfx_dashboard_group\" \""+nrDashboard.getId()+"\" {"); 
    lines.add("  name = \""+nrDashboard.getName()+"\"");
    lines.add("  description = \""+nrDashboard.getDescription()+"\"");
    lines.add("  permissions {");
    lines.add("    principal_id    = \""+props.getProperty("principal.id")+"\"");
    lines.add("    principal_type  = \"ORG\"");
    lines.add("    actions         = [\"READ\", \"WRITE\"]");
    lines.add("  }");
    lines.add("}");
    return lines;
  }

  private void writeChartsFile(File dashboardChartsFile, List<NRWidgetAndChartWrapper<? extends Chart>> charts) {
    List<String> lines = new ArrayList<String>();
    for (NRWidgetAndChartWrapper<? extends Chart> pair : charts) {
      lines.addAll(pair.getRight().getAsTerraformText());
      lines.add("");
    }
    
    fileUtils.writeToFile(lines, dashboardChartsFile, true);
  }
}
