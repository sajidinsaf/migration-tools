package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.util.ArrayList;
import java.util.List;

import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.nr.dashboard.NrqlQuery;

public class NrqlToProgramTextTransformer implements Transformer<NRWidget, String> {

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
			String programTextLine = programTextString.replaceAll("metricName", metricName).replaceAll("label", getAsLabel(metricName));
			sb.append(programTextLine);
		}
		return sb.toString();
	}
	
	private String getAsLabel(String metricName) {
		return metricName.replaceAll("\\.", "_");
	}
	
	
	public List<String> getMetricNames(List<NrqlQuery> nrqls) {
		List<String> metricNames = new ArrayList<String>();
		for (NrqlQuery nrqlQuery : nrqls) {
			String query = nrqlQuery.getQuery();
			String metricName = findAndMapMetricName(query);
			metricNames.add(metricName);
		}
		return metricNames;
	}
	
	private String findAndMapMetricName(String query) {
      String nrMetricName = query.split("(")[1].split(")")[0];
      String mappedMetricName = metricNameTransformer.transform(nrMetricName);
      return mappedMetricName;
	}

}
