package com.attuned.o11ytools.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
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
		// System.out.println(dash);
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
		return name.replaceAll(" ", "_").replaceAll(":", "__").replaceAll("/", "_slash_");
	}
	
	public List<String> loadFileLines(File file) {
		if (file == null) {
			throw new IllegalArgumentException("Null file object provided for loading lines. Cannot proceed.");
		}
		BufferedReader reader = null;
		try {
		 reader = new BufferedReader(new FileReader(file));
			String line = null;
			
			List<String> lines = new ArrayList<String>();
			
			while((line = reader.readLine()) != null) {
				lines.add(line);
			}

			return lines;
		} catch (Exception e) {
			throw new RuntimeException("Error while loading lines from file: "+file.getAbsolutePath(), e);
		} finally {
		  if (reader != null) {
		    try {
		      reader.close();
		    } catch (Exception e) {
		      System.out.println("exception while closing reader for file: "+file.getAbsolutePath()+". "+e.getMessage());
		    }
		  }
		}
	}


  public void writeToNewFile(List<String> lines, File file) {
    
   PrintWriter writer = null;
   try {
     writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
     
     for (String line : lines) {
       writer.println(line);
      
     }
     
     
   } catch (Exception e) {
     throw new RuntimeException("Excpetion while writing to file ", e);
   } finally {
     if (writer != null) {
       try {
         writer.flush();
         writer.close();
       } catch (Exception e) {
         System.out.println("exception while closing writer for file: "+file.getAbsolutePath()+". "+e.getMessage());
       }
     }     
   }
    
  }
}