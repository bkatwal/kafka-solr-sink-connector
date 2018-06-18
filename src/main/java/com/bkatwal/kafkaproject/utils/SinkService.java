package com.bkatwal.kafkaproject.utils;


public interface SinkService<K,V> {

    boolean update(K id, V data);
    boolean deleteById(K id);
    boolean insert(K id, V data);

    void stop();
}
