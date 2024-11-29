package com.attuned.o11ytools.migrate.nr_to_splunk.transform;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.attuned.o11ytools.migrate.nr_to_splunk.transform.nrql.DefaultMetricNameTransformer;
import com.attuned.o11ytools.model.nr.dashboard.NRRawConfiguration;
import com.attuned.o11ytools.model.nr.dashboard.NRWidget;
import com.attuned.o11ytools.model.nr.dashboard.NrqlQuery;

class NrqlToProgramTextTransformerTest {

  @BeforeEach
  void setUp() throws Exception {
  }

  @AfterEach
  void tearDown() throws Exception {
  }

  @Test
  void testTransform() {
    
    NrqlToProgramTextTransformer transformer = new NrqlToProgramTextTransformer(new DefaultMetricNameTransformer(getMetricMatchingProperties()));
    
    NRWidget w = new NRWidget();
    String nrqlString = "select uniqueCount(traceId)as 'overall traffic' FROM Transaction where  tags.environment = {{brandplusenv}} limit max compare with 1 week ago";
    
    NRRawConfiguration rc = new NRRawConfiguration();
    NrqlQuery query = new NrqlQuery();
    query.setQuery(nrqlString);
    rc.setNrqlQueries(Arrays.asList(query));
    w.setRawConfiguration(rc);
    String progText = transformer.transform(w);
    
    assertEquals("data(\"traces.count\").publish(label=\"traces_count\")", progText);
  }
  
  private Properties getMetricMatchingProperties() {
    Properties props = new Properties();
    props.put("default", "cpu.utilization");
    props.put("traceId", "traces.count");
    props.put("apm.service.error.count", "traces.count");
    props.put("k8s.container.cpuCoresUtilization", "container.cpu.utilization");
    return props;
  }

}
