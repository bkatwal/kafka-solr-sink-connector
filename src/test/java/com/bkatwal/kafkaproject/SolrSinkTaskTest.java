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

import static com.bkatwal.kafkaproject.SolrSinkConnectorConfig.COLLECTION_CONFIG;
import static com.bkatwal.kafkaproject.SolrSinkConnectorConfig.SOLRMODE_CONFIG;
import static com.bkatwal.kafkaproject.SolrSinkConnectorConfig.SOLRURL_CONFIG;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class SolrSinkTaskTest {

  SolrSinkTask solrSinkTask =  new SolrSinkTask();


  private Map<String, String> initialConfig() {
    Map<String, String> baseProps = new HashMap<>();
    baseProps.put(SOLRURL_CONFIG, "localhost:2182,localhost:2183,localhost:2184");
    baseProps.put(COLLECTION_CONFIG, "asda-recipe-topic");
    baseProps.put(SOLRMODE_CONFIG, "CLOUD");
    return baseProps;
  }

  @Test
  public void test() {
    solrSinkTask.setConfig(new SolrSinkConnectorConfig(initialConfig()));

    solrSinkTask.start(initialConfig());
    }
}
