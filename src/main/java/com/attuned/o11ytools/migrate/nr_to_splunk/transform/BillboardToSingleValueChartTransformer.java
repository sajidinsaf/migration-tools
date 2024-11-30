package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.splunk.terraform.SingleValueChart;
import com.attuned.o11ytools.model.splunk.terraform.TimeChart;

import static com.attuned.o11ytools.migrate.nr_to_splunk.transform.NRWidgetType.*;

public class BillboardToSingleValueChartTransformer extends WidgetToChartTransformer<SingleValueChart> {

  public BillboardToSingleValueChartTransformer(Transformer<NRWidget, String> chartIdTransformer,
      Transformer<NRWidget, String> nrqlToProgramTextTransformer, SplunkO11yTemplateFactory templateFactory) {
    super(chartIdTransformer, nrqlToProgramTextTransformer, templateFactory);

  }

  @Override
  protected SingleValueChart buildChart(NRWidget w, String id, String title, String description, String programText) {
    List<String> terraformTemplate = getTerraformTemplate("signalfx_single_value_chart");

    SingleValueChart chart = new SingleValueChart(id, title, description, programText, terraformTemplate);
    return chart;
  }

  @Override
  public List<NRWidgetType> getSupportedNRWidgetTypes() {
    return Arrays.asList(viz_billboard);
  }
  

}
