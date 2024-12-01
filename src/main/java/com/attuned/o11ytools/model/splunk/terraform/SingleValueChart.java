package com.attuned.o11ytools.model.splunk.terraform;

import java.util.List;

public class SingleValueChart extends Chart {

  private String color_by = "Scale", secondaryVisualization = "Radial";

  private List<ColourScale> colourScaleBlock;

  public SingleValueChart(String id, String name, String description, String programText,
      List<String> terraformTemplateLines) {
    super(id, name, description, programText, terraformTemplateLines);
  }

  public String getColor_by() {
    return color_by;
  }

  public void setColor_by(String color_by) {
    this.color_by = color_by;
  }

  public String getSecondaryVisualization() {
    return secondaryVisualization;
  }

  public void setSecondaryVisualization(String secondaryVisualization) {
    this.secondaryVisualization = secondaryVisualization;
  }

  public List<ColourScale> getColourScaleBlock() {
    return colourScaleBlock;
  }

  public void setColourScaleBlock(List<ColourScale> colourScale) {
    this.colourScaleBlock = colourScale;
  }

  @Override
  public String toString() {
    return "SingleValueChart [color_by=" + color_by + ", secondaryVisualization=" + secondaryVisualization
        + ", colourScaleBlock=" + colourScaleBlock + ", " + super.toString() + "]";
  }

  @Override
  protected String renderSpecifics(String line) {
    if (line.contains("@@color_by_block@@")) {
      if (colourScaleBlock == null || colourScaleBlock.isEmpty()) {
        return "";
      }

      String lof = getAsString(colourScaleBlock);
      line = line.replaceAll("@@color_by_block@@", lof);
    } 

    return line;
  }

  private String getAsString(List<ColourScale> colourScaleBlocks) {
    StringBuilder sb = new StringBuilder();
    for (ColourScale s : colourScaleBlocks) {
      sb.append(s.getAsTerraformText()).append(System.getProperty("line.separator"));
    }
    return sb.toString();
  } 
  
  public static void main(String[] args) {
    StringBuilder sb = new StringBuilder("asd").append(System.getProperty("line.separator")).append("bbc").append(System.getProperty("line.separator")).append("asd").append(System.getProperty("line.separator"));
    
    for (String s : sb.toString().split(System.getProperty("line.separator"))) {
      System.out.println(s);
    }
  }

  @Override
  public String getSignalFxTerraformChartType() {
    return "signalfx_single_value_chart";
  }
}