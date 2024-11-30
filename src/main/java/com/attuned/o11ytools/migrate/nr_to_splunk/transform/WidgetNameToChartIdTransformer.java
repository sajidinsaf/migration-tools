package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.util.concurrent.atomic.AtomicInteger;

import com.attuned.o11ytools.model.nr.dashboard.NRWidget;

public class WidgetNameToChartIdTransformer implements Transformer<NRWidget, String> {
	
	private AtomicInteger counter = null;
	public WidgetNameToChartIdTransformer() {
		counter = new AtomicInteger(0);
	}

	@Override
	public String transform(NRWidget w) {
		String name = w.getTitle();
		name = clean(name)+"_"+counter.incrementAndGet();
		return name;
	}
	
	public String clean(String name) {
		return name.replaceAll(" ", "_");
	}

}
