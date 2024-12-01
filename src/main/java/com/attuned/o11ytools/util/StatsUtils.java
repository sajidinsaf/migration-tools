package com.attuned.o11ytools.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.attuned.o11ytools.model.nr.dashboard.NRDashboard;
import com.attuned.o11ytools.model.nr.dashboard.NRPage;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.nr.dashboard.NrqlQuery;

public class StatsUtils {


  public void printNRWidgetStats(List<NRDashboard> nrDashboards, boolean runWithStats) {
    
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

  public void printNRDashboardStats(List<NRDashboard> nrDashboards, boolean runWithStats) {
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
