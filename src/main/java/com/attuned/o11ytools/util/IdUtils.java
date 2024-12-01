package com.attuned.o11ytools.util;

import com.attuned.o11ytools.model.splunk.terraform.Chart;
import com.attuned.o11ytools.model.wrapper.NRWidgetAndChartWrapper;

public class IdUtils {

  public String buildIdFromName(String name) {
    String id = name.replaceAll("\\.", "_").replaceAll("\\$", "usd").replaceAll("\\(", "_").replaceAll("\\)", "_").replaceAll("\\%", "pct").replaceAll("'", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "_");
    return id;
  }

  public String getChartIdForTerraformDashboard(NRWidgetAndChartWrapper<? extends Chart> wrapper) {
    Chart chart = wrapper.getRight();
    String id = chart.getTerraformChartIDForDashboard();
    return id;
  }
}
