package com.bkatwal.kafkaproject.utils;

import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.solr.common.SolrInputDocument;

import java.io.InvalidObjectException;

public interface JsonSolrDocMapper {

    SolrInputDocument convertToSolrDocument(SinkRecord sinkRecord) throws InvalidObjectException;

}
