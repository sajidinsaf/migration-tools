package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.attuned.o11ytools.migrate.nr_to_splunk.Constants;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.nr.dashboard.NrqlQuery;

public class NrqlToProgramTextTransformer implements Transformer<NRWidget, String> {

  private static final Logger LOGGER = LogManager.getLogger(NrqlToProgramTextTransformer.class);
  
  private static final String regex = "^(SELECT|select|Select) (apdex|average|bucketPercentile|cardinality|cdfPercentage|count|derivative|earliest|filter|funnel|histogram|latest|latestrate|max|median|min|percentage|percentile|predictLinear|rate|stdvar|sum|uniqueCount|uniques).*";
 
  private static final Pattern functionPattern = Pattern.compile(regex);
  
	private Transformer<String, String> metricNameTransformer;
	private String programTextString = "data(\"metricName\").publish(label=\"labelName\")";
	
	public NrqlToProgramTextTransformer(Transformer<String, String> metricNameTransformer) {
		this.metricNameTransformer = metricNameTransformer;
	}
	@Override
	public String transform(NRWidget t) {
		List<NrqlQuery> nrqls = t.getRawConfiguration().getNrqlQueries();
		String programText = buildProgramText(getMetricNames(nrqls));
		return programText;
	}
	
	private String buildProgramText(List<String> metricNames) {
		StringBuilder sb = new StringBuilder();
		for (String metricName : metricNames) {
			if (!sb.toString().equals("")) {
				sb.append(System.getProperty("line.separator"));
			}
			String programTextLine = programTextString.replaceAll("metricName", metricName).replaceAll("labelName", getAsLabel(metricName));
			sb.append(programTextLine);
		}
		return sb.toString();
	}
	
	private String getAsLabel(String metricName) {
		return metricName.replaceAll("\\.", "_");
	}
	
	
	private List<String> getMetricNames(List<NrqlQuery> nrqls) {
		List<String> metricNames = new ArrayList<String>();
		for (NrqlQuery nrqlQuery : nrqls) {
			String query = nrqlQuery.getQuery();
			String metricName = findAndMapMetricName(query);
			metricNames.add(metricName);
		}
		if (metricNames.isEmpty()) {
		  return Arrays.asList(Constants.DEFAULT_METRIC_NAME);
		}
		return metricNames;
	}
	
	private String findAndMapMetricName(String query) {
	  try {
	    String nrMetricName = "";
	    if (functionPattern.matcher(query).matches()) {
        nrMetricName = query.split("\\(")[1].split("\\)")[0];
	    } else {
	      nrMetricName = query.split(" ")[1];
	    }
      // System.out.println(nrMetricName);
      String mappedMetricName = metricNameTransformer.transform(nrMetricName);
      return mappedMetricName;
	  } catch (Exception e) {
	    LOGGER.error("Exception wnile extracting metric name. Deafult metric name will be used. For query: "+query, e);
	  }
    return Constants.DEFAULT_METRIC_NAME;
	}


}
