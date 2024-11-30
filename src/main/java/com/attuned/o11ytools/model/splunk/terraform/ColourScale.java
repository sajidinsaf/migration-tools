package com.attuned.o11ytools.model.splunk.terraform;

import java.util.ArrayList;
import java.util.List;

import com.attuned.o11ytools.migrate.terraform.Terraformable;

public class ColourScale implements Terraformable {

  private double gte, lte;

  private String color;

  public ColourScale() {

  }

  public ColourScale(double gte, double lte, String color) {
    super();
    this.gte = gte;
    this.lte = lte;
    this.color = color;
  }

  public double getGte() {
    return gte;
  }

  public void setGte(double gte) {
    this.gte = gte;
  }

  public double getLte() {
    return lte;
  }

  public void setLte(double lte) {
    this.lte = lte;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  @Override
  public List<String> getAsTerraformText() {
    
    return new ArrayList<String>();
  }

  
  
}
