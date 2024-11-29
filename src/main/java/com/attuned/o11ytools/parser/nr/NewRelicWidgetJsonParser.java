package com.attuned.o11ytools.parser.nr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.attuned.o11ytools.model.nr.dashboard.NRFacet;
import com.attuned.o11ytools.model.nr.dashboard.NRLayout;
import com.attuned.o11ytools.model.nr.dashboard.NrqlQuery;
import com.attuned.o11ytools.model.nr.dashboard.NRPlatformOptions;
import com.attuned.o11ytools.model.nr.dashboard.NRRawConfiguration;
import com.attuned.o11ytools.model.nr.dashboard.NRThresholds;
import com.attuned.o11ytools.model.nr.dashboard.NRVisualization;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.nr.dashboard.NRYAxisPos;

import static com.attuned.o11ytools.parser.nr.NewRelicDashboardJsonParser.getVal;
import static com.attuned.o11ytools.parser.nr.NewRelicDashboardJsonParser.getKey;
public class NewRelicWidgetJsonParser {
	
	public NRWidget parseWidget(List<String> widgetLines) {
		
		NRWidget w = new NRWidget();
		for (int i = 0; i<widgetLines.size(); i++) {
			String s = widgetLines.get(i);
			if (s.contains("\"title\":")) {
				String title = getVal(s);
				w.setTitle(title);
			} else if (s.contains("\"layout\":")) {
				w.setLayout(getLayout(widgetLines, i));
			} else if (s.contains("\"linkedEntityGuids\"")) {
				w.setLinkedEntityGuids(getLinkedEntityGuids(widgetLines, i));
			} else if (s.contains("\"visualization\"")) {
				w.setVisualization(getVisualization(widgetLines, i));
			} else if (s.contains("\"rawConfiguration\"")) {
				w.setRawConfiguration(getRawConfiguration(widgetLines, i));
			}
		}
		
		return w;
		
	}

	private NRRawConfiguration getRawConfiguration(List<String> widgetLines, int rawConfigIndex) {
		boolean showOtherSeries = Boolean.parseBoolean(getVal(widgetLines.get(rawConfigIndex+2)));
		int nrqlQueryIndex = rawConfigIndex+4;
		List<NrqlQuery> queries = getNrqlQueries(widgetLines, nrqlQueryIndex);
		NRPlatformOptions platformOptions = getPlatformOptions(widgetLines, nrqlQueryIndex);
		NRThresholds thresholds = getThreshold(widgetLines, nrqlQueryIndex);
		NRYAxisPos yAxisLeft = getYAxisPos(widgetLines, nrqlQueryIndex, "\"yAxisLeft\"");
		NRYAxisPos yAxisRight = getYAxisPos(widgetLines, nrqlQueryIndex, "\"yAxisRight\"");
		NRRawConfiguration r = new NRRawConfiguration(new NRFacet(showOtherSeries), queries, platformOptions, thresholds, yAxisLeft, yAxisRight);
		return r;
	}

	private NRYAxisPos getYAxisPos(List<String> widgetLines, int nrqlQueryIndex, String yAxisString) {
		
		Map<String, String> vals = parseKeyValueSection(widgetLines, nrqlQueryIndex, yAxisString);
		
		if (vals == null || vals.isEmpty()) {
			return null;
		}
		boolean zero = Boolean.parseBoolean(vals.get("zero"));
		return new NRYAxisPos(zero);
	}

	private NRThresholds getThreshold(List<String> widgetLines, int nrqlQueryIndex) {
		
		Map<String, String> keyValues = parseKeyValueSection(widgetLines, nrqlQueryIndex, "\"thresholds\":");

		return keyValues == null || keyValues.isEmpty() ? null : new NRThresholds(keyValues);
	}

	private NRPlatformOptions getPlatformOptions(List<String> widgetLines, int nrqlQueryIndex) {
		
		Map<String, String> keyValues = parseKeyValueSection(widgetLines, nrqlQueryIndex, "\"platformOptions\":");
	
		return keyValues == null || keyValues.isEmpty() ? null : new NRPlatformOptions(keyValues);
	}
	
	
	private Map<String, String> parseKeyValueSection(List<String> widgetLines, int nrqlQueryIndex, String keyValueStart) {
		boolean matchFound = false;
		Map<String, String> po = new HashMap<String, String>();
		for (int i=nrqlQueryIndex; i<widgetLines.size(); i++) {
			String line = widgetLines.get(i);
			if (line.contains(keyValueStart)) {
				matchFound =true;
			} else if (matchFound && !line.contains("}")) {
				String key = getKey(line);
				String val = getVal(line);
				po.put(key, val);
			} else if (matchFound && line.contains("}")) {
				return po;
			}
		}
		return null;
	}

	private List<NrqlQuery> getNrqlQueries(List<String> widgetLines, int queryIndex) {
		List<NrqlQuery> nrqlQueries = new ArrayList<NrqlQuery>();
		String[] accountIds = null;
		for (int i=queryIndex+4; i<widgetLines.size(); i++) {
			String line = widgetLines.get(i);
			if (line.contains("\"accountIds\"")) {
				accountIds = getQueryAccountIds(widgetLines, i);
			} else if (line.contains("\"query\":")) {
				String queryString = getVal(line);
				nrqlQueries.add(new NrqlQuery(accountIds, queryString));
			}
		}	
		return nrqlQueries;
	}

	private String[] getQueryAccountIds(List<String> widgetLines, int accountIdsIndex) {
		StringBuilder sb = new StringBuilder();
		for (int i = accountIdsIndex+1 ; i<widgetLines.size(); i++) { // we expect that accountIds Index will be of the line for equch nrqlQuery that contains "accountIds". So we add one to it and proceed to the next line containing the account id. 
			String line = widgetLines.get(i).trim().replaceAll(",", "");

			if (line.equals("]")) {
				return sb.toString().split(",");
			} else if (sb.toString().equals("")) { // if this is the first id don't add a comma after it
				sb.append(line);
			} else {
				sb.append(line).append(",");
			}
			
		}
		return null;
	}

	private NRVisualization getVisualization(List<String> widgetLines, int i) {
		String id = getVal(widgetLines.get(i+1));
		NRVisualization v = new NRVisualization(id);
		return v;
	}

	private NRLayout getLayout(List<String> widgetLines, int indexOfLayout) {
		int column = Integer.parseInt(getVal(widgetLines.get(indexOfLayout+1)));
		int row =  Integer.parseInt(getVal(widgetLines.get(indexOfLayout+2)));
		int width = Integer.parseInt(getVal(widgetLines.get(indexOfLayout+3)));
		int height = Integer.parseInt(getVal(widgetLines.get(indexOfLayout+4)));
		NRLayout l = new NRLayout(column, row, width, height);
		return l;
	}
	
	public String[] getLinkedEntityGuids(List<String> widgetLines, int indexOfGuids) {
		String val = getVal(widgetLines.get(indexOfGuids));
		if (val == null) {
			return null;
		}
		
		String[] sGuids = val.split(",");
		
		return sGuids;
	}

}
