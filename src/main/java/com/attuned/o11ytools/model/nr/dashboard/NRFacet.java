package com.attuned.o11ytools.model.nr.dashboard;

public class NRFacet {

	private boolean showOtherSeries;
	 
	public NRFacet() {
		
	}

	public NRFacet(boolean showOtherSeries) {
		super();
		this.showOtherSeries = showOtherSeries;
	}

	public boolean isShowOtherSeries() {
		return showOtherSeries;
	}

	public void setShowOtherSeries(boolean showOtherSeries) {
		this.showOtherSeries = showOtherSeries;
	}

  @Override
  public String toString() {
    return "NRFacet [showOtherSeries=" + showOtherSeries + "]";
  }
	 
}
