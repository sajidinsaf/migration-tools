package com.attuned.o11ytools.model.splunk.terraform;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeCharTest {


	@BeforeEach
	void setUp() throws Exception {
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetAsTerraformText() {
		String id = "chart01";
		String name = "Chart 01";
		String description = "Chart description";
		String programText = "        data(\"deleted_namespace_pods_count\", filter=filter('k8s.cluster.name', 'aerial-optics-416210-gke-splunk')).sum(by=\"namespace\").publish(label= \"killed pods by namespace\")";
		String plotType = "LineChart";
		boolean showDataMarkers = true;
		TimeChart timeChart = new TimeChart(id, name, description, programText, plotType, showDataMarkers, getTemplate());
		
		List<String> tfLines = timeChart.getAsTerraformText();
		
		for (String s : tfLines) {
		System.out.println(s);
		}
	}
	
	
	public List<String> getTemplate() {
		List<String> template = new ArrayList<String>();
		template.add("resource \"signalfx_time_chart\" \"@@chartId@@\" {");
		template.add("    name = \"@@chartName@@\"");
		template.add("    program_text = <<-EOF");
		template.add("        @@programText@@");
		template.add("        EOF");
		template.add("    plot_type = \"LineChart\"");
		template.add("    show_data_markers = @@showDataMarkers@@");
		template.add("    description = \"@@description@@\"");
		template.add("    @@legend_options_fields@@");
		template.add("}");
		
		return template;
	}

}
