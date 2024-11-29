package com.attuned.o11ytools.migrate.terraform;

import java.util.List;

public interface Terraformable {
	
	public List<String> getAsTerraformText();

}
