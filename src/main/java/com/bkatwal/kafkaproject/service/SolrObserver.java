package com.bkatwal.kafkaproject.service;

import com.bkatwal.kafkaproject.api.AbstractObserver;
import com.bkatwal.kafkaproject.api.JsonDocMapper;
import com.bkatwal.kafkaproject.api.SolrService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.solr.common.SolrInputDocument;

@Slf4j
@RequiredArgsConstructor
public class SolrObserver extends AbstractObserver {

  @NonNull private final String collectionName;
  @NonNull private final JsonDocMapper<SolrInputDocument> jsonDocMapper;
  @NonNull private final SolrService solrService;
  @NonNull private final RateLimiter rateLimiter;
  @NonNull private final int commitWithinMs;

  @Override
  public void delete(SinkRecord sinkRecord) {
    rateLimiter.acquire();

    if (sinkRecord.key() == null) {
      log.debug("null ID found in sink record. Cannot perform delete operation.");
      return;
    }
    String id = String.valueOf(sinkRecord.key());
    solrService.deleteById(collectionName, id, commitWithinMs);
  }

  @Override
  public void insert(SinkRecord sinkRecord) {
    rateLimiter.acquire();
    SolrInputDocument solrInputDocument = jsonDocMapper.convert(sinkRecord);
    solrService.updateSingleDoc(collectionName, solrInputDocument, commitWithinMs);
  }
}
