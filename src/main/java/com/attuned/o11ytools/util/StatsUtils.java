package com.attuned.o11ytools.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.attuned.o11ytools.model.nr.dashboard.NRDashboard;
import com.attuned.o11ytools.model.nr.dashboard.NRPage;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.nr.dashboard.NrqlQuery;

public class StatsUtils {

  private static final Logger LOGGER = LogManager.getLogger(StatsUtils.class);
  
  public void printNRWidgetStats(List<NRDashboard> nrDashboards, boolean runWithStats) {
    
    if (!runWithStats) {
      return;
    }

    Map<String, Integer> vizs = new HashMap<String, Integer>();
    int totalNrqlCount = 0;
    int totalNrqlWithFacets = 0;
    int totalNrqlFromLogs = 0;
    int totalNrqlWithFacetsAndLogs = 0;
    LOGGER.info("========================================NRQLS========================================");
    for (NRDashboard dash : nrDashboards) {
      for (NRPage page : dash.getPages()) {

        for (NRWidget w : page.getWidgets()) {
          Integer vizCount = vizs.get(w.getVisualization().getId()) == null ? 0 : vizs.get(w.getVisualization().getId()); 
          vizs.put(w.getVisualization().getId(), ++vizCount);
          List<NrqlQuery> nrqlQueries = w.getRawConfiguration().getNrqlQueries();

          for (NrqlQuery query : nrqlQueries) {
            ++totalNrqlCount;
            if (query.getQuery().toLowerCase().contains("facet")) {
              ++totalNrqlWithFacets;
            }
            if (query.getQuery().toLowerCase().contains("from log")) {
              ++totalNrqlFromLogs;
            }
            
            if (query.getQuery().toLowerCase().contains("facet") && query.getQuery().toLowerCase().contains("from log")) {
              ++totalNrqlWithFacetsAndLogs;
            }
            // LOGGER.info(query.getQuery());
             // System.out.println(query.getQuery());
//            if (dash.getName().contains("SRE APM insight HLE")) {
//               System.out.println(query.getQuery());
//            }
          }

        }
      }
    }
    LOGGER.info("========================================NRQLS========================================");    
    LOGGER.info("Widget types with count:" + vizs.toString());
    LOGGER.info("Total number of NRQL queries: "+totalNrqlCount);
    LOGGER.info("Total number of NRQL queries with facets: "+totalNrqlWithFacets);
    LOGGER.info("Total number of NRQL queries from Log: "+totalNrqlFromLogs);
    LOGGER.info("Total number of NRQL queries from Log with Facets: "+totalNrqlWithFacetsAndLogs);
  }

  public void printNRDashboardStats(List<NRDashboard> nrDashboards, boolean runWithStats) {
    if (!runWithStats) {
      return;
    }
    int totalWidgetCount = 0;
    int totalPageCount =  0;
    int totalDashboardCount = 0;
    for (NRDashboard dash : nrDashboards) {
      ++totalDashboardCount;
      for (NRPage page : dash.getPages()) {
        ++totalPageCount;
        int pageCount = page.getWidgets().size();
        totalWidgetCount += pageCount;
        LOGGER.info("Dashboard: "+dash.getName()+ " NRPage: "+page.getName()+ " WidgetCount: "+pageCount);
      }
    }
    
    LOGGER.info("Total NR dashboard count: "+ totalDashboardCount);
    LOGGER.info("Total page count: "+ totalPageCount);
    LOGGER.info("Total widget count: "+ totalWidgetCount);


    
  }

}
