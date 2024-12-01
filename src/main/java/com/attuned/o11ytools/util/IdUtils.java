package com.attuned.o11ytools.util;

import com.attuned.o11ytools.model.splunk.terraform.Chart;
import com.attuned.o11ytools.model.wrapper.NRWidgetAndChartWrapper;

public class IdUtils {

  public String buildIdFromName(String name) {
    String id = name.replaceAll("\\.", "_").replaceAll("\\$", "usd").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\%", "pct").replaceAll("'", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "_").replaceAll("\\\\", "").replaceAll("-", "").replaceAll("<", "").replaceAll(">", "").replaceAll("\\/", "").replaceAll("/", "");
    return id;
  }

  public String getChartIdForTerraformDashboard(NRWidgetAndChartWrapper<? extends Chart> wrapper) {
    Chart chart = wrapper.getRight();
    String id = chart.getSignalFxTerraformChartType() + "." + chart.getId();
    return id;
  }

  public String cleanForTerraform(String name) {
    return buildIdFromName(name);
  }
}
