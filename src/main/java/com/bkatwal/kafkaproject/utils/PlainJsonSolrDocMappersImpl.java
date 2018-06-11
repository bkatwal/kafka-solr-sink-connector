package com.bkatwal.kafkaproject.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Assumes all fields are flattened and no fields has Map or any complex type.
 * List will be mapped to multivalued field
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

        Map<String, Object> obj = (Map<String, Object>) sinkRecord.value();

        JsonConverter  jsonConverter = new JsonConverter();

        jsonConverter.configure(Collections.singletonMap("schemas.enable", "false"), false);

        return toSolrDoc(obj);
    }

    private void addFieldsToDoc(Map<String, Object> objectMap, SolrInputDocument doc) {
        for (String key : objectMap.keySet()) {
            Object val = objectMap.get(key);
            if (val != null) {
                doc.setField(key, val);
            }

        }
    }


}
