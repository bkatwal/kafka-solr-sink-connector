package com.bkatwal.kafkaproject.service;

import com.bkatwal.kafkaproject.api.KafkaRecordSubject;
import com.bkatwal.kafkaproject.api.Observer;
import com.bkatwal.kafkaproject.utils.Operation;
import org.apache.kafka.connect.sink.SinkRecord;

import java.util.ArrayList;
import java.util.Collection;

/** @author "Bikas Katwal" 19/03/19 */
public class KafkaRecordSubjectImpl implements KafkaRecordSubject {

  private final Collection<Observer> observers;

  public KafkaRecordSubjectImpl() {
    this.observers = new ArrayList<>();
  }

  @Override
  public void registerObserver(Observer observer) {
    if (observer == null) {
      throw new NullPointerException("Null Observer");
    }

    observers.add(observer);
  }

  @Override
  public void notifyObservers(SinkRecord sinkRecord, Operation operation) {
    observers.forEach(obj -> obj.notify(sinkRecord, operation));
  }
}
