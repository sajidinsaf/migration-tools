package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.util.concurrent.atomic.AtomicInteger;

import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.util.IdUtils;

public class WidgetNameToChartIdTransformer implements Transformer<NRWidget, String> {
	
	private AtomicInteger counter = null;
	private IdUtils idUtils;
	public WidgetNameToChartIdTransformer(IdUtils idUtils) {
		counter = new AtomicInteger(0);
		this.idUtils = idUtils;
	}

	@Override
	public String transform(NRWidget w) {
		String name = w.getTitle();
		name = clean(name)+"_"+counter.incrementAndGet();
		return name;
	}
	
	public String clean(String name) {
		return "chart_"+idUtils.buildIdFromName(name);
	}

}
