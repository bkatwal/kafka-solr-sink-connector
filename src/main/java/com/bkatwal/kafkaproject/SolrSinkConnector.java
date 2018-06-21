/**
 * Copyright 2018 Bikas Katwal.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/

package com.bkatwal.kafkaproject;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolrSinkConnector extends SinkConnector {

  private SolrSinkConnectorConfig config;

  @Override
  public String version() {
    return VersionUtil.getVersion(SolrSinkConnector.class);
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
