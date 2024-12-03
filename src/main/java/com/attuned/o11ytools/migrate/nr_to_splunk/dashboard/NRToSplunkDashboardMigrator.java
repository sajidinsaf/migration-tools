package com.attuned.o11ytools.migrate.nr_to_splunk.dashboard;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.attuned.o11ytools.migrate.nr_to_splunk.transform.BillboardToSingleValueChartTransformer;
import com.attuned.o11ytools.migrate.nr_to_splunk.transform.NrqlToProgramTextTransformer;
import com.attuned.o11ytools.migrate.nr_to_splunk.transform.SplunkO11yTemplateFactory;
import com.attuned.o11ytools.migrate.nr_to_splunk.transform.TimeWidgetToTimeChartTransformer;
import com.attuned.o11ytools.migrate.nr_to_splunk.transform.Transformer;
import com.attuned.o11ytools.migrate.nr_to_splunk.transform.TransformerFactory;
import com.attuned.o11ytools.migrate.nr_to_splunk.transform.WidgetNameToChartIdTransformer;
import com.attuned.o11ytools.migrate.nr_to_splunk.transform.nrql.DefaultMetricNameTransformer;
import com.attuned.o11ytools.model.nr.dashboard.NRDashboard;
import com.attuned.o11ytools.model.nr.dashboard.NrqlQuery;
import com.attuned.o11ytools.model.splunk.terraform.Chart;
import com.attuned.o11ytools.model.wrapper.NRWidgetAndChartWrapper;
import com.attuned.o11ytools.model.nr.dashboard.NRPage;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.parser.nr.NewRelicDashboardJsonParser;
import com.attuned.o11ytools.parser.nr.NewRelicPageJsonParser;
import com.attuned.o11ytools.parser.nr.NewRelicWidgetJsonParser;
import com.attuned.o11ytools.util.FileUtils;
import com.attuned.o11ytools.util.IdUtils;
import com.attuned.o11ytools.util.StatsUtils;

public class NRToSplunkDashboardMigrator {
	
	private static boolean runWithStats = true;

	private StatsUtils statsUtils;
  private IdUtils idUtils;
  private NewRelicPageJsonParser nrPageJsonParser;
  private NRDashboardToSplunkO11yTerraformBuilder terraformBuilder;
  
	public NRToSplunkDashboardMigrator(StatsUtils statsUtils, IdUtils idUtils, NewRelicPageJsonParser nrPageJsonParser, NRDashboardToSplunkO11yTerraformBuilder terraformBuilder) {

		this.statsUtils = statsUtils;
		this.idUtils = idUtils;
		this.nrPageJsonParser = nrPageJsonParser;
		this.terraformBuilder = terraformBuilder;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("No dashboard propeties file provide");
			System.exit(1);
		}
		
		Properties props = new Properties();
		props.load(new FileReader(args[0]));
		
		IdUtils idUtils = new IdUtils();
    FileUtils fileUtils = new FileUtils();
    
    NewRelicPageJsonParser nrPageJsonParser = new NewRelicPageJsonParser(new NewRelicWidgetJsonParser(), idUtils);

    Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> widgetToChartTransformers = new TransformerFactory(fileUtils).getWidgetToChartTransformers(props, idUtils);
    
    NRDashboardToSplunkO11yTerraformBuilder terraformBuilder = new NRDashboardToSplunkO11yTerraformBuilder(fileUtils, new IdUtils(), widgetToChartTransformers);
		
    new NRToSplunkDashboardMigrator(new StatsUtils(), idUtils, nrPageJsonParser, terraformBuilder).process(props);
		

	}
	
	private void process(Properties props) throws Exception {
		String dashboardJsonDirPath = props.getProperty("nr.dashboards.json.base.dir");

		// System.out.println(dashboardJsonDirPath);
		File dir = new File(dashboardJsonDirPath);
		
		List<NRDashboard> nrDashboards = new NewRelicDashboardJsonParser(idUtils, nrPageJsonParser).parse(dir);

		statsUtils.printNRDashboardStats(nrDashboards, runWithStats);
		statsUtils.printNRWidgetStats(nrDashboards, runWithStats);

	  terraformBuilder.build(nrDashboards, props);
		
	}

}
