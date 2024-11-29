package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.util.Arrays;
import java.util.List;

import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.splunk.terraform.Chart;
import com.attuned.o11ytools.model.splunk.terraform.TimeChart;

import static com.attuned.o11ytools.migrate.nr_to_splunk.transform.NRWidgetType.*;

public class NRTimeWidgetToSplunkO11yTimeChartTransformer extends NRWidgetToSplunkOllyChartTransformer {

  public NRTimeWidgetToSplunkO11yTimeChartTransformer(Transformer<NRWidget, String> chartIdTransformer,
      Transformer<NRWidget, String> nrqlToProgramTextTransformer) {
    super(chartIdTransformer, nrqlToProgramTextTransformer);
  }

  @Override
  protected Chart buildChart(NRWidget w, String id, String title, String title2, String programText) {
    
    TimeChart timeChart = new TimeChart(id, title, title2, programText, programText, false, null);
    return null;
  }

  @Override
  public List<NRWidgetType> getSupportedNRWidgetTypes() {
    return Arrays.asList(viz_line, viz_area, viz_stackedBar, viz_bar);
  }

}
