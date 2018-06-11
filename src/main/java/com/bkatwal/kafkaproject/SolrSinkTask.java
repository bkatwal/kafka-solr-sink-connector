package com.bkatwal.kafkaproject;

import com.bkatwal.kafkaproject.utils.SinkService;
import com.bkatwal.kafkaproject.utils.SolrSinkService;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.apache.solr.client.solrj.response.UpdateResponse;
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
        return VersionUtil.getVersion();
    }

    @Override
    public void start(Map<String, String> configMap) {
        config = new SolrSinkConnectorConfig(configMap);
        sinkService = new SolrSinkService(config);
        log.debug("Created config:" + config);

    }

    @Override
    public void put(Collection<SinkRecord> kafkaRecords) {

        for (SinkRecord record : kafkaRecords) {
            String id = record.key() != null ? record.key().toString() : null;


            if (record.value() == null) {
                log.error("No value passed for doc ID, {}", id);
                continue;
            }

            UpdateResponse updateResponse;
            Map<String, Object> jsonValueMap = (Map<String, Object>) record.value();

            Object delVal = jsonValueMap.get("_delete_");

            //delete the field "_delete_" after reading the value from it
            jsonValueMap.remove("_delete_");

            //if _delete_ is passed in doc and is false, will try to delete doc
            if (isDeleteRequest(delVal)) {
                sinkService.deleteById(id);
            } else {
                sinkService.insert(id, record);
            }


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
