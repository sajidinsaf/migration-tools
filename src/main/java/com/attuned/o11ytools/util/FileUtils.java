package com.attuned.o11ytools.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.attuned.o11ytools.model.nr.dashboard.NRDashboard;
import com.attuned.o11ytools.model.nr.dashboard.NRPage;

public class FileUtils {
	
	public File getUniquePathUnderBaseDir(File baseDir, String dirPrefix) {
		String basePath = baseDir.getAbsolutePath();
		
		String filePath = basePath + System.getProperty("file.separator")+dirPrefix+"_"+System.currentTimeMillis();
		
		File newDir = new File(filePath);
	    newDir.mkdir();

		return newDir;

	}


	public File getSplunkDashboardGroupTFFilePath(NRDashboard dash, File baseDir) {
		String basePath = baseDir.getAbsolutePath();
		String fileName = cleanForFileName(dash.getName())+".tf";
		String filePath = basePath + System.getProperty("file.separator")+fileName;
		return new File(filePath);
	}
	
	public File getChartsFilePathForDashboard(NRDashboard dash, File baseDir) {
		String basePath = baseDir.getAbsolutePath();
		String filePath = basePath + System.getProperty("file.separator")+cleanForFileName(dash.getName())+"_Charts.tf";
		return new File(filePath);
	}


	private String cleanForFileName(String name) {
		return name.replaceAll(" ", "_").replaceAll(":", "__");
	}
	
	public List<String> loadFileLines(File file) {
		if (file == null) {
			throw new IllegalArgumentException("Null file object provided for loading lines. Cannot proceed.");
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			
			List<String> lines = new ArrayList<String>();
			
			while((line = reader.readLine()) != null) {
				lines.add(line);
			}
			return lines;
		} catch (Exception e) {
			throw new RuntimeException("Error while loading lines from file: "+file.getAbsolutePath(), e);
		}
	}
}