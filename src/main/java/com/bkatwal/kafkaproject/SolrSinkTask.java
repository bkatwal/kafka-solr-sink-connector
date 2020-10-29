/**
 * Copyright 2018 Bikas Katwal.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bkatwal.kafkaproject;

import com.bkatwal.kafkaproject.api.KafkaRecordSubject;
import com.bkatwal.kafkaproject.api.Observer;
import com.bkatwal.kafkaproject.service.KafkaRecordSubjectImpl;
import com.bkatwal.kafkaproject.service.PlainJsonDocMappersImpl;
import com.bkatwal.kafkaproject.service.SolrObserver;
import com.bkatwal.kafkaproject.service.SolrServiceImpl;
import com.bkatwal.kafkaproject.utils.Operation;
import com.bkatwal.kafkaproject.utils.SolrClientFactory;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.util.Collection;
import java.util.Map;

import static com.bkatwal.kafkaproject.utils.KafkaUtil.getOperation;

@Slf4j
public class SolrSinkTask extends SinkTask {

  private KafkaRecordSubject kafkaRecordSubject;
  private SolrSinkConnectorConfig config;

  @Override
  public String version() {
    return VersionUtil.getVersion(SolrSinkTask.class);
  }

  @Override
  public void start(Map<String, String> configMap) {
    config = new SolrSinkConnectorConfig(configMap);

    Observer solrObserver =
        new SolrObserver(
            config.getCollectionConfig(),
            new PlainJsonDocMappersImpl(),
            new SolrServiceImpl(
                SolrClientFactory.getClient(config.getSolrModeConfig(), config.getSolrURLConfig())),
            RateLimiter.create(config.getSolrWritesPerSec()),
            config.getCommitWithinMs());

    kafkaRecordSubject = new KafkaRecordSubjectImpl();
    kafkaRecordSubject.registerObserver(solrObserver);

    log.info("Created config: {}", config);
  }

  @Override
  public void put(Collection<SinkRecord> kafkaRecords) {

    for (SinkRecord record : kafkaRecords) {

      try {
        // not a plain json data/schema less data
        // Expecting schema less record
        if (record.valueSchema() != null) {
          log.error(
              "Check if record in topic is plain json data and value is schema less. Set schema.enable=false for value.");
          throw new ConnectException(
              "Check if record in topic is plain json data and value is schema less. Set schema.enable=false for value.");
        }

        // if _delete_ field is passed as false in value or if value is null, respective
        // doc will be deleted from solr
        Operation operation = getOperation(record);
        kafkaRecordSubject.notifyObservers(record, operation);
      } catch (SerializationException e) {
        if (config.isBadMessageOffsetIgnore()) {
          log.error("Ignoring bad message: {}", record.value());
          log.error("exception {}", e);
        } else {
          throw e;
        }
      }
    }
  }

  @Override
  public void flush(Map<TopicPartition, OffsetAndMetadata> map) {}

  @Override
  public void stop() {}

  public void setConfig(SolrSinkConnectorConfig config) {
    this.config = config;
  }

  public SolrSinkConnectorConfig getConfig() {
    return config;
  }
}
