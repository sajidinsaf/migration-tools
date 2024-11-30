package com.attuned.o11ytools.model.wrapper;

import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.splunk.terraform.Chart;

public class NRWidgetAndChartWrapper<P extends Chart> implements Wrapper<NRWidget, P> {

  private NRWidget left;
  private P right;
  
  
	public NRWidgetAndChartWrapper(NRWidget left, P right) {
    super();
    this.left = left;
    this.right = right;
  }

  @Override
	public NRWidget getLeft() {
		return left;
	}

	@Override
	public P getRight() {
		return right;
	}

}
