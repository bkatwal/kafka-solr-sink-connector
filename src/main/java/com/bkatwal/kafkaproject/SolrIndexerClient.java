package com.bkatwal.kafkaproject;

import com.bkatwal.kafkaproject.utils.JsonSolrDocMapper;
import com.bkatwal.kafkaproject.utils.PlainJsonSolrDocMappersImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SolrIndexerClient {

    private static final Logger log = LoggerFactory.getLogger(SolrIndexerClient.class);

    private SolrClient client;

    private String collection;

    public SolrIndexerClient(CloudSolrClient config){
        this.client = client;

    }

    public boolean indexDoc(SolrInputDocument doc) throws IOException, SolrServerException {

        client.add(doc);
        return true;
    }

    public boolean indexDocs(List<SolrInputDocument> docs){
        JsonSolrDocMapper jsonSolrDocMapper = new PlainJsonSolrDocMappersImpl();
        ObjectMapper mapper = new ObjectMapper();
        return true;
    }
}
