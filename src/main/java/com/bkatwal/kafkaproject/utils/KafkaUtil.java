package com.bkatwal.kafkaproject.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.connect.header.Header;
import org.apache.kafka.connect.sink.SinkRecord;

import java.util.Map;

@Slf4j
public final class KafkaUtil {

  private KafkaUtil() {}

  @SneakyThrows
  public static Operation getOperation(SinkRecord sinkRecord) {

    if (!(sinkRecord.value() instanceof Map)) {
      throw new SerializationException("Expected json but found, " + sinkRecord.value().getClass());
    }

    if (isDeleteRequest(sinkRecord)) {
      return Operation.DELETE;
    }

    return Operation.INSERT;
  }

  private static boolean isDeleteRequest(SinkRecord sinkRecord) {

    for (Header header : sinkRecord.headers()) {
      String name = header.key();
      if ("_delete_".equalsIgnoreCase(name)) {
        String val = (String) header.value();
        return Boolean.parseBoolean(val);
      }
    }
    Map<String, Object> jsonValueMap = (Map<String, Object>) sinkRecord.value();
    Object delete = jsonValueMap.get("_delete_");
    // delete the field "_delete_" after reading the value from it
    jsonValueMap.remove("_delete_");
    return delete != null
        && (delete instanceof String ? Boolean.parseBoolean((String) delete) : (Boolean) delete);
  }
}
