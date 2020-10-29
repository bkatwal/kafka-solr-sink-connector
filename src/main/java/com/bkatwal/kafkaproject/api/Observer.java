package com.bkatwal.kafkaproject.api;

import com.bkatwal.kafkaproject.utils.Operation;
import org.apache.kafka.connect.sink.SinkRecord;

public interface Observer {

  void notify(SinkRecord sinkRecord, Operation operation);
}
