package com.attuned.o11ytools.model.splunk.terraform;

import java.util.ArrayList;
import java.util.List;

public class SingleValueChart extends Chart {

  private String color_by = "Scale", secondaryVisualization = "Radial";

  private List<ColourScale> colourScaleBlock;

  public SingleValueChart(List<String> terraformTemplateLines) {
    super(terraformTemplateLines);
  }

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
    //line.replaceAll("@@showDataMarkers@@", "true");

    return line;
  }

}