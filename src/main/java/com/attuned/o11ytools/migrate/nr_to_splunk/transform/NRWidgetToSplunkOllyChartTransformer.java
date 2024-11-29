package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.util.List;

import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.splunk.terraform.Chart;
import com.attuned.o11ytools.model.wrapper.Wrapper;

public abstract class NRWidgetToSplunkOllyChartTransformer implements Transformer<NRWidget, Wrapper<Chart, NRWidget>> {

	private Transformer<NRWidget, String> chartIdTransformer;
	private Transformer<NRWidget, String> nrqlToProgramTextTransformer;
	
	
	public NRWidgetToSplunkOllyChartTransformer(Transformer<NRWidget, String> chartIdTransformer, Transformer<NRWidget, String> nrqlToProgramTextTransformer) {
		this.chartIdTransformer = chartIdTransformer;
		this.nrqlToProgramTextTransformer = nrqlToProgramTextTransformer;
		
	}
	@Override
	public  Wrapper<Chart, NRWidget> transform(NRWidget w) {
		
		String id = chartIdTransformer.transform(w);
		
		String programText = nrqlToProgramTextTransformer.transform(w);
		
		Chart chart = buildChart(w, id, w.getTitle(), w.getTitle(), programText);
		return null;
	}
	
	protected abstract Chart buildChart(NRWidget w, String id, String title, String title2, String programText);
	
	public abstract List<NRWidgetType> getSupportedNRWidgetTypes();

}
