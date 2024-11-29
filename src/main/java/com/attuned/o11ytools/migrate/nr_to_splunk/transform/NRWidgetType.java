package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

public enum NRWidgetType {

	 viz_funnel("viz.funnel"), 
	 logger_logTableTidget("logger.log-table-widget"), 
	 viz_bar("viz.bar"), 
	 viz_markdown("viz.markdown"), 
	 viz_line("viz.line"), 
	 viz_area("viz.area"), 
	 viz_table("viz.table"), 
	 viz_billboard("viz.billboard"), 
	 viz_pie("viz.pie"), 
	 viz_stackedBar("viz.stacked-bar");
	 
	 private String name;
	 private NRWidgetType(String name) {
		 this.name = name;
	 }

	 public String getName() {
		 return name;
	 }
	 
	// viz.funnel, logger.log-table-widget, viz.bar, viz.markdown, viz.line, viz.area, viz.table, viz.billboard, viz.pie, viz.stacked-bar	

}
