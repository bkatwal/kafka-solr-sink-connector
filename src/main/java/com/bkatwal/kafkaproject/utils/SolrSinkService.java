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

package com.bkatwal.kafkaproject.utils;

import com.bkatwal.kafkaproject.SolrSinkConnectorConfig;
import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        } catch (SolrServerException | IOException e) {
            log.error("Unable to send delete request to solr");
            throw new RetriableException(e);
        }
        log.debug("Document {}, deleted in: {}", id, updateResponse.getQTime());
        return true;
    }

    @Override
    public boolean insert(final String id, final SinkRecord record){
        UpdateResponse updateResponse = null;
        SolrInputDocument solrInputDocument = jsonSolrDocMapper.convertToSolrDocument(record);
        try {
            updateResponse = cloudSolrClient.add(collection, solrInputDocument, 10);
        } catch (SolrServerException | IOException e) {
            log.error("Unable to send update request to solr");
            throw new RetriableException(e);
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

