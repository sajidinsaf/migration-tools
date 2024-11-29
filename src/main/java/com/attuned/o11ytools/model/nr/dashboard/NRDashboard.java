package com.attuned.o11ytools.model.nr.dashboard;

import java.util.List;

public class NRDashboard {
	public String name;
	public String description;
	public String permissions;
	public List<NRPage> pages;
	public NRDashboard() {
		
	}
	public NRDashboard(String name, String description, String permissions, List<NRPage> pages) {
		super();
		this.name = name;
		this.description = description;
		this.permissions = permissions;
		this.pages = pages;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPermissions() {
		return permissions;
	}
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	public List<NRPage> getPages() {
		return pages;
	}
	public void setPages(List<NRPage> pages) {
		this.pages = pages;
	}
	@Override
	public String toString() {
		return "NRDashboard [name=" + name + ", description=" + description + ", permissions=" + permissions + ", pages="
				+ pages + "]";
	}
	
	
	
}
