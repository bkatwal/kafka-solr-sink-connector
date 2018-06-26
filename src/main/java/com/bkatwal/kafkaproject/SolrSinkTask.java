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

import com.bkatwal.kafkaproject.utils.SinkService;
import com.bkatwal.kafkaproject.utils.SolrSinkService;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

public class SolrSinkTask extends SinkTask {
    private static Logger log = LoggerFactory.getLogger(SolrSinkTask.class);

    private SinkService sinkService;
    private SolrSinkConnectorConfig config;

    @Override
    public String version() {
        return VersionUtil.getVersion(SolrSinkTask.class);
    }

    @Override
    public void start(Map<String, String> configMap) {
        config = new SolrSinkConnectorConfig(configMap);
        sinkService = new SolrSinkService(config);
        log.debug("Created config:" + config);

    }

    @Override
    public void put(Collection<SinkRecord> kafkaRecords){

        for (SinkRecord record : kafkaRecords) {
            String id = record.key() != null ? record.key().toString() : null;

            Schema valueSchema = record.valueSchema();

            //not a plain json data/schema less data
            //Expecting schema less record
            //TODO handle schema based record later
            if (valueSchema == null) {

                Map<String, Object> jsonValueMap = (Map<String, Object>) record.value();

                Object delVal = jsonValueMap.get("_delete_");

                //delete the field "_delete_" after reading the value from it
                jsonValueMap.remove("_delete_");

                //if _delete_ field is passed as false in value or if value is null, respective
                // doc will be deleted from solr
                if (isDeleteRequest(delVal) || record.value() == null) {

                    sinkService.deleteById(id);
                    
                } else {

                    sinkService.insert(id, record);

                }
            }
            log.error("Check if record in topic is plain json data and value is schema less. Set schema.enable=false for value.");

        }

    }

    private boolean isDeleteRequest(Object _delete_) {
        return _delete_ == null ? false : _delete_ instanceof String ?
                Boolean.parseBoolean((String) _delete_) : (Boolean) _delete_;
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> map) {

    }


    @Override
    public void stop() {
        sinkService.stop();
    }


    public void setConfig(SolrSinkConnectorConfig config) {
        this.config = config;
    }

    public SolrSinkConnectorConfig getConfig() {
        return config;
    }
}
