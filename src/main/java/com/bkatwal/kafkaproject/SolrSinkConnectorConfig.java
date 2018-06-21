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

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import java.util.Map;


public class SolrSinkConnectorConfig extends AbstractConfig {

    private static final String TOPIC_DOC = "Topic from where data needs to be read.";

    public static final String COLLECTION_CONFIG = "solr.collection";
    private static final String COLLECTION_DOC = "Solr Collection name to which data need to be be written";

    public static final String ZKHOSTS_CONFIG = "solr.zkhosts";
    private static final String ZKHOSTS_DOC = "Comma separated zookeeper hosts, eg: localhost:2181,localhost:2182,localhost:2183";

    public static final String USERNAME_CONFIG = "solr.username";
    private static final String USERNAME_DOC = "username to connect to solr.";

    public static final String PASSWORD_CONFIG = "solr.password";
    private static final String PASSWORD_DOC = "password to connect to solr.";


    public SolrSinkConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
        super(config, parsedConfig);
    }

    public SolrSinkConnectorConfig(Map<String, String> parsedConfig) {
        this(conf(), parsedConfig);
    }

    public static ConfigDef conf() {
        return new ConfigDef()
                .define(COLLECTION_CONFIG, Type.STRING, Importance.HIGH, COLLECTION_DOC)
                .define(ZKHOSTS_CONFIG, Type.STRING, Importance.HIGH, ZKHOSTS_DOC)
                .define(USERNAME_CONFIG, Type.STRING, "", Importance.MEDIUM, USERNAME_DOC)
                .define(PASSWORD_CONFIG, Type.PASSWORD, "", Importance.MEDIUM, PASSWORD_DOC);
    }


    public  String getCollectionConfig() {
        return this.getString(COLLECTION_CONFIG);
    }


    public  String getZkhostsConfig() {
        return this.getString(ZKHOSTS_CONFIG);
    }


    public  String getUsernameConfig() {
        return this.getString(USERNAME_CONFIG);
    }


    public  String getPasswordConfig() {
        return this.getString(PASSWORD_CONFIG);
    }

}
