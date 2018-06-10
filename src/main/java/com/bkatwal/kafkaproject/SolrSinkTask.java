package com.bkatwal.kafkaproject;

import com.bkatwal.kafkaproject.utils.JsonSolrDocMapper;
import com.bkatwal.kafkaproject.utils.PlainJsonSolrDocMappersImpl;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class SolrSinkTask extends SinkTask {
    private static Logger log = LoggerFactory.getLogger(SolrSinkTask.class);

    private SolrClient cloudSolrClient;

    private SolrSinkConnectorConfig config;

    private String collection;

    @Override
    public String version() {
        return VersionUtil.getVersion();
    }

    @Override
    public void start(Map<String, String> configMap) {
        config = new SolrSinkConnectorConfig(configMap);
        log.debug("Created config:" + config);
        String zkHostsSgtr = config.getZkhostsConfig();
        List<String> zkHosts = Arrays.asList(zkHostsSgtr.split(","));
        CloudSolrClient.Builder builder = new CloudSolrClient.Builder(zkHosts, Optional.empty());
        this.cloudSolrClient = builder.build();
        this.collection = config.getCollectionConfig();
    }

    @Override
    public void put(Collection<SinkRecord> kafkaRecords) {

        JsonSolrDocMapper jsonSolrDocMapper = new PlainJsonSolrDocMappersImpl();
        for (SinkRecord record : kafkaRecords) {
            String id = record.key() != null ? record.key().toString() : null;

            if (id == null) {
                log.error("null ID found in topic.");
                continue;
            }
            if (record.value() == null) {
                log.error("No value passed for doc ID, {}", id);
                continue;
            }
            try {
                UpdateResponse updateResponse;
                Map<String, Object> jsonValueMap = (Map<String, Object>) record.value();

                Object delVal = jsonValueMap.get("_delete_");

                //delete the field "_delete_" after reading the value from it
                jsonValueMap.remove("_delete_");

                //if _delete_ is passed in doc and is false, will try to delete doc
                if (isDeleteRequest(delVal)) {
                    updateResponse = cloudSolrClient.deleteById(collection, id, 10);
                    log.debug("Document {}, delete in: {}", id, updateResponse.getQTime());
                } else {
                    SolrInputDocument solrInputDocument = jsonSolrDocMapper.convertToSolrDocument(record);
                    updateResponse = cloudSolrClient.add(collection, solrInputDocument, 10);
                    log.debug("Document {}, added in: {}", id, updateResponse.getQTime());
                }

            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
        try {
            if (cloudSolrClient != null) {
                cloudSolrClient.close();
            }
        } catch (IOException e) {

        }
    }

    public void setCloudSolrClient(SolrClient cloudSolrClient) {
        this.cloudSolrClient = cloudSolrClient;
    }

    public void setConfig(SolrSinkConnectorConfig config) {
        this.config = config;
    }

    public SolrClient getCloudSolrClient() {
        return cloudSolrClient;
    }

    public SolrSinkConnectorConfig getConfig() {
        return config;
    }
}
