package com.bkatwal.kafkaproject;

import org.apache.kafka.common.config.ConfigDef;
import org.junit.Test;

public class SolrSinkConnectorConfigTest {

  private ConfigDef configDef = SolrSinkConnectorConfig.conf();

  @Test
  public void doc() {
    System.out.println(SolrSinkConnectorConfig.conf().toRst());
  }
}
