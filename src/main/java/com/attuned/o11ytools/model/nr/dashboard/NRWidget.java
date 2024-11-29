 package com.attuned.o11ytools.model.nr.dashboard;

import java.util.Arrays;

public class NRWidget {

	public String title;
	public NRLayout layout;
	public String[] linkedEntityGuids;
	public NRVisualization  visualization;
	public NRRawConfiguration rawConfiguration;

	public NRWidget() {
		
	}

	public NRWidget(String title, NRLayout layout, String[] linkedEntityGuids, NRVisualization visualization,
			NRRawConfiguration rawConfiguration) {
		super();
		this.title = title;
		this.layout = layout;
		this.linkedEntityGuids = linkedEntityGuids;
		this.visualization = visualization;
		this.rawConfiguration = rawConfiguration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public NRLayout getLayout() {
		return layout;
	}

	public void setLayout(NRLayout layout) {
		this.layout = layout;
	}

	public String[] getLinkedEntityGuids() {
		return linkedEntityGuids;
	}

	public void setLinkedEntityGuids(String[] linkedEntityGuids) {
		this.linkedEntityGuids = linkedEntityGuids;
	}

	public NRVisualization getVisualization() {
		return visualization;
	}

	public void setVisualization(NRVisualization visualization) {
		this.visualization = visualization;
	}

	public NRRawConfiguration getRawConfiguration() {
		return rawConfiguration;
	}

	public void setRawConfiguration(NRRawConfiguration rawConfiguration) {
		this.rawConfiguration = rawConfiguration;
	}

	@Override
	public String toString() {
		return "NRWidget [title=" + title + ", layout=" + layout + ", linkedEntityGuids="
				+ Arrays.toString(linkedEntityGuids) + ", visualization=" + visualization + ", rawConfiguration="
				+ rawConfiguration + "]";
	}
	
	
	
}
