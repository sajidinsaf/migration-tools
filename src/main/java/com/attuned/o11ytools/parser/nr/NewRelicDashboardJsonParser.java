package com.attuned.o11ytools.parser.nr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.attuned.o11ytools.model.nr.dashboard.NRDashboard;
import com.attuned.o11ytools.model.nr.dashboard.NrqlQuery;
import com.attuned.o11ytools.model.nr.dashboard.NRPage;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;

public class NewRelicDashboardJsonParser {

	public List<NRDashboard> parse(File dir) throws Exception {

		
		List<NRDashboard> dashboards = new ArrayList<NRDashboard>();

		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				continue;
			}
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line = null;
			List<String> lines = new ArrayList<String>();

			boolean pageOpen = false;

			List<List<String>> pagesJson = new ArrayList<List<String>>();

			List<String> pageJson = new ArrayList<String>();

			List<String> variablesJson = new ArrayList<String>();
			boolean variablesOpen = false;
			String name = null;
			String description = null;
			String permissions = null;
			while ((line = reader.readLine()) != null) {

				if (name == null && line.contains("\"name\":")) {
					name = getVal(line);
				} else if (description == null && line.contains("\"description\":")) {
					description = getVal(line);
				} else if (permissions == null && line.contains("\"permissions\":")) {
					permissions = getVal(line);
				} else if (line.equals("  \"variables\": [") || variablesOpen) {
					variablesOpen = true;
					variablesJson.add(line);
				} else if (!variablesOpen && line.equals("    {")) {
					pageOpen = true;
					pageJson = new ArrayList<String>();
				} else if (!variablesOpen && line.equals("    },")) {
					pageOpen = false;
					pagesJson.add(pageJson);
				} else if (pageOpen) {
					pageJson.add(line);
				}

				lines.add(line);
			}

//		for (String s : pagesJson.get(2)) {
//			System.out.println(s);
//		}

			List<PageJson> pageJsons = new ArrayList<PageJson>();

			boolean widgetOpen = false;
			for (List<String> pgJsonLines : pagesJson) {
				List<List<String>> wdgtJsons = new ArrayList<List<String>>();
				PageJson pgJsnObj = new PageJson();

				List<String> widgetLines = new ArrayList<String>();

				for (String s : pgJsonLines) {
					if (s.startsWith("      \"name\":")) {
						pgJsnObj.setName(getVal(s));
					} else if (s.startsWith("      \"description\":")) {
						pgJsnObj.setDescription(getVal(s));
					} else if (s.startsWith("          \"title\"")) {
						widgetOpen = true;
						widgetLines = new ArrayList<String>();
						widgetLines.add(s);
					} else if (s.startsWith("        },")) {
						widgetOpen = false;
						wdgtJsons.add(widgetLines);
					} else if (widgetOpen) {
						widgetLines.add(s);
					}
				}
				pgJsnObj.setWdgtJsons(wdgtJsons);
				pageJsons.add(pgJsnObj);

			}

			NewRelicPageJsonParser nrPageJsonParser = new NewRelicPageJsonParser(new NewRelicWidgetJsonParser());
			List<NRPage> pages = nrPageJsonParser.parsePages(pageJsons);

			dashboards.add(new NRDashboard(name, description, permissions, pages));




		}

		
		return dashboards;
	}

	public static String getVal(String line) {
		if (!line.contains(":") ) {
			return null;
		}
		String rawVal = line.split(":")[1].replaceAll("\"", "").trim().replaceAll(",", "");
		if (rawVal.equals("null")) {
			return null;
		}
		return rawVal;
	}
	

	public static String getKey(String line) {
		if (!line.contains(":") ) {
			return null;
		}
		return line.split(":")[0].replaceAll("\"", "").trim();
	}

}

class PageJson {
    private String name;
    private String description;
    
	private List<List<String>> wdgtJsons;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<List<String>> getWdgtJsons() {
		return wdgtJsons;
	}

	public void setWdgtJsons(List<List<String>> wdgtJsons) {
		this.wdgtJsons = wdgtJsons;
	}

	@Override
	public String toString() {
		return "PageJson [name=" + name + ", description=" + description + ", wdgtJsons=" + wdgtJsons + "]";
	}
	
	
	
}