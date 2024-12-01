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

public class NRToSplunkDashboardMigrator {
	
	private static boolean runWithStats = false;
	
	private FileUtils filePathBuilder;
	
	public NRToSplunkDashboardMigrator(FileUtils filePathBuilder) {
		this.filePathBuilder = filePathBuilder;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("No dashboard propeties file provide");
			System.exit(1);
		}
		
		Properties props = new Properties();
		props.load(new FileReader(args[0]));
		

		new NRToSplunkDashboardMigrator(new FileUtils()).process(props);
		

	}
	
	private void process(Properties props) throws Exception {
		String dashboardJsonDirPath = props.getProperty("nr.dashboards.json.base.dir");

		// System.out.println(dashboardJsonDirPath);
		File dir = new File(dashboardJsonDirPath);
		
		IdUtils idUtils = new IdUtils();
    NewRelicPageJsonParser nrPageJsonParser = new NewRelicPageJsonParser(new NewRelicWidgetJsonParser(), idUtils);
		
		List<NRDashboard> nrDashboards = new NewRelicDashboardJsonParser(idUtils, nrPageJsonParser).parse(dir);

		printNRDashboardStats(nrDashboards);
		
		printNRWidgetStats(nrDashboards);
		
		Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> widgetToChartTransformers = getWidgetToChartTransformers(props);
	  new NRDashboardToSplunkO11yTerraformBuilder(filePathBuilder, new IdUtils(), widgetToChartTransformers).build(nrDashboards, props);
		
	}
	
	@SuppressWarnings({ "unchecked" })
  private Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> getWidgetToChartTransformers(Properties props) {
	  Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> widgetToChartTransformers = new HashMap<String, Transformer<NRWidget,NRWidgetAndChartWrapper<? extends Chart>>>();

    Transformer<NRWidget, String> chartIdTransformer = new WidgetNameToChartIdTransformer();
    
    Transformer<NRWidget, String> nrqlToProgramTextTransformer = new NrqlToProgramTextTransformer(new DefaultMetricNameTransformer(getMetricNameMapping(props)));
            
    SplunkO11yTemplateFactory templateFactory = new SplunkO11yTemplateFactory(filePathBuilder, props);
    
    Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>> timeWidgetTransformer = (Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>) getTimeWidgetToTimeChartTransformer(chartIdTransformer, nrqlToProgramTextTransformer, templateFactory,props);
	  
	  for (String name : new String[] {"viz.bar", "viz.markdown", "viz.line", "viz.area", "viz.table", "viz.pie", "viz.stacked-bar"}) {
	    widgetToChartTransformers.put(name, timeWidgetTransformer);
	  }
	  
	  Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>> billboardWidgetTransformer = (Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>) getBillboardWidgetTransformer(chartIdTransformer, nrqlToProgramTextTransformer, templateFactory);
    widgetToChartTransformers.put("viz.billboard", billboardWidgetTransformer);
    
    return widgetToChartTransformers;
  }

  private Object getBillboardWidgetTransformer(Transformer<NRWidget, String> chartIdTransformer, Transformer<NRWidget, String> nrqlToProgramTextTransformer, SplunkO11yTemplateFactory templateFactory) {
    BillboardToSingleValueChartTransformer biilboardTransformer = new BillboardToSingleValueChartTransformer(chartIdTransformer, nrqlToProgramTextTransformer, templateFactory);
    return biilboardTransformer;
  }

  private Object getTimeWidgetToTimeChartTransformer(Transformer<NRWidget, String> chartIdTransformer, Transformer<NRWidget, String> nrqlToProgramTextTransformer, SplunkO11yTemplateFactory templateFactory,
      Properties props) {
    return new TimeWidgetToTimeChartTransformer(chartIdTransformer, nrqlToProgramTextTransformer, templateFactory);

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

  private void printNRWidgetStats(List<NRDashboard> nrDashboards) {
		
		if (!runWithStats) {
			return;
		}

		Set<String> vizs = new HashSet<String>();
		for (NRDashboard dash : nrDashboards) {
			for (NRPage page : dash.getPages()) {

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
			for (NRPage page : dash.getPages()) {
				int pageCount = page.getWidgets().size();
				totalCount += pageCount;
				System.out.println("Dashboard: "+dash.getName()+ " NRPage: "+page.getName()+ " WidgetCount: "+pageCount);
			}
		}
		System.out.println("Total widget count: "+ totalCount);
	}

}
