package com.attuned.o11ytools.migrate.nr_to_splunk.dashboard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.attuned.o11ytools.model.nr.dashboard.NRDashboard;
import com.attuned.o11ytools.model.nr.dashboard.NRPage;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.splunk.terraform.Chart;
import com.attuned.o11ytools.util.FileUtils;

public class NRDashboardToSplunkO11yTerraformBuilder {
	
	private FileUtils fileUtils;
	
	public NRDashboardToSplunkO11yTerraformBuilder(FileUtils fileUtils) {
		this.fileUtils = fileUtils;
	}

	public void build(List<NRDashboard> nrDashboards, Properties props) throws Exception {
		
		String tfDirNamePrefix = props.getProperty("tf.dir.name.prefix");
		
		File terraformDir = fileUtils.getUniquePathUnderBaseDir(new File(props.getProperty("splunk.o11y.terrform.base.dir")), tfDirNamePrefix);
		
		
		
		for (NRDashboard nrDashboard : nrDashboards) {
			File dashboardGroupTFFile = fileUtils.getSplunkDashboardGroupTFFilePath(nrDashboard, terraformDir);
			File dashboardChartsFile = fileUtils.getChartsFilePathForDashboard(nrDashboard, terraformDir);
			
			List<Chart> charts = new ArrayList<Chart>();
			
			for (NRPage page : nrDashboard.getPages()) {
				for (NRWidget w : page.getWidgets()) {
					
				}
			}
			
			
		}
		
		
	}
}
