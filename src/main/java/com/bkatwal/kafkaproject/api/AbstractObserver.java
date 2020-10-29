package com.bkatwal.kafkaproject.api;

import com.bkatwal.kafkaproject.utils.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.sink.SinkRecord;

@Slf4j
public abstract class AbstractObserver implements Observer {

  @Override
  public void notify(SinkRecord sinkRecord, Operation operation) {

    switch (operation) {
      case DELETE:
        delete(sinkRecord);
        break;
      case INSERT:
        insert(sinkRecord);
        break;
      default:
        throw new UnsupportedOperationException(operation + " is not supported.");
    }
  }

  public abstract void delete(SinkRecord sinkRecord);

  public abstract void insert(SinkRecord sinkRecord);
}
