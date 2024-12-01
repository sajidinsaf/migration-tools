package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.splunk.terraform.TimeChart;
import com.attuned.o11ytools.util.IdUtils;

import static com.attuned.o11ytools.migrate.nr_to_splunk.transform.NRWidgetType.*;

public class TimeWidgetToTimeChartTransformer extends WidgetToChartTransformer<TimeChart> {

  private Map<String, String> plotTypeMap;
  public TimeWidgetToTimeChartTransformer(Transformer<NRWidget, String> chartIdTransformer,
      Transformer<NRWidget, String> nrqlToProgramTextTransformer, SplunkO11yTemplateFactory templateFactory, IdUtils idUtils) {
    super(chartIdTransformer, nrqlToProgramTextTransformer, templateFactory, idUtils);
    
    plotTypeMap = new HashMap<String, String>();
    
    plotTypeMap.put(viz_line.getName(), "LineChart");
    plotTypeMap.put(viz_area.getName(), "AreaChart");
    plotTypeMap.put(viz_stackedBar.getName(), "ColumnChart");
    plotTypeMap.put(viz_bar.getName(), "Histogram");
  }

  @Override
  protected TimeChart buildChart(NRWidget w, String id, String title, String description, String programText) {
    List<String> terraformTemplate = getTerraformTemplate("signalfx_time_chart");
    String plotType = plotTypeMap.get(w.getVisualization().getId());
    if (plotType == null) {
      plotType = "LineChart";
    }
    TimeChart timeChart = new TimeChart(id, title, description, programText, plotType, true, terraformTemplate);
    return timeChart;
  }

  @Override
  public List<NRWidgetType> getSupportedNRWidgetTypes() {
    return Arrays.asList(viz_line, viz_area, viz_stackedBar, viz_bar);
  }
  

}
