package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.attuned.o11ytools.util.FileUtils;

class SplunkO11yTemplateFactoryTest {

  @BeforeEach
  void setUp() throws Exception {
  }

  @AfterEach
  void tearDown() throws Exception {
  }

  @Test
  void testGetChartTFTemplate() {
    
    SplunkO11yTemplateFactory factory = new SplunkO11yTemplateFactory(new FileUtils(), getProperties());
    assertNotNull(factory.getChartTFTemplate("signalfx_time_chart"));

  }

  private Properties getProperties() {
    Properties props = new Properties();
    String templatesDirLoc = SplunkO11yTemplateFactory.class.getClassLoader().getResource("../classes/com/attuned/o11ytools/migrate/terraform").getFile();
    // System.out.println(templatesDirLoc);
    props.put("splunk.o11y.chart.templates.dir", templatesDirLoc);
    return props;
  }

}
