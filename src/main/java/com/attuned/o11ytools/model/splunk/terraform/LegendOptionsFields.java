package com.attuned.o11ytools.model.splunk.terraform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.attuned.o11ytools.migrate.terraform.Terraformable;

public class LegendOptionsFields implements Terraformable {

	private Map<String, Boolean> fields;

	public LegendOptionsFields() {
		fields = new HashMap<String, Boolean>();
	}
	public LegendOptionsFields(Map<String, Boolean> fields) {
		super();
		this.fields = fields;
	}
	public Map<String, Boolean> getFields() {
		return fields;
	}
	public void setFields(Map<String, Boolean> fields) {
		this.fields = fields;
	}
	
	public void addField(String key, boolean value) {
		fields.put(key, value);
	}
	@Override
	public String toString() {
		return "LegendOptionsFields [fields=" + fields + ", " + super.toString() + "]";
	}
	
	@Override
	public List<String> getAsTerraformText() {
		
		if (fields == null || fields.isEmpty()) {
			return new ArrayList<String>();
		}
		
		List<String> lines = new ArrayList<String>();


		for (String key : fields.keySet()) {
			lines.add("    legend_options_fields {");
			lines.add("      property = \""+key+"\"");
			lines.add("      value = "+fields.get(key));
			lines.add("    }");
			lines.add("");
		}

		return null;
	}
	
	
	
}
