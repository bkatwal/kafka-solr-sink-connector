/**
 * Copyright 2018 Bikas Katwal.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 **/

package com.bkatwal.kafkaproject;

import com.bkatwal.kafkaproject.utils.SolrMode;
import java.util.Map;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;


public class SolrSinkConnectorConfig extends AbstractConfig {

  public static final String COLLECTION_CONFIG = "solr.collection";
  private static final String COLLECTION_DOC = "Solr Collection name to which data need to be be written";

  public static final String SOLRURL_CONFIG = "solr.url";
  private static final String SOLRURL_DOC =
      "Comma separated zookeeper hosts, eg: localhost:2181,localhost:2182,localhost:2183" +
          " or it could be standalone solr mode too, ex: localhost:8983/solr";

  public static final String USERNAME_CONFIG = "solr.username";
  private static final String USERNAME_DOC = "username to connect to solr.";

  public static final String PASSWORD_CONFIG = "solr.password";
  private static final String PASSWORD_DOC = "password to connect to solr.";

  public static final String COMMIT_WITHIN_MS = "commit.within.ms";
  private static final String COMMIT_WITHIN_MS_DOC = "solr commit within milli seconds param.";

  public static final String SOLRMODE_CONFIG = "solr.mode";
  private static final String SOLRMODE_DOC = "solr mode can be STANDALONE/CLOUD";


  public SolrSinkConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
    super(config, parsedConfig);
  }

  public SolrSinkConnectorConfig(Map<String, String> parsedConfig) {
    this(conf(), parsedConfig);
  }

  public static ConfigDef conf() {
    return new ConfigDef()
        .define(COLLECTION_CONFIG, Type.STRING, Importance.HIGH, COLLECTION_DOC)
        .define(SOLRURL_CONFIG, Type.STRING, Importance.HIGH, SOLRURL_DOC)
        .define(USERNAME_CONFIG, Type.STRING, "", Importance.MEDIUM, USERNAME_DOC)
        .define(PASSWORD_CONFIG, Type.PASSWORD, "", Importance.MEDIUM, PASSWORD_DOC)
        .define(SOLRMODE_CONFIG, Type.STRING, "", Importance.MEDIUM, SOLRMODE_DOC)
        .define(COMMIT_WITHIN_MS, Type.INT, 10, Importance.MEDIUM, COMMIT_WITHIN_MS_DOC);
  }


  public String getCollectionConfig() {
    return this.getString(COLLECTION_CONFIG);
  }


  public String getSolrURLConfig() {
    return this.getString(SOLRURL_CONFIG);
  }


  public String getUsernameConfig() {
    return this.getString(USERNAME_CONFIG);
  }


  public String getPasswordConfig() {
    return this.getString(PASSWORD_CONFIG);
  }

  public SolrMode getSolrModeConfig() {
    return SolrMode.valueOf(this.getString(SOLRMODE_CONFIG));
  }

  public int getCommitWithinMs(){
    return getInt(COMMIT_WITHIN_MS);
  }
}
