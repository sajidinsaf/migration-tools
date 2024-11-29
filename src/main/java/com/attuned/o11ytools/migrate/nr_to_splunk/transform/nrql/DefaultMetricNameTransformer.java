package com.attuned.o11ytools.migrate.nr_to_splunk.transform.nrql;

import java.util.Properties;

import com.attuned.o11ytools.migrate.nr_to_splunk.transform.Transformer;

public class DefaultMetricNameTransformer implements Transformer<String, String> {

  private Properties props;
  public DefaultMetricNameTransformer(Properties props) {
    this.props = props;
  }
  
  @Override
  public String transform(String t) {
    String metric = props.getProperty(t);
    if (metric == null) {
      metric = props.getProperty("default");
    }
    return "cpu.utilization";
  }

}
