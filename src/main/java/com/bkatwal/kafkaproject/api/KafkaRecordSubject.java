package com.bkatwal.kafkaproject.api;

import com.bkatwal.kafkaproject.utils.Operation;
import org.apache.kafka.connect.sink.SinkRecord;

public interface KafkaRecordSubject {

  void registerObserver(Observer observer);

  void notifyObservers(SinkRecord sinkRecord, Operation operation);
}
