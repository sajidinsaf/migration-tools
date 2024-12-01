package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.attuned.o11ytools.migrate.nr_to_splunk.transform.nrql.DefaultMetricNameTransformer;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.splunk.terraform.Chart;
import com.attuned.o11ytools.model.wrapper.NRWidgetAndChartWrapper;
import com.attuned.o11ytools.util.FileUtils;
import com.attuned.o11ytools.util.IdUtils;

public class TransformerFactory {

  private FileUtils filePathBuilder;
  
  public TransformerFactory(FileUtils filePathBuilder) {
    this.filePathBuilder = filePathBuilder;
  }
  @SuppressWarnings({ "unchecked" })
  public Map<String, Transformer<NRWidget, NRWidgetAndChartWrapper<? extends Chart>>> getWidgetToChartTransformers(Properties props, IdUtils idUtils) {
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
