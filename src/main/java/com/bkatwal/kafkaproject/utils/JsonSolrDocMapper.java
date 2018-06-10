package com.bkatwal.kafkaproject.utils;

import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface JsonSolrDocMapper {

    SolrInputDocument convertToSolrDocument(SinkRecord sinkRecord);

}
