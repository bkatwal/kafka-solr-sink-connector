package com.bkatwal.kafkaproject.utils;


import java.io.InvalidObjectException;

public interface SinkService<K,V> {

    boolean update(K id, V data);
    boolean deleteById(K id);
    boolean insert(K id, V data) throws InvalidObjectException;

    void stop();
}
