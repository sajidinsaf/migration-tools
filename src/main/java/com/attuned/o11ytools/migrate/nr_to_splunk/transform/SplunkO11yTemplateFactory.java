package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.attuned.o11ytools.util.FileUtils;

public class SplunkO11yTemplateFactory {
	
	private Map<String, List<String>> templatesMap;
	
	public SplunkO11yTemplateFactory(FileUtils fileUtils, Properties props) {
		templatesMap = load(fileUtils, props);
	}

	private Map<String, List<String>> load(FileUtils fileUtils, Properties props) {
		try {
			File templateDir = new File(props.getProperty("splunk.o11y.chart.templates.dir"));
			Map<String, List<String>> templates = loadTemplates(templateDir, fileUtils);
			return templates;
		} catch (Exception e) {
			throw new RuntimeException("Exception while loading Splunk O11y Chart Templates", e);
		}
	}

	private Map<String, List<String>> loadTemplates(File dir, FileUtils fileUtils) throws IOException {
		
		Map<String, List<String>> templates  = new HashMap<String, List<String>>();
		
		for (File file : dir.listFiles()) {
			List<String> lines = fileUtils.loadFileLines(file);
			String templateName = file.getName().split("\\.")[0];
			templates.put(templateName, lines);
		}
		return templates;
	}
	
	public List<String> getChartTFTemplate(String name) {
		return templatesMap.get(name);
	}
}
