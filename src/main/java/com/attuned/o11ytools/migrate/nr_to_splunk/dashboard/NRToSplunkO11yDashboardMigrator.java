package com.attuned.o11ytools.migrate.nr_to_splunk.dashboard;

import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.attuned.o11ytools.model.nr.dashboard.NRDashboard;
import com.attuned.o11ytools.model.nr.dashboard.NrqlQuery;
import com.attuned.o11ytools.model.nr.dashboard.NRPage;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.parser.nr.NewRelicDashboardJsonParser;
import com.attuned.o11ytools.util.FileUtils;

public class NRToSplunkO11yDashboardMigrator {
	
	private static boolean runWithStats = true;
	
	private FileUtils filePathBuilder;
	
	public NRToSplunkO11yDashboardMigrator(FileUtils filePathBuilder) {
		this.filePathBuilder = filePathBuilder;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("No dashboard propeties file provide");
			System.exit(1);
		}
		
		Properties props = new Properties();
		props.load(new FileReader(args[0]));
		

		new NRToSplunkO11yDashboardMigrator(new FileUtils()).process(props);
		

	}
	
	private void process(Properties props) throws Exception {
		String dashboardJsonDirPath = props.getProperty("nr.dashboards.json.base.dir");

		System.out.println(dashboardJsonDirPath);
		File dir = new File(dashboardJsonDirPath);
		
		List<NRDashboard> nrDashboards = new NewRelicDashboardJsonParser().parse(dir);

		printNRDashboardStats(nrDashboards);
		
		printNRWidgetStats(nrDashboards);
		
		
	    // new NRDashboardToSplunkO11yTerraformBuilder(filePathBuilder).build(nrDashboards, props);
		
	}
	
	private void printNRWidgetStats(List<NRDashboard> nrDashboards) {
		
		if (!runWithStats) {
			return;
		}

		Set<String> vizs = new HashSet<String>();
		for (NRDashboard dash : nrDashboards) {
			for (NRPage page : dash.pages) {

				for (NRWidget w : page.getWidgets()) {
					vizs.add(w.getVisualization().getId());
					List<NrqlQuery> nrqlQueries = w.getRawConfiguration().getNrqlQueries();
					for (NrqlQuery query : nrqlQueries) {
						if (dash.getName().contains("SRE APM insight HLE")) {
						   System.out.println(query.getQuery());
						}
					}

				}
			}
		}
		System.out.println(vizs);

	}

	private void printNRDashboardStats(List<NRDashboard> nrDashboards) {
		if (!runWithStats) {
			return;
		}
		int totalCount = 0;
		for (NRDashboard dash : nrDashboards) {
			for (NRPage page : dash.pages) {
				int pageCount = page.getWidgets().size();
				totalCount += pageCount;
				System.out.println("Dashboard: "+dash.getName()+ " NRPage: "+page.getName()+ " WidgetCount: "+pageCount);
			}
		}
		System.out.println("Total widget count: "+ totalCount);
	}

}
