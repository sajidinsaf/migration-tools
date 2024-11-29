package com.attuned.o11ytools.parser.nr;

import java.util.ArrayList;
import java.util.List;

import com.attuned.o11ytools.model.nr.dashboard.NRPage;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;

public class NewRelicPageJsonParser {
	
	private NewRelicWidgetJsonParser widgetParser;
	public NewRelicPageJsonParser(NewRelicWidgetJsonParser widgetParser) {
		this.widgetParser = widgetParser;
	}
	
	public List<NRPage> parsePages(List<PageJson> pageJsons) {
		List<NRPage> pages = new ArrayList<NRPage>();
		
		for (PageJson pgJson: pageJsons) {
			NRPage page = new NRPage();
			page.setName(pgJson.getName());
			page.setDescription(pgJson.getDescription());
			

			for (List<String> widgetLines : pgJson.getWdgtJsons()) {
			    NRWidget w = widgetParser.parseWidget(widgetLines);
			    page.addWidget(w);
			}
			
			pages.add(page);
		}
		
		return pages;
	}

}
