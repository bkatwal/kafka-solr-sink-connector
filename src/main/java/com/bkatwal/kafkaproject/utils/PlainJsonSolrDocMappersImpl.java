package com.bkatwal.kafkaproject.utils;

import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InvalidObjectException;
import java.util.Collections;
import java.util.Map;


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
    public SolrInputDocument convertToSolrDocument(SinkRecord sinkRecord) throws InvalidObjectException {

        //for now throwing exception for any other type which is not schemaless json
        //TODO need to support other types
        if (!(sinkRecord.value() instanceof Map)) {
            throw new InvalidObjectException("Data format is not schemaless json.");
        }

        Map<String, Object> obj = (Map<String, Object>) sinkRecord.value();

        JsonConverter jsonConverter = new JsonConverter();

        jsonConverter.configure(Collections.singletonMap("schemas.enable", "false"), false);

        return toSolrDoc(obj);
    }


    private void addFieldsToDoc(Map<String, Object> objectMap, SolrInputDocument doc) {

        objectMap.forEach((key, val) -> {
            if (val != null)
                doc.setField(key, val);
        });

    }


}
