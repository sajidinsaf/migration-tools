package com.attuned.o11ytools.model.nr.dashboard;
import java.util.List;
public class NRRawConfiguration {

	private NRFacet facet;
	private List<NrqlQuery> nrqlQueries;
	private NRPlatformOptions platformOptions;
	private NRThresholds thresholds;
	private NRYAxisPos yAxisLeft;
	private NRYAxisPos yAxisRight;
	
	public NRRawConfiguration() {
		
	}
	
	public NRRawConfiguration(NRFacet facet, List<NrqlQuery> nrqlQueries, NRPlatformOptions platformOptions,
			NRThresholds thresholds, NRYAxisPos yAxisLeft, NRYAxisPos yAxisRight) {
		super();
		this.facet = facet;
		this.nrqlQueries = nrqlQueries;
		this.platformOptions = platformOptions;
		this.thresholds = thresholds;
		this.yAxisLeft = yAxisLeft;
		this.yAxisRight = yAxisRight;
	}


	public NRFacet getFacet() {
		return facet;
	}


	public void setFacet(NRFacet facet) {
		this.facet = facet;
	}


	public List<NrqlQuery> getNrqlQueries() {
		return nrqlQueries;
	}


	public void setNrqlQueries(List<NrqlQuery> nrqlQueries) {
		this.nrqlQueries = nrqlQueries;
	}


	public NRPlatformOptions getPlatformOptions() {
		return platformOptions;
	}


	public void setPlatformOptions(NRPlatformOptions platformOptions) {
		this.platformOptions = platformOptions;
	}


	public NRThresholds getThresholds() {
		return thresholds;
	}


	public void setThresholds(NRThresholds thresholds) {
		this.thresholds = thresholds;
	}


	public NRYAxisPos getyAxisLeft() {
		return yAxisLeft;
	}


	public void setyAxisLeft(NRYAxisPos yAxisLeft) {
		this.yAxisLeft = yAxisLeft;
	}


	public NRYAxisPos getyAxisRight() {
		return yAxisRight;
	}


	public void setyAxisRight(NRYAxisPos yAxisRight) {
		this.yAxisRight = yAxisRight;
	}

	@Override
	public String toString() {
		return "NRRawConfiguration [facet=" + facet + ", nrqlQueries=" + nrqlQueries + ", platformOptions="
				+ platformOptions + ", thresholds=" + thresholds + ", yAxisLeft=" + yAxisLeft + ", yAxisRight="
				+ yAxisRight + "]";
	}
	
	
	


}
