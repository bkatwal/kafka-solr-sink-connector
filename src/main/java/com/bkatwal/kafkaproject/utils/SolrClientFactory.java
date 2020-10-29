package com.bkatwal.kafkaproject.utils;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SolrClientFactory {

  public static SolrClient getClient(SolrMode solrMode, String url) {

    if (solrMode.equals(SolrMode.STANDALONE)) {
      return new HttpSolrClient.Builder(url).build();
    }

    if (solrMode.equals(SolrMode.CLOUD)) {
      List<String> zkHosts = Arrays.asList(url.split(","));
      CloudSolrClient.Builder builder = new CloudSolrClient.Builder(zkHosts, Optional.empty());
      return builder.build();
    }
    return null;
  }
}
