package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.util.List;

import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.splunk.terraform.Chart;
import com.attuned.o11ytools.model.wrapper.NRWidgetAndChartWrapper;
import com.attuned.o11ytools.model.wrapper.Wrapper;

public abstract class WidgetToChartTransformer<C extends Chart> implements Transformer<NRWidget, Wrapper<C, NRWidget>> {

	private Transformer<NRWidget, String> chartIdTransformer;
	private Transformer<NRWidget, String> nrqlToProgramTextTransformer;
	private SplunkO11yTemplateFactory templateFactory;
	
	
	public WidgetToChartTransformer(Transformer<NRWidget, String> chartIdTransformer, Transformer<NRWidget, String> nrqlToProgramTextTransformer, SplunkO11yTemplateFactory templateFactory) {
		this.chartIdTransformer = chartIdTransformer;
		this.nrqlToProgramTextTransformer = nrqlToProgramTextTransformer;
		this.templateFactory = templateFactory;
		
	}
	@Override
	public  Wrapper<C, NRWidget> transform(NRWidget w) {
		
		String id = chartIdTransformer.transform(w);
		
		String programText = nrqlToProgramTextTransformer.transform(w);
		
		C chart = buildChart(w, id, w.getTitle(), w.getTitle(), programText);
		return new NRWidgetAndChartWrapper(w, chart);
	}
	
	
	public Transformer<NRWidget, String> getChartIdTransformer() {
    return chartIdTransformer;
  }

  public Transformer<NRWidget, String> getNrqlToProgramTextTransformer() {
    return nrqlToProgramTextTransformer;
  }

  public SplunkO11yTemplateFactory getTemplateFactory() {
    return templateFactory;
  }


  protected abstract C buildChart(NRWidget w, String id, String title, String description, String programText);
	
	public abstract List<NRWidgetType> getSupportedNRWidgetTypes();

	protected List<String> getTerraformTemplate(String templateName) {
	  return templateFactory.getChartTFTemplate(templateName);
	}
}
