package com.attuned.o11ytools.parser.nr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.attuned.o11ytools.model.nr.dashboard.NRPage;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.util.IdUtils;

public class NewRelicPageJsonParser {
	
	private NewRelicWidgetJsonParser widgetParser;
	private IdUtils idUtils;
  private AtomicInteger pageCounter = new AtomicInteger();
	public NewRelicPageJsonParser(NewRelicWidgetJsonParser widgetParser, IdUtils idUtils) {
		this.widgetParser = widgetParser;
		this.idUtils = idUtils;
	}
	
	public List<NRPage> parsePages(List<PageJson> pageJsons) {
		List<NRPage> pages = new ArrayList<NRPage>();
		
		for (PageJson pgJson: pageJsons) {
			NRPage page = new NRPage();
			String name = pgJson.getName();
			page.setId(idUtils.buildIdFromName("dashboard_"+name+"_"+pageCounter.getAndIncrement()));
			page.setName(idUtils.buildIdFromName(name));
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
