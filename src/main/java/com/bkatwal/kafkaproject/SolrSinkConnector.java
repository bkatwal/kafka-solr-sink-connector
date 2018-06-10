package com.bkatwal.kafkaproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrSinkConnector extends SinkConnector {

  private SolrSinkConnectorConfig config;

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  @Override
  public void start(Map<String, String> map) {
    config = new SolrSinkConnectorConfig(map);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return SolrSinkTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    // Define the individual task configurations that will be executed.
    List<Map<String, String>> configs = new ArrayList<>(1);
    configs.add(config.originalsStrings());
    return configs;
  }

  @Override
  public void stop() {
    //TODO: Do things that are necessary to stop your connector.
    // not applicable
  }

  @Override
  public ConfigDef config() {
    return SolrSinkConnectorConfig.conf();
  }
}
