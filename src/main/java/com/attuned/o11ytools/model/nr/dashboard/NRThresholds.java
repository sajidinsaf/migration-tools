package com.attuned.o11ytools.model.nr.dashboard;

import java.util.HashMap;
import java.util.Map;

public class NRThresholds {

	private Map<String, String> thresholds = new HashMap<String, String>();

    
    public NRThresholds() {
    	
    }
    
	public NRThresholds(Map<String, String> keyValues) {
		this.thresholds = keyValues;
	}

	public void addOption(String key, String value) {
		thresholds.put(key, value);
	}

    public Map<String, String> getThresholds() {
    	return thresholds;
    }

	@Override
	public String toString() {
		return "NRThresholds [thresholds=" + thresholds + "]";
	}
    
    
}
