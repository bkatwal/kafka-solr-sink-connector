/**
 * Copyright 2018 Bikas Katwal.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/

package com.bkatwal.kafkaproject.utils;

import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public SolrInputDocument convertToSolrDocument(SinkRecord sinkRecord){

        //for now throwing exception for any other type which is not schemaless json
        //TODO need to support other types
        if (!(sinkRecord.value() instanceof Map)) {
            throw new ConnectException("Record Value is not schemaless json.");
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
