package com.bkatwal.kafkaproject.api;

import com.bkatwal.kafkaproject.utils.SolrAtomicUpdateOperations;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import java.util.Map;

public interface SolrService {
  /**
   * @param collection solr collection name
   * @param id solr doc unique key to delete
   * @return solr update response
   */
  UpdateResponse deleteById(final String collection, final String id, final int commitWithin);

  /**
   * @param collection solr collection name
   * @param record field value Map of a json doc
   * @return solr update response
   */
  UpdateResponse updateSingleDoc(
      final String collection, final Map<String, Object> record, final int commitWithin);

  /**
   * @param collection solr collection name
   * @param record solr input doc
   * @return solr update response
   */
  UpdateResponse updateSingleDoc(
      final String collection, final SolrInputDocument record, final int commitWithin);

  /**
   * @param collection collection solr collection name
   * @param record POJO class type record to be indexed
   * @param <T> POJO class type
   * @return update response
   */
  <T> UpdateResponse updateSingleDoc(
      final String collection, final T record, final int commitWithin);

  /**
   * @param collection solr collection name
   * @param id solr doc unique key to update
   * @param field field to update
   * @param solrAtomicUpdateOperations Operation on field, check:
   *     https://lucene.apache.org/solr/guide/7_3/updating-parts-of-documents.html for ref
   * @param newVal new value to update
   * @return solr update response
   */
  UpdateResponse updateFieldsInDoc(
      final String collection,
      final String id,
      final String field,
      SolrAtomicUpdateOperations solrAtomicUpdateOperations,
      Object newVal,
      final int commitWithin);

  void closeSolrClient();
}
