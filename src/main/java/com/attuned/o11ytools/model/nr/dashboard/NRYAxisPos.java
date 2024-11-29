package com.attuned.o11ytools.model.nr.dashboard;

public class NRYAxisPos {
	
	private boolean zero;
	
	public NRYAxisPos() {
		
	}

	public NRYAxisPos(boolean zero) {
		super();
		this.zero = zero;
	}

	public boolean isZero() {
		return zero;
	}

	public void setZero(boolean zero) {
		this.zero = zero;
	}

	@Override
	public String toString() {
		return "NRYAxisPos [zero=" + zero + "]";
	}
	
	

}
