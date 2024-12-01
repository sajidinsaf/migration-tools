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
	
	private static boolean runWithStats = false;
	
	private FileUtils filePathBuilder;
	private StatsUtils statsUtils;
	
	public NRToSplunkDashboardMigrator(FileUtils filePathBuilder, StatsUtils statsUtils) {
		this.filePathBuilder = filePathBuilder;
		this.statsUtils = statsUtils;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("No dashboard propeties file provide");
			System.exit(1);
		}
		
		Properties props = new Properties();
		props.load(new FileReader(args[0]));
		

		new NRToSplunkDashboardMigrator(new FileUtils(), new StatsUtils()).process(props);
		

	}
	
	private void process(Properties props) throws Exception {
		String dashboardJsonDirPath = props.getProperty("nr.dashboards.json.base.dir");

		// System.out.println(dashboardJsonDirPath);
		File dir = new File(dashboardJsonDirPath);
		
		IdUtils idUtils = new IdUtils();
    NewRelicPageJsonParser nrPageJsonParser = new NewRelicPageJsonParser(new NewRelicWidgetJsonParser(), idUtils);
		
		List<NRDashboard> nrDashboards = new NewRelicDashboardJsonParser(idUtils, nrPageJsonParser).parse(dir);

		statsUtils.printNRDashboardStats(nrDashboards, runWithStats);
		
		statsUtils.printNRWidgetStats(nrDashboards, runWithStats);
		
		Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> widgetToChartTransformers = getWidgetToChartTransformers(props, idUtils);
	  new NRDashboardToSplunkO11yTerraformBuilder(filePathBuilder, new IdUtils(), widgetToChartTransformers).build(nrDashboards, props);
		
	}
	
	@SuppressWarnings({ "unchecked" })
  private Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> getWidgetToChartTransformers(Properties props, IdUtils idUtils) {
	  Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> widgetToChartTransformers = new HashMap<String, Transformer<NRWidget,NRWidgetAndChartWrapper<? extends Chart>>>();

    Transformer<NRWidget, String> chartIdTransformer = new WidgetNameToChartIdTransformer(idUtils);
    
    Transformer<NRWidget, String> nrqlToProgramTextTransformer = new NrqlToProgramTextTransformer(new DefaultMetricNameTransformer(getMetricNameMapping(props)));
            
    SplunkO11yTemplateFactory templateFactory = new SplunkO11yTemplateFactory(filePathBuilder, props);
    
    Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>> timeWidgetTransformer = (Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>) getTimeWidgetToTimeChartTransformer(chartIdTransformer, nrqlToProgramTextTransformer, templateFactory,props, idUtils);
	  
	  for (String name : new String[] {"viz.bar", "viz.markdown", "viz.line", "viz.area", "viz.table", "viz.pie", "viz.stacked-bar"}) {
	    widgetToChartTransformers.put(name, timeWidgetTransformer);
	  }
	  
	  Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>> billboardWidgetTransformer = (Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>) getBillboardWidgetTransformer(chartIdTransformer, nrqlToProgramTextTransformer, templateFactory, idUtils);
    widgetToChartTransformers.put("viz.billboard", billboardWidgetTransformer);
    
    return widgetToChartTransformers;
  }

  private Object getBillboardWidgetTransformer(Transformer<NRWidget, String> chartIdTransformer, Transformer<NRWidget, String> nrqlToProgramTextTransformer, SplunkO11yTemplateFactory templateFactory, IdUtils idUtils) {
    BillboardToSingleValueChartTransformer billboardTransformer = new BillboardToSingleValueChartTransformer(chartIdTransformer, nrqlToProgramTextTransformer, templateFactory, idUtils);
    return billboardTransformer;
  }

  private Object getTimeWidgetToTimeChartTransformer(Transformer<NRWidget, String> chartIdTransformer, Transformer<NRWidget, String> nrqlToProgramTextTransformer, SplunkO11yTemplateFactory templateFactory,
      Properties props, IdUtils idUtils) {
    return new TimeWidgetToTimeChartTransformer(chartIdTransformer, nrqlToProgramTextTransformer, templateFactory, idUtils);

  }

  private Properties getMetricNameMapping( Properties props) {

    String metricNameMappingProperty = "nr.to.splunk.metric.name.mapping";
    Properties nameMapping = new Properties();
    try {
      nameMapping.load(new FileReader(props.getProperty(metricNameMappingProperty)));
    } catch (IOException e) {
      throw new RuntimeException("Could not load metric mappins from: "+props.getProperty(metricNameMappingProperty) + ". Property name to investigate int configuration: "+metricNameMappingProperty);
    }
    return nameMapping;
  }

}
