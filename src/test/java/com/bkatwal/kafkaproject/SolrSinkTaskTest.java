package com.bkatwal.kafkaproject;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.bkatwal.kafkaproject.SolrSinkConnectorConfig.COLLECTION_CONFIG;
import static com.bkatwal.kafkaproject.SolrSinkConnectorConfig.ZKHOSTS_CONFIG;

public class SolrSinkTaskTest {

  SolrSinkTask solrSinkTask =  new SolrSinkTask();


  private Map<String, String> initialConfig() {
    Map<String, String> baseProps = new HashMap<>();
    baseProps.put(ZKHOSTS_CONFIG, "localhost:2182,localhost:2183,localhost:2184");
    baseProps.put(COLLECTION_CONFIG, "asda-recipe-topic");
    return baseProps;
  }

  @Test
  public void test() {
    solrSinkTask.setConfig(new SolrSinkConnectorConfig(initialConfig()));

    solrSinkTask.start(initialConfig());
    }
}
