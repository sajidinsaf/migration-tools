package com.attuned.o11ytools.model.nr.dashboard;

import java.util.HashMap;
import java.util.Map;

public class NRPlatformOptions {

	
	private Map<String, String> options = new HashMap<String, String>();

	public NRPlatformOptions() {
		
	}

	public NRPlatformOptions(Map<String, String> keyValues) {
		options = keyValues;
	}

	public void addOption(String key, String value) {
		options.put(key, value);
	}
	
	public Map<String, String> getOptions() {
		return options;
	}

	@Override
	public String toString() {
		return "NRPlatformOptions [options=" + options + "]";
	}
	
}
