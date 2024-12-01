package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.util.List;

import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.splunk.terraform.Chart;
import com.attuned.o11ytools.model.wrapper.NRWidgetAndChartWrapper;
import com.attuned.o11ytools.model.wrapper.Wrapper;
import com.attuned.o11ytools.util.IdUtils;

public abstract class WidgetToChartTransformer<C extends Chart> implements Transformer<NRWidget, Wrapper<C, NRWidget>> {

	private Transformer<NRWidget, String> chartIdTransformer;
	private Transformer<NRWidget, String> nrqlToProgramTextTransformer;
	private SplunkO11yTemplateFactory templateFactory;
	private IdUtils idUtils;
	
	
	public WidgetToChartTransformer(Transformer<NRWidget, String> chartIdTransformer, Transformer<NRWidget, String> nrqlToProgramTextTransformer, SplunkO11yTemplateFactory templateFactory, IdUtils idUtils) {
		this.chartIdTransformer = chartIdTransformer;
		this.nrqlToProgramTextTransformer = nrqlToProgramTextTransformer;
		this.templateFactory = templateFactory;
		this.idUtils = idUtils;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
	public  Wrapper<C, NRWidget> transform(NRWidget w) {
		
		String id = chartIdTransformer.transform(w);
		
		String programText = nrqlToProgramTextTransformer.transform(w);
		
		String name = idUtils.buildIdFromName(w.getTitle());
		
		if (name.equals("")) {
		  name =id;
		}
		
		C chart = buildChart(w, id, name, w.getTitle(), programText);
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
