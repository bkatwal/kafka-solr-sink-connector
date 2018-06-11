package com.bkatwal.kafkaproject.utils;

import com.bkatwal.kafkaproject.SolrSinkConnectorConfig;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SolrSinkService implements SinkService<String, SinkRecord> {

    private static final Logger log = LoggerFactory.getLogger(SolrSinkService.class);

    private SolrClient cloudSolrClient;

    private String collection;

    private final JsonSolrDocMapper jsonSolrDocMapper = new PlainJsonSolrDocMappersImpl();

    public SolrSinkService(SolrSinkConnectorConfig config){
        String zkHostsSgtr = config.getZkhostsConfig();
        List<String> zkHosts = Arrays.asList(zkHostsSgtr.split(","));
        CloudSolrClient.Builder builder = new CloudSolrClient.Builder(zkHosts, Optional.empty());
        this.cloudSolrClient = builder.build();
        this.collection = config.getCollectionConfig();
    }

    public SolrSinkService(){
       //Do not use this
    }

    @Override
    public boolean update(final String id, final SinkRecord data) {
        return false;
    }

    @Override
    public boolean deleteById(final String id) {
        UpdateResponse updateResponse = null;
        try {
            updateResponse = cloudSolrClient.deleteById(collection, id, 10);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("Document {}, deleted in: {}", id, updateResponse.getQTime());
        return true;
    }

    @Override
    public boolean insert(final String id, final SinkRecord record) throws InvalidObjectException {
        UpdateResponse updateResponse = null;
        SolrInputDocument solrInputDocument = jsonSolrDocMapper.convertToSolrDocument(record);
        try {
            updateResponse = cloudSolrClient.add(collection, solrInputDocument, 10);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("Document {}, added in: {}", id, updateResponse.getQTime());
        return true;
    }

    @Override
    public void stop(){
        try {
            if (cloudSolrClient != null) {
                cloudSolrClient.close();
            }
        } catch (IOException e) {

        }
    }

}

