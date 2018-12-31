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

package com.bkatwal.kafkaproject.utils;

import java.io.IOException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.SolrInputDocument;

@Slf4j
@Builder
public class SolrSinkService implements SinkService<String, SinkRecord> {

  private SolrClient solrClient;

  private String collection;

  private JsonSolrDocMapper jsonSolrDocMapper;

  private int commitWithinMs;

  //TODO add solr update fields here
  @Override
  public boolean update(final String id, final SinkRecord data) {
    return false;
  }

  @Override
  public boolean deleteById(final String id) {
    UpdateResponse updateResponse;
    try {
      updateResponse = solrClient.deleteById(collection, id, 10);
    } catch (SolrServerException | IOException e) {
      log.error("Unable to send delete request to solr");
      throw new SolrException(ErrorCode.SERVER_ERROR, "Unable to send delete request to solr", e);
    }
    log.debug("Document {}, deleted in: {}", id, updateResponse.getQTime());
    return true;
  }

  @Override
  public boolean insert(final String id, final SinkRecord record) {
    UpdateResponse updateResponse;
    SolrInputDocument solrInputDocument = jsonSolrDocMapper.convertToSolrDocument(record);
    try {
      updateResponse = solrClient.add(collection, solrInputDocument, 10);
      log.debug("saved document: {}", solrInputDocument);
    } catch (SolrServerException | IOException e) {
      log.error("Unable to send update request to solr");
      throw new SolrException(ErrorCode.SERVER_ERROR, "Unable to send update request to solr", e);
    }
    log.debug("Document {}, added in: {}", id, updateResponse.getQTime());
    return true;
  }

  @Override
  public void stop() {
    try {
      if (solrClient != null) {
        solrClient.close();
      }
    } catch (IOException e) {
      log.error("could not close solr client: {}", e);
    }
  }

}

