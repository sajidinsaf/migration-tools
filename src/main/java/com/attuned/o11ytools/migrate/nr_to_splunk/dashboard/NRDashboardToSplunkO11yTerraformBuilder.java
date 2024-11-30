package com.attuned.o11ytools.migrate.nr_to_splunk.dashboard;

import java.io.File;
import java.util.ArrayList;
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

public class NRDashboardToSplunkO11yTerraformBuilder {
	
	private FileUtils fileUtils;
	private Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> widgetToChartTransformers;
	
	public NRDashboardToSplunkO11yTerraformBuilder(FileUtils fileUtils, Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> widgetToChartTransformers) {
		this.fileUtils = fileUtils;
		this.widgetToChartTransformers = widgetToChartTransformers;
	}

  public void build(List<NRDashboard> nrDashboards, Properties props) throws Exception {
		
		String tfDirNamePrefix = props.getProperty("tf.dir.name.prefix");
		
		File terraformDir = fileUtils.getUniquePathUnderBaseDir(new File(props.getProperty("splunk.o11y.terrform.base.dir")), tfDirNamePrefix);
		
		
		
		for (NRDashboard nrDashboard : nrDashboards) {
			File dashboardGroupTFFile = fileUtils.getSplunkDashboardGroupTFFilePath(nrDashboard, terraformDir);
			File dashboardChartsFile = fileUtils.getChartsFilePathForDashboard(nrDashboard, terraformDir);
			
			List<NRWidgetAndChartWrapper<? extends Chart>> charts = new ArrayList<NRWidgetAndChartWrapper<? extends Chart>>();
			
			for (NRPage page : nrDashboard.getPages()) {
				for (NRWidget w : page.getWidgets()) {
					String widgetId = w.getVisualization().getId();
					Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>> transformer = widgetToChartTransformers.get(widgetId);
					if (transformer == null) {
					  System.out.println("No transformer found for NR widget: "+w);
					  continue;
					}
					NRWidgetAndChartWrapper<? extends Chart> wrapper = transformer.transform(w);
					charts.add(wrapper);
				}
				
			}
			
      writeChartsFile(dashboardChartsFile ,charts);
			
		}
		
		
	}

  private void writeChartsFile(File dashboardChartsFile, List<NRWidgetAndChartWrapper<? extends Chart>> charts) {
    List<String> lines = new ArrayList<String>();
    for (NRWidgetAndChartWrapper<? extends Chart> pair : charts) {
      lines.addAll(pair.getRight().getAsTerraformText());
      lines.add("");
    }
    
    fileUtils.writeToNewFile(lines, dashboardChartsFile);
  }
}
