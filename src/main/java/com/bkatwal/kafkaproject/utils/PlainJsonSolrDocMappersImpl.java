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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Assumes all fields are flattened and no fields has Map or any complex type. List will be mapped
 * to multivalued field
 */
public class PlainJsonSolrDocMappersImpl implements JsonSolrDocMapper {

  private static Logger log = LoggerFactory.getLogger(PlainJsonSolrDocMappersImpl.class);


  public SolrInputDocument toSolrDoc(Map<String, Object> objectMap) {
    SolrInputDocument doc = new SolrInputDocument();

    addFieldsToDoc(objectMap, doc);
    return doc;
  }

  @Override
  public SolrInputDocument convertToSolrDocument(SinkRecord sinkRecord) {

    //for now throwing exception for any other type which is not schemaless json
    if (!(sinkRecord.value() instanceof Map)) {
      throw new ConnectException("Record Value is not schemaless json.");
    }

    Map<String, Object> obj = (Map<String, Object>) sinkRecord.value();

    return toSolrDoc(createDynamicFieldsForRecordIfExists(obj));
  }

  /*
  only one level of child document supported
   */
  private void addFieldsToDoc(Map<String, Object> objectMap, SolrInputDocument doc) {
    objectMap.forEach((key, val) -> {
      if (val != null) {
        if ("_childDocuments_".equalsIgnoreCase(key)) {
          doc.addChildDocuments(getChildDocuments(val));
        } else {
          // adding shitty logic as solr doesn't support BigDecimal
          if (val instanceof BigDecimal) {
            val = ((BigDecimal) val).doubleValue();
          }
          doc.setField(key, val);
        }
      }
    });

  }

  private Map<String, Object> createDynamicFieldsForRecordIfExists(
      final Map<String, Object> record) {

    Map<String, Object> newRecord = new LinkedHashMap<>();

    for (Entry<String, Object> entry : record.entrySet()) {

      String entryKey = entry.getKey();
      Object entryValue = entry.getValue();

      if (entryValue instanceof Map) {
        Map<String, Object> columnVal = (Map<String, Object>) entry.getValue();
        String columnName = entry.getKey();
        columnVal.forEach(
            (key, val) -> newRecord.put(columnName.concat("_").concat(key), val));
      } else {
        newRecord.put(entryKey, entryValue);
      }
    }
    record.clear();
    return newRecord;
  }

  private Collection<SolrInputDocument> getChildDocuments(Object childDocuments) {

    List<Map<String, Object>> childDocsList = (List<Map<String, Object>>) childDocuments;

    List<SolrInputDocument> solrInputDocuments = new ArrayList<>(childDocsList.size());
    for (Map<String, Object> record : childDocsList) {
      SolrInputDocument solrInputDocument = new SolrInputDocument();
      record.forEach(
          (key, val) -> {
            if (val != null) {
              // adding shitty logic as solr doesn't support BigDecimal
              if (val instanceof BigDecimal) {
                val = ((BigDecimal) val).doubleValue();
              }
              solrInputDocument.setField(key, val);
            }
          });
      solrInputDocuments.add(solrInputDocument);
    }
    return solrInputDocuments;

  }
}
